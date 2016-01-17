package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostItem {

  @JsonField(name = "kind") @Expose private String kind;
  @JsonField(name = "data") @Expose private PostData data;

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
   * @return The data
   */
  public PostData getData() {
    return data;
  }

  /**
   * @param data The data
   */
  public void setData(PostData data) {
    this.data = data;
  }
}
