package org.bendra.codesample.order.entity;

/**
 * Represents a single update one order (which may or may not actually exist in
 * the system).
 * 
 * @author Ben Drasin
 */
public class OrderUpdate {
	private Integer orderId;
	private Integer updateId;
	private OrderStatus status;
	private Integer amount;

	// Constructors
	public OrderUpdate() {
	}

	public OrderUpdate(Integer orderId, Integer updateId, OrderStatus status) {
		this(orderId, updateId, status, null);
	}

	public OrderUpdate(Integer orderId, Integer updateId, OrderStatus status,
			Integer amount) {
		this.orderId = orderId;
		this.updateId = updateId;
		this.status = status;
		this.amount = amount;
	}

	// Accessors
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setOrderStatus(OrderStatus status) {
		this.status = status;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	// Nice display of fields
	public String toString() {
		return "orderId: " + orderId + " updateId: " + updateId + " status: "
				+ status + " amount: " + amount;
	}
}
