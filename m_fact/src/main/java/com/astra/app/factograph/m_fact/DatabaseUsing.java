package com.astra.app.factograph.m_fact;
/**
 * Created by konnor2007 on 07.09.14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUsing extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "applicationdata";

    private static final int DATABASE_VERSION = 4;

    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table todo (_id integer primary key autoincrement, "
            + "category text not null, summary text not null, description text not null);";

    public DatabaseUsing(Context context) {
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
        database.execSQL("DROP TABLE IF EXISTS todo");
        onCreate(database);
    }


}
