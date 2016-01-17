package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;
import java.util.List;

@JsonObject
public class Preview {

  @JsonField(name = "images")
  private List<Image> images = new ArrayList<Image>();

  /**
   * @return The images
   */
  public List<Image> getImages() {
    return images;
  }

  /**
   * @param images The images
   */
  public void setImages(List<Image> images) {
    this.images = images;
  }
}
