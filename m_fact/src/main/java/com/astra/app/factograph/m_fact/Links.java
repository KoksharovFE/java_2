package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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
    private Spinner type1, id1, type2, id2;
    private EditText name;
//    private LinksDbAdapter mDbHelper;
//    private EFDbAdapted efDbHelper;
    private boolean update = false;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
//        mDbHelper = new LinksDbAdapter(this);
//        efDbHelper = new EFDbAdapted(this);

//        mRowId = null;
//        Bundle extras = getIntent().getExtras();
//        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
//                .getSerializable(EFDbAdapted.KEY_ROWID);
//        if (extras != null) {
//            mRowId = extras.getLong(EFDbAdapted.KEY_ROWID);
//            update=true;
//        }

        type1 = (Spinner) findViewById(R.id.typeSpinner1);
        id1 = (Spinner) findViewById(R.id.idSpinner1);
        type2 = (Spinner) findViewById(R.id.typeSpinner2);
        id2 = (Spinner) findViewById(R.id.idSpinner2);
        name = (EditText) findViewById(R.id.links_name);

    }

//        try{
//            populateFields();
//        }
//        catch(SQLiteException s){
//            Log.e("sql not exists","sqlerror in links");
//        }


    @SuppressLint ("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.links_create: {
                saveState();
                finish();
                break;
            }
            case R.id.links_cancel: {
                finish();
                break;
            }
            case R.id.links_update_fields: {
//                efDbHelper.open();
                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();
                ArrayList<String> typeDinamic = new ArrayList<String>();
                ArrayList<Item> itemsDinamic1 = new ArrayList<Item>();
                ArrayList<Item> itemsDinamic2 = new ArrayList<Item>();

//                cursor = efDbHelper.fetchAllTodos();
                cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS,ContentProviderForDb.PROJECTION_EVENTS,null,null,null);
                if (cursor.moveToFirst()) {
                    do {
                        Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String efname = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                        String eftype = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE));
                        String efdescription = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_DESCRIPTION));
                        String efcategory = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_CATEGORY));

                        namesDinamic.add(efname);
                        descrptionDinamic.add(_id.toString());
                        typeDinamic.add(eftype);

                    } while (cursor.moveToNext());
                }
                for (int i = 0; i < namesDinamic.size(); i++) {
                    //Log.i("element "+i,namesDinamic.get(i)+" "+descrptionDinamic.get(i)+" "+typeDinamic.get(i));
                    if (typeDinamic.get(i).equals(type1.getSelectedItem())) {
                        Item item = new Item(namesDinamic.get(i), descrptionDinamic.get(i));
                        itemsDinamic1.add(item);
                        //Log.i("add item to iD1",namesDinamic.get(i)+" "+descrptionDinamic.get(i));
                    }
                    if (typeDinamic.get(i).equals(type2.getSelectedItem())) {
                        Item item = new Item(namesDinamic.get(i), descrptionDinamic.get(i));
                        itemsDinamic2.add(item);
                        //Log.i("add item to iD2",namesDinamic.get(i)+" "+descrptionDinamic.get(i));

                    }
                }
                id1.setAdapter(new LinksSpinnerViewAdapter(this, R.layout.custom_spinner, itemsDinamic1));
                id2.setAdapter(new LinksSpinnerViewAdapter(this, R.layout.custom_spinner, itemsDinamic2));
//                efDbHelper.close();
                break;
            }
        }
    }

//    private void populateFields() {
//        mDbHelper.open();
//        efDbHelper.open();
//        if (mRowId != null) {
//            Cursor to do = mDbHelper.fetchTodo(mRowId);
//            startManagingCursor(to do);
//            String category = to do.getString(to do
//                    .getColumnIndexOrThrow(LinksDbAdapter.KEY_TYPE1));
//            for (int i = 0; i < type1.getCount(); i++) {
//                String s = (String) type1.getItemAtPosition(i);
//                //Log.e(null, s + " " + category);
//                if (s.equalsIgnoreCase(category)) {
//                    type1.setSelection(i);
//                }
//            }
//            String type = todo.getString(todo
//                    .getColumnIndexOrThrow(LinksDbAdapter.KEY_TYPE2));
//            for (int i = 0; i < type2.getCount(); i++) {
//                String s = (String) type2.getItemAtPosition(i);
//                //Log.e(null, s + " " + type);
//                if (s.equalsIgnoreCase(type)) {
//                    type2.setSelection(i);
//                }
//            }
//
//
//            name.setText(todo.getString(todo
//                    .getColumnIndexOrThrow(LinksDbAdapter.KEY_NAME)));
//        }
//        efDbHelper.close();
//        mDbHelper.close();
//    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        //outState.putSerializable(DBadapter.KEY_ROWID, mRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.links, menu);
        return true;
    }
//    private void saveState() {
//        //TODO rights
//        mDbHelper.open();
//        String names = (String) name.getText().toString();
//        String type1s = (String) type1.getSelectedItem();
//        Item id1s = (Item) id1.getSelectedItem();
//        String type2s = (String)type2.getSelectedItem();
//        Item id2s = (Item)id2.getSelectedItem();
//        if(name.length() > 0){
//            if (mRowId == null) {
//                long id = mDbHelper.createTodo(names, type1s, Integer.parseInt(id1s.getDescription()), type2s,Integer.parseInt(id2s.getDescription()));
//                if (id > 0) {
//                    mRowId = id;
//                }
//            } else {
//                mDbHelper.updateTodo(mRowId, names, type1s, Integer.parseInt(id1s.getDescription()), type2s,Integer.parseInt(id2s.getDescription()));
//            }
//        }
//        mDbHelper.close();
//    }

    private class LinksSpinnerViewAdapter extends ArrayAdapter<Item> {

        private final Context context;
        private final ArrayList<Item> itemsArrayList;

        public LinksSpinnerViewAdapter(Context context, int txtViewResourceId, ArrayList<Item> itemsArrayList) {
//            RelativeLayout lin = (RelativeLayout)findViewById(txtViewResourceId);
            super(context, txtViewResourceId, itemsArrayList);
            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.custom_spinner, parent, false);

//            RelativeLayout rl = (RelativeLayout) rowView.findViewById(R.id.spinner_relative_layout);
            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.spinner_text);
            TextView descriptionView = (TextView) rowView.findViewById(R.id.spinner_description);

            // 4. Set the text for textView
            labelView.setText(itemsArrayList.get(position).getTitle());
            descriptionView.setText(itemsArrayList.get(position).getDescription());
//            rl.removeAllViewsInLayout();
//            rl.addView(labelView);
//            rl.addView(descriptionView);

            // 5. retrn rowView
            return rowView;
        }

    }

    ;


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

    private void saveState() {
//        mDbHelper.open();
        String names = name.getText().toString();
        String type1s = (String) type1.getSelectedItem();
        Item id1s = (Item) id1.getSelectedItem();
        String type2s = (String) type2.getSelectedItem();
        Item id2s = (Item) id2.getSelectedItem();
//        if (mRowId == null) {
//            long id = mDbHelper.createTodo(names, type1s,Integer.parseInt(id1s.getDescription()),
//                    type2s, Integer.parseInt(id2s.getDescription()));
//            if (id > 0) {
//                mRowId = id;
//            }
//        } else {
//            mDbHelper.updateTodo(mRowId, names, type1s, Integer.parseInt(id1s.getDescription()),
//                    type2s, Integer.parseInt(id2s.getDescription()));
//        }
        ContentValues lvalues = new ContentValues();
        lvalues.put(ContentProviderForDb.COLUMN_NAME,names);
        lvalues.put(ContentProviderForDb.COLUMN_TYPE1,type1s);
        lvalues.put(ContentProviderForDb.COLUMN_ID1,id1s.getDescription());
        lvalues.put(ContentProviderForDb.COLUMN_TYPE2,type2s);
        lvalues.put(ContentProviderForDb.COLUMN_ID2,id2s.getDescription());
        getContentResolver().insert(ContentProviderForDb.PROVIDER_LINKS,lvalues);
//        long id = mDbHelper.createTodo(names, type1s, Integer.parseInt(id1s.getDescription()),
//                type2s, Integer.parseInt(id2s.getDescription()));
//        mDbHelper.close();
    }
}
