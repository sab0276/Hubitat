//Can be used to integrate other systems/devices into Hubitat via 3rd party platforms like IFTTT, Alexa, Webhooks, etc
//Alexa Routines need to use Contact Sensors or Motion Sensors for their Triggers
//so if you need Alexa integration, make sure you enable the Contact or Motion Sensor functions in the preferences
//To add more capabilities check out https://docs.hubitat.com/index.php?title=Driver_Capability_List.  
//Note adding some capabilities like Lock or Door Control may limit where it can be used due to security
//Idea from Mike Maxwell's SmartThings uDTH: https://community.smartthings.com/t/release-universal-virtual-device-type-and-translator/47836

metadata {
    definition (name: "Virtual Switch uDTH", namespace: "sab0276", author: "Scott Barton") {
        capability "Sensor"
        capability "Switch"		//"on", "off"
        capability "Contact Sensor"	//"open", "closed"
        capability "Motion Sensor"	//"active", "inactive"
        capability "Presence Sensor"	//"present", "not present"
        capability "Smoke Detector"    //"detected", "clear", "tested"
        capability "Water Sensor"      //"wet", "dry"
        capability "Shock Sensor"	//"detected", "clear"
        capability "Sleep Sensor"	//"sleeping", "not sleeping"
        capability "Battery"    //100, 0
        //capability "Valve"    //"open", "closed"
	}   
    
    preferences {
        input name: "contact", type: "bool", title: "Contact Sensor", defaultValue: false
        input name: "motion", type: "bool", title: "Motion Sensor", defaultValue: false
        input name: "presence", type: "bool", title: "Presence Sensor", defaultValue: false
        input name: "smoke", type: "bool", title: "Smoke Detector", defaultValue: false
        input name: "water", type: "bool", title: "Water Detector", defaultValue: false
        input name: "shock", type: "bool", title: "Shock Sensor", defaultValue: false
        input name: "sleep", type: "bool", title: "Sleep Sensor", defaultValue: false
        input name: "battery", type: "bool", title: "Battery Sensor (0 or 100)", defaultValue: false
        //input name: "valve", type: "bool", title: "Valve", defaultValue: false
        input name: "autoOff", type: "enum", description: "", title: "Enable auto off", options: [[0:"Disabled"],[1:"1s"],[2:"2s"],[5:"5s"],[10:"10s"],[20:"20s"],[30:"30s"],[60:"1m"],[120:"2m"],[300:"5m"],[1800:"30m"],[3200:"60m"]], defaultValue: 0
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
    if (sleep) sendEvent(name: "sleeping", value: "not sleeping")
    if (battery) sendEvent(name: "battery", value: 0)
    //if (valve) sendEvent(name: "valve", value: "closed")
}

def on() {
    sendEvent(name: "switch", value: "on")
    if (contact) sendEvent(name: "contact", value: "open")
    if (motion) sendEvent(name: "motion", value: "active")
    if (presence) sendEvent(name: "presence", value: "present")
    if (smoke) sendEvent(name: "smoke", value: "detected")
    if (water) sendEvent(name: "water", value: "wet")
    if (shock) sendEvent(name: "shock", value: "detected")
    if (sleep) sendEvent(name: "sleeping", value: "sleeping")
    if (battery) sendEvent(name: "battery", value: 100)
	//if (valve) sendEvent(name: "valve", value: "open")
    
    if (autoOff.toInteger()>0){
        runIn(autoOff.toInteger(), off)
    }
}

def close(){
    off()
}

def open(){
    on()
}

def installed() {
}
