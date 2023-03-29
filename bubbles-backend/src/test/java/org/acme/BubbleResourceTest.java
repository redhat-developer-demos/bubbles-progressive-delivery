package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;;

@QuarkusTest
public class BubbleResourceTest {

    @Test
    public void testBubbleEndpoint() {
        given()
          .when().get("/bubble")
          .then()
             .statusCode(200)
             .body(containsString("\"color\":"));
    }

}