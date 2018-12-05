package ioTConnectedDevicesGateWay;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientConnectorLocal implements MqttCallback {

	private static final Logger _Logger = Logger.getLogger(MqttClientConnectorLocal.class.getName());

	private String _protocol;
	private String _host;
	private int _port;

	private String _clientID;
	private String _brokerAddr;
	private MqttClient _mqttClient;
	public int _currentTemp; // SensorData(int)
	public int _ActuatorTemp; // ActuatorData
	public String currentTemp;// SensorData(String)

	/**
	 * Constructor for set protocol, host and prot number
	 * 
	 * @param protocol
	 * @param host
	 * @param port
	 */
	public MqttClientConnectorLocal(String protocol, String host, int port) {
		super();

		_protocol = protocol;
		_host = host;
		_port = port;
		_clientID = MqttClient.generateClientId();
		_brokerAddr = _protocol + "://" + _host + ":" + _port;
		_Logger.info("Using URL for broker connecting : " + _brokerAddr);

	}

	/**
	 * connect function, using for connecting to broker
	 */
	public void connect() {
		if (_mqttClient == null) {
			MemoryPersistence persistence = new MemoryPersistence();
			try {
				_mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);

				MqttConnectOptions connOpets = new MqttConnectOptions();
				connOpets.setCleanSession(true);

				_mqttClient.setCallback(this);
				// set connecting with MqttConnectOptions
				_mqttClient.connect(connOpets);
				_Logger.info("Connecting to broker: " + _brokerAddr);

			} catch (MqttException e) {
				_Logger.log(Level.SEVERE, "Failed to connect to broker: " + _brokerAddr, e);
			}
		}

	}

	/**
	 * disconnect to broker
	 */
	public void disconnect() {
		try {
			_mqttClient.disconnect();
			_Logger.info("Disconnected from broker: " + _brokerAddr);
		} catch (MqttException e) {
			_Logger.log(Level.SEVERE, "Failed to disconnect from broker: " + _brokerAddr, e);
		}

	}

	/**
	 * publish message function, send data to broker
	 * 
	 * @param topic
	 * @param qos
	 * @param payload
	 * @return if successfully connected, false if failed
	 * 
	 */
	public boolean publishMessage(String topic, int qos, byte[] payload) {
		// set tag to check if success
		boolean success = false;
		try {
			_Logger.info("Publishing message to topic: " + topic);
			MqttMessage message = new MqttMessage();
			// payload data add in message
			message.setPayload(payload);
			// Qos level set
			message.setQos(qos);
			_mqttClient.publish(topic, message);
			_Logger.info("Published Success!  MqttMessageID:" + message.getId());
			success = true;

		} catch (MqttException e) {
			_Logger.log(Level.SEVERE, "Failed to publish MQTT message: " + e.getMessage());
		}

		return success;

	}

	/**
	 * subscribeToAll get all msg
	 * 
	 * @return if successfully connected, false if failed
	 */
	public boolean subscribeToAll() {
		try {
			_mqttClient.subscribe("$SYS/#");
			return true;
		} catch (MqttException e) {
			_Logger.log(Level.WARNING, "Failed to subscribe to all topics.", e);
		}
		return false;
	}

	/**
	 * subscribe the topic only, filter other unnecessary topic
	 * 
	 * @param topic
	 * @param qos
	 * @return if successfully connected, false if failed
	 */
	public boolean subscribeTopic(String topic, int qos) {
		try {
			_mqttClient.subscribe(topic, qos);
			_Logger.info("Subscribed to Topic:  " + topic);
			return true;
		} catch (MqttException e) {
			_Logger.log(Level.WARNING, "Failed to subscribe  topics.", e);
		}
		return false;
	}

	/**
	 * log the connection lost
	 * 
	 */
	public void connectionLost(Throwable t) {
		_Logger.log(Level.WARNING, "Connection to broker lost. Will retry soon.", t);

	}

	/**
	 * publish client log the delivery if compelte
	 * 
	 * @param token
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			_Logger.info("Delivery complete: ID = " + token.getMessageId() + " - " + token.getResponse() + " - "
					+ token.getMessage());
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to retrieve message from token.", e);
		}

	}

	/**
	 * sub client log the data arrive
	 * 
	 * @param data
	 * @param msg
	 */
	public void messageArrived(String data, MqttMessage msg) throws Exception {
		// TODO: should you analyze the message or just log it?
		currentTemp = new String(msg.getPayload());
		_currentTemp = Integer.parseInt(new String(msg.getPayload()));
		_Logger.info("Message arrived: " + data + ", " + msg.getId() + ", PAYLOAD : " + _currentTemp);
	}

}
