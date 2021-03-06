'''
Created on 2018年9月15日
 
@author: heychris
'''

from random import uniform
from time import sleep
from threading import Thread
from labs.common.SensorData import SensorData
import sense_hat
from labs.common import  ConfigUtil


class TempSensorAdaptor(Thread): 
    alertDiff = 5 
    curTemp = 0
    max_value = 0
    min_value = 0
    enableTempEmulator = False
    maxUpdateTime = 0
    minUpdatetime = 0
    tempSensorData = SensorData("Temperature")
    emailSender = None
    isPrevTempSet = True
    rateInSec = 1
    sh = None
    deviceConfigReader = None
    nominalTemp = 0

# constructor 
    def __init__(self, max_temp, min_temp, max_updateTime, min_updateTime, configDir):
        Thread.__init__(self)
        self.max_value = max_temp
        self.min_value = min_temp
        self.maxUpdateTime = max_updateTime
        self.minUpdatetime = min_updateTime
#         get the data of device like: enableEmulator,nominalTemp...
        self.deviceConfigReader = ConfigUtil.ConfigUtil(configDir, "device")
        self.enableTempEmulator = self.deviceConfigReader.getProperty("enableEmulator")
        self.nominalTemp = self.deviceConfigReader.getProperty("nominalTemp")
#         if we chose sensehat, we should connect to sensehat part, dont add temperature by uniform function
        self.sh = sense_hat.SenseHat()

    def getCurrValue(self):
        return self.curTemp
    
#     TempSensorEmulator thread which  gets newTemp around min / max updatetime(In this part we set the time is 1 sec)
#      thread to calculate the curVal . then send the data to MQtt broker if temperature different 

    def run(self):
        while True:
            if self.enableTempEmulator == "True":
                if self.isPrevTempSet == False:
                    # corner code
                    self.prevTemp = self.curTemp
                    self.isPrevTempSet = True
                else:
                    self.curTemp = int(uniform(int(self.min_value), int(self.max_value)))
                    self.tempSensorData.addNewValue(self.curTemp)      
            else:
                print("using Sensor_Hat")
                self.curTemp = int(self.sh.get_temperature())
                self.tempSensorData.addNewValue(self.curTemp)
                print('New sensor readings:')
                print(str(self.tempSensorData))
            sleep(self.rateInSec)


