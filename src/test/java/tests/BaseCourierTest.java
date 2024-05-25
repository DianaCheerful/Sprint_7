package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierId;
import org.apache.http.protocol.HTTP;
import org.junit.BeforeClass;
import org.junit.runners.Parameterized;

import static constants.CourierData.*;
import static constants.TestConstants.*;
import static io.restassured.RestAssured.given;

public class BaseCourierTest {

    protected final Courier courier;

    public BaseCourierTest(Courier courier) {
        this.courier = courier;
    }

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Parameterized.Parameters
    public static Object[][] getCourier() {
        return new Object[][]{
                {new Courier(CD_1.getLogin(), DEFAULT_PASSWORD, CD_1.getFirstname())},
                {new Courier(CD_2.getLogin(), DEFAULT_PASSWORD, CD_2.getFirstname())},
                {new Courier(CD_3.getLogin(), DEFAULT_PASSWORD, CD_3.getFirstname())},
                {new Courier(CD_4.getLogin(), DEFAULT_PASSWORD, CD_4.getFirstname())},
        };
    }
    @Step("Creating a courier for testing by POST-request /api/v1/courier")
    public void createCourier(Courier courier) {
        given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .body(courier)
                .post(CREATE_COURIER_METHOD);
    }

    @Step("Getting a courier id for deleting by POST-request /api/v1/courier/login")
    private int getCourierId(Courier courier) {
        return given()
                .header(HTTP.CONTENT_TYPE, JSON_TYPE)
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .post(COURIER_LOGIN_METHOD)
                .body().as(CourierId.class).getId();
    }

    @Step("Send DELETE-request /api/v1/courier/:id to delete courier after tests")
    public void deleteCourier(Courier courier) {
        given().delete(DELETE_COURIER_METHOD + getCourierId(courier));
    }
}