package com.admedia.bendre.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "href"
})
public class VersionHistory implements Serializable {
    @JsonProperty("count")
    private Long count;
    @JsonProperty("href")
    private String href;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public VersionHistory(JsonObject object) {
        if (object.get("count") != null)
        {
            this.count = object.get("count").getAsLong();
        }
        if (object.get("href") != null)
        {
            this.href = object.get("href").getAsString();
        }
    }

    @JsonProperty("count")
    public Long getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Long count) {
        this.count = count;
    }

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
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