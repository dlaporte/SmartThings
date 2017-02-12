/**
 *  Rollie Oil Tank Gauge
 *
 *  Version - 0.2
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
 *
 *  Instructions:
 *
 *	1) For US, visit: https://graph.api.smartthings.com
 *	2) For UK, visit: https://graph-eu01-euwest1.api.smartthings.com
 *	3) Click "My Device Handlers"
 *	4) Click "New Device Handler" in the top right
 *	5) Click the "From Code" tab
 *	6) Paste in the code from: https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/rollie-gauge.groovy
 *	7) Click "Create"
 *	8) Click "Publish -> For Me"
 *
 */

include 'asynchttp_v1'

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

        attribute "gallons", "number"
        attribute "level", "string"
    }

    simulator {

    }

	tiles(scale: 2) {

		valueTile("gallons_icon", "device.gallons") {
			state("gallons", label: '${currentValue} gallons', unit: "gal",
				icon: "https://raw.githubusercontent.com/dlaporte/SmartThings/master/DeviceHandlers/rollie-gauge/oil-icon.png",
				backgroundColors: [
					[value: 0, color: "#bc2323"],
					[value: 50, color: "#1e9cbb"],
					[value: 100, color: "#7bb630"]
				]
			)
  		}

		multiAttributeTile(name:"summary", type:"generic", width:6, height:4) {
			tileAttribute("device.gallons", key: "PRIMARY_CONTROL") {
				attributeState("gallons", label: '${currentValue} gallons',
					icon: "https://raw.githubusercontent.com/dlaporte/SmartThings/master/DeviceHandlers/rollie-gauge/oil-drop-icon-png-large.png",
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
		}

		standardTile("today", "today", width: 2, height: 2) {
			state("default", label: "Today")
		}

		valueTile("gallons_today_usage", "device.gallons_today_usage", width: 2, height: 2, decoration: "flat", wordWrap: false) {
	        	state("gallons_today_usage", label: '${currentValue}')
		}
        
		valueTile("level_today_usage", "device.level_today_usage", width: 2, height: 2) {
			state("level_today_usage", label: '${currentValue}')
		}
        
		standardTile("yesterday", "yesterday", width: 2, height: 2) {
			state("default", label: "Yesterday")
		}
        
		valueTile("gallons_yesterday_usage", "device.gallons_yesterday_usage", width: 2, height: 2, decoration: "flat", wordWrap: false) {
			state("gallons_yesterday_usage", label: '${currentValue}')
		}
        
		valueTile("level_yesterday_usage", "device.level_yesterday_usage", width: 2, height: 2) {
	        	state("level_yesterday_usage", label: '${currentValue}')
		}
        
		standardTile("week", "week", width: 2, height: 2) {
			state("default", label: "Last Week")
		}
        
		valueTile("gallons_week_usage", "device.gallons_week_usage", width: 2, height: 2, decoration: "flat", wordWrap: false) {
			state("gallons_week_usage", label: '${currentValue}')
		}
        
		valueTile("level_week_usage", "device.level_week_usage", width: 2, height: 2) {
			state("level_week_usage", label: '${currentValue}')
		}
  
		standardTile("refresh", "device.poll", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state("default", action:"polling.poll", icon:"st.secondary.refresh")
		}

		standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
		    state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}

		valueTile("updated", "device.updated", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
		    state "updated", label: 'Updated:\n${currentValue}'
		}
        
		valueTile("level", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "level", label: '${currentValue} inches'
		}
        
		main "gallons_icon"
  		details(["summary", "today", "gallons_today_usage", "level_today_usage", "yesterday", "gallons_yesterday_usage", "level_yesterday_usage", "week", "gallons_week_usage", "level_week_usage", "updated", "refresh", "configure"])
	}
}

def updated() {
	log.debug("updated with settings: ${settings.inspect()}")
	pullAllControllers()
    pullHistory()
}

def refresh() {
	log.debug "refresh called"
	pullAllControllers()
    pullHistory()
}

def poll() {
	log.debug "poll called"
	pullAllControllers()
	pullHistory()
}

def pullAllControllers() {
    log.debug "pullAllControllers called"
 
	rollieLogin()

	def params = [
		uri: "http://rollieapp.com",
        	path: "/gauges/AllControllers.php",
        	headers: [
			"Cookie": data.cookies
		]
	]

    try {
		asynchttp_v1.get('parseAllControllers', params)
    } catch (e) {
        log.error "something went wrong: $e"
    }
}

def parseAllControllers(response, data) {
	log.debug "parseAllControllers called"
	log.debug "Request was successful, ${response.status}"    
	def m
	if ((m = response.getData() =~ /(?ms)<div class=\'container\'>(.+?)<\/div>/)) {
		String div = m[0][1]

		def xmlParser = new XmlSlurper()
		def table = xmlParser.parseText(div)
        
		def sn = table[0].children[1].children[0].children[15].children[0].text()
		def date = table[0].children[1].children[0].children[15].children[1].text()
		def time = table[0].children[1].children[0].children[15].children[2].text()
		def level_fraction = table[0].children[1].children[0].children[15].children[3].text()
		def gallons = table[0].children[1].children[0].children[15].children[4].text()


		def whole = level_fraction.split('-')
        def fraction = whole[1].split('/')
        def level
		if (fraction[0] == "0" && fraction[1] == "0") {
        	level = new BigDecimal(whole[0])
        } else {
			level = new BigDecimal(whole[0]) + new BigDecimal(fraction[0]) / new BigDecimal(fraction[1])
    	}

        sendEvent(name: 'level', value: "${level}", unit: "inches")
		sendEvent(name: 'gallons', value: "${gallons}", unit: "gallons")
		sendEvent(name: 'date', value: "${date}")
		sendEvent(name: 'time', value: "${time}")
        sendEvent(name: 'sn', value: "${sn}", displayed: false)
		sendEvent(name: 'updated', value: "${date}\n${time}", displayed: false)
        
	} else {
		log.debug "parseAllControllers parse error"
	}
}

def pullHistory() {
    log.debug "pullHistory called"
 
	rollieLogin()

	def params = [
		uri: "http://rollieapp.com",
		path: "/gauges/loadhistory.php",
		query: ["sn": "${settings.serial}", "pid": "11"],
		headers: [
			"Cookie": data.cookies
		]
	]

    try {
		asynchttp_v1.get('parseHistory', params)
    } catch (e) {
        log.error "something went wrong: $e"
    }
}

def parseHistory(response, data) {
	log.debug "parseHistory called"
	log.debug "Request was successful, ${response.status}"
	
	def xmlParser = new XmlSlurper()
	def history = xmlParser.parseText(response.getData())


	def level_history = []
	def gallons_history = []
	def date_history = []
    
    history[0].children[1].children.each {
    	def date = new Date().parse('yyyy-MM-dd hh:mm:ss', it.children[4].text() + " " + it.children[5].text())
    	date_history.add(date)
        
        if (it.children[1].text()) {
			level_history.add(new BigDecimal(it.children[1].text()))
		} else {
        	level_history.add(new BigDecimal(0))
		}
        if (it.children[2].text()) {
			gallons_history.add(new BigDecimal(it.children[2].text()))
		} else {
        	gallons_history.add(new BigDecimal(0))
		}
    }
	if (gallons_history.size >= 1 && gallons_today_usage > 0 && level_today_usage > 0) {
	    def gallons_today_usage = gallons_history[-1] - device.currentValue("gallons")
    	def level_today_usage = level_history[-1] - new BigDecimal(device.currentValue("level"))

		sendEvent(name: 'level_today_usage', value: "${level_today_usage}in", unit: "inches")
		sendEvent(name: 'gallons_today_usage', value: "${gallons_today_usage}gal", unit: "gallons")
	}

	def gallons_yesterday_usage = gallons_history[-2] - gallons_history[-1]
	def level_yesterday_usage = level_history[-2] - level_history[-1]

	if (gallons_history.size >= 2 && gallons_yesterday_usage > 0 && level_yesterday_usage > 0) {
		sendEvent(name: 'level_yesterday_usage', value: "${level_yesterday_usage}in", unit: "inches")
		sendEvent(name: 'gallons_yesterday_usage', value: "${gallons_yesterday_usage}gal", unit: "gallons")
	}

	def gallons_week_usage = gallons_history[-8] - gallons_history[-1]
   	def level_week_usage = level_history[-8] - level_history[-1]

    if (gallons_history.size >= 8 && gallons_week_usage > 0 && level_week_usage > 0) {
		sendEvent(name: 'level_week_usage', value: "${level_week_usage}in", unit: "inches")
		sendEvent(name: 'gallons_week_usage', value: "${gallons_week_usage}gal", unit: "gallons")
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
		query: ["userdata[]": [
			"${settings.password}", "${settings.threshold}",
			"${settings.v_height}", "${settings.v_diameter}",
			"${settings.h_diameter}", "${settings.h_length}",
			"${settings.s_height}", "${settings.s_length}", "${settings.s_width}",
			"${settings.tanktype}", "1", "${settings.email}", "${settings.sms}", "11"]
		],
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
