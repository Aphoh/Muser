package com.aphoh.muser.data.network.model.reddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Data {

  @SerializedName("modhash") @Expose private String modhash;
  @SerializedName("children") @Expose private List<PostItem> postItems = new ArrayList<PostItem>();
  @SerializedName("after") @Expose private String after;
  @SerializedName("before") @Expose private String before;

  /**
   * @return The modhash
   */
  public String getModhash() {
    return modhash;
  }

  /**
   * @param modhash The modhash
   */
  public void setModhash(String modhash) {
    this.modhash = modhash;
  }

  /**
   * @return The postItems
   */
  public List<PostItem> getPostItems() {
    return postItems;
  }

  /**
   * @param postItems The postItems
   */
  public void setPostItems(List<PostItem> postItems) {
    this.postItems = postItems;
  }

  /**
   * @return The after
   */
  public String getAfter() {
    return after;
  }

  /**
   * @param after The after
   */
  public void setAfter(String after) {
    this.after = after;
  }

  /**
   * @return The before
   */
  public Object getBefore() {
    return before;
  }

  /**
   * @param before The before
   */
  public void setBefore(String before) {
    this.before = before;
  }
}
