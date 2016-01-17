package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

  @JsonField(name = "url") @Expose private String url;
  @JsonField(name = "width") @Expose private long width;
  @JsonField(name = "height") @Expose private long height;

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
}
