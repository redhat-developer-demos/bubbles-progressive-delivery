package org.acme;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;
import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.runtime.configuration.ProfileManager;

@Path("/bubble")
public class BubbleResource {
   
    private AtomicLong errors = new AtomicLong();

    @RestClient
    BubbleService bubbleService;

    @Inject
    Statistics statistics;

    @Inject
    Hosts hosts;

    @Inject
    Logger log;

    @GET
    public JsonObject bubble() throws UnknownHostException {
        try {
            JsonObject bubble = bubbleService.bubble();
            statistics.add(bubble);
            return bubble;
        } catch(WebApplicationException e) {
            JsonObject bubble = Json.createObjectBuilder().add("color", "yellow")
                .add("hostname", "error")
                .add("requests", errors.incrementAndGet()).build();

            //System.out.println(e.getResponse().readEntity(JsonObject.class));
            statistics.add(bubble);
            return bubble;
        }
    }

    @GET
    @Path("/statistics")
    public Statistics statistics() {
        return statistics;
    }

    @Inject
    KubernetesClient kubernetesClient;

    @Named("namespace") String namespace;

    @GET
    @Path("/kn/{name}")
    public Hosts getKNativeRoute(@PathParam("name") String knativeRouteName) {
        if (kubernetesClient != null && ! ConfigUtils.getProfiles().stream().anyMatch(profile -> profile.contains("dev") || profile.contains("test") )) {
            GenericKubernetesResource kNativeRoute = getKNativeRouteDefinition(knativeRouteName);

            if (kNativeRoute != null) {
                populateKNativeRouteHosts(kNativeRoute);
            }
        }

        return hosts;
    }

    private GenericKubernetesResource getKNativeRouteDefinition(String knativeRouteName) {
        final ResourceDefinitionContext resourceDefinitionContext = new ResourceDefinitionContext.Builder()
            .withGroup("serving.knative.dev")
            .withVersion("v1")
            .withKind("Route")
            .withPlural("routes")
            .withNamespaced(true)
            .build();

        final GenericKubernetesResource knativeRoute = kubernetesClient.genericKubernetesResources(resourceDefinitionContext).inNamespace(namespace).withName(knativeRouteName).get();
        return knativeRoute;
    }

    private void populateKNativeRouteHosts(final GenericKubernetesResource kNativeRoute) {
        final Map<String, Object> spec = (Map<String, Object>) kNativeRoute.getAdditionalProperties().get("spec");
        final List<Map<String, Object>> traffics = (List<Map<String, Object>>) spec.get("traffic");

        for (Map<String, Object> traffic : traffics) {
            Integer weight = (Integer) traffic.get("percent");
            String revisionName = (String) traffic.get("revisionName");

            hosts.add(revisionName, weight);
        }

    }

    @GET
    @Path("/vs/{name}")
    public Hosts getVirtualService(@PathParam("name") String virtualServiceName) {

        if (kubernetesClient != null) {
            final GenericKubernetesResource virtualResource = getVirtualServiceDefinition(virtualServiceName);

            if (virtualResource != null) {
                populateVirtualServicesHosts(virtualResource);
            }
        }
        return hosts;
    }

    private GenericKubernetesResource getVirtualServiceDefinition(String virtualServiceName) {
        final ResourceDefinitionContext resourceDefinitionContext = new ResourceDefinitionContext.Builder()
            .withGroup("networking.istio.io")
            .withVersion("v1alpha3")
            .withKind("VirtualService")
            .withPlural("virtualservices")
            .withNamespaced(true)
            .build();

        final GenericKubernetesResource virtualResource = kubernetesClient.genericKubernetesResources(resourceDefinitionContext).inNamespace(namespace).withName(virtualServiceName).get();
        return virtualResource;
    }

    private void populateVirtualServicesHosts(final GenericKubernetesResource virtualResource) {
        final Map<String, Object> spec = (Map<String, Object>) virtualResource.getAdditionalProperties().get("spec");
        final List<Map<String, Object>> https = (List<Map<String, Object>>) spec.get("http");

        for (Map<String, Object> route : https) {
            final List<Map<String, Object>> destinations = (List<Map<String, Object>>) route.get("route");
            for (Map<String, Object> destination : destinations) {
                Integer weight = (Integer) destination.get("weight");
                
                Map<String, Object> d = (Map<String, Object>) destination.get("destination");
                String host = (String) d.get("host");
                if (d.containsKey("subset")) {
                    host += " " + (String) d.get("subset");
                }

                hosts.add(host, weight);
            }
        }
    }

}