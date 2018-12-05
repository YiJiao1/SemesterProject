package ioTConnectedDevicesGateWay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FIleStream {

	/**
	 * @param path    file_path
	 * @param payload temperature Data
	 */
	public void tempWrite(String path, String payload) {
		try {
			File writename = new File(path); // add path
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(payload);
			out.flush();
			out.close(); // close string buffer
		} catch (Exception e) {
			System.out.println("write error");
		}
	}

	/**
	 *  Read data from text function
	 *  @param path
	 *  @return value of the temperature
	 */
	public String tempRead(String path) {
		File file02 = new File(path);
		FileInputStream is = null;
		StringBuilder stringBuilder = null;
		try {
			if (file02.length() != 0) {

				is = new FileInputStream(file02);
				InputStreamReader streamReader = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(streamReader);
				String line;
				stringBuilder = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				reader.close();
				is.close();
			} else {
				System.out.println("file has nothing");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(stringBuilder);
	}

}
