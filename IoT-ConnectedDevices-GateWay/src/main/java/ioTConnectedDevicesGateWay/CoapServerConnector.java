package ioTConnectedDevicesGateWay;

import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class CoapServerConnector {

	// static
	private static final Logger _Logger = Logger.getLogger(CoapServerConnector.class.getName());
	// private var's
	private CoapServer _coapServer;

	/**
	 * Constructor & add the resourse, here using generic class ResourceTemp to
	 * handle request
	 */
	public CoapServerConnector() {
		super();
		_coapServer = new CoapServer();
		ResourceTemp resourceTemp = new ResourceTemp("test1");
		addResource(resourceTemp);
	}

	public CoapServerConnector(String... resourceNames) {

	}

	public void addDefaultResource(String Name) {

	}

	/**
	 * add the Resource to server
	 */
	public void addResource(CoapResource resource) {
		if (resource != null) {
			_coapServer.add(resource);
		}
	}

	/**
	 * start server
	 */
	public void start() {
		_Logger.info("Starting CoAP server...");
		_coapServer.start();
	}

	/**
	 * stop server
	 */
	public void stop() {
		_Logger.info("Stopping CoAP server...");
		_coapServer.stop();
	}

}
