package org.bendra.codesample.order.json;

/**
 * Copyright Ben Drasin 2013
 */

import java.lang.reflect.Type;

import org.bendra.codesample.order.entity.OrderStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This is a simple factory to create a Google Gson parser with a
 * serializer/deserializer for OrderStatus enums
 * 
 * @author Ben Drasin
 */
public class UpdateGsonFactory {

	/**
	 * 
	 * @return a Gson parser for converting JSON strings to OrderUpdate objects
	 */
	public static Gson createGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(OrderStatus.class,
				new OrderStatusSerializer());
		builder.registerTypeAdapter(OrderStatus.class,
				new OrderStatusDeserializer());
		Gson gson = builder.create();
		return gson;
	}

	// Static inner classes for OrderStatus enums
	public static class OrderStatusDeserializer implements
			JsonDeserializer<OrderStatus> {
		@Override
		public OrderStatus deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return OrderStatus.valueOf(OrderStatus.class, json
					.getAsJsonPrimitive().getAsString());
		}
	}

	public static class OrderStatusSerializer implements
			JsonSerializer<OrderStatus> {
		@Override
		public JsonElement serialize(OrderStatus src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.getName());
		}
	}
}
