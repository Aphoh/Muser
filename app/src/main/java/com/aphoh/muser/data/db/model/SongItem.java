package com.aphoh.muser.data.db.model;

import com.aphoh.muser.data.network.model.reddit.Oembed_;
import com.aphoh.muser.data.network.model.reddit.PostData;
import com.aphoh.muser.data.network.model.reddit.PostItem;
import com.aphoh.muser.network.MuserDataInteractor;
import com.aphoh.muser.network.SongResponseModel;
import com.aphoh.muser.util.LogUtil;
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

    public SongItem(String id, String image, String thumbnail, String linkUrl, String streamUrl, String waveformUrl, int score, String artist, String songTitle) {
        this.id = id;
        this.image = image;
        this.thumbnail = thumbnail;
        this.linkUrl = linkUrl;
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
    public String image;

    @Column
    public String thumbnail;

    @Column(name = "link_url")
    public String linkUrl;

    @Column(name = "stream_url")
    public String streamUrl;

    @Column(name = "waveform_url")
    public String waveformUrl;

    @Column
    public int score;

    @Column
    public String artist;

    @Column
    public String songTitle;

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

    public static SongItem fromPostData(PostData data) {
        LogUtil log = new LogUtil(SongItem.class.getSimpleName());

        Oembed_ oembed = data.getMedia().getOembed();
        if(oembed == null){
            log.e("Media is null for data with url " + data.getUrl());
        }
        return new SongItem(data.getId(),
                oembed.getThumbnail_url(),
                oembed.getThumbnail_url(),
                data.getUrl(),
                null,
                null,
                ((int) data.getScore()),
                oembed.getAuthor_name(),
                MuserDataInteractor.Utils.removeByLine(oembed.getTitle()));
    }
}
