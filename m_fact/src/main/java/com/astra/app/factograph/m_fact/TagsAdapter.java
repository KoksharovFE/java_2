package com.astra.app.factograph.m_fact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by teodor on 21.11.2014.
 */
public class TagsAdapter {
    // поля базы данных
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TAG = "tag";
    public static final String KEY_EF_ID = "ef_id";
    private static final String DATABASE_TABLE = "tags";
    private Context context;
    private SQLiteDatabase database;
    private TagUsing dbHelper;

    public TagsAdapter(Context context) {
        this.context = context;
    }

    public TagsAdapter open() throws SQLException {
        dbHelper = new TagUsing(context);
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
    public long createTodo(String tag, String ef_id) {
        ContentValues initialValues = createContentValues(tag, ef_id);
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * обновить список
     */
    public boolean updateTodo(long rowId, String tag, String ef_id) {
        ContentValues updateValues = createContentValues(tag, ef_id);
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
                        KEY_TAG, KEY_EF_ID}, null, null, null,
                null, null
        );
    }

    /**
     * возвращает курсор, спозиционированный на указанной записи
     */
    public Cursor fetchTodo(long rowId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, KEY_TAG,
                        KEY_EF_ID},
                KEY_ROWID + "=" + rowId, null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private ContentValues createContentValues(String tag, String ef_id) {
        ContentValues values = new ContentValues();
        values.put(KEY_TAG, tag);
        values.put(KEY_EF_ID, ef_id);
        return values;
    }
}
