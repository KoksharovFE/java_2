package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by teodor on 27.10.2014.
 */

public class MonteMoiEF extends ListActivity {
    private Cursor cursor;
    private Spinner mType;
    private EditText mSearch;
    private ListView mListView;
    ViewPager mViewPager;
    private Menu menu;
//    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */


    /**
     * The {@link ViewPager} that will host the section contents.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_monte_moi_e);
        //setContentView(R.layout.activity_monte_moi_ef);
        mType = (Spinner) findViewById(R.id.monte_moi_spinner);
        mSearch = (EditText) findViewById(R.id.monte_moi_search);
        mListView = (ListView) this.getListView();
    }

    @SuppressLint ("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.tags_update: {
                //cursor.close();
                super.onStop();
                setResult(RESULT_OK);
                this.finish();
                break;

            }
            case R.id.ef_edit_linked_users_add: {
//                MyGlobalSigns appState = ((MyGlobalSigns)getApplicationContext());
//                String state = appState.getRights();
                Intent intent = new Intent(MonteMoiEF.this, EditEF.class);
                startActivityForResult(intent, 1);
                break;
            }
//            Intent intent = getIntent();
//            String fName = intent.getStringExtra("fname");
//            String lName = intent.getStringExtra("lname");
//            intent.putExtra("fname", etFName.getText().toString());
//            intent.putExtra("lname", etLName.getText().toString());
            case R.id.button3: {
                //TODO search
//                userHelper.open();
//                efHelper.open();
//                linksHelper.open();
//шаблоны
//http://javagu.ru/portal/dt?last=false&provider=javaguru&ArticleId=GURU_ARTICLE_64530&SecID=GURU_SECTION_63111
                String type = (String) mType.getSelectedItem();
//                String[] names = new String[0];
                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();

                if (type.equals("User")) {//Вывод Пользователей
                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS,
                            ContentProviderForDb.PROJECTION_USERS,null,null,null);
//                    cursor = ContentProviderForDb.fetchAllTodos();
                    if (cursor.moveToFirst()) {
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String ulogin = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                            String upass = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_PASSWORD));
                            String urights = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_RIGHTS));

                            try {
                                    Pattern p = Pattern.compile(mSearch.getText().toString());
                                    Matcher m = p.matcher(ulogin);
                                    boolean matches = m.matches();
                                    if (matches) {
                                        namesDinamic.add(ulogin);
                                        descrptionDinamic.add(_id.toString());
                                    }
                            } catch (Exception e) {
                                Log.e("Pattern err", "in monte moi ef users");
                            }
                        } while (cursor.moveToNext());
//                        names = new String[namesDinamic.size()];
//                        namesDinamic.toArray(names);
                    }

                }

                if (type.equals("Fact") || type.equals("Event") || type.equals("Place")) {
                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS,ContentProviderForDb.PROJECTION_EVENTS,null,null,null);
                    if (cursor.moveToFirst()) {
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String efname = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                            String eftype = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE));
                            String efdescription = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_DESCRIPTION));
                            String efcategory = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_CATEGORY));

                            if (type.equals(eftype)) {
                                try {
                                    if(!mSearch.getText().toString().substring(0,1).equals("#")) {
                                        Pattern p = Pattern.compile(mSearch.getText().toString());
                                        Matcher m = p.matcher(efname);
                                        boolean matches = m.matches();
                                        //TODO filtering
                                        if (matches) {
                                            namesDinamic.add(efname);
                                            descrptionDinamic.add(_id.toString());
                                        }

                                  } else {
                                        String lTagName = mSearch.getText().toString().substring(1).replace(" ","");
                                        Cursor cursor2 = getContentResolver().query(ContentProviderForDb.PROVIDER_TAGS,
                                                ContentProviderForDb.PROJECTION_TAGS,null,null,null);
                                        boolean writeOnlyOne = false;
                                        if (cursor2.moveToFirst()) {
                                            do {
                                                Integer _id_tag = cursor2.getInt(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                                                String efid = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_EFID));
                                                String tag_name = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_TAG));
                                                if( tag_name.equals(lTagName) && !writeOnlyOne && _id == Integer.parseInt(efid) ) { //efid.equals(_id) &&
                                                    namesDinamic.add(efname);
                                                    descrptionDinamic.add(_id.toString());
                                                    writeOnlyOne = true;
                                                }
                                            } while (cursor2.moveToNext());
                                        }
                                        cursor2.close();
                                  }
                                } catch (Exception e) {
                                    Log.e("Pattern err", "in monte moi ef ef");
                                    namesDinamic.add("Pattern error");
                                    descrptionDinamic.add("Pattern not valid");
                                }
                            }

                        } while (cursor.moveToNext());
                    }
                }

//                if (type.equals("Link")) {//Вывод Связей
//                    //TO DO for links output && matcher
////                    names = new String[1];
////                    names[0] = "Links not work";
//                    namesDinamic.add("Links not work");
//                    descrptionDinamic.add("no one link");
//                }

                ArrayList<Item> items = new ArrayList<Item>();
//                items.addAll(namesDinamic,descrptionDinamic);
                int i = 0;
                while (i < namesDinamic.size()) {
                    items.add(new Item(namesDinamic.get(i), descrptionDinamic.get(i)));
                    i++;
                }
                MyEDListViewAdapter adapter = new MyEDListViewAdapter(this, items);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, names);
                mListView.setAdapter(adapter);
                cursor.close();
                break;
            }
        }
    }

    public void ItemClickListener(AdapterView<?> a, View v, int position, long id) {
        Item chosenItem = (Item) a.getSelectedItem();
        String name = chosenItem.getTitle();
        String _id = chosenItem.getDescription();
        String type = (String) mType.getSelectedItem();
        if (type.equals("Fact") || type.equals("Event") || type.equals("Place")) {
            Intent intent = new Intent(MonteMoiEF.this, EditEF.class);
            intent.putExtra(ContentProviderForDb.COLUMN_ID, Long.parseLong(_id));
            startActivityForResult(intent, 1);
        }
        if (type.equals("User")) {
            //TODO users edit
            Log.i("Users Editor", "Not Created");
        }
        if (type.equals("Link")) {
            //TODO links edit
            Log.i("Links Editor", "Not Created");
        }
    }

    // Создаем меню, основанное на XML-файле
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
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
    }

    protected void onStop() {
        super.onStop();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Intent i = new Intent(this, Details.class);
//        i.putExtra(EFDbAdapted.KEY_ROWID, id);
//        // активити вернет результат если будет вызвано с помощью этого метода
//        startActivityForResult(i,1);
        try {
            super.onListItemClick(l, v, position, id);
            MyEDListViewAdapter adapter = (MyEDListViewAdapter) l.getAdapter();
            Item chosenItem = adapter.getItem(position);
            String name = chosenItem.getTitle();
            String _id = chosenItem.getDescription();
            String type = (String) mType.getSelectedItem();
            if (type.equals("Fact") || type.equals("Event") || type.equals("Place")) {
                Intent intent = new Intent(MonteMoiEF.this, EditEF.class);
                intent.putExtra(ContentProviderForDb.COLUMN_ID, Long.parseLong(_id));
                startActivityForResult(intent, 1);
            }
            if (type.equals("User")) {
                //TODO users edit && rights
                Log.i("Users Editor", "Not Created");
            }
//            if (type.equals("Link")) {
//                //TO DO links edit
//                Log.i("Links Editor", "Not Created");
//            }
//            userHelper.close();
//            efHelper.close();
        } catch (NullPointerException e) {
            Log.e("null err", "in monte moi ef on ite click");
        }
//        finally { not work
//            userHelper.close();
//            efHelper.close();
//        }
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

    }

    ;
}