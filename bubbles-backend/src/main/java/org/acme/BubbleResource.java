package org.acme;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@Path("/bubble")
public class BubbleResource {

    @Inject
    Logger log;

    @ConfigProperty(name = "bubble.color")
    String color;

    private boolean misbehave;
    private boolean sleep;

    private AtomicLong counter = new AtomicLong(0);

    @GET
    @Path("/sleep")
    @Produces(MediaType.TEXT_PLAIN)
    public String sleep() {
        log.info("Sleeping");
        sleep = true;
        return "Sleep Neo";
    }

    @GET
    @Path("/awake")
    @Produces(MediaType.TEXT_PLAIN)
    public String awake() {
        log.info("Awaking");
        sleep = false;
        return "Awaking";
    }

    @GET
    @Path("/misbehave")
    @Produces(MediaType.TEXT_PLAIN)
    public String misbehave() {
        log.info("Misbehaving");
        misbehave = true;
        return "Following calls will return a 5XX error code";
    }

    @GET
    @Path("/behave")
    @Produces(MediaType.TEXT_PLAIN)
    public String behave() {
        log.info("Behaving");
        misbehave = false;
        return "Back to normal";
    }

    @GET
    public Response bubble(@Context HttpHeaders headers) throws UnknownHostException {

        if (misbehave) {

            final Bubble b = new Bubble(  "yellow", 
                            InetAddress.getLocalHost().getHostName(), 
                            counter.incrementAndGet()
                        );

            log.infov("Generating {0} Bubble", b.color);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(b)
                            .build();
        }

        if (sleep) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        final Bubble b = new Bubble(  color, 
                            InetAddress.getLocalHost().getHostName(), 
                            counter.incrementAndGet()
                        );
        
        log.infov("Generating {0} Bubble {1}", b.color, counter.get());
        return Response.ok(b).build();
    }

}