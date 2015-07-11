package com.aphoh.muser.data.network.model.reddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Oembed_ {

    @SerializedName("provider_url")
    @Expose
    private String provider_url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("thumbnail_width")
    @Expose
    private long thumbnail_width;
    @SerializedName("height")
    @Expose
    private long height;
    @SerializedName("width")
    @Expose
    private long width;
    @SerializedName("html")
    @Expose
    private String html;
    @SerializedName("author_name")
    @Expose
    private String author_name;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("provider_name")
    @Expose
    private String provider_name;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnail_url;
    @SerializedName("thumbnail_height")
    @Expose
    private long thumbnail_height;
    @SerializedName("author_url")
    @Expose
    private String author_url;

    /**
     * @return The provider_url
     */
    public String getProvider_url() {
        return provider_url;
    }

    /**
     * @param provider_url The provider_url
     */
    public void setProvider_url(String provider_url) {
        this.provider_url = provider_url;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The thumbnail_width
     */
    public long getThumbnail_width() {
        return thumbnail_width;
    }

    /**
     * @param thumbnail_width The thumbnail_width
     */
    public void setThumbnail_width(long thumbnail_width) {
        this.thumbnail_width = thumbnail_width;
    }

    /**
     * @return The height
     */
    public long getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(long height) {
        this.height = height;
    }

    /**
     * @return The width
     */
    public long getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(long width) {
        this.width = width;
    }

    /**
     * @return The html
     */
    public String getHtml() {
        return html;
    }

    /**
     * @param html The html
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * @return The author_name
     */
    public String getAuthor_name() {
        return author_name;
    }

    /**
     * @param author_name The author_name
     */
    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    /**
     * @return The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return The provider_name
     */
    public String getProvider_name() {
        return provider_name;
    }

    /**
     * @param provider_name The provider_name
     */
    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    /**
     * @return The thumbnail_url
     */
    public String getThumbnail_url() {
        return thumbnail_url;
    }

    /**
     * @param thumbnail_url The thumbnail_url
     */
    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    /**
     * @return The thumbnail_height
     */
    public long getThumbnail_height() {
        return thumbnail_height;
    }

    /**
     * @param thumbnail_height The thumbnail_height
     */
    public void setThumbnail_height(long thumbnail_height) {
        this.thumbnail_height = thumbnail_height;
    }

    /**
     * @return The author_url
     */
    public String getAuthor_url() {
        return author_url;
    }

    /**
     * @param author_url The author_url
     */
    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

}
