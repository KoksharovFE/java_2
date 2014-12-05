package com.astra.app.factograph.m_fact;
/**
 * Created by konnor2007 on 07.09.14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

//    String DATABASE_NAME = "applicationdata";
//    private final int DATABASE_VERSION = 12;
//    private Context context;
//    final String ef_Create = "create table IF NOT EXISTS eventFact (_id integer primary key autoincrement, "
//            + "name text not null, type text not null, description text, category text not null, image_url text, music_url text);";
//    final String usersData_Create = "create table IF NOT EXISTS usersData (_id integer primary key autoincrement, "
//            + "login text not null, password text not null, rights text not null);";
//    final String tags_Create = "create table IF NOT EXISTS tags (_id integer " +
//            "primary key autoincrement, "
//            + "tag text not null, " +
//            "ef_id integer not null);";
//    final String links_Create = "create table IF NOT EXISTS links (_id integer primary key autoincrement, "
//            + "name text not null, type1 text not null, id1 integer not null, type2 text not null, id2 integer not null);";
//    final String todo_Create = "create table IF NOT EXISTS todo (_id integer primary key autoincrement, "
//            + "category text not null, summary text not null, description text not null);";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DatabaseHelper DBhelper = new DatabaseHelper(context);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.tags_update: {
//                Intent intent = new Intent(MainActivity.this, Overview.class);
//                startActivityForResult(intent, 1);
                Log.d("tags_update","in MainActivity");
                //startActivity(intent);
                Intent intent = new Intent(MainActivity.this, Export.class);
                startActivityForResult(intent, 1);
                break;
            }

            case R.id.ef_edit_linked_users_add: {
                this.finish();
                System.exit(0);
                break;
            }
            case R.id.button3: {
                Intent intent = new Intent(MainActivity.this, Auth.class);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.button4: {
                Intent intent = new Intent(MainActivity.this, Restrate.class);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.button5: {
                Intent intent = new Intent(MainActivity.this, MonteMoiEF.class);
                startActivityForResult(intent, 1);
                break;
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    private class DatabaseHelper extends SQLiteOpenHelper {
//        DatabaseHelper(Context context) {
//            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL(ef_Create);
//            db.execSQL(usersData_Create);
//            db.execSQL(tags_Create);
//            db.execSQL(links_Create);
//            db.execSQL(todo_Create);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            onCreate(db);
//        }
//    }
}
