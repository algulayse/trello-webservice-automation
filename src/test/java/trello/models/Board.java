package trello.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties({"desc","descData","closed","idOrganization","pinned"
        ,"url","shortUrl","prefs","labelNames","limits","idEnterprise"})
public class Board {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}