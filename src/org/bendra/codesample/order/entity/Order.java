/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents one order in the system.
 * 
 * @author Ben Drasin
 */
public class Order {

	//uniquely identifies order
	private Integer orderId;

	//keep all processed updates so we can check for duplicates. 
	//we don't really need the whole object (just the id), but
	//it could be useful for auditing in the future
	private Map<Integer, OrderUpdate> processedUpdates;

	//other fields
	private Integer amount;
	private OrderStatus currentStatus;

	/**
	 * Creates an Order. Provided argument must have status of NEW
	 * 
	 * @param order
	 */
	public Order(OrderUpdate order) {
		if (OrderStatus.NEW != order.getStatus()) {
			throw new IllegalArgumentException(
					"Not legal to create order with status other than NEW");
		}

		amount = order.getAmount();
		orderId = order.getOrderId();

		processedUpdates = new HashMap<Integer, OrderUpdate>();

		applyUpdate(order);
	}

	/**
	 * Returns processed update with given updateId. Null if no update with this
	 * id has been applied to this order.
	 * 
	 * @param updateId
	 * @return
	 */
	public OrderUpdate getUpdate(Integer updateId) {
		return processedUpdates.get(updateId);
	}

	/**
	 * Process an update to this order. Update state as appropriate and add
	 * order to the list of processed orders if successful
	 * 
	 * @param update
	 * @throws IllegalArgumentException
	 *             if update is for different order, if update with this id has
	 *             already been processed, or involves an illegal state
	 *             transition (e.g. from RECIEVED to PROCESSING)
	 */
	public void processUpdate(OrderUpdate update) {
		// update must be for this order
		if (update.getOrderId() != orderId) {
			throw new IllegalArgumentException(
					"Can not apply update for orderId " + update.getOrderId()
							+ " to orderId " + orderId);
		}
		// only process an update 1 time
		if (processedUpdates.get(update.getUpdateId()) != null) {
			throw new IllegalArgumentException("Update with updateId "
					+ update.getUpdateId()
					+ " has already been applied to orderId " + orderId);
		}
		// check if transition is legal
		if (!currentStatus.canTransitionTo(update.getStatus())) {
			throw new IllegalArgumentException(
					"Transition not allowed from status " + currentStatus
							+ " to " + update.getStatus() + " for order "
							+ orderId);
		}
		// got here, must be ok!
		applyUpdate(update);
	}

	/**
	 * This is the internal method to apply updates.  Note there is
	 * no validation here; data has already been confirmed as valid
	 * 
	 * @param update
	 */
	private void applyUpdate(OrderUpdate update) {
		// set current state
		currentStatus = update.getStatus();
		processedUpdates.put(update.getUpdateId(), update);
	}

	public String toString() {
		return "Order id: " + orderId + ", amount: " + amount
				+ ", currentStatus: " + currentStatus + ", processedUpdates: "
				+ processedUpdates;
	}

	/**
	 * This is the effective charge for the order. Currently is either 0 or the
	 * specified amount, depending on the status
	 * 
	 * @return
	 */
	public Integer getAmountCharged() {
		if (OrderStatus.NEW != currentStatus
				&& OrderStatus.RETURNED != currentStatus
				&& OrderStatus.CANCELED != currentStatus) {
			return amount;
		} else {
			return 0;
		}
	}

	public OrderStatus getCurrentStatus() {
		return currentStatus;
	}

	//Accessors
	public Integer getOrderId() {
		return orderId;
	}
	public Map<Integer, OrderUpdate> getProcessedUpdates() {
		return processedUpdates;
	}
	
	public Integer getAmount() {
		return amount;
	}
}
