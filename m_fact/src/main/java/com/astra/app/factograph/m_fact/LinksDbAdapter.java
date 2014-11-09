package com.astra.app.factograph.m_fact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by teodor on 09.11.2014.
 */
public class LinksDbAdapter {
    // поля базы данных
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE1 = "type1";
    public static final String KEY_ID1 = "id1";
    public static final String KEY_TYPE2 = "type2";
    public static final String KEY_ID2 = "id2";
    private static final String DATABASE_TABLE = "links";
    private Context context;
    private SQLiteDatabase database;
    private EFDbUsing dbHelper;
    public LinksDbAdapter(Context context) {
        this.context = context;
    }
    public LinksDbAdapter open() throws SQLException {
        dbHelper = new EFDbUsing(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }

    /**
     * создать новый элемент списка юхеров. если создан успешно - возвращается номер строки rowId
     * иначе -1
     */
    public long createTodo(String name, String type1, Integer id1, String type2, Integer id2) {
        ContentValues initialValues = createContentValues(name, type1, id1, type2, id2);
        return database.insert(DATABASE_TABLE, null, initialValues);
    }
    /**
     * обновить список
     */
    public boolean updateTodo(long rowId, String name, String type1, Integer id1, String type2, Integer id2) {
        ContentValues updateValues = createContentValues(name, type1, id1, type2, id2);
        return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
                + rowId, null) > 0;
    }
    /**
     * удаляет элемент списка
     */
    public boolean deleteTodo(long rowId) {
        return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    /**
     * возвращает курсор со всеми элементами списка дел
     *
     * @return курсор с результатами всех записей
     */
    public Cursor fetchAllTodos() {
        return database.query(DATABASE_TABLE, new String[]{KEY_ROWID,
                        KEY_NAME, KEY_TYPE1, KEY_ID1, KEY_TYPE2, KEY_ID2}, null, null, null,
                null, null
        );
    }
    /**
     * возвращает курсор, спозиционированный на указанной записи
     */
    public Cursor fetchTodo(long rowId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                        KEY_NAME, KEY_TYPE1, KEY_ID1, KEY_TYPE2, KEY_ID2},
                KEY_ROWID + "=" + rowId, null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    private ContentValues createContentValues(String name, String type1, Integer id1, String type2, Integer id2) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_TYPE1, type1);
        values.put(KEY_ID1, id1);
        values.put(KEY_TYPE2, type2);
        values.put(KEY_ID2, id2);
        return values;
    }
}
