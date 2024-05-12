package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static constants.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseOrderTest {

    private Order order;

    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Before
    public void fillOrder() {
        order = new Order(
                ORDER_FIRST_NAME,
                ORDER_ADDRESS,
                ORDER_METRO_STATION,
                ORDER_PHONE,
                ORDER_RENT_TIME,
                ORDER_METRO_DELIVERY_DATE,
                ORDER_METRO_COMMENT,
                BLACK_COLOR_ARR
        );
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][]{
                {new String[]{BLACK_COLOR}},
                {new String[]{GREY_COLOR}},
                {new String[]{BLACK_COLOR, GREY_COLOR}},
                {new String[]{}},
        };
    }


    @Test
    @DisplayName("Should create an order with different colors data")
    @Description("Checking one or several colors while creating an order for /api/v1/orders endpoint - POST method")
    public void shouldCreateOrderWithDifferentColorsData() {
        setColorParameter();
        Response response = sendPostRequestToCreateOrderWithColorOptions();
        checkTrackFieldIsNotEmptyAndStatusCode(response);
    }

    @Step("Set color parameter")
    private void setColorParameter() {
        order.setColor(color);
    }


    @Step("Send POST-request /api/v1/orders with color options")
    private Response sendPostRequestToCreateOrderWithColorOptions() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(order)
                .when()
                .post(ORDERS_METHOD);
    }

    @Step("Check Id-field and Status code")
    private void checkTrackFieldIsNotEmptyAndStatusCode(Response response) {
        response.then().assertThat().body(TRACK_FIELD, notNullValue())
                .and().statusCode(HttpStatus.SC_CREATED);
    }
}