package com.astra.app.factograph.m_fact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by teodor on 27.10.2014.
 */
public class EFDbUsing extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "applicationdata";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_TABLE = "eventFact";
    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table "+ DATABASE_TABLE+ " (_id integer primary key autoincrement, "
            + "name text not null, type text not null, description text, category text not null);";

    public EFDbUsing(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // метод вызывается при создании базы данных
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // метод вызывается при обновлении базы данных, например, когда вы увеличиваете номер версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(DatabaseUsing.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        database.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        onCreate(database);
    }
}