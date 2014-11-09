package com.astra.app.factograph.m_fact;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by teodor on 09.11.2014.
 */
public class LinksDbUsing extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "applicationdata";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_TABLE = "links";
    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table "+ DATABASE_TABLE+ " (_id integer primary key autoincrement, "
            + "type1 text not null, id1 integer not null, type2 text not null, id2 integer not null);";

    public LinksDbUsing(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
