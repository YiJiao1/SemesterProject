from labs.deviceModule import TempSensorAdaptor
from time import sleep
from labs.deviceModule import mqttClientConnector
from threading import Thread
import sense_hat


# handle callback when Rpi connect to MQqtt broker
def on_connect(mqttc, obj, flags, rc):
    print("Connect to GatewayBroker")

    
# handle callback when subscribed actuatorData arrive, showing on LED actuator     
def on_message(mqttc, obj, msg):
    data = "TempActuatorData arrived from :" + msg.topic + " QoS = " + str(msg.qos) + " Message:" + str(msg.payload.decode("utf-8"))
    print(data)
    sh = sense_hat.SenseHat()
    sh.show_message(data, scroll_speed=0.1)


# handle  when publish successfully
def on_publish(mqttc, obj, mid):
    print("Successfully published SensorData to topic iot/sensorData/temperature, ID: " + str(mid))


# handle callback when subscribe successfully
def on_subscribe(mqttc, obj, mid, granted_qos):
    print("Successfully Subscribed to topic: " + +str(mid) + " QoS:" + str(granted_qos))


class TempManagementApp(Thread):

    tempSensorAdaptor = None
    tempSensorData = None
    deviceConfiReader = None
    pollCycleSecs = None
    mqttClient = None 
     
    def __init__(self, configDir):
        Thread.__init__(self)
        
        # senseHatLED activator threads are started when TempActuatorEmulator instance is initialized..  
        self.tempSensor = TempSensorAdaptor.TempSensorAdaptor(40, 20, 4, 1, configDir)
        self.tempSensor.daemon = True
        self.tempSensor.start()
        # connect to broker and start 
        self.mqttClient = mqttClientConnector.MqttClientConnector("127.0.0.1", 1883, on_connect, on_message, on_publish, on_subscribe)
        self.mqttClient.connect()
        self.mqttClient.subscribeTopic("iot/device/TempActuator", 0)

    def run(self):
        while True:
            # Read new sensorData and publish to gateway broker        
            self.mqttClient.publishMessage("iot/device/TempCur", str(self.tempSensor.getCurrValue()), 0)     
            sleep(10)  # wait for 10 seconds

# set path
path = "../../../data/ConnectedDevicesConfig.props"
# create thread object
app = TempManagementApp(path);
# start thread
app.start()

while True:
    pass
