package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import java.util.*;


@SuppressWarnings ("ALL")
public class LinkedUser extends Activity {
    String mDateBegin, mDateEnd;
    DatePicker mDatePicker;
    TimePicker mTimePicker;
    Spinner mSpinner;
    Item chosenUser;
    Long efId;
    LayoutInflater inflater;
    ViewFlipper flipper;
    protected int counter = 0, flipDisp = 0, flipMax = 0;
//    final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_linked_user, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_user);

//        flipper = (ViewFlipper) findViewById(R.id.ef_edit_flipper);
//        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        int layouts[];
//        layouts = new int[] {R.layout.activity_linked_user, R.layout.linked_user_second, R.layout.linked_user_third};
//        flipMax = layouts.length;
//        try {//TODO check the problem
//            for (int layout : layouts) {
//                //flipper.addView(inflater.inflate(layout, null));
//                View views = inflater.inflate(layout, null);
//                flipper.addView(views);
//            }
//        } catch (NullPointerException e) {
//            Log.e("NullPointerException", "flipper/s");
//        }
    }

    @SuppressLint ("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.linked_user_next: {
                mDatePicker = (DatePicker) findViewById(R.id.datePicker);
                mTimePicker = (TimePicker) findViewById(R.id.timePicker);
                int day = mDatePicker.getDayOfMonth();
                int mouth = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();
                mDateBegin = year + ":" + mouth + ":" + day + ":" + hour + ":"+minute;

//                flipDisp++;
//                flipper.setDisplayedChild(flipDisp);
                setContentView(R.layout.linked_user_second);
                break;
            }
            case R.id.linked_user_next2: {
                mDatePicker = (DatePicker) findViewById(R.id.datePicker2);
                mTimePicker = (TimePicker) findViewById(R.id.timePicker2);
                int day = mDatePicker.getDayOfMonth();
                int mouth = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();
                mDateEnd = year + ":" + mouth + ":" + day + ":" + hour + ":"+minute;

//                flipDisp++;
//                flipper.setDisplayedChild(flipDisp);
                setContentView(R.layout.linked_user_third);

                mSpinner = (Spinner) findViewById(R.id.linked_user_spinner);

                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();
                ArrayList<Item> itemsDinamic1 = new ArrayList<Item>();
                ArrayList<Item> itemsDinamic2 = new ArrayList<Item>();

                Cursor cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS,ContentProviderForDb.PROJECTION_USERS,null,null,null);
                if (cursor.moveToFirst()) {
                    do {
                        Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String userName = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                        String userRights = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_RIGHTS));

                        namesDinamic.add(userName);
                        descrptionDinamic.add(_id.toString());
                        Item item = new Item(userName, _id.toString());
                        itemsDinamic1.add(item);

                    } while (cursor.moveToNext());
                }

                mSpinner.setAdapter(new LinksSpinnerViewAdapter(this, R.layout.custom_spinner, itemsDinamic1));
                break;
            }
            case R.id.linked_user_confirm: {
                mSpinner.findViewById(R.id.linked_user_spinner);
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                efId = extras.getLong(ContentProviderForDb.COLUMN_ID);//intent.getLongExtra(ContentProviderForDb.COLUMN_ID);//(Long) extras.get(ContentProviderForDb.COLUMN_ID);
                boolean userEmployed=false;//NotAvailable

                chosenUser = (Item) mSpinner.getSelectedItem();
                String chosenUserId = chosenUser.getDescription();
                Cursor cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_VISITORS, ContentProviderForDb.PROJECTION_VISITORS,null,null,null);
                if (cursor.moveToFirst()) {
                    do {
                        Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String linkedUserEFID = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_FACTID));
                        String linkedUserUID = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_USERID));
                        String linkedUserBeginTime = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_BEGINTIME));
                        String linkedUserEndTime = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ENDTIME));
                        String DATE_FORMAT = "yyyy-MM-dd-hh-mm";
                        java.text.SimpleDateFormat sdf =
                                new java.text.SimpleDateFormat(DATE_FORMAT);
                        String[] savedBeginData = linkedUserBeginTime.split(":");
                        String[] savedEndData = linkedUserEndTime.split(":");
                        String[] beginData = mDateBegin.split(":");
                        String[] endData = mDateEnd.split(":");
                        Calendar cSavedBeginData = Calendar.getInstance();
                        Calendar cSavedEndData = Calendar.getInstance();
                        Calendar cBeginData = Calendar.getInstance();
                        Calendar cEndData = Calendar.getInstance();
                        cSavedBeginData.set(Integer.parseInt(savedBeginData[0]),Integer.parseInt(savedBeginData[1]),
                                Integer.parseInt(savedBeginData[2]),Integer.parseInt(savedBeginData[3]),Integer.parseInt(savedBeginData[4]));
                        cSavedEndData.set(Integer.parseInt(savedEndData[0]),Integer.parseInt(savedEndData[1]),
                                Integer.parseInt(savedEndData[2]),Integer.parseInt(savedEndData[3]),Integer.parseInt(savedEndData[4]));
                        cBeginData.set(Integer.parseInt(beginData[0]),Integer.parseInt(beginData[1]),
                                Integer.parseInt(beginData[2]),Integer.parseInt(beginData[3]),Integer.parseInt(beginData[4]));
                        cEndData.set(Integer.parseInt(endData[0]),Integer.parseInt(endData[1]),
                                Integer.parseInt(endData[2]),Integer.parseInt(endData[3]),Integer.parseInt(endData[4]));

                        if (_id == Integer.parseInt(chosenUserId.toString()) ) {
                            boolean success1 = false, success2 = false, success3 = false;
//
//                            for (int i = 0; i < endData.length; i++) {
//                                if (Integer.parseInt(beginData[i]) > Integer.parseInt(endData[i]) && !success1) {
//                                    userEmployed = true;
//                                    Log.i("Visitor_create", "date of end earlier than date of begin");
//                                }
//                                if (Integer.parseInt(savedBeginData[i]) < Integer.parseInt(endData[i]) && !success2
//                                        && Integer.parseInt(savedBeginData[i]) > Integer.parseInt(endData[i])) {
//                                    userEmployed = true;
//                                    Log.i("Visitor_create", "user will be in other event in same time (ed<sbd)");
//                                }
//                                if (Integer.parseInt(beginData[i]) < Integer.parseInt(savedEndData[i]) && !success3) {
//                                    userEmployed = true;
//                                    Log.i("Visitor_create", "user will be in other event in same time (bd<sed)");
//                                }
//                                if (Integer.parseInt(beginData[i]) < Integer.parseInt(endData[i]) && !success1) {
//                                    success1=true;
//                                }
//                                if (Integer.parseInt(savedBeginData[i]) > Integer.parseInt(endData[i]) && !success2) {
//                                    success2=true;
//                                }
//                                if (Integer.parseInt(beginData[i]) > Integer.parseInt(savedEndData[i]) && !success3) {
//                                    success2=true;
//                                }
//                            }
                            if (cEndData.after(cSavedBeginData) && cBeginData.before(cEndData)
                                    && cEndData.after(cSavedEndData)
                                    && cBeginData.after(cEndData)
                                    && cBeginData.after(cSavedEndData) ) {

                            } else if(cEndData.before(cSavedBeginData) && cBeginData.before(cEndData)
                                    && cEndData.before(cSavedEndData)
                                    && cBeginData.before(cEndData)
                                    && cBeginData.before(cSavedEndData) ){

                            } else {
                                userEmployed = true;
                            }
                        }
                    } while (cursor.moveToNext()) ;
                }
                if(!userEmployed){
                    ContentValues lvalues = new ContentValues();
                    lvalues.put(ContentProviderForDb.COLUMN_FACTID, efId);
                    lvalues.put(ContentProviderForDb.COLUMN_USERID, chosenUserId);
                    lvalues.put(ContentProviderForDb.COLUMN_BEGINTIME,mDateBegin);
                    lvalues.put(ContentProviderForDb.COLUMN_ENDTIME,mDateEnd);
                    getContentResolver().insert(ContentProviderForDb.PROVIDER_VISITORS,lvalues);
                    TextView textView = (TextView) findViewById(R.id.linked_user_third_info);
                    textView.setText("Written");
                    finish();
                } else {
                    TextView textView = (TextView) findViewById(R.id.linked_user_third_info);
                    textView.setText("ChosenLinkedUser / not available");
                    Log.i("ChosenLinkedUser ","not available");
                    finish();
                }
                break;
            }
            case R.id.linked_user_cancel: {
                finish();
                break;
            }
            case R.id.linked_user_cancel2: {
                finish();
                break;
            }
            case R.id.linked_user_cancel3: {
                finish();
                break;
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.linked_user, menu);
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
}
