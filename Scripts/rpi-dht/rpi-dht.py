#!/usr/bin/python
  
import sys
import json
import pprint
import requests
import Adafruit_DHT

client = ''
access_token=''

def main():

	sensor = 22
	pin = 4

	humidity, temperature_c = Adafruit_DHT.read_retry(sensor, pin)
	temperature = 9 * temperature_c / 5 + 32

	if humidity is not None and temperature_c is not None:
		print 'Temp: {0:0.1f}F  Humidity: {1:0.1f}%'.format(temperature, humidity)
	else:
		print('Failed to get reading. Try again!')
		sys.exit(1)


	endpoints_url = "https://graph.api.smartthings.com/api/smartapps/endpoints/%s?access_token=%s" % (client, access_token)
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
