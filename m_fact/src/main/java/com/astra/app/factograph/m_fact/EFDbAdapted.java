package com.astra.app.factograph.m_fact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by teodor on 27.10.2014.
 */
public class EFDbAdapted {
    // поля базы данных
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";
    private static final String DATABASE_TABLE = "eventFact";
    private Context context;
    private SQLiteDatabase database;
    private EFDbUsing dbHelper;

    public EFDbAdapted(Context context) {
        this.context = context;
    }

    public EFDbAdapted open() throws SQLException {
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
    public long createTodo(String name, String type, String description, String category) {
        ContentValues initialValues = createContentValues(name, type,
                description, category);
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * обновить список
     */
    public boolean updateTodo(long rowId, String name, String type, String description, String category) {
        ContentValues updateValues = createContentValues(name, type,
                description, category);
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
        return database.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_TYPE, KEY_DESCRIPTION, KEY_CATEGORY}, null, null, null,
                null, null
        );
    }

    /**
     * возвращает курсор, спозиционированный на указанной записи
     */
    public Cursor fetchTodo(long rowId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, KEY_NAME,
                        KEY_TYPE, KEY_DESCRIPTION, KEY_CATEGORY},
                KEY_ROWID + "=" + rowId, null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private ContentValues createContentValues(String name, String type,
                                              String description, String category) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_TYPE, type);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_CATEGORY, category);
        return values;
    }
}

