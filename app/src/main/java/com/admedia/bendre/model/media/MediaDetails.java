package com.admedia.bendre.model.media;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.admedia.bendre.model.ImageMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "width",
        "height",
        "file",
        "sizes",
        "image_meta"
})
public class MediaDetails implements Serializable {

    @JsonProperty("width")
    private Long width;
    @JsonProperty("height")
    private Long height;
    @JsonProperty("file")
    private String file;
    @JsonProperty("sizes")
    private Sizes sizes;
    @JsonProperty("image_meta")
    private ImageMeta imageMeta;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("file")
    public String getFile() {
        return file;
    }

    @JsonProperty("file")
    public void setFile(String file) {
        this.file = file;
    }

    @JsonProperty("sizes")
    public Sizes getSizes() {
        return sizes;
    }

    @JsonProperty("sizes")
    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    @JsonProperty("image_meta")
    public ImageMeta getImageMeta() {
        return imageMeta;
    }

    @JsonProperty("image_meta")
    public void setImageMeta(ImageMeta imageMeta) {
        this.imageMeta = imageMeta;
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