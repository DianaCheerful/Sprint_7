package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersList {

    private List<OrderResponse> orders;

    public OrdersList() {
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}