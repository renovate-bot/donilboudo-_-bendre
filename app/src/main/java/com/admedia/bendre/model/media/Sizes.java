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
        "thumbnail",
        "medium",
        "medium_large",
        "large",
        "covernews-slider-center",
        "covernews-featured",
        "covernews-medium",
        "covernews-medium-square",
        "woocommerce_thumbnail",
        "woocommerce_single",
        "woocommerce_gallery_thumbnail",
        "shop_catalog",
        "shop_single",
        "shop_thumbnail",
        "full"
})
public class Sizes implements Serializable {

    @JsonProperty("thumbnail")
    private Thumbnail thumbnail;
    @JsonProperty("medium")
    private Medium medium;
    @JsonProperty("medium_large")
    private MediumLarge mediumLarge;
    @JsonProperty("large")
    private Large large;
    @JsonProperty("covernews-slider-center")
    private CovernewsSliderCenter covernewsSliderCenter;
    @JsonProperty("covernews-featured")
    private CovernewsFeatured covernewsFeatured;
    @JsonProperty("covernews-medium")
    private CovernewsMedium covernewsMedium;
    @JsonProperty("covernews-medium-square")
    private CovernewsMediumSquare covernewsMediumSquare;
    @JsonProperty("woocommerce_thumbnail")
    private WoocommerceThumbnail woocommerceThumbnail;
    @JsonProperty("woocommerce_single")
    private WoocommerceSingle woocommerceSingle;
    @JsonProperty("woocommerce_gallery_thumbnail")
    private WoocommerceGalleryThumbnail woocommerceGalleryThumbnail;
    @JsonProperty("shop_catalog")
    private ShopCatalog shopCatalog;
    @JsonProperty("shop_single")
    private ShopSingle shopSingle;
    @JsonProperty("shop_thumbnail")
    private ShopThumbnail shopThumbnail;
    @JsonProperty("full")
    private Full full;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("thumbnail")
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @JsonProperty("thumbnail")
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @JsonProperty("medium")
    public Medium getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    @JsonProperty("medium_large")
    public MediumLarge getMediumLarge() {
        return mediumLarge;
    }

    @JsonProperty("medium_large")
    public void setMediumLarge(MediumLarge mediumLarge) {
        this.mediumLarge = mediumLarge;
    }

    @JsonProperty("large")
    public Large getLarge() {
        return large;
    }

    @JsonProperty("large")
    public void setLarge(Large large) {
        this.large = large;
    }

    @JsonProperty("covernews-slider-center")
    public CovernewsSliderCenter getCovernewsSliderCenter() {
        return covernewsSliderCenter;
    }

    @JsonProperty("covernews-slider-center")
    public void setCovernewsSliderCenter(CovernewsSliderCenter covernewsSliderCenter) {
        this.covernewsSliderCenter = covernewsSliderCenter;
    }

    @JsonProperty("covernews-featured")
    public CovernewsFeatured getCovernewsFeatured() {
        return covernewsFeatured;
    }

    @JsonProperty("covernews-featured")
    public void setCovernewsFeatured(CovernewsFeatured covernewsFeatured) {
        this.covernewsFeatured = covernewsFeatured;
    }

    @JsonProperty("covernews-medium")
    public CovernewsMedium getCovernewsMedium() {
        return covernewsMedium;
    }

    @JsonProperty("covernews-medium")
    public void setCovernewsMedium(CovernewsMedium covernewsMedium) {
        this.covernewsMedium = covernewsMedium;
    }

    @JsonProperty("covernews-medium-square")
    public CovernewsMediumSquare getCovernewsMediumSquare() {
        return covernewsMediumSquare;
    }

    @JsonProperty("covernews-medium-square")
    public void setCovernewsMediumSquare(CovernewsMediumSquare covernewsMediumSquare) {
        this.covernewsMediumSquare = covernewsMediumSquare;
    }

    @JsonProperty("woocommerce_thumbnail")
    public WoocommerceThumbnail getWoocommerceThumbnail() {
        return woocommerceThumbnail;
    }

    @JsonProperty("woocommerce_thumbnail")
    public void setWoocommerceThumbnail(WoocommerceThumbnail woocommerceThumbnail) {
        this.woocommerceThumbnail = woocommerceThumbnail;
    }

    @JsonProperty("woocommerce_single")
    public WoocommerceSingle getWoocommerceSingle() {
        return woocommerceSingle;
    }

    @JsonProperty("woocommerce_single")
    public void setWoocommerceSingle(WoocommerceSingle woocommerceSingle) {
        this.woocommerceSingle = woocommerceSingle;
    }

    @JsonProperty("woocommerce_gallery_thumbnail")
    public WoocommerceGalleryThumbnail getWoocommerceGalleryThumbnail() {
        return woocommerceGalleryThumbnail;
    }

    @JsonProperty("woocommerce_gallery_thumbnail")
    public void setWoocommerceGalleryThumbnail(WoocommerceGalleryThumbnail woocommerceGalleryThumbnail) {
        this.woocommerceGalleryThumbnail = woocommerceGalleryThumbnail;
    }

    @JsonProperty("shop_catalog")
    public ShopCatalog getShopCatalog() {
        return shopCatalog;
    }

    @JsonProperty("shop_catalog")
    public void setShopCatalog(ShopCatalog shopCatalog) {
        this.shopCatalog = shopCatalog;
    }

    @JsonProperty("shop_single")
    public ShopSingle getShopSingle() {
        return shopSingle;
    }

    @JsonProperty("shop_single")
    public void setShopSingle(ShopSingle shopSingle) {
        this.shopSingle = shopSingle;
    }

    @JsonProperty("shop_thumbnail")
    public ShopThumbnail getShopThumbnail() {
        return shopThumbnail;
    }

    @JsonProperty("shop_thumbnail")
    public void setShopThumbnail(ShopThumbnail shopThumbnail) {
        this.shopThumbnail = shopThumbnail;
    }

    @JsonProperty("full")
    public Full getFull() {
        return full;
    }

    @JsonProperty("full")
    public void setFull(Full full) {
        this.full = full;
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