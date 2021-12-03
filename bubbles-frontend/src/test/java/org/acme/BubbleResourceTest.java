package org.acme;

import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.KubernetesTestServer;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WithKubernetesTestServer(crud = false)
@QuarkusTest
public class BubbleResourceTest {

    @KubernetesTestServer
    KubernetesServer mockServer;

    @BeforeEach
    public void before() throws IOException {
        //mockServer.getClient().inNamespace("default");
        final Path path = Paths.get("src/test/resources/virtual-service.json");
        byte[] content = Files.readAllBytes(path);
        mockServer.expect().get().withPath("/apis/networking.istio.io/v1alpha3/namespaces/default/virtualservices/bubble-backend")
                .andReturn(HttpURLConnection.HTTP_OK, new String(content)).always();
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/bubble/vs/bubble-backend")
          .then()
             .statusCode(200)
             .body(is("Hello"));
    }

}
