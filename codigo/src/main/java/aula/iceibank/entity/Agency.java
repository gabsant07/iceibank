package aula.iceibank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agencies")
public class Agency {

    @Id
    private Integer id;
    private String name;
    private String baseUrl;

    protected Agency() {
    }

    public Agency(Integer id, String name, String baseUrl) {
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
