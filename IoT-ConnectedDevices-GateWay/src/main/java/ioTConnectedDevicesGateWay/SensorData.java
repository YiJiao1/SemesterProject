package ioTConnectedDevicesGateWay;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String startedTime = null;
	private String timeData = null;
	private String type = "Not Set";
	private int currValue = 0;
	private int avgValue = 0;
	private int minValue = 0;
	private int maxValue = 0;
	private int totalValue = 0;
	private int count = 0;

	public SensorData() {

	}

	public SensorData(String type) {
		this.type = type;
	}

	/**
	 * add new temperature data
	 * 
	 * @param newValue
	 */
	public void addNewValue(int newValue) {
		if (startedTime == null) {
			minValue = newValue;
			maxValue = newValue;
			startedTime = new SimpleDateFormat("yyyy.MM.dd HH:mm.ss").format(new Date());
		}

		currValue = newValue;
		timeData = new SimpleDateFormat("yyyy.MM.dd HH:mm.ss").format(new Date());
		count += 1;
		totalValue += newValue;
		avgValue = totalValue / count;

		if (newValue > maxValue) {
			maxValue = newValue;
		}
		if (newValue < minValue) {
			minValue = newValue;
		}

	}

	public int getAvgValue() {
		return avgValue;
	}

	public int getCurrValue() {
		return currValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public String getTimeData() {
		return timeData;
	}

	public String getStartedTime() {
		return startedTime;
	}

	public int getTotalValue() {
		return totalValue;
	}

	public int getCount() {
		return count;
	}

	public String getType() {
		return type;
	}

	/**
	 * set the data as String frormation
	 * 
	 * @return sensor data information
	 */
	public String toString() {
		String sensorInfo = "Type:\t" + type + "(StartedSince:" + startedTime + ")\n" + "\tTime:" + timeData + "\n"
				+ "\tCurrentValue:" + Integer.toString(currValue) + "\n" + "\tAverage:" + Integer.toString(avgValue)
				+ "\n" + "\tSampleNum:" + Integer.toString(count) + "\n" + "\tMinValue:" + Integer.toString(minValue)
				+ "\n" + "\tMaxValue:" + Integer.toString(maxValue) + "\n";
		return sensorInfo;
	}

}
