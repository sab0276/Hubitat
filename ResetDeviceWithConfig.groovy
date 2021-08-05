//Can be used to integrate other systems/devices into Hubitat via 3rd party platforms like IFTTT, Alexa, Webhooks, etc
//Alexa Routines need to use Contact Sensors or Motion Sensors for their Triggers
//so if you need Alexa integration, make sure you enable the Contact or Motion Sensor functions in the preferences
//Note adding some capabilities like Lock or Door Control may limit where it can be used due to security
//Idea from Mike Maxwell's SmartThings uDTH: https://community.smartthings.com/t/release-universal-virtual-device-type-and-translator/47836
//If you need more than just SWITCH, CONTACT, MOTION, and/or PRESENCE, use my Virtual Switch uDTH Super device driver for that device instead.    

//Force State Update preference will send an event everytime you manually push a form button or app tries to do something with the device.  Ie.  If the device is already On, and an app tries to turn it On, it will send a On/Open/Motion/Present event. 

metadata {
    definition (name: "Reset Device with Config", namespace: "sab0276", author: "Scott Barton") {
        capability "Sensor"
        capability "Actuator"
        capability "Configuration"
        capability "Refresh"
        capability "Switch"		//"on", "off"
        capability "Contact Sensor"	//"open", "closed"
        capability "Motion Sensor"	//"active", "inactive"
        capability "Presence Sensor"	//"present", "not present"
        capability "Smoke Detector"    //"detected", "clear", "tested"
        capability "Water Sensor"      //"wet", "dry"
        capability "Acceleration Sensor" //"active", "inactive"
        capability "Shock Sensor"	//"detected", "clear"
        capability "Sleep Sensor"	//"sleeping", "not sleeping"
        capability "Battery"    //100, 0
        capability "FilterStatus"    //"replace", "normal"
        capability "TamperAlert" //"detected", "clear"
        capability "IlluminanceMeasurement" //1000, 0
        capability "SoundSensor"
        
        capability "Valve"    //"open", "closed"
        capability "Lock" // "locked", "locked"
        capability "DoorControl"  //"open", "closed"
      
        capability "PushableButton"
        capability "ReleasableButton"
        capability "HoldableButton"
        
        capability "Alarm"
        capability "CarbonMonoxideDetector"
        capability "ColorControl"
        capability "ColorMode"
        capability "ColorTemperature"
        capability "CurrentMeter"
        capability "DoubleTapableButton"
        capability "EnergyMeter"
        capability "FanControl"
        capability "GasDetector"
        capability "HealthCheck"
        capability "Indicator"
        capability "LevelPreset"
        capability "LockCodes"
        capability "PowerMeter"
        capability "PowerSource"
        capability "RelativeHumidityMeasurement"
        capability "TemperatureMeasurement"
        capability "Thermostat"
        capability "ThreeAxis"
        capability "TouchSensor"
        capability "VoltageMeasurement"
        capability "WindowBlind"
        capability "pHMeasurement"
        
 	}   
    
    preferences {
        input name: "forceUpdate", type: "bool", title: "Force State Update", description: "Send event everytime, regardless of current status. ie Send/Do On even if already On.",  defaultValue: false
    }
}

def off() {
    sendEvent(name: "switch", value: "off", isStateChange: forceUpdate)
}

def on() {
    sendEvent(name: "switch", value: "on", isStateChange: forceUpdate)
}




def setColor() {
}

def lock() {
}

def fanOn() {
}

def siren() {
}

def unlock() {
}

def strobe() {
}

def cycleSpeed() {
}

def cool() {
}

def auto() {
}

def both() {
}

def close() {
}

def open() {
}

def push(btnNum) {
}

def hold(btnNum) {
}

def release(btnNum) {
}



def installed() {
}


//Use only if you are on 2.2.8.141 or later.  device.deleteCurrentState() is new to that version and will not work on older versions.  
def configure(){  

    unschedule()
    state.clear()
    //device.deleteCurrentState("contact")
    //state.remove("isDigital")
    if (device.currentValue("switch") != null) device.deleteCurrentState("switch")
    if (device.currentValue("contact") != null) device.deleteCurrentState("contact")
    if (device.currentValue("motion") != null) device.deleteCurrentState("motion")
    if (device.currentValue("presence") != null) device.deleteCurrentState("presence")
    if (device.currentValue("acceleration") != null) device.deleteCurrentState("acceleration")
    if (device.currentValue("smoke") != null) device.deleteCurrentState("smoke")
    if (device.currentValue("water") != null) device.deleteCurrentState("water")
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
    if (device.currentValue("numberOfButtons") != null) device.deleteCurrentState("numberOfButtons")
    if (device.currentValue("holdableButton") != null) device.deleteCurrentState("holdableButton")
    if (device.currentValue("button") != null) device.deleteCurrentState("button")
    if (device.currentValue("doubleTapped") != null) device.deleteCurrentState("doubleTapped")
  
    if (device.currentValue("alarm") != null) device.deleteCurrentState("alarm")
    if (device.currentValue("carbonMonoxide") != null) device.deleteCurrentState("carbonMonoxide")
    if (device.currentValue("status") != null) device.deleteCurrentState("status")
    if (device.currentValue("RGB") != null) device.deleteCurrentState("RGB")
    if (device.currentValue("color") != null) device.deleteCurrentState("color")
    if (device.currentValue("colorName") != null) device.deleteCurrentState("colorName")
    if (device.currentValue("hue") != null) device.deleteCurrentState("hue")
    if (device.currentValue("saturation") != null) device.deleteCurrentState("saturation")
    if (device.currentValue("colorMode") != null) device.deleteCurrentState("colorMode")
    if (device.currentValue("colorTemperature") != null) device.deleteCurrentState("status")
    if (device.currentValue("amperage") != null) device.deleteCurrentState("amperage")
    if (device.currentValue("energy") != null) device.deleteCurrentState("energy")
    if (device.currentValue("speed") != null) device.deleteCurrentState("speed")
    if (device.currentValue("supportedFanSpeeds") != null) device.deleteCurrentState("supportedFanSpeeds")
    if (device.currentValue("naturalGas") != null) device.deleteCurrentState("naturalGas")
    if (device.currentValue("checkInterval") != null) device.deleteCurrentState("checkInterval")
    if (device.currentValue("indicatorStatus") != null) device.deleteCurrentState("indicatorStatus")
    if (device.currentValue("levelPreset") != null) device.deleteCurrentState("levelPreset")
    if (device.currentValue("mode") != null) device.deleteCurrentState("mode")
    if (device.currentValue("codeChanged") != null) device.deleteCurrentState("codeChanged")
    if (device.currentValue("codeLength") != null) device.deleteCurrentState("codeLength")
    if (device.currentValue("lockCodes") != null) device.deleteCurrentState("lockCodes")
    if (device.currentValue("maxCodes") != null) device.deleteCurrentState("maxCodes")
    if (device.currentValue("securityKeypad") != null) device.deleteCurrentState("securityKeypad")
    if (device.currentValue("armingIn") != null) device.deleteCurrentState("armingIn")
    if (device.currentValue("level") != null) device.deleteCurrentState("level")
    if (device.currentValue("power") != null) device.deleteCurrentState("power")
    if (device.currentValue("powerSource") != null) device.deleteCurrentState("powerSource")
    if (device.currentValue("humidity") != null) device.deleteCurrentState("humidity")
    if (device.currentValue("volume") != null) device.deleteCurrentState("volume")
    if (device.currentValue("sound") != null) device.deleteCurrentState("sound")
    if (device.currentValue("mute") != null) device.deleteCurrentState("mute")
    if (device.currentValue("temperature") != null) device.deleteCurrentState("temperature")
    if (device.currentValue("coolingSetpoint") != null) device.deleteCurrentState("coolingSetpoint")
    if (device.currentValue("heatingSetpoint") != null) device.deleteCurrentState("heatingSetpoint")
    if (device.currentValue("schedule") != null) device.deleteCurrentState("schedule")
    if (device.currentValue("supportedThermostatFanModes") != null) device.deleteCurrentState("supportedThermostatFanModes")
    if (device.currentValue("supportedThermostatModes") != null) device.deleteCurrentState("supportedThermostatModes")
    if (device.currentValue("temperature") != null) device.deleteCurrentState("temperature")
    if (device.currentValue("thermostatFanMode") != null) device.deleteCurrentState("thermostatFanMode")
    if (device.currentValue("thermostatMode") != null) device.deleteCurrentState("thermostatMode")
    if (device.currentValue("thermostatOperatingState") != null) device.deleteCurrentState("thermostatOperatingState")
    if (device.currentValue("thermostatSetpoint") != null) device.deleteCurrentState("thermostatSetpoint")
    if (device.currentValue("threeAxis") != null) device.deleteCurrentState("threeAxis")
    if (device.currentValue("touch") != null) device.deleteCurrentState("touch")
    if (device.currentValue("voltage") != null) device.deleteCurrentState("NAME")
    if (device.currentValue("frequency") != null) device.deleteCurrentState("NAME")
    if (device.currentValue("position") != null) device.deleteCurrentState("position")
    if (device.currentValue("windowShade") != null) device.deleteCurrentState("windowShade")
    if (device.currentValue("tilt") != null) device.deleteCurrentState("tilt")
    if (device.currentValue("pH") != null) device.deleteCurrentState("pH")
    if (device.currentValue("mediaInputSource") != null) device.deleteCurrentState("mediaInputSource")
    if (device.currentValue("lastUpdate") != null) device.deleteCurrentState("lastUpdate")
    //if (device.currentValue("syncStatus") != null) device.deleteCurrentState("syncStatus")
    
}
