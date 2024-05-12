package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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
    @DisplayName("Should create a courier")
    @Description("Create test for /api/v1/courier endpoint - POST method")
    public void shouldCreateCourier() {
        Response response = sendPostRequestCreateCourier();
        checkResponseAndStatusCode(response);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Should not create the same courier twice")
    @Description("Checking double creating for /api/v1/courier endpoint - POST method")
    public void shouldNotCreateCourierTwice() {
        createCourier(courier);
        Response response = sendPostRequestCreateCourier();
        checkLoginIsAlreadyUsedErrorAndStatusCode(response);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Should not create a courier without login field")
    @Description("Checking lack of login for /api/v1/courier endpoint - POST method")
    public void shouldNotCreateCourierWithoutLogin() {
        Response response = sendPostRequestCreateCourierWithEmptyLogin();
        checkLackOfDataErrorAndStatusCode(response);
    }

    @Test
    @DisplayName("Should not create a courier without password field")
    @Description("Checking lack of password for /api/v1/courier endpoint - POST method")
    public void shouldNotCreateCourierWithoutPassword() {
        Response response = sendPostRequestCreateCourierWithEmptyPassword();
        checkLackOfDataErrorAndStatusCode(response);
    }

    @Test
    @DisplayName("Should not create a courier without any field")
    @Description("Checking lack of fields for /api/v1/courier endpoint - POST method")
    public void shouldNotCreateCourierWithoutFields() {
        Response response = sendPostRequestCreateCourierWithEmptyFields();
        checkLackOfDataErrorAndStatusCode(response);
    }

    @Step("Send POST-request /api/v1/courier with correct data")
    private Response sendPostRequestCreateCourier() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(courier)
                .when()
                .post(CREATE_COURIER_METHOD);
    }

    @Step("Check that courier was created successfully")
    private void checkResponseAndStatusCode(Response response) {
        response
                .then().assertThat().body(RESPONSE_OK, equalTo(true))
                .and().statusCode(HttpStatus.SC_CREATED);
    }

    @Step("Check that courier with the same data cannot be created twice")
    private void checkLoginIsAlreadyUsedErrorAndStatusCode(Response response) {
        response
                .then().assertThat().body(BODY_MESSAGE, equalTo(LOGIN_IS_ALREADY_USED_ERROR))
                .and().statusCode(HttpStatus.SC_CONFLICT);
    }

    @Step("Send POST-request /api/v1/courier with empty login")
    private Response sendPostRequestCreateCourierWithEmptyLogin() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(EMPTY_STRING, courier.getPassword()))
                .when()
                .post(CREATE_COURIER_METHOD);
    }

    @Step("Send POST-request /api/v1/courier with empty password")
    private Response sendPostRequestCreateCourierWithEmptyPassword() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier(courier.getLogin(), EMPTY_STRING))
                .when()
                .post(CREATE_COURIER_METHOD);
    }

    @Step("Send POST-request /api/v1/courier with empty fields")
    private Response sendPostRequestCreateCourierWithEmptyFields() {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .and()
                .body(new Courier())
                .when()
                .post(CREATE_COURIER_METHOD);
    }

    @Step("Check that courier with the same data cannot be created twice")
    private void checkLackOfDataErrorAndStatusCode(Response response) {
        response
                .then().assertThat().body(BODY_MESSAGE, equalTo(LACK_OF_CREATE_DATA_ERROR))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}