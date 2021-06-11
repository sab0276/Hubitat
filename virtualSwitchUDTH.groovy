//Can be used to integrate other systems/devices into Hubitat via 3rd party platforms like IFTTT, Alexa, Webhooks, etc
//Alexa Routines need to use Contact Sensors or Motion Sensors for their Triggers
//so if you need Alexa integration, make sure you enable the Contact or Motion Sensor functions in the preferences

metadata {
	definition (name: "Virtual Switch uDTH", namespace: "sab0276", author: "Scott Barton") {
        capability "Sensor"
        capability "Switch"		//"on", "off"
	capability "Contact Sensor"	//"open", "closed"
        capability "Motion Sensor"	//"active", "inactive"
        capability "Presence Sensor"	//"present", "not present"
        capability "Smoke Detector"    //"detected", "clear", "tested"
        capability "Water Sensor"      //"dry", "wet"
	capability "ShockSensor"	//"clear", "detected"	
	}   
    
    preferences {
        input name: "contact", type: "bool", title: "Contact Sensor", defaultValue: false
        input name: "motion", type: "bool", title: "Motion Sensor", defaultValue: false
        input name: "presence", type: "bool", title: "Presence Sensor", defaultValue: false
        input name: "smoke", type: "bool", title: "Smoke Detector", defaultValue: false
        input name: "water", type: "bool", title: "Water Detector", defaultValue: false
	input name: "shock", type: "bool", title: "Shock Sensor", defaultValue: false
        input name: "autoOff", type: "enum", description: "", title: "Enable auto off", options: [[0:"Disabled"],[1:"1s"],[2:"2s"],[3:"3s"],[4:"4s"],[5:"5s"],[6:"6s"],[7:"7s"],[8:"8s"],[9:"9s"]], defaultValue: 0
    }
}

def off() {
    sendEvent(name: "switch", value: "off")
    if (contact) sendEvent(name: "contact", value: "closed")
    if (motion) sendEvent(name: "motion", value: "inactive")
    if (presence) sendEvent(name: "presence", value: "not present")
    if (smoke) sendEvent(name: "smoke", value: "clear")
    if (water) sendEvent(name: "water", value: "dry")
    if (shock) sendEvent(name: "shock", value: "clear")
}

def on() {
    sendEvent(name: "switch", value: "on")
    if (contact) sendEvent(name: "contact", value: "open")
    if (motion) sendEvent(name: "motion", value: "active")
    if (presence) sendEvent(name: "presence", value: "present")
    if (smoke) sendEvent(name: "smoke", value: "detected")
    if (water) sendEvent(name: "water", value: "wet")
    if (shock) sendEvent(name: "shock", value: "detected")
	
    if (autoOff.toInteger()>0){
        runIn(autoOff.toInteger(), off)
    }
}


def installed() {
}
