/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order.service;

import static org.junit.Assert.*;

import org.bendra.codesample.order.entity.OrderStatus;
import org.bendra.codesample.order.entity.OrderUpdate;
import org.bendra.codesample.order.service.OrderService;
import org.junit.Test;

/**
 * @author Owner
 * 
 */
public class OrderServiceTest {

	/**
	 * This is the most comprehensive test case; it uses the input/output from the provided example
	 * 
	 * Test method for
	 * {@link org.bendra.codesample.order.service.OrderService}
	 * .
	 */
	@Test
	public void testOrderService() {
		OrderService service = OrderService.getInstance();
		service.processUpdate(new OrderUpdate(100, 287, OrderStatus.NEW, 20));
		service.processUpdate(new OrderUpdate(100, 289, OrderStatus.PROCESSING));
		try{
			service.processUpdate(new OrderUpdate(100, 289, OrderStatus.PROCESSING));
		}catch(IllegalArgumentException ex){
			//do nothing - this is expected
		}
		service.processUpdate(new OrderUpdate(101, 289, OrderStatus.NEW, 13));
		service.processUpdate(new OrderUpdate(100, 294, OrderStatus.ENROUTE));
		service.processUpdate(new OrderUpdate(102, 291, OrderStatus.NEW, 17));
		service.processUpdate(new OrderUpdate(101, 290, OrderStatus.CANCELED));
		
		//confirm results
		int[] report = service.summaryReport();
		assertEquals(1, report[0]);
		assertEquals(0, report[1]);
		assertEquals(1, report[2]);
		assertEquals(0, report[3]);
		assertEquals(1, report[4]);
		assertEquals(0, report[5]);
		assertEquals(20, report[6]);
	}

}
