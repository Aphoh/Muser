package com.aphoh.muser.data.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

/**
 * Created by Will on 7/5/2015.
 */
@Table(databaseName = SongDatabase.NAME, tableName = "SongItems")
public class SongItem extends BaseModel {

    public SongItem() {
        super();
    }

    public SongItem(String id, String image, String thumbnail, String url, String streamUrl, String waveformUrl, long score, String artist, String songTitle) {
        this.id = id;
        this.image = image;
        this.thumbnail = thumbnail;
        this.url = url;
        this.streamUrl = streamUrl;
        this.waveformUrl = waveformUrl;
        this.score = score;
        this.artist = artist;
        this.songTitle = songTitle;
    }

    @Column
    @PrimaryKey
    public String id;

    @Column
    String image;

    @Column
    String thumbnail;

    @Column
    String url;

    @Column(name = "stream_url")
    String streamUrl;

    @Column(name = "waveform_url")
    String waveformUrl;

    @Column
    long score;

    @Column
    String artist;

    @Column
    String songTitle;

    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "subreddit_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false)
    ForeignKeyContainer<Subreddit> subredditModelContainer;

    public void associateSubreddit(Subreddit subreddit) {
        subredditModelContainer = new ForeignKeyContainer<>(Subreddit.class);
        subredditModelContainer.setModel(subreddit);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getWaveformUrl() {
        return waveformUrl;
    }

    public void setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
}
