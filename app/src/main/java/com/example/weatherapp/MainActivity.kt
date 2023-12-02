package com.example.weatherapp
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Location services

    private lateinit var locationLabel: TextView
    private lateinit var locationField: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationLabel = findViewById(R.id.locationLabel)
        locationField = findViewById(R.id.locationField)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}