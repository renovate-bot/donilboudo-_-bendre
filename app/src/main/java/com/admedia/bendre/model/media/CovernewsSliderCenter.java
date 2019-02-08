package com.admedia.bendre.model.media;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "file",
        "width",
        "height",
        "mime_type",
        "source_url"
})
public class CovernewsSliderCenter implements Serializable {

    @JsonProperty("file")
    private String file;
    @JsonProperty("width")
    private Long width;
    @JsonProperty("height")
    private Long height;
    @JsonProperty("mime_type")
    private String mimeType;
    @JsonProperty("source_url")
    private String sourceUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("file")
    public String getFile() {
        return file;
    }

    @JsonProperty("file")
    public void setFile(String file) {
        this.file = file;
    }

    @JsonProperty("width")
    public Long getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Long width) {
        this.width = width;
    }

    @JsonProperty("height")
    public Long getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Long height) {
        this.height = height;
    }

    @JsonProperty("mime_type")
    public String getMimeType() {
        return mimeType;
    }

    @JsonProperty("mime_type")
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("source_url")
    public String getSourceUrl() {
        return sourceUrl;
    }

    @JsonProperty("source_url")
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
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