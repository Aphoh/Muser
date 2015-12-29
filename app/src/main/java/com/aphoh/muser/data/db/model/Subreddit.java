package com.aphoh.muser.data.db.model;

import java.util.List;

/**
 * Created by Will on 7/5/2015.
 */
public class Subreddit {
  long id;

  String name;

  List<SongItem> songs;

  public long getId() {
    return id;
  }

  public List<SongItem> getSongs() {
    return songs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
