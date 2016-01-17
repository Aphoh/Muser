package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media_embed {

  @JsonField(name = "content") @Expose private String content;
  @JsonField(name = "width") @Expose private long width;
  @JsonField(name = "scrolling") @Expose private boolean scrolling;
  @JsonField(name = "height") @Expose private long height;

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
