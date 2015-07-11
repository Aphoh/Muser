package com.aphoh.muser.data.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Will on 7/5/2015.
 */
@ModelContainer
@Table(databaseName = SongDatabase.NAME, tableName = "Subreddits")
public class Subreddit extends BaseModel {
    @Column
    @PrimaryKey
    long id;

    @Column
    String name;

    List<SongItem> songs;

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "songs")
    public List<SongItem> getMySongItems() {
        if (songs == null) {
            songs = new Select().from(SongItem.class)
                    .where(Condition.column(SongItem$Table.SUBREDDITMODELCONTAINER_SUBREDDIT_ID).is(id))
                    .queryList();
        }
        return songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
