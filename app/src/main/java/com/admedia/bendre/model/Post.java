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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "date",
        "date_gmt",
        "guid",
        "modified",
        "modified_gmt",
        "slug",
        "status",
        "type",
        "link",
        "title",
        "content",
        "excerpt",
        "author",
        "featured_media",
        "comment_status",
        "ping_status",
        "sticky",
        "template",
        "format",
        "meta",
        "categories",
        "tags",
        "_links",
        "_embedded"
})
public class Post implements Serializable {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("date")
    private String date;
    @JsonProperty("date_gmt")
    private String dateGmt;
    @JsonProperty("guid")
    private Guid guid;
    @JsonProperty("modified")
    private String modified;
    @JsonProperty("modified_gmt")
    private String modifiedGmt;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private String type;
    @JsonProperty("link")
    private String link;
    @JsonProperty("title")
    private Title title;
    @JsonProperty("content")
    private Content content;
    @JsonProperty("excerpt")
    private Excerpt excerpt;
    @JsonProperty("author")
    private Long author;
    @JsonProperty("featured_media")
    private Long featuredMedia;
    @JsonProperty("comment_status")
    private String commentStatus;
    @JsonProperty("ping_status")
    private String pingStatus;
    @JsonProperty("sticky")
    private Boolean sticky;
    @JsonProperty("template")
    private String template;
    @JsonProperty("format")
    private String format;
    @JsonProperty("meta")
    private List<Object> meta = null;
    @JsonProperty("categories")
    private List<Long> categories = null;
    @JsonProperty("tags")
    private List<Object> tags = null;
    @JsonProperty("_links")
    private Links links;
    @JsonProperty("_embedded")
    private Embedded embedded;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    @JsonIgnore
    private String mediaLink;
    @JsonIgnore
    private String authorName;
    private String postType;

    public Post() {

    }

    public Post(JsonObject object) {
        this.id = object.get("id").getAsLong();
        this.date = object.get("date").getAsString();
        this.dateGmt = object.get("date_gmt").getAsString();
//        this.guid = object.get("guid").getAsJsonObject()
        this.modified = object.get("modified").getAsString();
        this.modifiedGmt = object.get("modified_gmt").getAsString();
        this.slug = object.get("slug").getAsString();
        this.status = object.get("status").getAsString();
        this.type = object.get("type").getAsString();
        this.link = object.get("link").getAsString();
        this.title = new Title(object.get("title").getAsJsonObject());
        this.content = new Content(object.get("content").getAsJsonObject());
        this.excerpt = new Excerpt(object.get("excerpt").getAsJsonObject());
        this.author = object.get("author").getAsLong();
        this.featuredMedia = object.get("featured_media").getAsLong();
        this.commentStatus = object.get("comment_status").getAsString();
        this.pingStatus = object.get("ping_status").getAsString();
        this.sticky = object.get("sticky").getAsBoolean();
        this.template = object.get("template").getAsString();
        this.format = object.get("format").getAsString();

        if (object.get("categories") != null)
        {
            categories = new ArrayList<>();
            JsonArray categoriesArray = object.get("categories").getAsJsonArray();
            for (int index = 0; index < categoriesArray.size(); index++)
            {
                categories.add(categoriesArray.get(index).getAsLong());
            }
        }
        if (object.get("links") != null)
        {
            this.links = new Links(object.get("links").getAsJsonObject());
        }
        if (object.get("_embedded") != null)
        {
            this.embedded = new Embedded(object.get("_embedded").getAsJsonObject());
        }

    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("date_gmt")
    public String getDateGmt() {
        return dateGmt;
    }

    @JsonProperty("date_gmt")
    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

    @JsonProperty("guid")
    public Guid getGuid() {
        return guid;
    }

    @JsonProperty("guid")
    public void setGuid(Guid guid) {
        this.guid = guid;
    }

    @JsonProperty("modified")
    public String getModified() {
        return modified;
    }

    @JsonProperty("modified")
    public void setModified(String modified) {
        this.modified = modified;
    }

    @JsonProperty("modified_gmt")
    public String getModifiedGmt() {
        return modifiedGmt;
    }

    @JsonProperty("modified_gmt")
    public void setModifiedGmt(String modifiedGmt) {
        this.modifiedGmt = modifiedGmt;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("title")
    public Title getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(Title title) {
        this.title = title;
    }

    @JsonProperty("content")
    public Content getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(Content content) {
        this.content = content;
    }

    @JsonProperty("excerpt")
    public Excerpt getExcerpt() {
        return excerpt;
    }

    @JsonProperty("excerpt")
    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }

    @JsonProperty("author")
    public Long getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(Long author) {
        this.author = author;
    }

    @JsonProperty("featured_media")
    public Long getFeaturedMedia() {
        return featuredMedia;
    }

    @JsonProperty("featured_media")
    public void setFeaturedMedia(Long featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    @JsonProperty("comment_status")
    public String getCommentStatus() {
        return commentStatus;
    }

    @JsonProperty("comment_status")
    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    @JsonProperty("ping_status")
    public String getPingStatus() {
        return pingStatus;
    }

    @JsonProperty("ping_status")
    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    @JsonProperty("sticky")
    public Boolean getSticky() {
        return sticky;
    }

    @JsonProperty("sticky")
    public void setSticky(Boolean sticky) {
        this.sticky = sticky;
    }

    @JsonProperty("template")
    public String getTemplate() {
        return template;
    }

    @JsonProperty("template")
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }

    @JsonProperty("meta")
    public List<Object> getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(List<Object> meta) {
        this.meta = meta;
    }

    @JsonProperty("categories")
    public List<Long> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonProperty("_embedded")
    public Embedded getEmbedded() {
        return embedded;
    }

    @JsonProperty("_embedded")
    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}