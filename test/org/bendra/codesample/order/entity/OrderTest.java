/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order.entity;

import static org.junit.Assert.*;

import org.bendra.codesample.order.entity.Order;
import org.bendra.codesample.order.entity.OrderStatus;
import org.bendra.codesample.order.entity.OrderUpdate;
import org.junit.Test;

/**
 * Tests for Order class
 * 
 * @author Ben Drasin
 */
public class OrderTest {

	@Test
	public void testCreate() {
		OrderUpdate update = new OrderUpdate();
		update.setOrderStatus(OrderStatus.NEW);
		update.setAmount(3);
		update.setOrderId(1);
		update.setUpdateId(2);
		Order order = new Order(update);
		assertTrue(order.getAmount() == 3);
		assertTrue(order.getCurrentStatus() == OrderStatus.NEW);
		assertTrue(order.getOrderId() == 1);
		assertTrue(order.getProcessedUpdates().size() == 1);
		assertTrue(order.getUpdate(2) != null);
	}

	@Test
	public void testDuplicateCreate() {
		OrderUpdate update1 = new OrderUpdate();
		update1.setOrderStatus(OrderStatus.NEW);
		update1.setAmount(3);
		update1.setOrderId(1);
		update1.setUpdateId(2);
		Order order = new Order(update1);
		OrderUpdate update2 = new OrderUpdate();
		update2.setOrderStatus(OrderStatus.NEW);
		update2.setAmount(3);
		update2.setOrderId(1);
		update2.setUpdateId(2);
		try {
			order.processUpdate(update2);
			// if we get here, it is a test failure
			fail("Re-applying the same update twice should cause error");
		} catch (IllegalArgumentException ex) {
			// no problem; this is good
		}
	}

	/**
	 * Moves an order through normal workflow and ensured that illegal
	 * transitions are not allowed. This isn't exhaustive; more tests could
	 * cover all possible scenarios.
	 */
	@Test
	public void testStateTransitions() {
		OrderUpdate newUpdate = new OrderUpdate(1, 1, OrderStatus.NEW, 3);
		OrderUpdate processingUpdate = new OrderUpdate(1, 2, OrderStatus.PROCESSING);
		OrderUpdate enrouteUpdate = new OrderUpdate(1, 3,
				OrderStatus.ENROUTE);
		OrderUpdate receivedUpdate = new OrderUpdate(1, 4,
				OrderStatus.RECIEVED);
		OrderUpdate returnedUpdate = new OrderUpdate(1, 5, OrderStatus.RETURNED);
		OrderUpdate cancelledUpdate = new OrderUpdate(1, 6,
				OrderStatus.CANCELED);
		OrderUpdate newUpdate2 = new OrderUpdate(1, 7, OrderStatus.NEW, 3);
		OrderUpdate processingUpdate2 = new OrderUpdate(1, 8, OrderStatus.PROCESSING);
		OrderUpdate enrouteUpdate2 = new OrderUpdate(1, 9,
				OrderStatus.ENROUTE);

		Order order = new Order(newUpdate);
		assertEquals(OrderStatus.NEW, order.getCurrentStatus());
		attemptBadUpdate(receivedUpdate, order);
		attemptBadUpdate(enrouteUpdate, order);
		attemptBadUpdate(returnedUpdate, order);

		order.processUpdate(processingUpdate);
		assertEquals(OrderStatus.PROCESSING, order.getCurrentStatus());
		attemptBadUpdate(newUpdate2, order);
		attemptBadUpdate(receivedUpdate, order);
		attemptBadUpdate(returnedUpdate, order);

		order.processUpdate(enrouteUpdate);
		assertEquals(OrderStatus.ENROUTE, order.getCurrentStatus());
		attemptBadUpdate(newUpdate2, order);
		attemptBadUpdate(processingUpdate2, order);
		attemptBadUpdate(returnedUpdate, order);

		order.processUpdate(receivedUpdate);
		assertEquals(OrderStatus.RECIEVED, order.getCurrentStatus());
		attemptBadUpdate(newUpdate2, order);
		attemptBadUpdate(processingUpdate2, order);
		attemptBadUpdate(enrouteUpdate2, order);
		attemptBadUpdate(cancelledUpdate, order);
	}

	// This is for an update that should not succeed; is failure if no error
	private void attemptBadUpdate(OrderUpdate badUpdate, Order order) {
		try {
			order.processUpdate(badUpdate);
			fail("Update to state " + badUpdate.getStatus().getName()
					+ " should not be allowed here.");
		} catch (IllegalArgumentException ex) {
			// this is fine; continue
		}
	}
}
