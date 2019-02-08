package com.admedia.bendre.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.admedia.bendre.model.media.WpFeaturedmedium;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "author",
        "wp:featuredmedia",
        "wp:term"
})
public class Embedded implements Serializable {

    @JsonProperty("author")
    private List<Author> author = null;
    @JsonProperty("wp:featuredmedia")
    private List<WpFeaturedmedium> wpFeaturedmedia = null;
    @JsonProperty("wp:term")
    private List<List<WpTerm>> wpTerm = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Embedded(JsonObject object) {
        if (object.get("author") != null)
        {
            author = new ArrayList<>();
            JsonArray authors = object.get("author").getAsJsonArray();
            for (int counter = 0; counter < authors.size(); counter++)
            {
                author.add(new Author(authors.get(counter).getAsJsonObject()));
            }
        }

        if (object.get("wp:featuredmedia") != null)
        {
            wpFeaturedmedia = new ArrayList<>();
            JsonArray medias = object.get("wp:featuredmedia").getAsJsonArray();
            for (int counter = 0; counter < medias.size(); counter++)
            {
                wpFeaturedmedia.add(new WpFeaturedmedium(medias.get(counter).getAsJsonObject()));
            }
        }

        JsonArray terms = object.get("wp:term").getAsJsonArray();
//        for (int counter = 0; counter < terms.size(); counter++)
//        {
//            wpTerm.add(new WpTerm(terms.get(counter).getAsJsonObject()));
//        }
    }

    @JsonProperty("author")
    public List<Author> getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    @JsonProperty("wp:featuredmedia")
    public List<WpFeaturedmedium> getWpFeaturedmedia() {
        return wpFeaturedmedia;
    }

    @JsonProperty("wp:featuredmedia")
    public void setWpFeaturedmedia(List<WpFeaturedmedium> wpFeaturedmedia) {
        this.wpFeaturedmedia = wpFeaturedmedia;
    }

    @JsonProperty("wp:term")
    public List<List<WpTerm>> getWpTerm() {
        return wpTerm;
    }

    @JsonProperty("wp:term")
    public void setWpTerm(List<List<WpTerm>> wpTerm) {
        this.wpTerm = wpTerm;
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