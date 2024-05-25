package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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
    @DisplayName("Should get OK status while getting orders list")
    public void shouldGetOkStatusWhenGetOrderList() {
        given()
                .when()
                .get(ORDERS_METHOD)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Should get 10 orders")
    @Description("Checking getting the list of 10 orders for /api/v1/orders endpoint - GET method")
    public void shouldGetTenOrders() {
        Response response = sendGetRequestToGetOrdersListSizeTen();
        List<OrderResponse> orders = responseToOrdersList(response);
        compareSize(orders);
    }

    @Step("Send GET-request /api/v1/orders with params limit 10 and page 0")
    private Response sendGetRequestToGetOrdersListSizeTen() {
        return given()
                .when()
                .queryParam(LIMIT_ARG, LIMIT_TEN)
                .queryParam(PAGE_ARG, PAGE_ZERO)
                .get(ORDERS_METHOD);
    }

    @Step("Get response as orders list")
    private List<OrderResponse> responseToOrdersList(Response response) {
        return response.body().as(OrdersList.class).getOrders();
    }

    @Step("Compare size")
    private void compareSize(List<OrderResponse> orders) {
        assertEquals(SIZE_TEN, orders.size());
    }
}