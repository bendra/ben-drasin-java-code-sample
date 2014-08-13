/**
 * Copyright Ben Drasin 2013
 */
package org.bendra.codesample.order.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Enum for legal status values an order can have
 * 
 * @author Ben Drasin
 */
public enum OrderStatus {
	NEW ("New"),
	PROCESSING("Processing"),
	ENROUTE("En Route"),
	RECIEVED("Recieved"),
	CANCELED("Canceled"),
	RETURNED("Returned");
	
	//build map of legal transitions
	private static Map<OrderStatus,Set<OrderStatus>> legalTransitions = new HashMap<OrderStatus, Set<OrderStatus>>();
	static{
		allowTransition(NEW, new OrderStatus[]{PROCESSING,CANCELED});
		allowTransition(PROCESSING, new OrderStatus[]{ENROUTE,CANCELED});
		allowTransition(ENROUTE, new OrderStatus[]{RECIEVED,CANCELED});
		allowTransition(RECIEVED, new OrderStatus[]{RETURNED});		
	}
	
	private static final void allowTransition(OrderStatus status, OrderStatus[] allowedTransitions){
		legalTransitions.put(status, new HashSet<OrderStatus>(Arrays.asList(allowedTransitions)));
	}
		
	private final String name;
	OrderStatus(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @param newStatus
	 * @return true if transition is allowed
	 */
	public boolean canTransitionTo(OrderStatus newStatus){
		return legalTransitions.get(this).contains(newStatus);
	}
}
