package tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static constants.TestConstants.SCOOTER_URL;

public class BaseOrderTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }
}