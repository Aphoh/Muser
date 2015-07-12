package com.aphoh.muser.data.network.model.soundcloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("last_modified")
    @Expose
    private String last_modified;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("permalink_url")
    @Expose
    private String permalink_url;
    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;

    /**
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return The permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * @param permalink The permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The last_modified
     */
    public String getLast_modified() {
        return last_modified;
    }

    /**
     * @param last_modified The last_modified
     */
    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    /**
     * @return The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return The permalink_url
     */
    public String getPermalink_url() {
        return permalink_url;
    }

    /**
     * @param permalink_url The permalink_url
     */
    public void setPermalink_url(String permalink_url) {
        this.permalink_url = permalink_url;
    }

    /**
     * @return The avatar_url
     */
    public String getAvatar_url() {
        return avatar_url;
    }

    /**
     * @param avatar_url The avatar_url
     */
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

}
