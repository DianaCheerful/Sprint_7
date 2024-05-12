package tests;

import model.Courier;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static constants.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CourierLoginTest extends BaseCourierTest {
    public CourierLoginTest(Courier courier) {
        super(courier);
    }

    @Before
    public void createCourier() {
        createCourier(courier);
    }

    @After
    public void deleteCourier() {
        deleteCourier(courier);
    }


    @Test
    public void shouldLogin() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .when()
                .post(COURIER_LOGIN_METHOD)
                .then().assertThat().body(ID_FIELD, notNullValue())
                .and()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldNotLoginWithoutLogin() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(EMPTY_STRING, courier.getPassword()))
                .when()
                .post(COURIER_LOGIN_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_LOGIN_DATA_ERROR))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldNotLoginWithoutPassword() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), EMPTY_STRING))
                .when()
                .post(COURIER_LOGIN_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_LOGIN_DATA_ERROR))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), WRONG_PASSWORD))
                .when()
                .post(COURIER_LOGIN_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(USER_NOT_FOUND_ERROR))
                .and()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}