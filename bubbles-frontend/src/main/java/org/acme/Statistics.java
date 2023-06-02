package org.acme;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;

@ApplicationScoped
public class Statistics {
    
    final private Set<String> hosts = new HashSet<>();
    final private Map<String, Long> requests = new ConcurrentHashMap<>(); 

    public void add(JsonObject object) {
        hosts.add(object.getString("hostname"));
        requests.put(object.getString("hostname"), 
                    object.getJsonNumber("requests").longValue()
        );
    }

    public Map<String, Long> getRequests() {
        return requests;
    }

    public Set<String> getHosts() {
        return hosts;
    }

}
