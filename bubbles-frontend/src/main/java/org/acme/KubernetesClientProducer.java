package org.acme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class KubernetesClientProducer {
    
    @Produces
    @Singleton
    @Named("namespace")
    String findMyCurrentNamespace() {

        String namespace;
        try {
            namespace = new String(Files.readAllBytes(Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/namespace")));
        } catch (IOException e) {
            namespace = "default";
        }

        return namespace;

    }

    @Produces
    @Singleton
    KubernetesClient makeDefaultClient(@Named("namespace") String namespace) {
        return new DefaultKubernetesClient().inNamespace(namespace);
    }

}
