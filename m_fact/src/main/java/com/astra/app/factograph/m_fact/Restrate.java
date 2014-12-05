package com.astra.app.factograph.m_fact;

/**
 * Created by teodor on 20.10.2014.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class Restrate extends Activity {
    //    private UserDbAdapter dbHelper;
//    private final ContentProviderForDb mContentProviderForDb = new ContentProviderForDb();
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
//        dbHelper = new UserDbAdapter(this);
//        dbHelper.open();
        rowId = null;
        loginInput = (EditText) this.findViewById(R.id.login_field_reg);
        passwordInput = (EditText) this.findViewById(R.id.password_field_reg);
        spinnerInput = (Spinner) this.findViewById(R.id.category);
        belowOutput = (TextView) this.findViewById(R.id.text_reg_below);

    }

    @SuppressLint ("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.tags_update: {
                MyGlobalSigns app = ((MyGlobalSigns) getApplicationContext());
                if(!app.getRights().equals("Read-Write")){
                    break;
                }

//                cursor = dbHelper.fetchAllTodos();
                cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS, ContentProviderForDb.PROJECTION_USERS, null, null, null);
                if (cursor.getCount() == 0) {
                    saveState();
                    belowOutput.setTextColor(getResources().getColor(R.color.cyan));
                    belowOutput.setText("user successfully created");
                    //belowOutput.setText("no one users");
                } else {
                    //              Log.i("cursor.getString(1);", cursor.getString(1));
                    //Log.i("UserDbAdapter.KEY_LOGIN;", cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_LOGIN)));
                    boolean exists = false;
                    String login_input = Restrate.this.loginInput.getText().toString();
                    if (cursor.moveToFirst()) {
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String login = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                            String pass = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_PASSWORD));
                            String rights = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_RIGHTS));
                            if (login.equals(login_input)) {
                                exists = true;
                            }
//                            Log.i("_id of ", _id+"");
//                            Log.i("login of ", login+""); debug
//                            Log.i("pass of ", pass+"");
//                            Log.i("rights of ", rights+"");
                        } while (cursor.moveToNext());
                    }
                    if (exists) {
                        //belowOutput.setTextColor(R.color.red);
                        belowOutput.setTextColor(getResources().getColor(R.color.red));
                        belowOutput.setText("user already exist");
                    }
                    if (!exists) {
                        String rights = "";
                        String password = "";
                        String login = "";
                        rights = spinnerInput.getSelectedItem().toString();
                        login = Restrate.this.loginInput.getText().toString();
                        password = Restrate.this.passwordInput.getText().toString();
                        if (login.length() >= 8) {
                            if (password.length() >= 8) {
                                saveState();
                                belowOutput.setTextColor(getResources().getColor(R.color.cyan));
                                belowOutput.setText("user successfully created");

                            } else {
                                belowOutput.setTextColor(getResources().getColor(R.color.red));
                                belowOutput.setText("password not contains 8 characters");
                            }
                        } else {
                            belowOutput.setTextColor(getResources().getColor(R.color.red));
                            belowOutput.setText("login not contains 8 characters");
                        }
                        //belowOutput.setTextColor(R.color.cyan);
                    }
                }

//                }catch(NullPointerException e){
//                    saveState();
//                    belowOutput.setText("no one users");
//                }
                break;

            }
            case R.id.ef_edit_linked_users_add: {
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

    //    protected void onStop() {
//        super.onStop();
//        setResult(RESULT_OK);
//        finish();
//    }
//    protected void onPause() {
//        super.onStop();
//        setResult(RESULT_OK);
//        finish();
//    }
    private void saveState() {
        String rights = "";
        String password = "";
        String login = "";
//        login_field_reg.setText("Login/2");
//        password_field_reg.setText("Login/2");
//        try {
        rights = (String) spinnerInput.getSelectedItem();
        login = (String) Restrate.this.loginInput.getText().toString();
        password = (String) Restrate.this.passwordInput.getText().toString();
        if (rowId == null) {
//                long id = dbHelper.createTodo(login, password, rights);
            ContentValues inBase = new ContentValues();
            inBase.put(ContentProviderForDb.COLUMN_NAME, login);
            inBase.put(ContentProviderForDb.COLUMN_PASSWORD, password);
            inBase.put(ContentProviderForDb.COLUMN_RIGHTS, rights);
            getContentResolver().insert(ContentProviderForDb.PROVIDER_USERS, inBase);
                /*
                try{

                } catch(NullPointerException e){
                    e.getStackTrace();
                    Log.e("NullPointerException","insert user in users");
                }
                */

        //Log.i("fields",rights+" "+password+" "+login);
//        } catch (NullPointerException e) {
//            Log.e(e.toString(),"null fields try to write in database");
//            Log.i("fields",rights+" "+password+" "+login);
//            super.onStop();
//            setResult(RESULT_CANCELED);
//        }
        }
    }
}
