'''
Created on 2018年9月15日
    
@author: heychris
'''
from datetime import datetime


class SensorData:
    timeStamp = None
    name = "No set"
    curTemp = None
    avgTemp = 0
    minTemp = None
    maxTemp = None
    totalTemp = 0
    countTemp = 0
    
    def __init__(self, name):
        self.name = name
        
# add newValue function, then add count, and renew data.
    def addNewValue(self, newVal):
        
        self.timeStamp = str(datetime.now())
        self.curTemp = newVal
        self.countTemp = self.countTemp + 1
        
        if self.maxTemp == None and self.minTemp == None:
            
            self.maxTemp = newVal
            self.minTemp = newVal
            
        else:
            if newVal > self.maxTemp:
                self.maxTemp = newVal
        
            if newVal < self.minTemp:
                self.minTemp = newVal
            
        self.totalTemp = self.totalTemp + newVal
        self.avgTemp = self.totalTemp / self.countTemp
        
# unit test, like new a instance, then print all information on this class.
    def printSensorInfo(self):
        
        print(self.name + ":" )
        print("\tTime:" + str(self.timeStamp))
        print("\tCurrent" + self.name + ":" + str(self.curTemp))
        print("\tAverage" + self.name + ":" + str(self.avgTemp))
        print("\tSampleNum:" + str(self.countTemp))
        print("\tMin_" + self.name + ":" + str(self.minTemp))
        print("\tMax_" + self.name + ":" + str(self.maxTemp))
         
# show all data in String
    def __str__(self):
        sensorInfo = str(self.name + ":" + "\n"
                        +"\tTime: " + str(self.timeStamp) + "\n"
                       +"\tCurrent" + self.name + " :" + str(self.curTemp) + "\n"
                       +"\tAverage" + self.name + " :" + str(self.avgTemp) + "\n"
                       +"\tSampleNum: " + str(self.countTemp) + "\n"
                       +"\tMin_" + self.name + ": " + str(self.minTemp) + "\n"
                       +"\tMax_" + self.name + ": " + str(self.maxTemp))
        return sensorInfo             
    
