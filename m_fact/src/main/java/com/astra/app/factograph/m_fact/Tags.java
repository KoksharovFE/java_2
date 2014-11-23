package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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


public class Tags extends Activity {
    private Long mRowId;
    private Spinner type1,id1,type2,id2;
    private EditText name;
    private TagsAdapter mDbHelper;
    private EFDbAdapted efDbHelper;
    private boolean update=false;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        mDbHelper = new TagsAdapter(this);
        efDbHelper = new EFDbAdapted(this);
        type1 = (Spinner) this.findViewById(R.id.typeSpinner);
        id1 = (Spinner) this.findViewById(R.id.idSpinner);
        name = (EditText) findViewById(R.id.tags_name);
    }


    @SuppressLint("ResourceAsColor")
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
                efDbHelper.open();
                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();
                ArrayList<String> typeDinamic = new ArrayList<String>();
                ArrayList<Item> itemsDinamic1 = new ArrayList<Item>();
                ArrayList<Item> itemsDinamic2 = new ArrayList<Item>();

                cursor=efDbHelper.fetchAllTodos();
                if (cursor.moveToFirst()) {
                    do {
                        Integer _id = cursor.getInt(cursor.getColumnIndex(EFDbAdapted.KEY_ROWID));
                        String efname = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_NAME));
                        String eftype = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_TYPE));
                        String efdescription = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_DESCRIPTION));
                        String efcategory = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_CATEGORY));

                        namesDinamic.add(efname);
                        descrptionDinamic.add(_id.toString());
                        typeDinamic.add(eftype);

                    } while (cursor.moveToNext());
                }
                for(int i=0;i<namesDinamic.size();i++){
                    //Log.i("element "+i,namesDinamic.get(i)+" "+descrptionDinamic.get(i)+" "+typeDinamic.get(i));
                    if(typeDinamic.get(i).equals(type1.getSelectedItem())){
                        Item item = new Item(namesDinamic.get(i),descrptionDinamic.get(i));
                        itemsDinamic1.add(item);
                        //Log.i("add item to iD1",namesDinamic.get(i)+" "+descrptionDinamic.get(i));
                    }
                }
                id1.setAdapter(new LinksSpinnerViewAdapter(this, R.layout.custom_spinner, itemsDinamic1));
                efDbHelper.close();
                break;
            }
        }
    }


    private class LinksSpinnerViewAdapter extends ArrayAdapter<Item> {

        private final Context context;
        private final ArrayList<Item> itemsArrayList;

        public LinksSpinnerViewAdapter(Context context,int  txtViewResourceId, ArrayList<Item> itemsArrayList) {
//            RelativeLayout lin = (RelativeLayout)findViewById(txtViewResourceId);
            super(context, txtViewResourceId, itemsArrayList);
            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getView(position, cnvtView, prnt);
        }
        @Override public View getView(int pos, View cnvtView, ViewGroup prnt) {
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

    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tags, menu);
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

    private void saveState() {
        mDbHelper.open();
        String names = name.getText().toString();
        String type1s = (String) type1.getSelectedItem();
        Item id1s = (Item) id1.getSelectedItem();
        long id = mDbHelper.createTodo(names, id1s.getDescription());
        mDbHelper.close();
    }
}
