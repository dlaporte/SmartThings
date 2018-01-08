#!/usr/bin/python

# rpi-dht.py
# sends DHT temperature/humidity data to rpi-dht SmartThings REST endpoint
# needs to run via cron or another scheduler:
# */5 * * * * /root/bin/rpi-dht.py

# version 1.0, 07 January 2017
# David LaPorte david@davidlaporte.org

# download and install tha Adafruit DHT python module
# https://github.com/adafruit/Adafruit_Python_DHT
	
import sys
import json
import pprint
import requests
import Adafruit_DHT

# set the client_id returned when you enabled OAuth and access_token obtained using your client_id and client_secret:
# http://docs.smartthings.com/en/latest/smartapp-web-services-developers-guide/authorization.html

client_id = ''
access_token=''

# 11 = DHT11, 22 = DHT22
sensor = 22
	
# GPIO data pin, for more information and a great tutorial, visit:
# http://www.circuitbasics.com/how-to-set-up-the-dht11-humidity-sensor-on-the-raspberry-pi/
pin = 4

def main():

	humidity, temperature_c = Adafruit_DHT.read_retry(sensor, pin)
	temperature = 9 * temperature_c / 5 + 32

	if humidity is not None and temperature_c is not None:
		print 'Temp: {0:0.1f}F  Humidity: {1:0.1f}%'.format(temperature, humidity)
	else:
		print('Failed to get reading. Try again!')
		sys.exit(1)

	endpoints_url = "https://graph.api.smartthings.com/api/smartapps/endpoints/%s?access_token=%s" % (client_id, access_token)
	r = requests.get(endpoints_url)
	if (r.status_code != 200):
		print("Error: " + r.status_code)
	else:
		theendpoints = json.loads( r.text )
		for endp in theendpoints:
			uri = endp['uri']
			temp_url = uri + ("/update/temperature/%.2f/F" % temperature)
			humidity_url = uri + ("/update/humidity/%.2f" % humidity)
			headers = { 'Authorization' : 'Bearer ' + access_token }
			r = requests.put(temp_url, headers=headers)
			r = requests.put(humidity_url, headers=headers)
main()
