package com.astra.app.factograph.m_fact;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Export extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
    }


    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.export_txt: {
                MyGlobalSigns app = ((MyGlobalSigns) getApplicationContext());
                if(!app.getRights().equals("Read-Write")){
                    break;
                }

                try {
                    OutputStream out = new FileOutputStream("/sdcard/Factograph.log");
                    byte[] bites;
                    Cursor cursor;
                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS,ContentProviderForDb.PROJECTION_USERS,null,null,null);
                    if (cursor.moveToFirst()) {
                        bites = (ContentProviderForDb.PROVIDER_USERS+"\n").getBytes();
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String login = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                            String pass = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_PASSWORD));
                            String rights = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_RIGHTS));
//                            String musicId = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_MUSICID));
//                            String imageId = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_IMAGEID));

                            bites = (_id + " " + login + " " + pass + " " + rights + " " + "\n").getBytes();//+ musicId + " " + imageId
                            out.write(bites);
                        } while (cursor.moveToNext());
                    }


                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS,ContentProviderForDb.PROJECTION_EVENTS,null,null,null);
                    if (cursor.moveToFirst()) {
                        bites = (ContentProviderForDb.PROJECTION_EVENTS+"\n").getBytes();
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String efname = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                            String eftype = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE));
                            String efdescription = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_DESCRIPTION));
                            String efcategory = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_CATEGORY));
                            bites = (_id + " " + efname + " " + eftype + " " + efdescription + " " + efcategory + "\n").getBytes();
                            out.write(bites);
                        } while (cursor.moveToNext());

                    }

                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_LINKS,ContentProviderForDb.PROJECTION_LINKS,null,null,null);
                    if (cursor.moveToFirst()) {
                        bites = (ContentProviderForDb.PROJECTION_LINKS+"\n").getBytes();
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String name = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                            String type1 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE1));
                            String id1 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID1));
                            String type2 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE2));
                            String id2 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID2));
                            bites = (_id + " " + name + " " + type1 + " " + id1 + " " + type2 + " " + id2 + "\n").getBytes();
                            out.write(bites);
                        } while (cursor.moveToNext());

                    }

                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_TAGS,ContentProviderForDb.PROJECTION_TAGS,null,null,null);
                    if (cursor.moveToFirst()) {
                        bites = (ContentProviderForDb.PROJECTION_TAGS+"\n").getBytes();
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String tags = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TAG));
                            String ef_id = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_EFID));
                            bites = (_id + " " + tags + " " + ef_id + "\n").getBytes();
                            out.write(bites);
                        } while (cursor.moveToNext());

                    }

                    cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_VISITORS,ContentProviderForDb.PROJECTION_VISITORS,null,null,null);
                    if (cursor.moveToFirst()) {
                        bites = (ContentProviderForDb.PROJECTION_VISITORS+"\n").getBytes();
                        do {
                            Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                            String visEFID = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_FACTID));
                            String visUID = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_USERID));
                            String visBeginTime = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_BEGINTIME));
                            String visEndTime = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ENDTIME));
                            bites = (_id + " " + visEFID + " " + visUID + " " + visBeginTime + " " + visEndTime +"\n").getBytes();
                            out.write(bites);
                        } while (cursor.moveToNext());

                    }

                    out.close();
                } catch( IOException ex){
                    ex.printStackTrace();
                }




//                BufferedWriter




                break;
            }
            case R.id.export_xml: {

                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.export, menu);
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
}
