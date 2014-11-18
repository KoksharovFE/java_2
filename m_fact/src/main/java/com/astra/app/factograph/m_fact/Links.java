package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class Links extends Activity {

    private Long mRowId;
    private Spinner type1,id1,type2,id2;
    private EditText name;
    private LinksDbAdapter mDbHelper;
    private EFDbAdapted efDbHelper;
    private boolean update=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ef);
        mDbHelper = new LinksDbAdapter(this);
        efDbHelper = new EFDbAdapted(this);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                .getSerializable(EFDbAdapted.KEY_ROWID);
        if (extras != null) {
            mRowId = extras.getLong(EFDbAdapted.KEY_ROWID);
            update=true;
        }
        type1 = (Spinner) findViewById(R.id.typeSpinner1);
        id1 = (Spinner) findViewById(R.id.idSpinner1);
        type2 = (Spinner) findViewById(R.id.typeSpinner2);
        id2 = (Spinner) findViewById(R.id.idSpinner2);
        name = (EditText) findViewById(R.id.links_name);


        populateFields();
        mDbHelper.close();

    }

    @SuppressLint("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.ef_edit_button: {

                finish();
                break;
            }
        }
    }

    private void populateFields() {
        mDbHelper.open();
        efDbHelper.open();
        if (mRowId != null) {
            Cursor todo = mDbHelper.fetchTodo(mRowId);
            startManagingCursor(todo);
            String category = todo.getString(todo
                    .getColumnIndexOrThrow(LinksDbAdapter.KEY_TYPE1));
            for (int i = 0; i < type1.getCount(); i++) {
                String s = (String) type1.getItemAtPosition(i);
                //Log.e(null, s + " " + category);
                if (s.equalsIgnoreCase(category)) {
                    type1.setSelection(i);
                }
            }
            String type = todo.getString(todo
                    .getColumnIndexOrThrow(LinksDbAdapter.KEY_TYPE2));
            for (int i = 0; i < type2.getCount(); i++) {
                String s = (String) type2.getItemAtPosition(i);
                //Log.e(null, s + " " + type);
                if (s.equalsIgnoreCase(type)) {
                    type2.setSelection(i);
                }
            }


            name.setText(todo.getString(todo
                    .getColumnIndexOrThrow(LinksDbAdapter.KEY_NAME)));
        }
        efDbHelper.close();
        mDbHelper.close();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DBadapter.KEY_ROWID, mRowId);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.links, menu);
        return true;
    }
    private void saveState() {
        //TODO rights
        mDbHelper.open();
        String names = (String) name.getText().toString();
        String type1s = (String) type1.getSelectedItem();
        Item id1s = (Item) id1.getSelectedItem();
        String type2s = (String)type2.getSelectedItem();
        Item id2s = (Item)id2.getSelectedItem();
        if(name.length() > 0){
            if (mRowId == null) {
                long id = mDbHelper.createTodo(names, type1s, Integer.parseInt(id1s.getDescription()), type2s,Integer.parseInt(id2s.getDescription()));
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateTodo(mRowId, names, type1s, Integer.parseInt(id1s.getDescription()), type2s,Integer.parseInt(id2s.getDescription()));
            }
        }
        mDbHelper.close();
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
            TextView labelView = (TextView) rowView.findViewById(R.id.spinner_text);
            TextView descriptionView = (TextView) rowView.findViewById(R.id.spinner_description);

            // 4. Set the text for textView
            labelView.setText(itemsArrayList.get(position).getTitle());
            descriptionView.setText(itemsArrayList.get(position).getDescription());

            // 5. retrn rowView
            return rowView;
        }

    };


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
