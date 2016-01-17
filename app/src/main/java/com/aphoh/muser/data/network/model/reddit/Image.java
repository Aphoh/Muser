package com.aphoh.muser.data.network.model.reddit;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Image {

  @JsonField(name = "source") @Expose private Source source;
  @JsonField(name = "resolutions") @Expose private List<Resolution> resolutions =
      new ArrayList<Resolution>();
  @JsonField(name = "variants") @Expose private Variants variants;
  @JsonField(name = "id") @Expose private String id;

  /**
   * @return The source
   */
  public Source getSource() {
    return source;
  }

  /**
   * @param source The source
   */
  public void setSource(Source source) {
    this.source = source;
  }

  /**
   * @return The resolutions
   */
  public List<Resolution> getResolutions() {
    return resolutions;
  }

  /**
   * @param resolutions The resolutions
   */
  public void setResolutions(List<Resolution> resolutions) {
    this.resolutions = resolutions;
  }

  /**
   * @return The variants
   */
  public Variants getVariants() {
    return variants;
  }

  /**
   * @param variants The variants
   */
  public void setVariants(Variants variants) {
    this.variants = variants;
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
}
