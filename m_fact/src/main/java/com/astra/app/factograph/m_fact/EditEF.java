package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class EditEF extends Activity {
    private EditText mName;
    private EditText mDescription;
    private Long mRowId;
    private EFDbAdapted mDbHelper;
    private Spinner mCategory;
    private Spinner mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ef);
        mDbHelper = new EFDbAdapted(this);
        mDbHelper.open();
        mCategory = (Spinner) findViewById(R.id.category);
        mType = (Spinner) findViewById(R.id.type);
        mName = (EditText) findViewById(R.id.ef_edit_Name);
        mDescription = (EditText) findViewById(R.id.ef_edit_description);
        //Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                .getSerializable(EFDbAdapted.KEY_ROWID);
        if (extras != null) {
            mRowId = extras.getLong(EFDbAdapted.KEY_ROWID);
        }
        populateFields();
//        try {
//            confirmButton.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    setResult(RESULT_OK);
//                    finish();
//                }
//
//            });
//        }catch (NullPointerException e){
//            Log.i("EditEF button not work", "NullPointerException when button listener created");
//        }
    }
    @SuppressLint("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.ef_edit_button: {
                Intent intent = new Intent(EditEF.this, MonteMoiEF.class);
                startActivityForResult(intent, 1);
                setResult(RESULT_OK);
                finish();
                break;
            }
        }
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor todo = mDbHelper.fetchTodo(mRowId);
            startManagingCursor(todo);
            String category = todo.getString(todo
                    .getColumnIndexOrThrow(EFDbAdapted.KEY_CATEGORY));
            for (int i = 0; i < mCategory.getCount(); i++) {
                String s = (String) mCategory.getItemAtPosition(i);
                //Log.e(null, s + " " + category);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }
            String type = todo.getString(todo
                    .getColumnIndexOrThrow(EFDbAdapted.KEY_TYPE));
            for (int i = 0; i < mType.getCount(); i++) {
                String s = (String) mType.getItemAtPosition(i);
                //Log.e(null, s + " " + type);
                if (s.equalsIgnoreCase(type)) {
                    mType.setSelection(i);
                }
            }
            mName.setText(todo.getString(todo
                    .getColumnIndexOrThrow(EFDbAdapted.KEY_NAME)));
            mDescription.setText(todo.getString(todo
                    .getColumnIndexOrThrow(EFDbAdapted.KEY_DESCRIPTION)));
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DBadapter.KEY_ROWID, mRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_e, menu);
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

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    protected void onStop() {
        super.onStop();
        setResult(RESULT_OK);
        finish();
    }
    private void saveState() {
        //TODO rights
        String category = (String) mCategory.getSelectedItem();
        String type = (String) mType.getSelectedItem();
        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        if(name.length() > 0){
            if (mRowId == null) {
                long id = mDbHelper.createTodo(name, type, description, category);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateTodo(mRowId, name, type, description, category);
            }
        }
    }

}
