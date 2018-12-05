'''
Created on 2018年9月15日
 
@author: heychris
'''
import configparser


class ConfigUtil(object):
    confDir = None
    section = None
    parser = configparser.ConfigParser()

#     this  is data read function
    def __init__(self, confDir, section):
        self.section = section
        self.confDir = confDir
        self.parser.read(self.confDir)
    
    def getProperty(self, option):
        return self.parser.get(self.section, option)
        
    def setSection(self, section):
        self.section = section
        
    def setConfigFile(self, confDir):
        self.confDir = confDir
        self.parser.read(self.confDir)
        
#  unit test part       
# parser=ConfigUtil("/Users/heychris/Documents/work/iot-device/data/ConnectedDevicesConfig.props", "smtp.cloud")
# print(parser.getProperty("host"))
