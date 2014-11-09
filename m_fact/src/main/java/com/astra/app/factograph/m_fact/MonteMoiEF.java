package com.astra.app.factograph.m_fact;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
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
/**
 * Created by teodor on 27.10.2014.
 */

public class MonteMoiEF extends ListActivity {
    private EFDbAdapted efHelper;
    private UserDbAdapter userHelper;
    //    private static final int ACTIVITY_CREATE = 0;
//    private static final int ACTIVITY_EDIT = 1;
//    private static final int DELETE_ID = Menu.FIRST + 1;
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
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
        mType = (Spinner) findViewById(R.id.monte_moi_spinner);
        mSearch = (EditText) findViewById(R.id.monte_moi_search);
        //mListView = (ListView) findViewById(R.id.monte_moi_list_view);
        //TODO databasetalbe_links
        efHelper = new EFDbAdapted(this);
        userHelper = new UserDbAdapter(this);
        mListView = (ListView) this.getListView();
//        userHelper.open();
//        efHelper.open();
    }

    @SuppressLint("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                //cursor.close();
                super.onStop();
                setResult(RESULT_OK);
                this.finish();
                break;

            }
            case R.id.button2: {
                //TODO rights && Global
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
                userHelper.open();
                efHelper.open();
                String type = (String) mType.getSelectedItem();
//                String[] names = new String[0];
                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();
                if (type.equals("User")) {//Вывод Пользователей
                    cursor = userHelper.fetchAllTodos();
                    if (cursor.moveToFirst()) {
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(UserDbAdapter.KEY_ROWID));
                            String ulogin = cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_LOGIN));
                            String upass = cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_PASSWORD));
                            String urights = cursor.getString(cursor.getColumnIndex(UserDbAdapter.KEY_RIGHTS));
                            namesDinamic.add(ulogin);
                            descrptionDinamic.add(_id.toString());
                        } while (cursor.moveToNext());
//                        names = new String[namesDinamic.size()];
//                        namesDinamic.toArray(names);
                    }

                }
                if (type.equals("Fact") || type.equals("Event") || type.equals("Place")) {
                    //Вывод Фактов || Вывод Событий || Вывод Мест
                    cursor = efHelper.fetchAllTodos();
                    if (cursor.moveToFirst()) {
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(EFDbAdapted.KEY_ROWID));
                            String efname = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_NAME));
                            String eftype = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_TYPE));
                            String efdescription = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_DESCRIPTION));
                            String efcategory = cursor.getString(cursor.getColumnIndex(EFDbAdapted.KEY_CATEGORY));
                            if (type.equals(eftype)) {
                                namesDinamic.add(efname);
                                descrptionDinamic.add(_id.toString());
                            }
                        } while (cursor.moveToNext());
//                        names = new String[namesDinamic.size()];
//                        namesDinamic.toArray(names);
                    }
                }
                if (type.equals("Link")) {//Вывод Связей
                    //TODO for links output
//                    names = new String[1];
//                    names[0] = "Links not work";
                    namesDinamic.add("Links not work");
                    descrptionDinamic.add("no one link");
                }
                ArrayList<Item> items = new ArrayList<Item>();
//                items.addAll(namesDinamic,descrptionDinamic);
                int i=0;
                while(i<namesDinamic.size()){
                    items.add(new Item(namesDinamic.get(i),descrptionDinamic.get(i)));
                    i++;
                }
                MyEDListViewAdapter adapter = new MyEDListViewAdapter(this,items);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, names);
                mListView.setAdapter(adapter);
                mListView.setTextFilterEnabled(true);
                userHelper.close();
                efHelper.close();
//                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
////                        String value; // Это то что вам надо
////                        value = (String) a.getItemAtPosition(position);
//                        ItemClickListener(a, v, position, id);
//                    }
//                });
                break;
            }
        }
    }

    public void ItemClickListener(AdapterView<?> a, View v, int position, long id) {
        //TODO not work, try to catch error
        Item chosenItem = (Item) a.getSelectedItem();
        String name = chosenItem.getTitle();
        String _id = chosenItem.getDescription();
        String type = (String) mType.getSelectedItem();
        if(type.equals("Fact") || type.equals("Event") || type.equals("Place")){
            Intent intent = new Intent(MonteMoiEF.this, EditEF.class);
            intent.putExtra(EFDbAdapted.KEY_ROWID,Long.parseLong(_id));
            startActivityForResult(intent, 1);
        }
        if (type.equals("User")) {
            //TODO users edit
            Log.i("Users Editor","Not Created");
        }
        if (type.equals("Link")) {
            //TODO links edit
            Log.i("Links Editor","Not Created");
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
        userHelper.close();
        efHelper.close();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        userHelper.open();
//        efHelper.open();
//    }

    protected void onStop() {
        super.onStop();
        userHelper.close();
        efHelper.close();
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
            //TODO not work, try to catch error
//            userHelper.open();
//            efHelper.open();
            MyEDListViewAdapter adapter =(MyEDListViewAdapter) l.getAdapter();
            Item chosenItem = adapter.getItem(position);
            String name = chosenItem.getTitle();
            String _id = chosenItem.getDescription();
            String type = (String) mType.getSelectedItem();
            if (type.equals("Fact") || type.equals("Event") || type.equals("Place")) {
                Intent intent = new Intent(MonteMoiEF.this, EditEF.class);
                intent.putExtra(EFDbAdapted.KEY_ROWID, Long.parseLong(_id));
                startActivityForResult(intent, 1);
                Log.i("try : ", "7");
            }
            if (type.equals("User")) {
                //TODO users edit && rights
                Log.i("Users Editor", "Not Created");
            }
            if (type.equals("Link")) {
                //TODO links edit
                Log.i("Links Editor", "Not Created");
            }
            userHelper.close();
            efHelper.close();
        }catch (NullPointerException e){
            Log.e("null err", "in monte moi ef");
        }
//        finally { not work
//            userHelper.close();
//            efHelper.close();
//        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            Locale l = Locale.getDefault();
//            switch (position) {
//                case 0:
//                    return getString(R.string.title_section1).toUpperCase(l);
//                case 1:
//                    return getString(R.string.title_section2).toUpperCase(l);
//                case 2:
//                    return getString(R.string.title_section3).toUpperCase(l);
//            }
//            return null;
//        }
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_monte_moi_e, container, false);
//            return rootView;
//        }
//    }

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