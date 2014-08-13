/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order.service;

import java.util.HashMap;
import java.util.Map;

import org.bendra.codesample.order.entity.Order;
import org.bendra.codesample.order.entity.OrderStatus;
import org.bendra.codesample.order.entity.OrderUpdate;

/**
 * Singleton class for processing orders. Updates will either succeed or throw
 * IllegalArgumentException
 * 
 * @author Ben Drasin
 * 
 */
public class OrderService {

	private static volatile OrderService instance;
	
	/**
	 * @return Singleton instance
	 */
	public static synchronized OrderService getInstance() {
		if (instance == null) {
			instance = new OrderService();
		}
		return instance;
	}

	// orders in system - just using in-memory storage for test.
	// in real life this would be some sort of persistent store
	private Map<Integer, Order> orders;

	/**
	 * No-arg constructor - cannot be instantiated outside of class
	 */
	private OrderService() {
		orders = new HashMap<Integer, Order>();
	}

	/**
	 * Handle a single update - locate/create order and apply update to it
	 * 
	 * @param updateToProcess
	 */
	public void processUpdate(OrderUpdate updateToProcess) {
		// confirm update contains required fields and can be processed
		if (!validateUpdateFields(updateToProcess)) {
			throw new IllegalArgumentException(
					"Update missing required field: "
							+ updateToProcess.toString());
		}

		// create order if new
		if (OrderStatus.NEW == updateToProcess.getStatus()) {
			processNewOrder(updateToProcess);
		} else {
			processUpdateOrder(updateToProcess);
		}
	}

	/**
	 * Find the order for this update and apply the update to it
	 * 
	 * @param updateToProcess
	 */
	private void processUpdateOrder(OrderUpdate updateToProcess) {
		// not a new order; process
		Order order = orders.get(updateToProcess.getOrderId());
		//ensure order exists first...
		if (order == null) {
			throw new IllegalArgumentException("Update " + updateToProcess
					+ " cannot be processed = no order with id "
					+ updateToProcess + " in system.");
		}
		order.processUpdate(updateToProcess);
	}

	/**
	 * Create an order using the update provided
	 * 
	 * @param updateToProcess
	 */
	private void processNewOrder(OrderUpdate updateToProcess) {
		if (orders.get(updateToProcess.getOrderId()) != null) {
			//is error if order already exists
			throw new IllegalArgumentException(
					"Cannot process NEW order update: "
							+ updateToProcess.toString()
							+ "; order with id "
							+ updateToProcess.getOrderId()
							+ " already exists");
		} else {
			//create and store order
			Order order = new Order(updateToProcess);
			orders.put(order.getOrderId(), order);
		}
	}

	/**
	 * All order updates must have order id, status, and update id
	 * 
	 * @param updateToProcess
	 * @return
	 */
	private boolean validateUpdateFields(OrderUpdate updateToProcess) {
		if (updateToProcess.getOrderId() == null
				|| updateToProcess.getStatus() == null
				|| updateToProcess.getUpdateId() == null) {
			return false;
		}
		// got here, must be valid
		return true;
	}

	/**
	 * Prepares summary of orders in system
	 * 
	 * @return
	 */
	public int[] summaryReport() {
		int[] report = new int[7];// conveniently initializes all to 0
		for (Order order : orders.values()) {
			report[order.getCurrentStatus().ordinal()]++;
			report[6] += order.getAmountCharged();
		}
		return report;
	}
}
