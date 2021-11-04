//https://github.com/sab0276/Hubitat/blob/main/virtualSwitchUDTH.groovy
//Can be used to integrate other systems/devices into Hubitat via 3rd party platforms like IFTTT, Alexa, Webhooks, etc
//Alexa Routines need to use Contact Sensors or Motion Sensors for their Triggers
//so if you need Alexa integration, make sure you enable the Contact or Motion Sensor functions in the preferences
//To add more capabilities check out https://docs.hubitat.com/index.php?title=Driver_Capability_List and the VirtualOmniSensor here https://github.com/hubitat/HubitatPublic/tree/master/examples/drivers.  
//Note adding some capabilities like Lock or Door Control may limit where it can be used due to security
//Idea from Mike Maxwell's SmartThings uDTH: https://community.smartthings.com/t/release-universal-virtual-device-type-and-translator/47836

//Force State Update preference will send an event everytime you manually push a form button or app tries to do something with the device.  Ie.  If the device is already On, and an app tries to turn it On, it will send a On/Open/Motion/Present/etc event. 

//ADJUST CAPABILITIES HERE.  You can install multiple versions of this code as long as the values below are different. The Device Type Name will automatically be updated. ie One Device Type that just has On/Off, One Device Type for a virtual switch with Locks, or Buttons functionality, etc.  
//Use the least amount of capabilities needed for your device/integration.  Will be more effecient, and less prone to restrictions.  
//If you only need, SWITCH, CONTACT, MOTION, and/or PRESENCE, use my Virtual Switch uDTH Lite device driver for that device instead.  Much more effecient and less restrictions.  
def doValves = true //Set to true to allow Valve functionality.  
def doButtons = false //Set to true to allow Button functionality.
def doLocks = false //Set to true to allow Lock functionality.  This may impact your ability to use this device driver for certain integrations due to security.  
def doDoorControl = true //Set to true to allow Garage/Door Control functionality.  This may impact your ability to use this device driver for certain integrations due to security.


def DTName = "Virtual Switch uDTH Super"
if (doButtons) DTName = DTName + " +Buttons"
if (doLocks) DTName = DTName + " +Locks"
if (doValves) DTName = DTName + " +Valves"
if (doDoorControl) DTName = DTName + " +Door"

metadata {
    definition (name: DTName, namespace: "sab0276", author: "Scott Barton") {
        capability "Sensor"
        capability "Actuator"
        capability "Configuration"
        //capability "Refresh"
        capability "Switch"		//"on", "off"
        capability "Contact Sensor"	//"open", "closed"
        capability "Motion Sensor"	//"active", "inactive"
        capability "Presence Sensor"	//"present", "not present"
        
        capability "Smoke Detector"    //"detected", "clear", "tested"
        capability "Water Sensor"      //"wet", "dry"
	    capability "SoundSensor"	//"detected", "not detected"
        capability "Acceleration Sensor" //"active", "inactive"
        capability "Shock Sensor"	//"detected", "clear"
        capability "Sleep Sensor"	//"sleeping", "not sleeping"
        capability "Battery"    //100, 0
        capability "FilterStatus"    //"replace", "normal"
        capability "TamperAlert" //"detected", "clear"
        capability "IlluminanceMeasurement" //1000, 0
          
        if (doValves) capability "Valve"   //"open", "closing", "closed", "opening"
        if (doLocks) capability "Lock" // "locked", "locked"
        if (doDoorControl){
        	capability "DoorControl"  //"open", "closing", "closed", "opening"
        	capability "GarageDoorControl" //"open", "closing", "closed", "opening"
        }
        if (doButtons){
            capability "PushableButton"
            capability "ReleasableButton"
            capability "HoldableButton"
           
            attribute "numberOfButtons", "NUMBER"
            attribute "currentButton", "NUMBER"
        }     
    }
   
    
    preferences {
        input name: "contact", type: "bool", title: "Contact Sensor", defaultValue: false
        input name: "motion", type: "bool", title: "Motion Sensor", defaultValue: false
        input name: "presence", type: "bool", title: "Presence Sensor", defaultValue: false
        
        input name: "smoke", type: "bool", title: "Smoke Detector", defaultValue: false
        input name: "water", type: "bool", title: "Water Detector", defaultValue: false
		input name: "sound", type: "bool", title: "Sound Sensor", defaultValue: false
        input name: "acceleration", type: "bool", title: "Acceleration Sensor", defaultValue: false
        input name: "shock", type: "bool", title: "Shock Sensor", defaultValue: false
        input name: "tamper", type: "bool", title: "Tamper Sensor", defaultValue: false
        input name: "sleep", type: "bool", title: "Sleep Sensor", defaultValue: false
        input name: "filter", type: "bool", title: "Filter Status", defaultValue: false
        input name: "battery", type: "bool", title: "Battery Sensor (0 or 100)", defaultValue: false
        input name: "illuminance", type: "bool", title: "Illuminance Sensor (0 or 1000)", defaultValue: false
            
        if(doButtons) input name: "button", type: "bool", title: "Button", defaultValue: false
        if (doValves) input name: "valve", type: "bool", title: "Valve", defaultValue: false
        if (doDoorControl) input name: "door", type: "bool", title: "Garage or Door Control", defaultValue: false
        if (doDoorControl || doValves) input name: "openingTime", type: "enum", description: "How long it takes for door/valve to open or close.", title: "Opening Time", options: [[0:"0s"],[1:"1s"],[2:"2s"],[3:"3s"],[4:"4s"],[5:"5s"],[6:"6s"],[7:"7s"],[8:"8s"],[9:"9s"],[10:"10s"],[11:"11s"],[12:"12s"],[13:"13s"],[14:"14s"],[15:"15s"],[20:"20s"],[30:"30s"]], defaultValue: 0
        if (doLocks) input name: "bLock", type: "bool", title: "Lock", defaultValue: false
	    
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
        input name: "forceUpdate", type: "bool", title: "Force State Update", description: "Send event everytime, regardless of current status. ie Send/Do On even if already On.",  defaultValue: false
        input name: "autoOff", type: "enum", description: "Automatically turns off the device after selected time.", title: "Enable Auto-Off", options: [[0:"Disabled"],[1:"1s"],[2:"2s"],[5:"5s"],[10:"10s"],[20:"20s"],[30:"30s"],[60:"1m"],[120:"2m"],[300:"5m"],[1800:"30m"],[3200:"60m"]], defaultValue: 0
    }
}

def off() {
    sendEvent(name: "switch", value: "off", isStateChange: forceUpdate)
    if (contact && !door && !valve) sendEvent(name: "contact", value: "closed", isStateChange: forceUpdate)
    if (motion) sendEvent(name: "motion", value: "inactive", isStateChange: forceUpdate)
    if (presence) sendEvent(name: "presence", value: "not present", isStateChange: forceUpdate)

    if (acceleration) sendEvent(name: "acceleration", value: "inactive", isStateChange: forceUpdate)
    if (smoke) sendEvent(name: "smoke", value: "clear", isStateChange: forceUpdate)
    if (water) sendEvent(name: "water", value: "dry", isStateChange: forceUpdate)
    if (sound) sendEvent(name: "sound", value: "not detected", isStateChange: forceUpdate)
    if (shock) sendEvent(name: "shock", value: "clear", isStateChange: forceUpdate)
    if (tamper) sendEvent(name: "tamper", value: "clear", isStateChange: forceUpdate)
    if (sleep) sendEvent(name: "sleeping", value: "not sleeping", isStateChange: forceUpdate)
    if (battery) sendEvent(name: "battery", value: 0, isStateChange: forceUpdate)
    if (illuminance) sendEvent(name: "illuminance", value: 0, isStateChange: forceUpdate)
    if (filter) sendEvent(name: "filterStatus", value: "normal", isStateChange: forceUpdate)
    
    if (valve && (device.currentValue('valve') != "closing" && device.currentValue('valve') != "closed")) {
        sendEvent(name: "valve", value: "closing", isStateChange: forceUpdate)
        runIn(openingTime.toInteger(), closed)
    }
    if (door && (device.currentValue('door') != "closing" && device.currentValue('door') != "closed")) {
    	sendEvent(name: "door", value: "closing", isStateChange: forceUpdate)
    	runIn(openingTime.toInteger(), closed)
    }
	if (bLock) sendEvent(name: "lock", value: "unlocked", isStateChange: forceUpdate)
    if (button){
        sendEvent(name: "released", value: state.currButton, isStateChange: true)
        sendEvent(name: "currentButton", value: state.currButton, isStateChange: true)
    }
    logTxt "turned Off"
}

def on() {
    sendEvent(name: "switch", value: "on", isStateChange: forceUpdate)
    if (contact) sendEvent(name: "contact", value: "open", isStateChange: forceUpdate)
    if (motion) sendEvent(name: "motion", value: "active", isStateChange: forceUpdate)
    if (presence) sendEvent(name: "presence", value: "present", isStateChange: forceUpdate)
    
    if (acceleration) sendEvent(name: "acceleration", value: "active", isStateChange: forceUpdate)
    if (smoke) sendEvent(name: "smoke", value: "detected", isStateChange: forceUpdate)
    if (water) sendEvent(name: "water", value: "wet", isStateChange: forceUpdate)
    if (sound) sendEvent(name: "sound", value: "detected", isStateChange: forceUpdate)
    if (shock) sendEvent(name: "shock", value: "detected", isStateChange: forceUpdate)
    if (tamper) sendEvent(name: "tamper", value: "detected", isStateChange: forceUpdate)
    if (sleep) sendEvent(name: "sleeping", value: "sleeping", isStateChange: forceUpdate)
    if (battery) sendEvent(name: "battery", value: 100, isStateChange: forceUpdate)
    if (illuminance) sendEvent(name: "illuminance", value: 1000, isStateChange: forceUpdate)
    if (filter) sendEvent(name: "filterStatus", value: "replace", isStateChange: forceUpdate)
	
    if (valve && (device.currentValue('valve') != "opening" && device.currentValue('valve') != "open")) {
        sendEvent(name: "valve", value: "opening", isStateChange: forceUpdate)
        runIn(openingTime.toInteger(), opened)
    }
    if (door && (device.currentValue('door') != "opening" && device.currentValue('door') != "open")) {
    	sendEvent(name: "door", value: "opening", isStateChange: forceUpdate)
    	runIn(openingTime.toInteger(), opened)
    }
    if (bLock) sendEvent(name: "lock", value: "locked", isStateChange: forceUpdate)
    if (button){
        if (state.currButton == null) state.currButton = 1 
        sendEvent(name: "pushed", value: state.currButton, isStateChange: true)
        sendEvent(name: "held", value: state.currButton, isStateChange: true)
        sendEvent(name: "currentButton", value: state.currButton, isStateChange: true)        
    }
    
    if (autoOff.toInteger()>0){
        runIn(autoOff.toInteger(), off)
    }
    logTxt "turned On"
}

def closed(){
	if (device.currentValue('door') == "closing") sendEvent(name: "door", value: "closed", isStateChange: forceUpdate)
    if (device.currentValue('valve') == "closing") sendEvent(name: "valve", value: "closed", isStateChange: forceUpdate)
    if (device.currentValue('contact') == "open" && device.currentValue('door') != "opening" && device.currentValue('valve') != "opening")  sendEvent(name: "contact", value: "closed", isStateChange: forceUpdate)
}

def opened(){
	if (device.currentValue('door') == "opening") sendEvent(name: "door", value: "open", isStateChange: forceUpdate)
    if (device.currentValue('valve') == "opening") sendEvent(name: "valve", value: "open", isStateChange: forceUpdate)
    if (device.currentValue('contact') == "closed") sendEvent(name: "contact", value: "open", isStateChange: forceUpdate)
}


def close(){
    off()
    logTxt "Closed"
}

def open(){
    on()
    logTxt "Opened"
}

def unlock(){
    off()
    logTxt "Unlocked"
}

def lock(){
    on()
    logTxt "Locked"
}

def setNumButtons(btnNum){
   if (btnNum > device.currentValue("numberOfButtons")) sendEvent(name:"numberOfButtons", value: btnNum)
}

def push(btnNum){
    setNumButtons(btnNum)
    state.currButton = btnNum
    on()
}

def hold(btnNum){
    setNumButtons(btnNum)
    state.currButton = btnNum
    on()
}

def release(btnNum){
    setNumButtons(btnNum)
    state.currButton = btnNum
    off()
}

def installed() {
}


void logTxt(String msg) {
	if (logEnable) log.info "${device.displayName} ${msg}"
}

//Use only if you are on 2.2.8.141 or later.  device.deleteCurrentState() is new to that version and will not work on older versions.  
def configure(){
    if (button) {
        state.currButton = 1
        sendEvent(name:"numberOfButtons", value: 1)
        //sendEvent(name: "currentButton", value: 1, isStateChange: true)
    }   
    if (device.currentValue("switch") != null) device.deleteCurrentState("switch")
    if (device.currentValue("contact") != null) device.deleteCurrentState("contact")
    if (device.currentValue("motion") != null) device.deleteCurrentState("motion")
    if (device.currentValue("presence") != null) device.deleteCurrentState("presence")
    
    if (device.currentValue("acceleration") != null) device.deleteCurrentState("acceleration")
    if (device.currentValue("smoke") != null) device.deleteCurrentState("smoke")
    if (device.currentValue("water") != null) device.deleteCurrentState("water")
    if (device.currentValue("sound") != null) device.deleteCurrentState("sound")
    if (device.currentValue("shock") != null) device.deleteCurrentState("shock")
    if (device.currentValue("sleeping") != null) device.deleteCurrentState("sleeping")
    if (device.currentValue("battery") != null) device.deleteCurrentState("battery")
    if (device.currentValue("illuminance") != null) device.deleteCurrentState("illuminance")
    if (device.currentValue("tamper") != null) device.deleteCurrentState("tamper")
    if (device.currentValue("filterStatus") != null) device.deleteCurrentState("filterStatus")
	
    if (device.currentValue("valve") != null) device.deleteCurrentState("valve")
    if (device.currentValue("door") != null) device.deleteCurrentState("door")
    if (device.currentValue("lock") != null) device.deleteCurrentState("lock")
    if (device.currentValue("pushed") != null) device.deleteCurrentState("pushed")
    if (device.currentValue("held") != null) device.deleteCurrentState("held")
    if (device.currentValue("released") != null) device.deleteCurrentState("released")
    if (device.currentValue("currentButton") != null) device.deleteCurrentState("currentButton")   
    logTxt "configured. State values reset."  
}
