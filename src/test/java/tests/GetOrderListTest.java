package tests;

import model.OrderResponse;
import model.OrdersList;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;

import static constants.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class GetOrderListTest extends BaseOrderTest {


    @Test
    public void shouldGetOkStatusWhenGetOrderList() {
        given()
                .when()
                .get(ORDERS_METHOD)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldGetTenOrders() {
        List<OrderResponse> orders = given()
                .when()
                .queryParam(LIMIT_ARG, LIMIT_TEN)
                .queryParam(PAGE_ARG, PAGE_ZERO)
                .get(ORDERS_METHOD)
                .body().as(OrdersList.class).getOrders();
        assertEquals(SIZE_TEN, orders.size());
    }
}