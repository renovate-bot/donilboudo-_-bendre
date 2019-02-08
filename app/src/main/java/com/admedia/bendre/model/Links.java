package com.admedia.bendre.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self",
        "collection",
        "about",
        "author",
        "replies",
        "version-history",
        "predecessor-version",
        "wp:attachment",
        "wp:term",
        "curies"
})
public class Links implements Serializable {

    @JsonProperty("self")
    private List<Self> self = null;
    @JsonProperty("collection")
    private List<Collection> collection = null;
    @JsonProperty("about")
    private List<About> about = null;
    @JsonProperty("author")
    private List<Author> author = null;
    @JsonProperty("replies")
    private List<Reply> replies = null;
    @JsonProperty("version-history")
    private List<VersionHistory> versionHistory = null;
    @JsonProperty("predecessor-version")
    private List<PredecessorVersion> predecessorVersion = null;
    @JsonProperty("wp:attachment")
    private List<WpAttachment> wpAttachment = null;
    @JsonProperty("wp:term")
    private List<WpTerm> wpTerm = null;
    @JsonProperty("curies")
    private List<Cury> curies = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Links(JsonObject object) {
        if (object.get("self") != null)
        {
            JsonArray selfs = object.get("self").getAsJsonArray();
            for (int counter = 0; counter < selfs.size(); counter++)
            {
                self.add(new Self(selfs.get(counter).getAsJsonObject()));
            }
        }

        if (object.get("collection") != null)
        {
            JsonArray collections = object.get("collection").getAsJsonArray();
            for (int counter = 0; counter < collections.size(); counter++)
            {
                collection.add(new Collection(collections.get(counter).getAsJsonObject()));
            }
        }

        if (object.get("replies") != null)
        {
            JsonArray repliesArray = object.get("replies").getAsJsonArray();
            for (int counter = 0; counter < repliesArray.size(); counter++)
            {
                replies.add(new Reply(repliesArray.get(counter).getAsJsonObject()));
            }
        }

        if (object.get("author") != null)
        {
            JsonArray authors = object.get("author").getAsJsonArray();
            for (int counter = 0; counter < authors.size(); counter++)
            {
                author.add(new Author(authors.get(counter).getAsJsonObject()));
            }
        }

        if (object.get("version-history") != null)
        {
            JsonArray versionHistories = object.get("version-history").getAsJsonArray();
            for (int counter = 0; counter < versionHistories.size(); counter++)
            {
                versionHistory.add(new VersionHistory(versionHistories.get(counter).getAsJsonObject()));
            }
        }
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

    @JsonProperty("about")
    public List<About> getAbout() {
        return about;
    }

    @JsonProperty("about")
    public void setAbout(List<About> about) {
        this.about = about;
    }

    @JsonProperty("author")
    public List<Author> getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    @JsonProperty("replies")
    public List<Reply> getReplies() {
        return replies;
    }

    @JsonProperty("replies")
    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @JsonProperty("version-history")
    public List<VersionHistory> getVersionHistory() {
        return versionHistory;
    }

    @JsonProperty("version-history")
    public void setVersionHistory(List<VersionHistory> versionHistory) {
        this.versionHistory = versionHistory;
    }

    @JsonProperty("predecessor-version")
    public List<PredecessorVersion> getPredecessorVersion() {
        return predecessorVersion;
    }

    @JsonProperty("predecessor-version")
    public void setPredecessorVersion(List<PredecessorVersion> predecessorVersion) {
        this.predecessorVersion = predecessorVersion;
    }

    @JsonProperty("wp:attachment")
    public List<WpAttachment> getWpAttachment() {
        return wpAttachment;
    }

    @JsonProperty("wp:attachment")
    public void setWpAttachment(List<WpAttachment> wpAttachment) {
        this.wpAttachment = wpAttachment;
    }

    @JsonProperty("wp:term")
    public List<WpTerm> getWpTerm() {
        return wpTerm;
    }

    @JsonProperty("wp:term")
    public void setWpTerm(List<WpTerm> wpTerm) {
        this.wpTerm = wpTerm;
    }

    @JsonProperty("curies")
    public List<Cury> getCuries() {
        return curies;
    }

    @JsonProperty("curies")
    public void setCuries(List<Cury> curies) {
        this.curies = curies;
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