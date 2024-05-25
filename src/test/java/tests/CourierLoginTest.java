package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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
    @DisplayName("Should login")
    @Description("Login test for /api/v1/courier/login endpoint - POST method")
    public void shouldLogin() {
        Response response = sendPostRequestToLoginWithLoginAndPassword();
        checkIdFieldIsNotEmptyAndStatusCode(response);
    }

    @Test
    @DisplayName("Should not login without login field")
    @Description("Checking lack of login for /api/v1/courier/login endpoint - POST method")
    public void shouldNotLoginWithoutLogin() {
        Response response = sendPostRequestToLoginWithoutLogin();
        checkBodyMessageLackOfDataErrorAndStatusCode(response);
    }

    @Test
    @DisplayName("Should not login without password field")
    @Description("Checking lack of password for /api/v1/courier/login endpoint - POST method")
    public void shouldNotLoginWithoutPassword() {
        Response response = sendPostRequestToLoginWithoutPassword();
        checkBodyMessageLackOfDataErrorAndStatusCode(response);
    }

    @Test
    @DisplayName("Should not login with the wrong password")
    @Description("Checking wrong password for /api/v1/courier/login endpoint - POST method")
    public void shouldNotLoginWithWrongPassword() {
        Response response = sendPostRequestToLoginWithWrongPassword();
        checkBodyMessageWrongPasswordErrorAndStatusCode(response);
    }

    @Step("Send POST-request /api/v1/courier/login with login and password")
    private Response sendPostRequestToLoginWithLoginAndPassword() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .when()
                .post(COURIER_LOGIN_METHOD);
    }

    @Step("Check Id-field and Status code")
    private void checkIdFieldIsNotEmptyAndStatusCode(Response response) {
        response.then().assertThat().body(ID_FIELD, notNullValue())
                .and().statusCode(HttpStatus.SC_OK);
    }

    @Step("Send POST-request /api/v1/courier/login without login")
    private Response sendPostRequestToLoginWithoutLogin() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(EMPTY_STRING, courier.getPassword()))
                .when()
                .post(COURIER_LOGIN_METHOD);
    }

    @Step("Send POST-request /api/v1/courier/login without password")
    private Response sendPostRequestToLoginWithoutPassword() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), EMPTY_STRING))
                .when()
                .post(COURIER_LOGIN_METHOD);
    }

    @Step("Send POST-request /api/v1/courier/login with the wrong password")
    private Response sendPostRequestToLoginWithWrongPassword() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), WRONG_PASSWORD))
                .when()
                .post(COURIER_LOGIN_METHOD);
    }

    @Step("Check Body message lack of login data error and Status code")
    private void checkBodyMessageLackOfDataErrorAndStatusCode(Response response) {
        response.then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_LOGIN_DATA_ERROR))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Step("Check Body message lack of wrong password error and Status code")
    private void checkBodyMessageWrongPasswordErrorAndStatusCode(Response response) {
        response.then().assertThat().body(BODY_MESSAGE, equalTo(USER_NOT_FOUND_ERROR))
                .and().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}