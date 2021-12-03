package org.acme;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Hosts {
    
    final private Set<String> hosts = new HashSet<>();
    final private Map<String, Integer> stats = new HashMap<>();

    public void add(String host, int percentage) {
        hosts.add(host);
        stats.put(host, percentage);
    }

    public Set<String> getHosts() {
        return hosts;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

}
