package com.astra.app.factograph.m_fact;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.security.acl.LastOwnerException;

public class ContentProviderForDb extends ContentProvider {

    public static final String DATABASE_NAME = "factographDb";
    public static final String TABLE_VISITORS = "visitors";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_EVENTS = "eventFact";
    public static final String TABLE_LINKS = "links";
    public static final String TABLE_TAGS = "tags";
    public final static String AUTHORITY = "com.astra.app.factograph.m_fact.ContentProviderForDb";
    public final static Uri PROVIDER_VISITORS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_VISITORS);
    public final static Uri PROVIDER_USERS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_USERS);
    public final static Uri PROVIDER_EVENTS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_EVENTS);
    public final static Uri PROVIDER_LINKS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_LINKS);
    public final static Uri PROVIDER_TAGS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TAGS);

    public static final String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_TYPE = "type";
    public final static String COLUMN_PASSWORD = "password";
    public final static String COLUMN_RIGHTS = "rights";
    public final static String COLUMN_FACTID = "fact_id";
    public final static String COLUMN_USERID = "user_id";
    public final static String COLUMN_BEGINTIME = "begin_time";
    public final static String COLUMN_ENDTIME = "end_time";
    public final static String COLUMN_TITLE = "title";
    public final static String COLUMN_VALUE = "value";
    public final static String COLUMN_TYPE1 = "type1";
    public final static String COLUMN_ID1 = "id1";
    public final static String COLUMN_TYPE2 = "type2";
    public final static String COLUMN_ID2 = "id2";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_CATEGORY = "category";
    public final static String COLUMN_MUSICID = "music_id";
    public final static String COLUMN_IMAGEID = "image_id";
    public final static String COLUMN_TAG = "tag";
    public final static String COLUMN_EFID = "ef_id";
//    public final static String COLUMN_LOGIN = "login";

    public final static String[] PROJECTION_VISITORS = new String[] {
            COLUMN_ID,
            COLUMN_FACTID,
            COLUMN_USERID,
            COLUMN_BEGINTIME,
            COLUMN_ENDTIME
    };
    public final static String[] PROJECTION_USERS = new String[] {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_PASSWORD,
            COLUMN_RIGHTS
    };
    public final static String[] PROJECTION_LINKS = new String[] {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_TYPE1,
            COLUMN_ID1,
            COLUMN_TYPE2,
            COLUMN_ID2
    };
    public final static String[] PROJECTION_EVENTS = new String[] {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_TYPE,
            COLUMN_DESCRIPTION,
            COLUMN_CATEGORY,
            COLUMN_MUSICID,
            COLUMN_IMAGEID
    };
    public final static String[] PROJECTION_TAGS = new String[] {
            COLUMN_ID,
            COLUMN_TAG,
            COLUMN_EFID
    };

    private SQLiteDatabase mDatabase;

    public ContentProviderForDb() {
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int VERSION = 19;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLE_VISITORS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
                    + COLUMN_FACTID + " VARCHAR, "
                    + COLUMN_USERID + " VARCHAR, "
                    + COLUMN_BEGINTIME + " VARCHAR,"
                    + COLUMN_ENDTIME + " VARCHAR"
                    + ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLE_USERS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
                    + COLUMN_NAME + " VARCHAR, "
                    + COLUMN_PASSWORD + " VARCHAR, "
                    + COLUMN_RIGHTS + " VARCHAR"
                    + ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLE_LINKS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
                    + COLUMN_NAME + " VARCHAR,"
                    + COLUMN_TYPE1 + " VARCHAR, "
                    + COLUMN_ID1 + " VARCHAR,"
                    + COLUMN_TYPE2 + " VARCHAR, "
                    + COLUMN_ID2 + " VARCHAR"
                    + ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLE_EVENTS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
                    + COLUMN_NAME + " VARCHAR, "
                    + COLUMN_TYPE + " VARCHAR, "
                    + COLUMN_DESCRIPTION + " VARCHAR,"
                    + COLUMN_CATEGORY + " VARCHAR, "
                    + COLUMN_MUSICID + " VARCHAR, "
                    + COLUMN_IMAGEID + " VARCHAR"
                    + ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLE_TAGS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
                    + COLUMN_TAG + " VARCHAR, "
                    + COLUMN_EFID + " VARCHAR"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO: backup and recover
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITORS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mDatabase.query(getTable(uri), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long id = mDatabase.insert(getTable(uri), null, values);
        return null;
    }

//    public Long insert(Uri uri, ContentValues values,boolean needIdReturn) {
//        Long id = mDatabase.insert(getTable(uri), null, values);
//        if (needIdReturn){
//            return id;
//        }
//        return null;
//    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mDatabase.delete(getTable(uri), selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        return mDatabase.update(getTable(uri), values, whereClause, whereArgs);
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URI_TABLE_VISITORS = 1;
    private static final int URI_TABLE_USERS = 2;
    private static final int URI_TABLE_EVENTS = 3;
    private static final int URI_TABLE_LINKS = 4;
    private static final int URI_TABLE_TAGS = 5;


    static {
        sURIMatcher.addURI(AUTHORITY, TABLE_VISITORS, URI_TABLE_VISITORS);
        sURIMatcher.addURI(AUTHORITY, TABLE_USERS, URI_TABLE_USERS);
        sURIMatcher.addURI(AUTHORITY, TABLE_EVENTS, URI_TABLE_EVENTS);
        sURIMatcher.addURI(AUTHORITY, TABLE_LINKS, URI_TABLE_LINKS);
        sURIMatcher.addURI(AUTHORITY, TABLE_TAGS, URI_TABLE_TAGS);
    }

    private String getTable(Uri uri) {
        String table = null;
        switch (sURIMatcher.match(uri)) {
            case URI_TABLE_VISITORS:
                Log.d(((Integer) URI_TABLE_VISITORS).toString(), TABLE_VISITORS);
                table = TABLE_VISITORS;
                break;
            case URI_TABLE_USERS:
                Log.d(((Integer) URI_TABLE_USERS).toString(), TABLE_USERS);
                table = TABLE_USERS;
                break;
            case URI_TABLE_EVENTS:
                Log.d(((Integer) URI_TABLE_EVENTS).toString(), TABLE_EVENTS);
                table = TABLE_EVENTS;
                break;
            case URI_TABLE_LINKS:
                Log.d(((Integer) URI_TABLE_LINKS).toString(), TABLE_LINKS);
                table = TABLE_LINKS;
                break;
            case URI_TABLE_TAGS:
                Log.d(((Integer) URI_TABLE_TAGS).toString(), TABLE_TAGS);
                table = TABLE_TAGS;
                break;
            default:
                Log.d(((Integer) sURIMatcher.match(uri)).toString(), "not found");
                break;
        }
        return table;
    }
}