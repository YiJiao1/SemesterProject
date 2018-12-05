package ioTConnectedDevicesGateWay;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class CoapClientConnector {
	// static
	private static final Logger _Logger = Logger.getLogger(CoapClientConnector.class.getName());

	// private var's
	private String _protocol = "coap";
	private String _host = "localhost";
	private int _port = 5683;
	private String _connUrl = null;
	private CoapClient _coapClient;
	private String _serverAddr;
	private boolean _isInitialized;

	/**
	 * Set the URL
	 */
	public CoapClientConnector() {
		super();
		_connUrl = new String(_protocol + "://" + _host + ":" + _port + "/");
	}

	/**
	 * public methods run all test function to make sure wo could use all API
	 */

	public void runTests() {
		initClient();
		pingServer();
		displayResources();
		sendGetRequest();
	}

	/**
	 * log the ping result
	 */
	public void pingServer() {
		initClient();
		if (_coapClient.ping()) {
			_Logger.info("Ping successful!");
		}
	}

	/**
	 * show all resourse in log
	 */
	public void displayResources() {
		initClient();
		_Logger.info("Getting all remote web likes...");
		Set<WebLink> webLinkSet = _coapClient.discover();
		if (webLinkSet != null) {
			for (WebLink wl : webLinkSet) {
				_Logger.info("--> WebLink:" + wl.getURI());
			}
		}
	}
	/*
	 * resource should only be the file path
	 */

	public void sendGetRequest() {
		sendGetRequest(null);
	}

	public void sendGetRequest(String resource) {
		initClient(resource);
		handleGetRequest(true);
	}

	public void sendDeleteRequest() {
		initClient();
		handleDeleteRequest();
	}

	/**
	 * DELETE API, using resourceName
	 * 
	 * @param resourceName
	 */
	public void sendDeleteRequest(String resourceName) {
		_isInitialized = false;
		initClient(resourceName);
		handleDeleteRequest();
	}

	/**
	 * POST API, using , payload
	 * 
	 * @param payload
	 * @param useCON
	 */
	public void sendPostRequest(String payload, boolean useCON) {
		initClient();
		handlePostRequest(payload, useCON);
	}

	/**
	 * POST API, using resourceName, payload
	 * 
	 * @param resourceName
	 * @param payload
	 * @param useCON
	 */
	public void sendPostRequest(String resourceName, String payload, boolean useCON) {
		initClient(resourceName);
		handlePostRequest(payload, useCON);
	}

	public void sendPutRequest(String payload, boolean useCON) {
		initClient();
		handlePutRequest(payload, useCON);
	}

	/**
	 * PUT API, using resourceName, payload
	 * 
	 * @param resourceName
	 * @param payload
	 * @param useCON
	 */
	public void sendPutRequest(String resourceName, String payload, boolean useCON) {
		_isInitialized = false;
		initClient(resourceName);
		handlePutRequest(payload, useCON);
	}

	private void initClient() {
		initClient(null);
	}

	/**
	 * connect to broker
	 * 
	 * @param resource
	 */
	private void initClient(String resource) {

		if (_coapClient != null) {
			_coapClient.shutdown();
			_coapClient = null;
		}
		try {
			if (resource != null && resource.trim().length() > 0) {
				_coapClient = new CoapClient(_connUrl + resource);
			} else {
				_coapClient = new CoapClient(_connUrl);
			}
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to connect to broker: ", e);
		}
	}

	/**
	 * here is handle function, get response from server log it
	 * 
	 * @param useCON
	 */
	private void handleGetRequest(boolean useCON) {

		CoapResponse response = null;
		if (useCON) {
			_coapClient.useCONs();
		}
		response = _coapClient.get();
		if (response != null) {
			byte[] rawPayload = response.getPayload();
			_Logger.info(
					"Response: " + response.isSuccess() + " - " + response.getCode() + " - " + new String(rawPayload));
		} else {
			_Logger.warning("No response received.");
		}
	}

	/**
	 * handle DELETE which from server
	 */
	private void handleDeleteRequest() {
		_Logger.info("Sending DELETE...");
		CoapResponse response = null;
		response = _coapClient.delete();
		byte[] rawPayload = response.getPayload();
		_Logger.info("Response: " + response.isSuccess() + " - " + response.getCode() + " - " + new String(rawPayload));

	}

	/**
	 * handle put which from server
	 * 
	 * @param payload
	 * @param useCON
	 */
	private void handlePutRequest(String payload, boolean useCON) {
		_Logger.info("Sending PUT...");
		CoapResponse response = null;
		if (useCON) {
			_coapClient.useCONs().useEarlyNegotiation(32).get();
		}
		response = _coapClient.put(payload, MediaTypeRegistry.TEXT_PLAIN);
		if (response != null) {
			byte[] rawPayload = response.getPayload();
			_Logger.info(
					"Response: " + response.isSuccess() + " - " + response.getCode() + " - " + new String(rawPayload));
		} else {
			_Logger.warning("No response received.");
		}
	}

	/**
	 * handle Post which from server, also deal with the post response
	 * 
	 * @param payload
	 * @param useCON
	 */
	private void handlePostRequest(String payload, boolean useCON) {
		_Logger.info("Sending POST...");
		CoapResponse response = null;
		if (useCON) {
			_coapClient.useCONs().useEarlyNegotiation(32).get();
		}
		response = _coapClient.post(payload, MediaTypeRegistry.TEXT_PLAIN);

		if (response != null) {
			byte[] rawPayload = response.getPayload();
			_Logger.info(
					"Response: " + response.isSuccess() + " - " + response.getCode() + " - " + new String(rawPayload));

		} else {
			_Logger.warning("No response received.");
		}
	}

	/**
	 * get the url to check problem or debug
	 */
	public String getCurrentUri() {
		return (_coapClient != null ? _coapClient.getURI() : _serverAddr);
	}
}
