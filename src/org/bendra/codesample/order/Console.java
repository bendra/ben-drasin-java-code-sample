/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bendra.codesample.order.entity.OrderStatus;
import org.bendra.codesample.order.entity.OrderUpdate;
import org.bendra.codesample.order.json.UpdateGsonFactory;
import org.bendra.codesample.order.service.OrderService;

import com.google.gson.Gson;

/**
 * This is the main class which creates the service, listens for input, prints
 * output
 * 
 * @author Ben Drasin
 */
public class Console {

	// This starts the application
	public static void main(String[] args) throws IOException {
		OrderService service = OrderService.getInstance();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		Gson gson = UpdateGsonFactory.createGson();
		
		//this is just to help me with debugging...
		boolean debug = "true".equals(System.getProperty("debug"));

		//BufferedReader.readLine returns null at EOF
		String input = reader.readLine();
		while (input != null) {
			try {
				//Use Gson parser to create object from input string
				OrderUpdate nextUpdate = gson.fromJson(input, OrderUpdate.class);
				if(nextUpdate != null){
					//All of the real work is here
					service.processUpdate(nextUpdate);
				}
			} catch(IllegalArgumentException ex) {
				//only print these if info level is on
				if(debug){
					//this will print errors to stderr
					ex.printStackTrace();
				}
			} catch (Throwable t) {			
				//unexpected error - print to stderr
				t.printStackTrace();
			}
			input = reader.readLine();
		}

		int[] summary = service.summaryReport();
		for (OrderStatus status : OrderStatus.values()) {
			System.out.println(status.getName() + ": "
					+ summary[status.ordinal()]);
		}
		System.out.println("Total amount charged: $" + summary[6]);
	}

}
