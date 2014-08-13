/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order.entity;

import org.bendra.codesample.order.entity.OrderStatus;
import org.bendra.codesample.order.entity.OrderUpdate;
import org.bendra.codesample.order.json.UpdateGsonFactory;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.gson.Gson;

/**
 * Tests creation of objects from JSON
 * 
 * @author Ben Drasin
 */
public class SerializationTest {

	private static final String NEW_ORDER_REQUEST = "{\"orderId\":100, \"status\":\"NEW\",\"updateId\":287,\"amount\":20}";
	private static final String NEW_ORDER_REQUEST_NOAMOUNT = "{\"orderId\":100,\"status\":\"NEW\",\"updateId\":287}";

	/**
	 * Just a couple of updates deserialized from json. Not close to exhaustive;
	 * more tests could capture more exhastively
	 */
	@Test
	public void testNewOrder() {
		Gson gson = UpdateGsonFactory.createGson();

		OrderUpdate update = gson
				.fromJson(NEW_ORDER_REQUEST, OrderUpdate.class);
		assertTrue(update.getOrderId() == 100);
		assertTrue(update.getStatus() == OrderStatus.NEW);
		assertTrue(update.getUpdateId() == 287);
		assertTrue(update.getAmount() == 20);

		update = gson.fromJson(NEW_ORDER_REQUEST_NOAMOUNT, OrderUpdate.class);
		assertTrue(update.getOrderId() == 100);
		assertTrue(update.getStatus() == OrderStatus.NEW);
		assertTrue(update.getUpdateId() == 287);
		assertTrue(update.getAmount() == null);
	}

}
