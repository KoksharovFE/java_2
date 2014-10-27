package com.astra.app.factograph.m_fact;

/**
 * Created by teodor on 20.10.2014.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import static com.astra.app.factograph.m_fact.R.id.login_field;
import static com.astra.app.factograph.m_fact.R.id.password_field;
import static com.astra.app.factograph.m_fact.R.string;


public class Restrate extends Activity {
    private UserDbAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private EditText loginInput;
    private EditText passwordInput;
    private TextView belowOutput;
    private Spinner spinnerInput;
    private Long rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrate);
        dbHelper = new UserDbAdapter(this);
        dbHelper.open();
        rowId = null;
        loginInput=(EditText) this.findViewById(R.id.login_field_reg);
        passwordInput=(EditText) this.findViewById(R.id.password_field_reg);
        spinnerInput=(Spinner) this.findViewById(R.id.category);
        belowOutput = (TextView) this.findViewById(R.id.text_reg_below);

    }
    @SuppressLint("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                cursor = dbHelper.fetchAllTodos();
                try {
//              Log.i("cursor.getString(1);", cursor.getString(1));
                cursor.moveToFirst();
                //Log.i("UserDbAdapter.KEY_LOGIN;", cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_LOGIN)));
                    boolean exists=false;
                    String login_input=Restrate.this.loginInput.getText().toString();
                    if (cursor.moveToFirst()){
                        do{
                            Integer _id = cursor.getInt(cursor.getColumnIndex(UserDbAdapter.KEY_ROWID));
                            String login = cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_LOGIN));
                            String pass = cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_PASSWORD));
                            String rights = cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_RIGHTS));
                            if(login.equals(login_input)){
                                exists=true;
                            }
//                            Log.i("_id of ", _id+"");
//                            Log.i("login of ", login+""); debug
//                            Log.i("pass of ", pass+"");
//                            Log.i("rights of ", rights+"");
                        }while(cursor.moveToNext());
                    }
                    if(exists){
                        //belowOutput.setTextColor(R.color.red);
                        belowOutput.setTextColor(string.red);
                        belowOutput.setText("user already exist");
                    }
                    if(!exists){
                        saveState();
                       //belowOutput.setTextColor(R.color.cyan);
                        belowOutput.setTextColor(string.cyan);
                        belowOutput.setText("user successfully created");
                    }
                }catch(NullPointerException e){
                    saveState();
                    belowOutput.setText("no one users");
                }
                break;

            }
            case R.id.button2: {
                cursor.close();
                super.onStop();
                setResult(RESULT_OK);
                this.finish();
                break;
            }
        }

//        Intent intent = new Intent(Restrate.this, Auth.class);
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restrate, menu);
        return true;
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
    protected void onStop() {
        cursor.close();
        super.onStop();
        setResult(RESULT_OK);
        finish();
    }
    protected void onPause() {
        cursor.close();
        super.onStop();
        setResult(RESULT_OK);
        finish();
    }
    private void saveState() {
        String rights = "";
        String password = "";
        String login= "";
//        login_field_reg.setText("Login/2");
//        password_field_reg.setText("Login/2");
        try {
            rights = (String) spinnerInput.getSelectedItem();
            login =(String) Restrate.this.loginInput.getText().toString();
            password =(String) Restrate.this.passwordInput.getText().toString();
                if (rowId == null) {
                    long id = dbHelper.createTodo(login, password, rights);
                    if (id > 0) {
                        rowId = id;
                    }
                }
            //Log.i("fields",rights+" "+password+" "+login);
        } catch (Exception e) {
            Log.e(e.toString(),"null fields try to write in database");
            Log.i("fields",rights+" "+password+" "+login);
            cursor.close();
            super.onStop();
            setResult(RESULT_CANCELED);
        }

    }
}
