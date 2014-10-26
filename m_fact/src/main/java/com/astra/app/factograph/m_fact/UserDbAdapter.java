package com.astra.app.factograph.m_fact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by teodor on 19.10.2014.
 */
public class UserDbAdapter {
    // поля базы данных
    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_RIGHTS = "rights";
    private static final String DATABASE_TABLE = "usersData";
    private Context context;
    private SQLiteDatabase database;
    private UserDbUsing dbHelper;

    public UserDbAdapter(Context context) {
        this.context = context;
    }

    public UserDbAdapter open() throws SQLException {
        dbHelper = new UserDbUsing(context);
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
    public long createTodo(String login, String password, String rights) {
        ContentValues initialValues = createContentValues(login, password,
                rights);

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * обновить список
     */
    public boolean updateTodo(long rowId, String login, String password,
                              String rights) {
        ContentValues updateValues = createContentValues(login, password,
                rights);

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
                        KEY_LOGIN, KEY_PASSWORD, KEY_RIGHTS}, null, null, null,
                null, null
        );
    }

    /**
     * возвращает курсор, спозиционированный на указанной записи
     */
    public Cursor fetchTodo(long rowId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{
                        KEY_ROWID, KEY_LOGIN, KEY_PASSWORD, KEY_RIGHTS},
                KEY_ROWID + "=" + rowId, null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    private ContentValues createContentValues(String login, String password,
                                              String rights) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, login);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_RIGHTS, rights);
        return values;
    }
}
