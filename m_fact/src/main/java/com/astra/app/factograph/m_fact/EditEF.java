package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;


public class EditEF extends Activity implements View.OnTouchListener{
    private EditText mName;
    private EditText mDescription;
    private Long mRowId;
    private EFDbAdapted mDbHelper;
    private Spinner mCategory;
    private Spinner mType;
    protected float fromPosition;
    protected int counter = 0;
    protected float MOVE_LENGTH = 200;
    ViewFlipper flipper;
    LayoutInflater inflater;

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
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layouts[] = new int[] {R.layout.edit_ef_links, R.layout.void_layout};//TODO tag layout
         try {//TODO check the problem
            for (int layout : layouts) {
                //flipper.addView(inflater.inflate(layout, null));
                View views= inflater.inflate(layout,null);
                flipper.addView(views);
            }
        } catch(NullPointerException e){
            Log.e("NullPointerException","flipper/s");
        }
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                .getSerializable(EFDbAdapted.KEY_ROWID);
        if (extras != null) {
            mRowId = extras.getLong(EFDbAdapted.KEY_ROWID);
        }
        populateFields();

    }
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
            case R.id.ef_links_create: {
                //TODO Create link
                Intent intent = new Intent(EditEF.this, Links.class);
                intent.putExtra(EFDbAdapted.KEY_ROWID, mRowId);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.ef_links_montre: {
                //TODO Montre link
                break;
            }
            case R.id.ef_links_delete_selected: {
                //TODO Delete link
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
        mDbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper.open();
        populateFields();
    }
    protected void onStop() {
        super.onStop();
        mDbHelper.close();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // Пользователь нажал на экран, т.е. начало движения
                // fromPosition - координата по оси X начала выполнения операции
                fromPosition = event.getX();
                break;
            // Вместо ACTION_UP
            case MotionEvent.ACTION_MOVE:
                float toPosition = event.getX();
                // MOVE_LENGTH - расстояние по оси X, после которого можно переходить на след. экран
                if ((fromPosition - MOVE_LENGTH) > toPosition) {
                    fromPosition = toPosition;
                    counter++;
                    flipper.showNext();
                } else if ((fromPosition + MOVE_LENGTH) < toPosition) {
                    fromPosition = toPosition;
                    counter--;
                    flipper.showPrevious();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public class Item {

        private String title;
        private String description;

        public Item(String title, String description) {
            super();
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
        // getters and setters...
    }

    private class MyEDListViewAdapter extends ArrayAdapter<Item> {

        private final Context context;
        private final ArrayList<Item> itemsArrayList;

        public MyEDListViewAdapter(Context context, ArrayList<Item> itemsArrayList) {
            super(context, R.layout.row, itemsArrayList);
            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.row, parent, false);

            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.monte_moi_item_name);
            TextView descriptionView = (TextView) rowView.findViewById(R.id.monte_moi_item_description);

            // 4. Set the text for textView
            labelView.setText(itemsArrayList.get(position).getTitle());
            descriptionView.setText(itemsArrayList.get(position).getDescription());

            // 5. retrn rowView
            return rowView;
        }

    };

}
