package com.aphoh.muser.data.db.model;

public class SongItemBuilder {
    private String id = "0";
    private String image = "fakeurl";
    private String thumbnail = "fakethumb";
    private String url = "fakeurl";
    private String streamUrl = "fakeurl";
    private String waveformUrl = "fakeurl";
    private long score = 0;
    private String artist = "fakeartist";
    private String songTitle = "faketitle";

    public SongItemBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SongItemBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public SongItemBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public SongItemBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public SongItemBuilder setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
        return this;
    }

    public SongItemBuilder setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
        return this;
    }

    public SongItemBuilder setScore(long score) {
        this.score = score;
        return this;
    }

    public SongItemBuilder setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public SongItemBuilder setSongTitle(String songTitle) {
        this.songTitle = songTitle;
        return this;
    }

    public SongItem createSongItem() {
        return new SongItem(id, image, thumbnail, url, streamUrl, waveformUrl, (int)score, artist, songTitle);
    }
}