package com.admedia.bendre.model.woocommerce;

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
        "length",
        "width",
        "height"
})
public class Dimensions implements Serializable {

    @JsonProperty("length")
    private String length;
    @JsonProperty("width")
    private String width;
    @JsonProperty("height")
    private String height;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Dimensions(JsonObject object) {
        if (object.get("length") != null)
        {
            length = object.get("length").getAsString();
        }
        if (object.get("width") != null)
        {
            width = object.get("width").getAsString();
        }
        if (object.get("height") != null)
        {
            height = object.get("height").getAsString();
        }
    }

    @JsonProperty("length")
    public String getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("width")
    public String getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(String width) {
        this.width = width;
    }

    @JsonProperty("height")
    public String getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(String height) {
        this.height = height;
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