package aula.iceibank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "bank")
public class BankProperties {

    private int agencyId;
    private Map<Integer, String> agencies = new HashMap<>();
    private String eventDirectory;
    private String internalApiKey;

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public Map<Integer, String> getAgencies() {
        return agencies;
    }

    public void setAgencies(Map<Integer, String> agencies) {
        this.agencies = agencies;
    }

    public String getEventDirectory() {
        return eventDirectory;
    }

    public void setEventDirectory(String eventDirectory) {
        this.eventDirectory = eventDirectory;
    }

    public String getInternalApiKey() {
        return internalApiKey;
    }

    public void setInternalApiKey(String internalApiKey) {
        this.internalApiKey = internalApiKey;
    }

    public String agencyUrl(int id) {
        String url = agencies.get(id);
        if (url == null) {
            throw new IllegalArgumentException("Agency " + id + " is not configured");
        }
        return url;
    }
}
