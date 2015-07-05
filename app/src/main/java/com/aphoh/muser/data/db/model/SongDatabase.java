package com.aphoh.muser.data.db.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Will on 7/5/2015.
 */
@Database(name = SongDatabase.NAME, version = SongDatabase.VERSION)
public class SongDatabase {

    public static final String NAME = "Songs";
    public static final int VERSION = 1;
}
