from flask import Flask, request
import json
from math import cos, asin, sqrt
from urllib.request import urlopen 

app = Flask(__name__)

# depeciate 'https://nominatim.openstreetmap.org/reverse?format=json&lat=' + currentLatitude + '&lon=' + currentLongitude + '&zoom=18&addressdetails=1'

@app.route("/weather", methods=["GET"])
def getDistrict_temperature():
    args = request.args
    lat = float(args['lat'])
    lon = float(args['lon'])

    location    = getClosestStation(lat, lon)
    temperature = getTemperature(location)
    result = {
        "location"    : location,
        "temperature" : temperature
    }
    return json.dumps(result)

# given a lon and lat -> return the closest station name in 'data/weather-station-info.json'
# Using Haversine formula

def HaversineDistance(lat1, lon1, lat2, lon2):
    p = 0.017453292519943295
    hav = 0.5 - cos((lat2-lat1)*p)/2 + cos(lat1*p)*cos(lat2*p) * (1-cos((lon2-lon1)*p)) / 2
    return 12742 * asin(sqrt(hav))

def getClosestStation(lat, lon):
    # open json
    f = open('./data/weather-station-info.txt')
    # load the json
    data = json.load(f)
    closest_station = ""
    min_distance = float("inf")
    # loop for all station
    for location in data:
        distance = HaversineDistance(lat, lon, location["latitude"], location["longitude"])
        if distance < min_distance:
            min_distance = distance
            closest_station = location['station_name_en']
    return closest_station 


# get temperature by station name 
# https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=en <- real time data

def getTemperature(station_name):
    response = urlopen("https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=en") 
    data = json.loads(response.read())
    for location in data["temperature"]["data"]:
        print(type(location["place"]))
        if location["place"] == station_name:
            return location["value"]
    