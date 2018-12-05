package ioTConnectedDevicesGateWay;


public class App extends Thread {
	String pem_path = "/Users/heychris/Downloads/IoT-Project-master/IoTGatewayJava/IoT-ConnectedDevices-GateWay/data/ubidots_cert.pem";
	private String sensorData_path = "/Users/heychris/Desktop/iot-gateway/IoT-ConnectedDevices-GateWay/data/TempSensor";
	private String authtoken = "{token}";
	private String tempCurTopic = "/v1.6/devices/iot-device/tempsensor";
	private String tempAveTopic = "/v1.6/devices/iot-device/tempaverage";
	private String tempActuTopic = "/v1.6/devices/iot-device/tempactuator/lv";

	/*
	 * Main Thread to run all gateway function, connect to each gateway part, from
	 * Rpi gateway mqtt to cloud , then send back to local gateway
	 */
	public void run() {
		// start a FIleStream object
		FIleStream Text_read = new FIleStream();
		// Created mqtt local client and connection
		MqttClientConnectorLocal _mqttClient = new MqttClientConnectorLocal("tcp", "127.0.0.1", 1883);
		_mqttClient.connect();

		// Subscribe SensorData form IotDevice
		_mqttClient.subscribeTopic("iot/device/TempCur", 0);

		// Created Coap server
		try {
			CoapServerConnector serverConnCoap = new CoapServerConnector();
			serverConnCoap.start();
		} catch (Exception e) {
			System.out.println("Coap sever Start error");
		}

		// Created coap client start
		CoapClientConnector clientConnCoap = new CoapClientConnector();

		// Created MQTT ssl client and connection
		MqttClientConnectorCloud _mqttClientCloud = new MqttClientConnectorCloud("things.ubidots.com", authtoken,
				pem_path);
		_mqttClientCloud.connect();
		// Subscribe ActuatorData form Ubidots
		_mqttClientCloud.subscribeTopic(tempActuTopic, 0);

		// create SensorData for sending E-mail and ubidots
		SensorData sensorData = new SensorData("Current temperature");

		while (true) {
			try {
				// wait for 5 seconds
				Thread.sleep(5000L);

				// Add new_ActuatorData from IotDevice in GataWay SensorData
				sensorData.addNewValue(_mqttClient._currentTemp);

				// if SensorData too High, send Email to user
				if (_mqttClient._currentTemp > 38) {
					SmtpConnector.SendMail(sensorData.toString());
				}

				// SensorTemp and AverageTemp as payload for Coap sending
				String payloadCur = String.valueOf(sensorData.getCurrValue());
				String payloadAve = String.valueOf(sensorData.getAvgValue());

				// Post SensorTemp by Coap Gateway, Storage in TempSenor.txt file
				clientConnCoap.sendPostRequest("test1", payloadCur, true);
				// Get SensorTemp read from TempSenor.txt file to check value
				clientConnCoap.sendGetRequest("test1");

				// publish Sensordata(read from TempSenor.txt file) to ubidots
				String sensorTempToCloud = Text_read.tempRead(sensorData_path);
				_mqttClientCloud.publishMessage(tempCurTopic, 0, sensorTempToCloud.getBytes());

				// wait for 3 seconds
				Thread.sleep(5000L);

				// publish AverageData(which don't pass Coap gateway) to ubidots
				_mqttClientCloud.publishMessage(tempAveTopic, 0, payloadAve.getBytes());

				// publish ActuatorData ( ActuatorData from Cloud ) to IotDevice
				_mqttClient.publishMessage("iot/device/TempActuator", 0, _mqttClientCloud._actuator.getBytes());

				// wait for 5 seconds, for next Start.
				Thread.sleep(5000L);
			} catch (Exception e) {
				System.exit(1);
			}
		}
	}

	public static void main(String[] args) {
		// Create new Thread, and start in main function
		App myApp = new App();
		myApp.start();
	}

}
