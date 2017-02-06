/**
 *  Rollie Oil Gauge
 *
 *  Version - 0.1
 *
 *  Copyright 2017 David LaPorte
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *  Instructions:
 *   - stuff
 */



preferences {
    input(name: "serial", type: "text", title: "Gauge Serial Number", required: true, capitalization: none)
    input(name: "password", type: "text", title: "Password",  required: true, defaultValue: "rollie", capitalization: none)
    input(name: "tanktype", type: "enum", options: ["0": "275 Vertical", "1": "275 Horizontal", "2": "330 Vertical", "3": "330 Horizonal", "4": "Roth 1000L", "5": "Roth 1500L", "6": "Roth 400L", "7": "Vertical Cylinder", "8": "Horizontal Cylinder", "9": "Square Tank"], defaultValue: "0", title: "Tank Type")
    input(name: "threshold", type: number, title: "Low Oil Alert (in gallons)", required: true, defaultValue: 37, capitalization: none)
    input(name: "email", type: email, title: "Alert Email", required: true, capitalization: none)
    input(name: "sms", type: phone, title: "Alert SMS Number (10 digits)", required: true, capitalization: none)
    }

metadata {
    definition (name: "Rollie Oil Tank Gauge", namespace: "dlaporte", author: "David LaPorte") {
        capability "Configuration"
        capability "Polling"
        capability "Sensor"
        capability "Refresh"

        //capability "Relative Humidity Measurement"
        //capability "Temperature Measurement"
        //capability "Illuminance Measurement"

        attribute "level", "string"
        attribute "gallons", "number"
        attribute "percent", "number"
    }

    simulator {

    }

    tiles {
        valueTile("level", "device.level", width: 1, height: 1, canChangeIcon: false) {
            state "level", label: '${currentValue}in', unit:"inches"
        }
        valueTile("percent", "device.level", inactiveLabel: false) {
            state "default", label:'${currentValue}%', unit:"%"
        }
        valueTile("gallons", "device.gallons", inactiveLabel: false) {
            state "default", label:'${currentValue} gallons', unit:" gallons"
        }
        standardTile("refresh", "device.poll", inactiveLabel: false, decoration: "flat") {
            state "default", action:"polling.poll", icon:"st.secondary.refresh"
        }
		standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat") {
		    state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}

        main "percent"
        details(["level", "gallons", "percent", "refresh", "configure"])
    }
}

def parse(String description) {
    log.debug "Parsing '${description}'"
}

def poll() {
    log.debug "Executing 'poll'"
    //sendEvent(name: 'level', value: "10", unit: "inches")
    //sendEvent(name: 'percent', value: "30", unit: "%")
 
	rollieLogin()

	def params = [
		uri: "http://rollieapp.com",
        	path: "/gauges/AllControllers.php",
        	headers: [
	          "Cookie": data.cookies
        	]
	]

    try {
        httpGet(params) { response ->
            log.debug "Request was successful, ${response.status}"
            def match
            if ((match = response.data =~ /(?s)<div class=\'container\'>(.+?)<\/div>/)) {
                log.debug "hello world"
        
                def div = match[0][1]
                def xmlParser = new XmlSlurper()
                def html = xmlParser.parseText(div)

                def sn = div.table.tbody.td[0].text()
                def date = div.table.tbody.td[1].text()
                def time = div.table.tbody.td[2].text()
                def level = div.table.tbody.td[3].text()
                def gallons = div.table.tbody.td[4].text()

                log.debug "gallons: $gallons"
            } else {
            	log.debug "NO MATCH"
            }
		}
    } catch (e) {
        log.error "something went wrong: $e"
    }
}

def rollieLogin() {
    log.debug "rollieLogin called"
    def params = [
        uri: "http://rollieapp.com",
        path: "/gauges"
    ]

    data.cookies = ''

	try {
        httpGet(params) { response ->
            log.debug "Request was successful, ${response.status}"
            response.getHeaders('Set-Cookie').each {
                String cookie = it.value.split(';')[0]
                log.debug "Adding cookie to collection: $cookie"
                data.cookies = data.cookies + cookie + ';'
            }
        }
    } catch (e) {
        log.error "something went wrong: $e"
    }

	params = [
        uri: "http://rollieapp.com",
        path: "/gauges/login.php",
        query: ["loginsn": "${settings.serial}", "password": "${settings.password}"],
        headers: [
          "Cookie": data.cookies
		]
	]
    try {    
        httpGet(params) { response ->
            log.debug "Request was successful, ${response.status}"
            def doc = response.data
            log.debug "login status: ${doc.text()}"
		}
	} catch (e) {
    	log.error "something went wrong: $e"
	}
}

def configure() {
	log.debug "configure called"
    rollieLogin()
    def params = [
	    uri: "http://rollieapp.com",
        path: "/gauges/updateclient.php",
        query: ["userdata[]": ["${settings.password}", "${settings.threshold}", "n", "n", "n", "n", "n", "n", "n", "${settings.tanktype}", "1", "${settings.email}", "${settings.sms}", "11"]],
        headers: [
          "Cookie": data.cookies
        ]
	]
	try {
        httpGet(params) { response ->
            log.debug "Request was successful, ${response.status}"
            def doc = response.data
            log.debug "configure status: ${doc.text()}"
        }
    } catch (e) {
        log.error "something went wrong: $e"
    }
}
