package com.aphoh.muser.data.db.model;

import com.aphoh.muser.data.network.model.reddit.Oembed_;
import com.aphoh.muser.data.network.model.reddit.PostData;
import com.aphoh.muser.network.MuserDataInteractor;
import com.aphoh.muser.util.LogUtil;

/**
 * Created by Will on 7/5/2015.
 */
public class SongItem {

  public String id;
  public String image;
  public String thumbnail;
  public String linkUrl;
  public String redditUrl;
  public String streamUrl;
  public String waveformUrl;
  public int score;
  public long length;
  public String artist;
  public String songTitle;

  public SongItem() {
    super();
  }

  public SongItem(String id, String image, String thumbnail, String linkUrl, String redditUrl, String streamUrl,
      String waveformUrl, int score, long length, String artist, String songTitle) {
    this.id = id;
    this.image = image;
    this.thumbnail = thumbnail;
    this.linkUrl = linkUrl;
    this.redditUrl = redditUrl;
    this.streamUrl = streamUrl;
    this.waveformUrl = waveformUrl;
    this.score = score;
    this.length = length;
    this.artist = artist;
    this.songTitle = songTitle;
  }

  public static SongItem fromPostData(PostData data) {
    LogUtil log = new LogUtil(SongItem.class.getSimpleName());

    Oembed_ oembed = data.getMedia().getOembed();
    if (oembed == null) {
      log.e("Media is null for data with url " + data.getUrl());
    }
    return new SongItem(
        data.getId(),
        oembed.getThumbnail_url(),
        oembed.getThumbnail_url(),
        data.getUrl(),
        "http://www.reddit.com" + data.getPermalink(),
        null,
        null,
        ((int) data.getScore()),
        -1,
        oembed.getAuthor_name(),
        MuserDataInteractor.Utils.removeByLine(oembed.getTitle()));
  }
}
