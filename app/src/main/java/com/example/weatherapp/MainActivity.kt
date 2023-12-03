package com.example.weatherapp
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {
    // Location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    // Other variables
    private lateinit var locationLabel: TextView
    private lateinit var locationField: TextView
    private lateinit var button: Button
    private var isEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationLabel = findViewById(R.id.locationLabel)
        locationField = findViewById(R.id.locationField)
        button = findViewById(R.id.button)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Create location request
        locationRequest = LocationRequest.create().apply {
            interval = 10000 // Interval at which you want to receive location updates (in milliseconds)
            fastestInterval = 5000 // Fastest interval at which the app can handle updates (in milliseconds)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Set the desired accuracy for location updates
        }

        button.setOnClickListener {
            if (!isEnabled) {
                requestLocationUpdates()
                button.text = "Stop Location Services"
                isEnabled = true
            }
            else {
                stopLocationUpdates()
                button.text = "Start Location Services"
                isEnabled = false
            }
        }

        // Create location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let { location ->
                    updateLocationViews(location)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requestLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocationViews(location: Location) {
        locationField.text = "${location.latitude}, ${location.longitude}"
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }
}