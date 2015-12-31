package com.aphoh.muser.data.network.model.reddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Secure_media_embed {

  @SerializedName("content") @Expose private String content;
  @SerializedName("width") @Expose private long width;
  @SerializedName("scrolling") @Expose private boolean scrolling;
  @SerializedName("height") @Expose private long height;

  /**
   * @return The content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content The content
   */
  public void setContent(String content) {
    this.content = content;
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
   * @return The scrolling
   */
  public boolean isScrolling() {
    return scrolling;
  }

  /**
   * @param scrolling The scrolling
   */
  public void setScrolling(boolean scrolling) {
    this.scrolling = scrolling;
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
