package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;

public class Secure_media {

  @JsonField(name = "oembed")
  private Oembed oembed;
  @JsonField(name = "type")
  private String type;

  /**
   * @return The oembed
   */
  public Oembed getOembed() {
    return oembed;
  }

  /**
   * @param oembed The oembed
   */
  public void setOembed(Oembed oembed) {
    this.oembed = oembed;
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
}
