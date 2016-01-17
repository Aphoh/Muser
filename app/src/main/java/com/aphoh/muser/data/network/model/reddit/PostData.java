package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;
import java.util.List;

@JsonObject
public class PostData {

  @JsonField(name = "domain")
  private String domain;
  @JsonField(name = "banned_by")
  private Object banned_by;
  @JsonField(name = "media_embed")
  private Media_embed media_embed;
  @JsonField(name = "subreddit")
  private String subreddit;
  @JsonField(name = "selftext_html")
  private String selftext_html;
  @JsonField(name = "selftext")
  private String selftext;
  @JsonField(name = "likes")
  private Object likes;
  @JsonField(name = "suggested_sort")
  private Object suggested_sort;
  @JsonField(name = "user_reports")
  private List<Object> user_reports =
      new ArrayList<Object>();
  @JsonField(name = "secure_media")
  private Secure_media secure_media;
  @JsonField(name = "link_flair_text")
  private String link_flair_text;
  @JsonField(name = "id")
  private String id;
  @JsonField(name = "from_kind")
  private Object from_kind;
  @JsonField(name = "gilded")
  private long gilded;
  @JsonField(name = "archived")
  private boolean archived;
  @JsonField(name = "clicked")
  private boolean clicked;
  @JsonField(name = "report_reasons")
  private Object report_reasons;
  @JsonField(name = "author")
  private String author;
  @JsonField(name = "media")
  private com.aphoh.muser.data.network.model.reddit.Media media;
  @JsonField(name = "score")
  private long score;
  @JsonField(name = "approved_by")
  private Object approved_by;
  @JsonField(name = "over_18")
  private boolean over_18;
  @JsonField(name = "hidden")
  private boolean hidden;
  @JsonField(name = "preview")
  private Preview preview;
  @JsonField(name = "num_comments")
  private long num_comments;
  @JsonField(name = "thumbnail")
  private String thumbnail;
  @JsonField(name = "subreddit_id")
  private String subreddit_id;
  @JsonField(name = "edited")
  private Object edited;
  @JsonField(name = "link_flair_css_class")
  private String link_flair_css_class;
  @JsonField(name = "author_flair_css_class")
  private Object author_flair_css_class;
  @JsonField(name = "downs")
  private long downs;
  @JsonField(name = "secure_media_embed")
  private Secure_media_embed secure_media_embed;
  @JsonField(name = "saved")
  private boolean saved;
  @JsonField(name = "removal_reason")
  private Object removal_reason;
  @JsonField(name = "post_hint")
  private String post_hint;
  @JsonField(name = "stickied")
  private boolean stickied;
  @JsonField(name = "from")
  private Object from;
  @JsonField(name = "is_self")
  private boolean is_self;
  @JsonField(name = "from_id")
  private Object from_id;
  @JsonField(name = "permalink")
  private String permalink;
  @JsonField(name = "name")
  private String name;
  @JsonField(name = "created")
  private long created;
  @JsonField(name = "url")
  private String url;
  @JsonField(name = "author_flair_text")
  private Object author_flair_text;
  @JsonField(name = "title")
  private String title;
  @JsonField(name = "created_utc")
  private long created_utc;
  @JsonField(name = "distinguished")
  private Object distinguished;
  @JsonField(name = "mod_reports")
  private List<Object> mod_reports = new ArrayList<Object>();
  @JsonField(name = "visited")
  private boolean visited;
  @JsonField(name = "num_reports")
  private Object num_reports;
  @JsonField(name = "ups")
  private long ups;

  /**
   * @return The domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * @param domain The domain
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * @return The banned_by
   */
  public Object getBanned_by() {
    return banned_by;
  }

  /**
   * @param banned_by The banned_by
   */
  public void setBanned_by(Object banned_by) {
    this.banned_by = banned_by;
  }

  /**
   * @return The media_embed
   */
  public Media_embed getMedia_embed() {
    return media_embed;
  }

  /**
   * @param media_embed The media_embed
   */
  public void setMedia_embed(Media_embed media_embed) {
    this.media_embed = media_embed;
  }

  /**
   * @return The subreddit
   */
  public String getSubreddit() {
    return subreddit;
  }

  /**
   * @param subreddit The subreddit
   */
  public void setSubreddit(String subreddit) {
    this.subreddit = subreddit;
  }

  /**
   * @return The selftext_html
   */
  public String getSelftext_html() {
    return selftext_html;
  }

  /**
   * @param selftext_html The selftext_html
   */
  public void setSelftext_html(String selftext_html) {
    this.selftext_html = selftext_html;
  }

  /**
   * @return The selftext
   */
  public String getSelftext() {
    return selftext;
  }

  /**
   * @param selftext The selftext
   */
  public void setSelftext(String selftext) {
    this.selftext = selftext;
  }

  /**
   * @return The likes
   */
  public Object getLikes() {
    return likes;
  }

  /**
   * @param likes The likes
   */
  public void setLikes(Object likes) {
    this.likes = likes;
  }

  /**
   * @return The suggested_sort
   */
  public Object getSuggested_sort() {
    return suggested_sort;
  }

  /**
   * @param suggested_sort The suggested_sort
   */
  public void setSuggested_sort(Object suggested_sort) {
    this.suggested_sort = suggested_sort;
  }

  /**
   * @return The user_reports
   */
  public List<Object> getUser_reports() {
    return user_reports;
  }

  /**
   * @param user_reports The user_reports
   */
  public void setUser_reports(List<Object> user_reports) {
    this.user_reports = user_reports;
  }

  /**
   * @return The secure_media
   */
  public Secure_media getSecure_media() {
    return secure_media;
  }

  /**
   * @param secure_media The secure_media
   */
  public void setSecure_media(Secure_media secure_media) {
    this.secure_media = secure_media;
  }

  /**
   * @return The link_flair_text
   */
  public String getLink_flair_text() {
    return link_flair_text;
  }

  /**
   * @param link_flair_text The link_flair_text
   */
  public void setLink_flair_text(String link_flair_text) {
    this.link_flair_text = link_flair_text;
  }

  /**
   * @return The id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id The id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return The from_kind
   */
  public Object getFrom_kind() {
    return from_kind;
  }

  /**
   * @param from_kind The from_kind
   */
  public void setFrom_kind(Object from_kind) {
    this.from_kind = from_kind;
  }

  /**
   * @return The gilded
   */
  public long getGilded() {
    return gilded;
  }

  /**
   * @param gilded The gilded
   */
  public void setGilded(long gilded) {
    this.gilded = gilded;
  }

  /**
   * @return The archived
   */
  public boolean isArchived() {
    return archived;
  }

  /**
   * @param archived The archived
   */
  public void setArchived(boolean archived) {
    this.archived = archived;
  }

  /**
   * @return The clicked
   */
  public boolean isClicked() {
    return clicked;
  }

  /**
   * @param clicked The clicked
   */
  public void setClicked(boolean clicked) {
    this.clicked = clicked;
  }

  /**
   * @return The report_reasons
   */
  public Object getReport_reasons() {
    return report_reasons;
  }

  /**
   * @param report_reasons The report_reasons
   */
  public void setReport_reasons(Object report_reasons) {
    this.report_reasons = report_reasons;
  }

  /**
   * @return The author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @param author The author
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * @return The media
   */
  public com.aphoh.muser.data.network.model.reddit.Media getMedia() {
    return media;
  }

  /**
   * @param media The media
   */
  public void setMedia(com.aphoh.muser.data.network.model.reddit.Media media) {
    this.media = media;
  }

  /**
   * @return The score
   */
  public long getScore() {
    return score;
  }

  /**
   * @param score The score
   */
  public void setScore(long score) {
    this.score = score;
  }

  /**
   * @return The approved_by
   */
  public Object getApproved_by() {
    return approved_by;
  }

  /**
   * @param approved_by The approved_by
   */
  public void setApproved_by(Object approved_by) {
    this.approved_by = approved_by;
  }

  /**
   * @return The over_18
   */
  public boolean isOver_18() {
    return over_18;
  }

  /**
   * @param over_18 The over_18
   */
  public void setOver_18(boolean over_18) {
    this.over_18 = over_18;
  }

  /**
   * @return The hidden
   */
  public boolean isHidden() {
    return hidden;
  }

  /**
   * @param hidden The hidden
   */
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  /**
   * @return The preview
   */
  public Preview getPreview() {
    return preview;
  }

  /**
   * @param preview The preview
   */
  public void setPreview(Preview preview) {
    this.preview = preview;
  }

  /**
   * @return The num_comments
   */
  public long getNum_comments() {
    return num_comments;
  }

  /**
   * @param num_comments The num_comments
   */
  public void setNum_comments(long num_comments) {
    this.num_comments = num_comments;
  }

  /**
   * @return The thumbnail
   */
  public String getThumbnail() {
    return thumbnail;
  }

  /**
   * @param thumbnail The thumbnail
   */
  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  /**
   * @return The subreddit_id
   */
  public String getSubreddit_id() {
    return subreddit_id;
  }

  /**
   * @param subreddit_id The subreddit_id
   */
  public void setSubreddit_id(String subreddit_id) {
    this.subreddit_id = subreddit_id;
  }

  /**
   * @return The edited
   */
  public Object getEdited() {
    return edited;
  }

  /**
   * @param edited The edited
   */
  public void setEdited(Object edited) {
    this.edited = edited;
  }

  /**
   * @return The link_flair_css_class
   */
  public String getLink_flair_css_class() {
    return link_flair_css_class;
  }

  /**
   * @param link_flair_css_class The link_flair_css_class
   */
  public void setLink_flair_css_class(String link_flair_css_class) {
    this.link_flair_css_class = link_flair_css_class;
  }

  /**
   * @return The author_flair_css_class
   */
  public Object getAuthor_flair_css_class() {
    return author_flair_css_class;
  }

  /**
   * @param author_flair_css_class The author_flair_css_class
   */
  public void setAuthor_flair_css_class(Object author_flair_css_class) {
    this.author_flair_css_class = author_flair_css_class;
  }

  /**
   * @return The downs
   */
  public long getDowns() {
    return downs;
  }

  /**
   * @param downs The downs
   */
  public void setDowns(long downs) {
    this.downs = downs;
  }

  /**
   * @return The secure_media_embed
   */
  public Secure_media_embed getSecure_media_embed() {
    return secure_media_embed;
  }

  /**
   * @param secure_media_embed The secure_media_embed
   */
  public void setSecure_media_embed(Secure_media_embed secure_media_embed) {
    this.secure_media_embed = secure_media_embed;
  }

  /**
   * @return The saved
   */
  public boolean isSaved() {
    return saved;
  }

  /**
   * @param saved The saved
   */
  public void setSaved(boolean saved) {
    this.saved = saved;
  }

  /**
   * @return The removal_reason
   */
  public Object getRemoval_reason() {
    return removal_reason;
  }

  /**
   * @param removal_reason The removal_reason
   */
  public void setRemoval_reason(Object removal_reason) {
    this.removal_reason = removal_reason;
  }

  /**
   * @return The post_hint
   */
  public String getPost_hint() {
    return post_hint;
  }

  /**
   * @param post_hint The post_hint
   */
  public void setPost_hint(String post_hint) {
    this.post_hint = post_hint;
  }

  /**
   * @return The stickied
   */
  public boolean isStickied() {
    return stickied;
  }

  /**
   * @param stickied The stickied
   */
  public void setStickied(boolean stickied) {
    this.stickied = stickied;
  }

  /**
   * @return The from
   */
  public Object getFrom() {
    return from;
  }

  /**
   * @param from The from
   */
  public void setFrom(Object from) {
    this.from = from;
  }

  /**
   * @return The is_self
   */
  public boolean isIs_self() {
    return is_self;
  }

  /**
   * @param is_self The is_self
   */
  public void setIs_self(boolean is_self) {
    this.is_self = is_self;
  }

  /**
   * @return The from_id
   */
  public Object getFrom_id() {
    return from_id;
  }

  /**
   * @param from_id The from_id
   */
  public void setFrom_id(Object from_id) {
    this.from_id = from_id;
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
   * @return The name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return The created
   */
  public long getCreated() {
    return created;
  }

  /**
   * @param created The created
   */
  public void setCreated(long created) {
    this.created = created;
  }

  /**
   * @return The url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url The url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return The author_flair_text
   */
  public Object getAuthor_flair_text() {
    return author_flair_text;
  }

  /**
   * @param author_flair_text The author_flair_text
   */
  public void setAuthor_flair_text(Object author_flair_text) {
    this.author_flair_text = author_flair_text;
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
   * @return The created_utc
   */
  public long getCreated_utc() {
    return created_utc;
  }

  /**
   * @param created_utc The created_utc
   */
  public void setCreated_utc(long created_utc) {
    this.created_utc = created_utc;
  }

  /**
   * @return The distinguished
   */
  public Object getDistinguished() {
    return distinguished;
  }

  /**
   * @param distinguished The distinguished
   */
  public void setDistinguished(Object distinguished) {
    this.distinguished = distinguished;
  }

  /**
   * @return The mod_reports
   */
  public List<Object> getMod_reports() {
    return mod_reports;
  }

  /**
   * @param mod_reports The mod_reports
   */
  public void setMod_reports(List<Object> mod_reports) {
    this.mod_reports = mod_reports;
  }

  /**
   * @return The visited
   */
  public boolean isVisited() {
    return visited;
  }

  /**
   * @param visited The visited
   */
  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  /**
   * @return The num_reports
   */
  public Object getNum_reports() {
    return num_reports;
  }

  /**
   * @param num_reports The num_reports
   */
  public void setNum_reports(Object num_reports) {
    this.num_reports = num_reports;
  }

  /**
   * @return The ups
   */
  public long getUps() {
    return ups;
  }

  /**
   * @param ups The ups
   */
  public void setUps(long ups) {
    this.ups = ups;
  }
}
