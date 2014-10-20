package com.astra.app.factograph.m_fact;

/**
 * Created by teodor on 20.10.2014.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


public class Restrate extends Activity {
    private UserDbAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    private EditText login_field_reg,password_field_reg;
    private TextView reg_below;
    private Spinner spinner_reg;
    private Long rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrate);
        dbHelper = new UserDbAdapter(this);
        dbHelper.open();
        rowId = null;
        login_field_reg=(EditText) findViewById(R.id.login_field);
        password_field_reg=(EditText) findViewById(R.id.password_field);
        spinner_reg=(Spinner) findViewById(R.id.category);
        reg_below = (TextView) findViewById(R.id.text_reg_below);

    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                cursor = dbHelper.fetchAllTodos();
                try {
                String[] login = new String[]{UserDbAdapter.KEY_LOGIN};
                String[] pass = new String[]{UserDbAdapter.KEY_PASSWORD};
                    for (int i = 0; i < login.length; i++) {
                        if (login[i].equals(login_field_reg.getText().toString())) {
                            reg_below.setTextColor(Integer.parseInt("#FF0000"));
                            reg_below.setText("user already exist");

                        } else {
                            saveState();
                            reg_below.setTextColor(Integer.parseInt("#00FF00"));
                            reg_below.setText("user successfully created");

                        }
                    }
                }catch(NullPointerException e){
                    saveState();
                    reg_below.setText("no one users");
                }

            }
            case R.id.button2: {
                this.finish();

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
        super.onStop();
        setResult(RESULT_OK);
    }
    private void saveState() {
        String login = (String) spinner_reg.getSelectedItem();
        String password = login_field_reg.getText().toString();
        String rights = password_field_reg.getText().toString();

        if (rowId == null) {
            long id = dbHelper.createTodo(login, password, rights);
            if (id > 0) {
                rowId = id;
            }
        }
    }
}
