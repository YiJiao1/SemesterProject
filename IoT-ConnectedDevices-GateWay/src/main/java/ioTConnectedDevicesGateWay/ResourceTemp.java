package ioTConnectedDevicesGateWay;

import java.util.logging.Logger;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class ResourceTemp extends CoapResource {
	private static final Logger _Logger = Logger.getLogger(ResourceTemp.class.getName());
	private String Temp_Path = "/Users/heychris/Desktop/iot-gateway/IoT-ConnectedDevices-GateWay/data/TempSensor";

	// constructors

	/**
	 * @param name
	 */
	public ResourceTemp(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param visible
	 */
	public ResourceTemp(String name, boolean visible) {
		super(name, visible);
	}

	/*
	 * public methods handle get request from client, and return response to client
	 */
	@Override
	public void handleGET(CoapExchange ce) {
		// Callback from client, read payload from TempSensor.txt file
		FIleStream read = new FIleStream();
		String tempData = read.tempRead(Temp_Path);
		ce.respond(ResponseCode.VALID, tempData);
		_Logger.info("Handling GET:" + tempData);

	}

	@Override

	public void handlePOST(CoapExchange exchange) {
		// Callback from client, save payload in TempSensor.txt file
		FIleStream read = new FIleStream();
		read.tempWrite(Temp_Path, exchange.getRequestText());
		exchange.respond(ResponseCode.CREATED, "POST successfully!");
		_Logger.info("Receive post request：" + exchange.getRequestText());
	}

	@Override
	/*
	 * handle PUT request from client, and return response to 
	 * client
	 */
	public void handlePUT(CoapExchange exchange) {
		// response by CREATED
		exchange.respond(ResponseCode.CHANGED, "PUT successfully!!!");
		_Logger.info("Receive post request：" + exchange.getRequestText());
	}

	public void handleDELETE(CoapExchange exchange) {
		// response by DELETED
		String responseMsg = super.getName();
		exchange.respond(ResponseCode.DELETED, "DELETE successfully!!!");
		_Logger.info("Receive DELETE request：" + responseMsg);
	}

}
