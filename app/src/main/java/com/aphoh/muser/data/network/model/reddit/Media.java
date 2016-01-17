package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Media {

  @JsonField(name = "oembed")
  private Oembed_ oembed;
  @JsonField(name = "type")
  private String type;

  /**
   * @return The oembed
   */
  public Oembed_ getOembed() {
    return oembed;
  }

  /**
   * @param oembed The oembed
   */
  public void setOembed(Oembed_ oembed) {
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
