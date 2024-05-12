package tests;

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
    public void shouldCreateOrderWithDifferentColorsData() {
        order.setColor(color);
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(order)
                .when()
                .post(ORDERS_METHOD)
                .then().assertThat().body(TRACK_FIELD, notNullValue())
                .and()
                .statusCode(HttpStatus.SC_CREATED);
    }
}