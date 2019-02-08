package com.admedia.bendre.model.woocommerce;

import com.admedia.bendre.model.Collection;
import com.admedia.bendre.model.Self;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self",
        "collection"
})
public class Links implements Serializable {
    @JsonProperty("self")
    private List<Self> self = null;
    @JsonProperty("collection")
    private List<Collection> collection = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Links (JsonObject object) {
//        if (object.get("self") != null)
//        {
//            List<Self> selfs = new ArrayList<>();
////            length = object.get("length").getAsString();
//        }
    }

    @JsonProperty("self")
    public List<Self> getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(List<Self> self) {
        this.self = self;
    }

    @JsonProperty("collection")
    public List<Collection> getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}