package com.aphoh.muser.data.network.model.soundcloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

  @SerializedName("kind") @Expose private String kind;
  @SerializedName("id") @Expose private long id;
  @SerializedName("created_at") @Expose private String created_at;
  @SerializedName("user_id") @Expose private long user_id;
  @SerializedName("duration") @Expose private long duration;
  @SerializedName("commentable") @Expose private boolean commentable;
  @SerializedName("state") @Expose private String state;
  @SerializedName("original_content_size") @Expose private long original_content_size;
  @SerializedName("last_modified") @Expose private String last_modified;
  @SerializedName("sharing") @Expose private String sharing;
  @SerializedName("tag_list") @Expose private String tag_list;
  @SerializedName("permalink") @Expose private String permalink;
  @SerializedName("streamable") @Expose private boolean streamable;
  @SerializedName("embeddable_by") @Expose private String embeddable_by;
  @SerializedName("downloadable") @Expose private boolean downloadable;
  @SerializedName("purchase_url") @Expose private Object purchase_url;
  @SerializedName("label_id") @Expose private Object label_id;
  @SerializedName("purchase_title") @Expose private Object purchase_title;
  @SerializedName("genre") @Expose private String genre;
  @SerializedName("title") @Expose private String title;
  @SerializedName("description") @Expose private String description;
  @SerializedName("label_name") @Expose private String label_name;
  @SerializedName("release") @Expose private String release;
  @SerializedName("track_type") @Expose private String track_type;
  @SerializedName("key_signature") @Expose private String key_signature;
  @SerializedName("isrc") @Expose private String isrc;
  @SerializedName("video_url") @Expose private Object video_url;
  @SerializedName("bpm") @Expose private long bpm;
  @SerializedName("release_year") @Expose private Object release_year;
  @SerializedName("release_month") @Expose private Object release_month;
  @SerializedName("release_day") @Expose private Object release_day;
  @SerializedName("original_format") @Expose private String original_format;
  @SerializedName("license") @Expose private String license;
  @SerializedName("uri") @Expose private String uri;
  @SerializedName("user") @Expose private User user;
  @SerializedName("permalink_url") @Expose private String permalink_url;
  @SerializedName("artwork_url") @Expose private String artwork_url;
  @SerializedName("waveform_url") @Expose private String waveform_url;
  @SerializedName("stream_url") @Expose private String stream_url;
  @SerializedName("download_url") @Expose private String download_url;
  @SerializedName("playback_count") @Expose private long playback_count;
  @SerializedName("download_count") @Expose private long download_count;
  @SerializedName("favoritings_count") @Expose private long favoritings_count;
  @SerializedName("comment_count") @Expose private long comment_count;
  @SerializedName("attachments_uri") @Expose private String attachments_uri;
  @SerializedName("policy") @Expose private String policy;
  @SerializedName("monetization_model") @Expose private String monetization_model;

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
   * @return The created_at
   */
  public String getCreated_at() {
    return created_at;
  }

  /**
   * @param created_at The created_at
   */
  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  /**
   * @return The user_id
   */
  public long getUser_id() {
    return user_id;
  }

  /**
   * @param user_id The user_id
   */
  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }

  /**
   * @return The duration
   */
  public long getDuration() {
    return duration;
  }

  /**
   * @param duration The duration
   */
  public void setDuration(long duration) {
    this.duration = duration;
  }

  /**
   * @return The commentable
   */
  public boolean isCommentable() {
    return commentable;
  }

  /**
   * @param commentable The commentable
   */
  public void setCommentable(boolean commentable) {
    this.commentable = commentable;
  }

  /**
   * @return The state
   */
  public String getState() {
    return state;
  }

  /**
   * @param state The state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * @return The original_content_size
   */
  public long getOriginal_content_size() {
    return original_content_size;
  }

  /**
   * @param original_content_size The original_content_size
   */
  public void setOriginal_content_size(long original_content_size) {
    this.original_content_size = original_content_size;
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
   * @return The sharing
   */
  public String getSharing() {
    return sharing;
  }

  /**
   * @param sharing The sharing
   */
  public void setSharing(String sharing) {
    this.sharing = sharing;
  }

  /**
   * @return The tag_list
   */
  public String getTag_list() {
    return tag_list;
  }

  /**
   * @param tag_list The tag_list
   */
  public void setTag_list(String tag_list) {
    this.tag_list = tag_list;
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
   * @return The streamable
   */
  public boolean isStreamable() {
    return streamable;
  }

  /**
   * @param streamable The streamable
   */
  public void setStreamable(boolean streamable) {
    this.streamable = streamable;
  }

  /**
   * @return The embeddable_by
   */
  public String getEmbeddable_by() {
    return embeddable_by;
  }

  /**
   * @param embeddable_by The embeddable_by
   */
  public void setEmbeddable_by(String embeddable_by) {
    this.embeddable_by = embeddable_by;
  }

  /**
   * @return The downloadable
   */
  public boolean isDownloadable() {
    return downloadable;
  }

  /**
   * @param downloadable The downloadable
   */
  public void setDownloadable(boolean downloadable) {
    this.downloadable = downloadable;
  }

  /**
   * @return The purchase_url
   */
  public Object getPurchase_url() {
    return purchase_url;
  }

  /**
   * @param purchase_url The purchase_url
   */
  public void setPurchase_url(Object purchase_url) {
    this.purchase_url = purchase_url;
  }

  /**
   * @return The label_id
   */
  public Object getLabel_id() {
    return label_id;
  }

  /**
   * @param label_id The label_id
   */
  public void setLabel_id(Object label_id) {
    this.label_id = label_id;
  }

  /**
   * @return The purchase_title
   */
  public Object getPurchase_title() {
    return purchase_title;
  }

  /**
   * @param purchase_title The purchase_title
   */
  public void setPurchase_title(Object purchase_title) {
    this.purchase_title = purchase_title;
  }

  /**
   * @return The genre
   */
  public String getGenre() {
    return genre;
  }

  /**
   * @param genre The genre
   */
  public void setGenre(String genre) {
    this.genre = genre;
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
   * @return The label_name
   */
  public String getLabel_name() {
    return label_name;
  }

  /**
   * @param label_name The label_name
   */
  public void setLabel_name(String label_name) {
    this.label_name = label_name;
  }

  /**
   * @return The release
   */
  public String getRelease() {
    return release;
  }

  /**
   * @param release The release
   */
  public void setRelease(String release) {
    this.release = release;
  }

  /**
   * @return The track_type
   */
  public String getTrack_type() {
    return track_type;
  }

  /**
   * @param track_type The track_type
   */
  public void setTrack_type(String track_type) {
    this.track_type = track_type;
  }

  /**
   * @return The key_signature
   */
  public String getKey_signature() {
    return key_signature;
  }

  /**
   * @param key_signature The key_signature
   */
  public void setKey_signature(String key_signature) {
    this.key_signature = key_signature;
  }

  /**
   * @return The isrc
   */
  public String getIsrc() {
    return isrc;
  }

  /**
   * @param isrc The isrc
   */
  public void setIsrc(String isrc) {
    this.isrc = isrc;
  }

  /**
   * @return The video_url
   */
  public Object getVideo_url() {
    return video_url;
  }

  /**
   * @param video_url The video_url
   */
  public void setVideo_url(Object video_url) {
    this.video_url = video_url;
  }

  /**
   * @return The bpm
   */
  public long getBpm() {
    return bpm;
  }

  /**
   * @param bpm The bpm
   */
  public void setBpm(long bpm) {
    this.bpm = bpm;
  }

  /**
   * @return The release_year
   */
  public Object getRelease_year() {
    return release_year;
  }

  /**
   * @param release_year The release_year
   */
  public void setRelease_year(Object release_year) {
    this.release_year = release_year;
  }

  /**
   * @return The release_month
   */
  public Object getRelease_month() {
    return release_month;
  }

  /**
   * @param release_month The release_month
   */
  public void setRelease_month(Object release_month) {
    this.release_month = release_month;
  }

  /**
   * @return The release_day
   */
  public Object getRelease_day() {
    return release_day;
  }

  /**
   * @param release_day The release_day
   */
  public void setRelease_day(Object release_day) {
    this.release_day = release_day;
  }

  /**
   * @return The original_format
   */
  public String getOriginal_format() {
    return original_format;
  }

  /**
   * @param original_format The original_format
   */
  public void setOriginal_format(String original_format) {
    this.original_format = original_format;
  }

  /**
   * @return The license
   */
  public String getLicense() {
    return license;
  }

  /**
   * @param license The license
   */
  public void setLicense(String license) {
    this.license = license;
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
   * @return The user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user The user
   */
  public void setUser(User user) {
    this.user = user;
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
   * @return The artwork_url
   */
  public String getArtwork_url() {
    return artwork_url;
  }

  /**
   * @param artwork_url The artwork_url
   */
  public void setArtwork_url(String artwork_url) {
    this.artwork_url = artwork_url;
  }

  /**
   * @return The waveform_url
   */
  public String getWaveform_url() {
    return waveform_url;
  }

  /**
   * @param waveform_url The waveform_url
   */
  public void setWaveform_url(String waveform_url) {
    this.waveform_url = waveform_url;
  }

  /**
   * @return The stream_url
   */
  public String getStream_url() {
    return stream_url;
  }

  /**
   * @param stream_url The stream_url
   */
  public void setStream_url(String stream_url) {
    this.stream_url = stream_url;
  }

  /**
   * @return The download_url
   */
  public String getDownload_url() {
    return download_url;
  }

  /**
   * @param download_url The download_url
   */
  public void setDownload_url(String download_url) {
    this.download_url = download_url;
  }

  /**
   * @return The playback_count
   */
  public long getPlayback_count() {
    return playback_count;
  }

  /**
   * @param playback_count The playback_count
   */
  public void setPlayback_count(long playback_count) {
    this.playback_count = playback_count;
  }

  /**
   * @return The download_count
   */
  public long getDownload_count() {
    return download_count;
  }

  /**
   * @param download_count The download_count
   */
  public void setDownload_count(long download_count) {
    this.download_count = download_count;
  }

  /**
   * @return The favoritings_count
   */
  public long getFavoritings_count() {
    return favoritings_count;
  }

  /**
   * @param favoritings_count The favoritings_count
   */
  public void setFavoritings_count(long favoritings_count) {
    this.favoritings_count = favoritings_count;
  }

  /**
   * @return The comment_count
   */
  public long getComment_count() {
    return comment_count;
  }

  /**
   * @param comment_count The comment_count
   */
  public void setComment_count(long comment_count) {
    this.comment_count = comment_count;
  }

  /**
   * @return The attachments_uri
   */
  public String getAttachments_uri() {
    return attachments_uri;
  }

  /**
   * @param attachments_uri The attachments_uri
   */
  public void setAttachments_uri(String attachments_uri) {
    this.attachments_uri = attachments_uri;
  }

  /**
   * @return The policy
   */
  public String getPolicy() {
    return policy;
  }

  /**
   * @param policy The policy
   */
  public void setPolicy(String policy) {
    this.policy = policy;
  }

  /**
   * @return The monetization_model
   */
  public String getMonetization_model() {
    return monetization_model;
  }

  /**
   * @param monetization_model The monetization_model
   */
  public void setMonetization_model(String monetization_model) {
    this.monetization_model = monetization_model;
  }
}
