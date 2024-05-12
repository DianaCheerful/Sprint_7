package tests;

import model.Courier;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static constants.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CourierCreateTest extends BaseCourierTest {


    public CourierCreateTest(Courier courier) {
        super(courier);
    }


    @Test
    public void shouldCreateCourier() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(courier)
                .when()
                .post(CREATE_COURIER_METHOD)
                .then().assertThat().body(RESPONSE_OK, equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_CREATED);
        deleteCourier(courier);
    }

    @Test
    public void shouldNotCreateCourierTwice() {
        createCourier(courier);
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(courier)
                .when()
                .post(CREATE_COURIER_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(LOGIN_IS_ALREADY_USED_ERROR))
                .and()
                .statusCode(HttpStatus.SC_CONFLICT);
        deleteCourier(courier);
    }

    @Test
    public void shouldNotCreateCourierWithoutLogin() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(EMPTY_STRING, courier.getPassword()))
                .when()
                .post(CREATE_COURIER_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_CREATE_DATA_ERROR))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateCourierWithoutPassword() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), EMPTY_STRING))
                .when()
                .post(CREATE_COURIER_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_CREATE_DATA_ERROR))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateCourierWithoutFields() {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier())
                .when()
                .post(CREATE_COURIER_METHOD)
                .then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_CREATE_DATA_ERROR))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}