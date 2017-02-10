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
	input(title: "", description: "Account Configuration", type: "paragraph", element: "paragraph")
	input(name: "serial", type: "text", title: "Gauge Serial Number", required: true, displayDuringSetup: true, capitalization: none)
	input(name: "password", type: "text", title: "Password",  required: true, defaultValue: "rollie", displayDuringSetup: true, capitalization: none)
	input(title: "", description: "Alerting Configuration", type: "paragraph", element: "paragraph")
	input(name: "threshold", type: "number", title: "Low Oil Alert (gallons)", required: true, defaultValue: 15, capitalization: none)
	input(name: "email", type: "email", title: "Alert Email", required: true, displayDuringSetup: true, capitalization: none)
	input(name: "sms", type: "phone", title: "Alert SMS Number (10 digits)", required: true, displayDuringSetup: true, capitalization: none)
	input(title: "", description: "Tank Configuration", type: "paragraph", element: "paragraph")
	input(name: "tanktype", type: "enum", options: ["0": "275 Vertical", "1": "275 Horizontal", "2": "330 Vertical", "3": "330 Horizonal", "4": "Roth 1000L", "5": "Roth 1500L", "6": "Roth 400L", "7": "Vertical Cylinder", "8": "Horizontal Cylinder", "9": "Square Tank"], defaultValue: "0", title: "Tank Type", displayDuringSetup: true)

	input(title: "", description: "Vertical Cylinder Measurements (optional) ", type: "paragraph image", image: "https://raw.githubusercontent.com/dlaporte/ST-Rollie/master/vertical.png", element: "paragraph")
    input(name: "v_height", type: "number", title: "Height", required: false)
	input(name: "v_diameter", type: "number", title: "Diameter", required: false)

	input(title: "", description: "Horizontal Cylinder Measurements (optional) ", type: "paragraph image", image: "https://raw.githubusercontent.com/dlaporte/ST-Rollie/master/horizontal.png", element: "paragraph")
	input(name: "h_length", type: "number", title: "Length", required: false)
	input(name: "h_diameter", type: "number", title: "Diameter", required: false)

	input(title: "", description: "Square Tank Measurements (optional) ", type: "paragraph image", image: "https://raw.githubusercontent.com/dlaporte/ST-Rollie/master/square.png", element: "paragraph")
	input(name: "s_length", type: "number", title: "Length", required: false)
    input(name: "s_width", type: "number", title: "Width", required: false)
	input(name: "s_height", type: "number", title: "Height", required: false)


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

        attribute "gallons", "number"
        attribute "level", "string"
    }

    simulator {

    }

	tiles(scale: 2) {

		valueTile("gallons", "device.gallons") {
			state("gallons", label: '${currentValue} gallons', unit: "gal",
				icon: "https://raw.githubusercontent.com/dlaporte/ST-Rollie/master/oil-icon.png",
				backgroundColors: [
					[value: 0, color: "#bc2323"],
					[value: 50, color: "#1e9cbb"],
					[value: 100, color: "#7bb630"]
				]
			)
  		}

		valueTile("level", "device.level") {
        	state("level", label: '${level} inches', unit: "in")
		}

		multiAttributeTile(name:"summary", type:"generic", width:6, height:4) {
			tileAttribute("device.gallons", key: "PRIMARY_CONTROL") {
				attributeState("gallons", label: '${currentValue} gallons',
					icon: "https://raw.githubusercontent.com/dlaporte/ST-Rollie/master/oil-drop-icon-png-large.png",
					unit: "gal",
                    backgroundColors: [
						[value: 0, color: "#bc2323"],
						[value: 50, color: "#1e9cbb"],
						[value: 100, color: "#7bb630"]
					]
				)
			}
			tileAttribute("device.level", key: "SECONDARY_CONTROL") {
				attributeState("level", label: '${currentValue} inches')
    		}
            			tileAttribute("device.serial", key: "TERTIARY_CONTROL") {
				attributeState("serial", label: '${settings.serial} inches')
    		}

		}
  
		standardTile("refresh", "device.poll", inactiveLabel: false, decoration: "flat", width: 3, height: 3) {
			state("default", action:"polling.poll", icon:"st.secondary.refresh")
		}

		standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat", width: 3, height: 3) {
		    state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}

		main "gallons"
  		details(["summary", "refresh", "configure"])
	}
}

def parse(String description) {
    log.debug "Parsing '${description}'"
}

def poll() {
    log.debug "poll called"
    sendEvent(name: 'level', value: "12", unit: "inches")
    sendEvent(name: 'gallons', value: "147", unit: "gallons")
 
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
            def regex = /(?s)<div class='container'>(.+?)<\/div>/
            if ((match = response.data =~ regex)) {
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

    if (!settings.v_height) {
    	settings.v_height = "n"
	}
	if (!settings.v_diameter) {
    	settings.v_diameter = "n"
	}
	if (!settings.h_length) {
    	settings.h_length = "n"
	}
	if (!settings.h_diameter) {
    	settings.h_diameter = "n"
	}
	if (!settings.s_length) {
    	settings.s_length = "n"
	}
	if (!settings.s_width) {
    	settings.s_width = "n"
	}
	if (!settings.s_height) {
    	settings.s_height = "n"
	}
    
    def params = [
	    uri: "http://rollieapp.com",
        path: "/gauges/updateclient.php",
        query: ["userdata[]": ["${settings.password}", "${settings.threshold}",
        					   "${settings.v_height}", "${settings.v_diameter}",
                               "${settings.h_diameter}", "${settings.h_length}",
                               "${settings.s_height}", "${settings.s_length}", "${settings.s_width}",
                               "${settings.tanktype}", "1", "${settings.email}", "${settings.sms}", "11"]],
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
