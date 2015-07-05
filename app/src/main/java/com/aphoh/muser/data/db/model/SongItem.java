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
    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String image;

    @Column
    String thumbnail;

    @Column
    String url;

    @Column
    int score;

    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "subreddit_id",
                                               columnType = Long.class,
                                               foreignColumnName = "id")},
            saveForeignKeyModel = false)
    ForeignKeyContainer<Subreddit> subredditModelContainer;

    public void associateSubreddit(Subreddit subreddit){
        subredditModelContainer = new ForeignKeyContainer<>(Subreddit.class);
        subredditModelContainer.setModel(subreddit);
    }
}
