package org.acme;


import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/bubble")
@RegisterRestClient(configKey = "bubbleservice")
@RegisterClientHeaders
public interface BubbleService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject bubble();
}
