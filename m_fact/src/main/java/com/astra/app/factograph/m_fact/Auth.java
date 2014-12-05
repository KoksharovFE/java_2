package com.astra.app.factograph.m_fact;

/**
 * Created by teodor on 20.10.2014.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Auth extends Activity {
    //    private UserDbAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private String mRights;
    private EditText login_field, password_field;
    private TextView auth_below;
//    private final ContentProviderForDb cpfDB = new ContentProviderForDb();

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
//        dbHelper = new UserDbAdapter(this);
//        dbHelper.open();
        login_field = (EditText) findViewById(R.id.login_field);
        password_field = (EditText) findViewById(R.id.password_field);
        auth_below = (TextView) findViewById(R.id.text_auth_below);
//        cursor = dbHelper.fetchAllTodos();

        cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS, ContentProviderForDb.PROJECTION_USERS, null, null, null);
        if (cursor.getCount() == 0) {
            Intent intent = new Intent(Auth.this, Restrate.class);
            startActivityForResult(intent, 1);
        }
//        if(cursor.getCount() == 0){
//
//            Intent intent = new Intent(Auth.this, Restrate.class);
//            startActivityForResult(intent, 1);
//        }
    }

    @SuppressLint ("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.tags_update: {
                mRights="";
//                cursor = dbHelper.fetchAllTodos();
                cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS, ContentProviderForDb.PROJECTION_USERS, null, null, null);
                String login_input = login_field.getText().toString();
                String password_input = password_field.getText().toString();
                boolean correct_credentials = false;
                if (cursor.moveToFirst()) {
                    do {
                        Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String login = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                        String pass = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_PASSWORD));
                        String rights = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_RIGHTS));
                        if (login_input.equals(login) && password_input.equals(pass)) {
                            mRights=rights;
                            correct_credentials = true;
//                            MyGlobalSigns appState = ((MyGlobalSigns)this.getApplicationContext());
//                            String s=rights; TODO global
//                            String state = appState.setRights("t");
                        }
//                        Log.i("login_input of ", login_input+"");
//                        Log.i("password_input of ", password_input+"");
//                        Log.i("_id of ", _id+"");
//                        Log.i("login of ", login+"");
//                        Log.i("pass of ", pass+"");
//                        Log.i("rights of ", rights+"");
                    } while (cursor.moveToNext());
                }
                if (correct_credentials) {
                    auth_below.setTextColor(getResources().getColor(R.color.cyan));
                    auth_below.setText("correct credentials");
                    Intent intent = new Intent(Auth.this, MainActivity.class);
                    Log.i("rights level",mRights);
                    MyGlobalSigns app = ((MyGlobalSigns) getApplicationContext());
                    app.setRights(mRights);
                    startActivity(intent);
                } else {
                    auth_below.setTextColor(getResources().getColor(R.color.red));
                    auth_below.setText("Incorrect credentials");
                }
            }

        }
    }
//    protected void onStop() {
//        super.onStop();
//        setResult(RESULT_OK);
//        this.finish();
//    }
//    protected void onPause() {
//        cursor.close();
//        super.onStop();
//        setResult(RESULT_OK);
//        this.finish();
//    }

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
}
