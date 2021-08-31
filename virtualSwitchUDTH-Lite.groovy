//https://github.com/sab0276/Hubitat/blob/main/virtualSwitchUDTH-Lite.groovy
//Can be used to integrate other systems/devices into Hubitat via 3rd party platforms like IFTTT, Alexa, Webhooks, etc
//Alexa Routines need to use Contact Sensors or Motion Sensors for their Triggers
//so if you need Alexa integration, make sure you enable the Contact or Motion Sensor functions in the preferences
//Note adding some capabilities like Lock or Door Control may limit where it can be used due to security
//Idea from Mike Maxwell's SmartThings uDTH: https://community.smartthings.com/t/release-universal-virtual-device-type-and-translator/47836
//If you need more than just SWITCH, CONTACT, MOTION, and/or PRESENCE, use my Virtual Switch uDTH Super device driver for that device instead.    

//Force State Update preference will send an event everytime you manually push a form button or app tries to do something with the device.  Ie.  If the device is already On, and an app tries to turn it On, it will send a On/Open/Motion/Present event. 

metadata {
    definition (name: "Virtual Switch uDTH Lite", namespace: "sab0276", author: "Scott Barton") {
        capability "Sensor"
        capability "Actuator"
        capability "Configuration"
        //capability "Refresh"
        capability "Switch"		//"on", "off"
        capability "Contact Sensor"	//"open", "closed"
        capability "Motion Sensor"	//"active", "inactive"
        capability "Presence Sensor"	//"present", "not present"        
 	}   
    
    preferences {
        input name: "contact", type: "bool", title: "Contact Sensor", defaultValue: false
        input name: "motion", type: "bool", title: "Motion Sensor", defaultValue: false
        input name: "presence", type: "bool", title: "Presence Sensor", defaultValue: false
        
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
        input name: "forceUpdate", type: "bool", title: "Force State Update", description: "Send event everytime, regardless of current status. ie Send/Do On even if already On.",  defaultValue: false
        input name: "autoOff", type: "enum", description: "Automatically turns off the device after selected time.", title: "Enable Auto-Off", options: [[0:"Disabled"],[1:"1s"],[2:"2s"],[5:"5s"],[10:"10s"],[20:"20s"],[30:"30s"],[60:"1m"],[120:"2m"],[300:"5m"],[1800:"30m"],[3200:"60m"]], defaultValue: 0
    } 
}

def off() {
    sendEvent(name: "switch", value: "off", isStateChange: forceUpdate)
    if (contact) sendEvent(name: "contact", value: "closed", isStateChange: forceUpdate)
    if (motion) sendEvent(name: "motion", value: "inactive", isStateChange: forceUpdate)
    if (presence) sendEvent(name: "presence", value: "not present", isStateChange: forceUpdate)
    logTxt "turned Off"
}

def on() {
    sendEvent(name: "switch", value: "on", isStateChange: forceUpdate)
    if (contact) sendEvent(name: "contact", value: "open", isStateChange: forceUpdate)
    if (motion) sendEvent(name: "motion", value: "active", isStateChange: forceUpdate)
    if (presence) sendEvent(name: "presence", value: "present", isStateChange: forceUpdate)
    logTxt "turned On"
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

void logTxt(String msg) {
	if (logEnable) log.info "${device.displayName} ${msg}"
}

//Use only if you are on 2.2.8.141 or later.  device.deleteCurrentState() is new to that version and will not work on older versions.  
def configure(){  
    if (device.currentValue("contact") != null) device.deleteCurrentState("contact")
    if (device.currentValue("motion") != null) device.deleteCurrentState("motion")
    if (device.currentValue("presence") != null) device.deleteCurrentState("presence")
    logTxt "configured. State values reset."
}
