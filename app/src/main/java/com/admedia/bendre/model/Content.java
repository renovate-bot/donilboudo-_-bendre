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
        "rendered",
        "protected"
})
public class Content implements Serializable {
    @JsonProperty("rendered")
    private String rendered;
    @JsonProperty("protected")
    private Boolean _protected;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Content() {

    }

    public Content(JsonObject object) {
        if (object.get("rendered") != null)
        {
            this.rendered = object.get("rendered").getAsString();
        }
        if (object.get("protected") != null)
        {
            this._protected = object.get("protected").getAsBoolean();
        }
    }

    @JsonProperty("rendered")
    public String getRendered() {
        return rendered;
    }

    @JsonProperty("rendered")
    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    @JsonProperty("protected")
    public Boolean getProtected() {
        return _protected;
    }

    @JsonProperty("protected")
    public void setProtected(Boolean _protected) {
        this._protected = _protected;
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