package com.astra.app.factograph.m_fact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Auth extends Activity {
    private UserDbAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private EditText login_field,password_field;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        dbHelper = new UserDbAdapter(this);
        dbHelper.open();
        login_field=(EditText) findViewById(R.id.login_field);
        password_field=(EditText) findViewById(R.id.password_field);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                cursor = dbHelper.fetchAllTodos();
                String[] from = new String[]{UserDbAdapter.KEY_LOGIN};
                for(int i=0;i<from.length;i++) {
                    if ( from[i].equals(login_field.getText().toString()) ) {
                        Intent intent = new Intent(Auth.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
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
}
