package com.astra.app.factograph.m_fact;

/**
 * Created by teodor on 20.10.2014.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Auth extends Activity {
    private UserDbAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private EditText login_field,password_field;
    private TextView auth_below;

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
        auth_below=(TextView) findViewById(R.id.text_auth_below);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                cursor = dbHelper.fetchAllTodos();
                String[] login = new String[]{UserDbAdapter.KEY_LOGIN};
                String[] pass = new String[]{UserDbAdapter.KEY_PASSWORD};
                for(int i=0;i<login.length;i++) {
                    if ( login[i].equals(login_field.getText().toString()) ) {
                        if( pass[i].equals(password_field.getText().toString()) ){
                        auth_below.setTextColor(Color.parseColor("#00FF00"));
                        auth_below.setText("correct credentials");
                        Intent intent = new Intent(Auth.this, MainActivity.class);
                        startActivity(intent);
                        }
                    }
                }
                auth_below.setTextColor(Color.parseColor("#FF0000"));
                auth_below.setText("Incorrect credentials");
            }

        }
    }
    protected void onStop() {
        cursor.close();
        super.onStop();
        setResult(RESULT_OK);
    }
    protected void onPause() {
        cursor.close();
        super.onStop();
        setResult(RESULT_OK);
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
