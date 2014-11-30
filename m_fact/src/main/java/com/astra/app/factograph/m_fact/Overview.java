package com.astra.app.factograph.m_fact;
/**
 * Created by konnor2007 on 07.09.14.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Overview extends ListActivity {
//    private DBadapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.important_list);
        this.getListView().setDividerHeight(2);
//        dbHelper = new DBadapter(this);
//        dbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    // Создаем меню, основанное на XML-файле
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    // Реакция на выбор меню
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createTodo();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createTodo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
//                dbHelper.deleteTodo(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createTodo() {
        Intent i = new Intent(this, Details.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, Details.class);
//        i.putExtra(DBadapter.KEY_ROWID, id);
        // активити вернет результат если будет вызвано с помощью этого метода
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();

    }

    private void fillData() {
//        cursor = dbHelper.fetchAllTodos();
//        startManagingCursor(cursor);

//        String[] from = new String[] {DBadapter.KEY_SUMMARY};
        int[] to = new int[] {R.id.label};

        // Теперь создадим адаптер массива и установим его для отображения наших данных
//        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
//                R.layout.important_row, cursor, from, to);
//        setListAdapter(notes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (dbHelper != null) {
//            dbHelper.close();
//        }
    }

    protected void onStop() {
//        if (dbHelper != null) {
//            dbHelper.close();
//        }
        super.onStop();
        setResult(RESULT_OK);
    }
}