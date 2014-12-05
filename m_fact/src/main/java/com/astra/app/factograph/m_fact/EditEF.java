package com.astra.app.factograph.m_fact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EditEF extends Activity implements View.OnTouchListener {
    private EditText mName;
    private EditText mDescription, mMusicLink, mImageLink;
    private Long mRowId;
    private MediaPlayer mediaPlayer;
    Item selectedFromList, selectedFromLinkedUsersList;
    private ListView mEfLinksListView, mEfTagsListView, mEfLinkedUsersListView;
//    private EFDbAdapted mDbHelper;
    private Spinner mCategory;
//    private TagsAdapter tagsDbHelper;
//    private LinksDbAdapter linksDbHelper;
    public int FILE_CHOOSER = 112;
    public int FILE_CHOOSER_IMAGE = 113;
    private Spinner mType;
    protected float fromPosition;
    protected int counter = 0, flipDisp = 0, flipMax = 0;
    private boolean update = false;
    protected float MOVE_LENGTH = 100;
    ViewFlipper flipper;
    LayoutInflater inflater;
    ImageView mImage;

    private int stateMediaPlayer;
    private final int stateMP_Error = 0;
    private final int stateMP_NotStarter = 1;
    private final int stateMP_Playing = 2;
    private final int stateMP_Pausing = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ef);
//        mDbHelper = new EFDbAdapted(this);
//        linksDbHelper = new LinksDbAdapter(this);
//        tagsDbHelper = new TagsAdapter(this);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                .getSerializable(ContentProviderForDb.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong(ContentProviderForDb.COLUMN_ID);
            update = true;
        }

        //Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
        flipper = (ViewFlipper) findViewById(R.id.ef_edit_flipper);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layouts[];
        if (update) {
            layouts = new int[] {R.layout.ef_main, R.layout.edit_ef_links, R.layout.edit_ef_tags, R.layout.ef_edit_linked_users, R.layout.edit_ef_music, R.layout.edit_ef_image, R.layout.void_layout};//TODO tag layout
        } else {
            layouts = new int[] {R.layout.ef_main, R.layout.void_layout};
        }
        flipMax = layouts.length;
        try {
            for (int layout : layouts) {
                //flipper.addView(inflater.inflate(layout, null));
                View views = inflater.inflate(layout, null);
                flipper.addView(views);
            }
        } catch (NullPointerException e) {
            Log.e("NullPointerException", "flipper/s");
        }

        mCategory = (Spinner) findViewById(R.id.category);
        mType = (Spinner) findViewById(R.id.type);
        mName = (EditText) findViewById(R.id.ef_edit_Name);
        mDescription = (EditText) findViewById(R.id.ef_edit_description);
        mEfLinksListView = (ListView) findViewById(R.id.ef_edit_links_listView);
        mEfTagsListView = (ListView) findViewById(R.id.ef_edit_tag_listView);
        mEfLinkedUsersListView = (ListView) findViewById(R.id.ef_edit_linked_users_listView);
        if(update) {
            mEfLinksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    selectedFromList = (Item) (mEfLinksListView.getItemAtPosition(myItemInt));
                }
            });
            mEfLinkedUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    selectedFromLinkedUsersList = (Item) (mEfLinkedUsersListView.getItemAtPosition(myItemInt));
                }
            });
            mMusicLink = (EditText) findViewById(R.id.ef_edit_music_stringfield);
            mImageLink =  (EditText) findViewById(R.id.ef_edit_image_stringfield);
            mImage = (ImageView) findViewById(R.id.imageView);
        }

//        mImageLink = "";//TODO image link
        populateFields();
//        Log.i("flipper is flipping?", flipper.isFlipping() + "");
    }

    @SuppressLint ("ResourceAsColor")
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.ef_edit_button: {
                Intent intent = new Intent(EditEF.this, MonteMoiEF.class);
                startActivityForResult(intent, 1);
                //setResult(RESULT_OK);
                finish();
                break;
            }
            case R.id.ef_links_create: {
                Intent intent = new Intent(EditEF.this, Links.class);
                intent.putExtra(ContentProviderForDb.COLUMN_ID, mRowId);
                startActivityForResult(intent, 1);
//                finish();
                break;
            }
            case R.id.ef_links_montre: {
//                linksDbHelper.open();

                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();
                ArrayList<Item> itemsDinamic1 = new ArrayList<Item>();
                Cursor cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_LINKS, ContentProviderForDb.PROJECTION_LINKS, null, null, null);
//                Cursor cursor = linksDbHelper.fetchAllTodos();
                if (cursor.moveToFirst()) {
                    do {
//                        mDbHelper.open();
                        Cursor cursor2;//TODO links limiters
                        Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String name = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                        String type1 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE1));
                        String id1 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID1));
                        String type2 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_TYPE2));
                        String id2 = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID2));
//                        cursor2 = mDbHelper.fetchTodo(Long.parseLong(id1));
                        cursor2 = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS, ContentProviderForDb.PROJECTION_EVENTS,
                                ContentProviderForDb.COLUMN_ID + "=" + id1, null, null);
                        cursor2.moveToFirst();
                        String nameEF1 = cursor2.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                        cursor2 = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS, ContentProviderForDb.PROJECTION_EVENTS,
                                ContentProviderForDb.COLUMN_ID + "=" + id2, null, null);
                        cursor2.moveToFirst();
                        String nameEF2 = cursor2.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
//                        Log.d("edit_ef_links","id1 = " + id1 + " id2 = " + id2 + " mRowId = " + mRowId);
                        if (Long.parseLong(id1) == mRowId || Long.parseLong(id2) == mRowId) {
                            namesDinamic.add(name + ":  " + nameEF1 + " - " + nameEF2);
                            descrptionDinamic.add(_id.toString() + " : " + type1 + " - " + type2);
                        }
                        cursor2.close();
                    } while (cursor.moveToNext());
                    ArrayList<Item> items = new ArrayList<Item>();
                    int i = 0;
                    while (i < namesDinamic.size()) {
                        items.add(new Item(namesDinamic.get(i), descrptionDinamic.get(i)));
                        i++;
                    }
                    MyEDListViewAdapter adapter = new MyEDListViewAdapter(this, items);
                    mEfLinksListView.setAdapter(adapter);
                }
                cursor.close();
                break;
            }
            case R.id.ef_links_delete_selected: {
                Item lItem = new Item("", "");
//                    Adapter adapter = mEfLinksListView.getAdapter();
//                    ;
//                    int iterator = mEfLinksListView.getSelectedItemPosition();
                lItem = selectedFromList;
                //lItem = (Item) adapter.getItem(iterator);
                String[] parsingItemDescription = lItem.getDescription().split(" ");

                getContentResolver().delete(ContentProviderForDb.PROVIDER_LINKS, ContentProviderForDb.COLUMN_ID.toString() + "=" + parsingItemDescription[0], null);
                View viewLinksMontre = findViewById(R.id.ef_links_montre);
                buttonClicked(viewLinksMontre);
                break;
            }
            case R.id.ef_previous_button: {
                //flipper.showPrevious();//flipper.showNext();
                if (flipDisp > 0) {
                    flipDisp--;
                    flipper.setDisplayedChild(flipDisp);
                }
                break;
            }
            case R.id.ef_next_button: {
                //flipper.showNext();
                if (flipDisp < flipMax - 1) {
                    flipDisp++;
                    flipper.setDisplayedChild(flipDisp);
                }
                break;
            }
            case R.id.ef_edit_tags_show: {
                //flipper.showNext();

                ArrayList<String> namesDinamic = new ArrayList<String>();
                ArrayList<String> descrptionDinamic = new ArrayList<String>();
                ArrayList<Item> itemsDinamic1 = new ArrayList<Item>();
//                tagsDbHelper.open();
                Cursor cursor2;//TODO tags output
//                cursor2 = tagsDbHelper.fetchAllTodos();
                cursor2 = getContentResolver().query(ContentProviderForDb.PROVIDER_TAGS, ContentProviderForDb.PROJECTION_TAGS, null, null, null);

                if (cursor2.moveToFirst()) {
                    do {
                        Integer _id = cursor2.getInt(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String tags = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_TAG));
                        String ef_id = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_EFID));
                        if (mRowId.toString().equals(ef_id)) {
                            namesDinamic.add(tags);
                            descrptionDinamic.add(_id.toString());
                        }
                    } while (cursor2.moveToNext());
                }
                ArrayList<Item> items = new ArrayList<Item>();
                int i = 0;
                while (i < namesDinamic.size()) {
                    items.add(new Item(namesDinamic.get(i), descrptionDinamic.get(i)));
                    i++;
                }
                MyEDListViewAdapter adapter = new MyEDListViewAdapter(this, items);
                mEfTagsListView.setAdapter(adapter);
//                tagsDbHelper.close();
                break;
            }
            case R.id.ef_edit_tags_create: {
                //flipper.showNext();
//                Intent intent = new Intent(EditEF.this, Tags.class);
//                intent.putExtra(EFDbAdapted.KEY_ROWID, mRowId);
//                startActivityForResult(intent, 1);
//                tagsDbHelper.open();
                EditText ef_edit_tag_name = (EditText) findViewById(R.id.ef_edit_tag_name);
                String tag_name = ef_edit_tag_name.getText().toString();
                ContentValues lvalues;
                lvalues = new ContentValues();
                tag_name = tag_name.replace(" ", "");
                lvalues.put(ContentProviderForDb.COLUMN_TAG, tag_name);
                lvalues.put(ContentProviderForDb.COLUMN_EFID, mRowId.toString());
                if (tag_name.length() > 0) {
                    getContentResolver().insert(ContentProviderForDb.PROVIDER_TAGS, lvalues);
                }
//                tagsDbHelper.createTodo(tag_name, mRowId.toString());
//                tagsDbHelper.close();

                break;
            }
            case R.id.ef_edit_linked_users_montre: {
                ArrayList<Item> itemsDinamic1 = new ArrayList<Item>();
                Cursor cursor = getContentResolver().query(ContentProviderForDb.PROVIDER_VISITORS, ContentProviderForDb.PROJECTION_VISITORS, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        Cursor cursor2;
                        Integer _id = cursor.getInt(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String visEFID = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_FACTID));
                        String visUID = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_USERID));
                        String visBeginTime = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_BEGINTIME));
                        String visEndTime = cursor.getString(cursor.getColumnIndex(ContentProviderForDb.COLUMN_ENDTIME));
                        cursor2 = getContentResolver().query(ContentProviderForDb.PROVIDER_USERS, ContentProviderForDb.PROJECTION_USERS,
                                ContentProviderForDb.COLUMN_ID + "=" + visUID, null, null);
//                        cursor2 = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS, ContentProviderForDb.PROJECTION_EVENTS,null , null, null);
                        cursor2.moveToFirst();
                        Log.i(cursor2.moveToFirst()+"",cursor2.getCount()+"");
                        String _idUser2 = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_ID));
                        String nameUser2 = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_NAME));
                        String passwordUser2 = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_PASSWORD));
                        String rightsUser2 = cursor2.getString(cursor2.getColumnIndex(ContentProviderForDb.COLUMN_RIGHTS));
                        nameUser2.replace(" ","");
                        if (Long.parseLong(visEFID) == mRowId) {
                            itemsDinamic1.add(new Item(nameUser2,_id+" : " + visBeginTime + " - " + visEndTime));
                        }
                        cursor2.close();
                    } while (cursor.moveToNext());
                }
                MyEDListViewAdapter adapter = new MyEDListViewAdapter(this, itemsDinamic1);
                mEfLinkedUsersListView.setAdapter(adapter);

                break;
            }
            case R.id.ef_edit_linked_users_delete: {
                Item lItem = new Item("", "");
                lItem = selectedFromLinkedUsersList;
                Log.d("links_delete", "lItem.getTitle() = "
                        + lItem.getTitle()
                        + "lItem.getDescription() = "
                        + lItem.getDescription());
                String[] parsingItemDescription = lItem.getDescription().split(" ");

                getContentResolver().delete(ContentProviderForDb.PROVIDER_VISITORS, ContentProviderForDb.COLUMN_ID.toString() + "=" + parsingItemDescription[0], null);
                View viewLinksMontre = findViewById(R.id.ef_edit_linked_users_montre);
                buttonClicked(viewLinksMontre);
                break;
            }
            case R.id.ef_edit_linked_users_add: {
                Intent intent = new Intent(EditEF.this, LinkedUser.class);
                intent.putExtra(ContentProviderForDb.COLUMN_ID, mRowId);
                startActivityForResult(intent, 1);
                break;
            }

            case R.id.ef_edit_music_add_update: {
                Intent intent = new Intent(this, FileChooser.class);
                ArrayList<String> extensions = new ArrayList<String>();
                extensions.add(".mp3");
                intent.putStringArrayListExtra("filterFileExtension", extensions);
                startActivityForResult(intent, FILE_CHOOSER);
                break;
            }
            case R.id.ef_edit_music_play: {
                mediaPlayer = new  MediaPlayer();
                initMediaPlayer(mMusicLink.getText().toString());
                mediaPlayer.start();
//                mediaPlayer = mediaPlayer.create(getApplicationContext(), Uri.parse(mMusicLink.toString()));//Environment.getExternalStorageDirectory().getPath() + "/Music/intro.mp3"
//                try {
//                    mediaPlayer.setDataSource(mMusicLink.getText().toString());
//                    Log.i("Music",mMusicLink.getText().toString());
//                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        public void onPrepared(MediaPlayer player) {
//                            player.start();
//                        }
//                    });
////                    mediaPlayer.setOnPreparedListener(this);
////                    mediaPlayer.prepareAsync();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.start();
//                if(mediaPlayer.isPlaying()){
//                    mediaPlayer.stop();
//                }
//                mediaPlayer.start();
//                mMusicLink
                break;
            }
            case R.id.ef_edit_music_stop: {
//                mMusicLink
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                break;
            }

            case R.id.ef_edit_image_add_update: {
                Intent intent = new Intent(this, FileChooser.class);
                ArrayList<String> extensions = new ArrayList<String>();
                extensions.add(".png");
//                extensions.add(".jpg");
                intent.putStringArrayListExtra("filterFileExtension", extensions);
                startActivityForResult(intent, FILE_CHOOSER_IMAGE);
                break;
            }
        }
    }

    private void populateFields() {
//        mDbHelper.open();
        if (mRowId != null) {

            Cursor todo = getContentResolver().query(ContentProviderForDb.PROVIDER_EVENTS,ContentProviderForDb.PROJECTION_EVENTS,
                    ContentProviderForDb.COLUMN_ID + "=" + mRowId,null,null);
            startManagingCursor(todo);
            todo.moveToFirst();
            String category = todo.getString(todo
                    .getColumnIndexOrThrow(ContentProviderForDb.COLUMN_CATEGORY));
            for (int i = 0; i < mCategory.getCount(); i++) {
                String s = (String) mCategory.getItemAtPosition(i);
                //Log.e(null, s + " " + category);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }
            String type = todo.getString(todo
                    .getColumnIndexOrThrow(ContentProviderForDb.COLUMN_TYPE));
            for (int i = 0; i < mType.getCount(); i++) {
                String s = (String) mType.getItemAtPosition(i);
                //Log.e(null, s + " " + type);
                if (s.equalsIgnoreCase(type)) {
                    mType.setSelection(i);
                }
            }
            mName.setText(todo.getString(todo
                    .getColumnIndexOrThrow(ContentProviderForDb.COLUMN_NAME)));
            mDescription.setText(todo.getString(todo
                    .getColumnIndexOrThrow(ContentProviderForDb.COLUMN_DESCRIPTION)));

            if(update) {
                mMusicLink.setText(todo.getString(todo
                        .getColumnIndexOrThrow(ContentProviderForDb.COLUMN_MUSICID)));
                mImageLink.setText(todo.getString(todo
                        .getColumnIndexOrThrow(ContentProviderForDb.COLUMN_IMAGEID)));
                try {
                    mImage.clearAnimation();
                    mImage.setImageBitmap(BitmapFactory.decodeFile(new File(mImageLink.getText().toString()).getAbsolutePath()));
                } catch(OutOfMemoryError ex){
                    ex.getStackTrace();
                }
            }

//            mImageLink.setText();//TODO images

        }
//        mDbHelper.close();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ContentProviderForDb.COLUMN_ID, mRowId);
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

    //    protected void onStop() {
//        super.onStop();
//        setResult(RESULT_OK);
//        finish();
//    }
    private void saveState() {
        //TODO rights
//        mDbHelper.open();
        MyGlobalSigns app = ((MyGlobalSigns) getApplicationContext());
        if (!app.getRights().equals("Read-Write")) {

        } else {
            String imagelink;
            String musiclink;
            if (update) {
                musiclink = mMusicLink.getText().toString();
                imagelink = mImageLink.getText().toString();//TODO images
            } else {
                musiclink = "";
                imagelink = "";
            }

            String category = (String) mCategory.getSelectedItem();
            String type = (String) mType.getSelectedItem();
            String name = mName.getText().toString();
            String description = mDescription.getText().toString();
            ContentValues lvalues = new ContentValues();
            lvalues.put(ContentProviderForDb.COLUMN_NAME, name);
            lvalues.put(ContentProviderForDb.COLUMN_TYPE, type);
            lvalues.put(ContentProviderForDb.COLUMN_DESCRIPTION, description);
            lvalues.put(ContentProviderForDb.COLUMN_MUSICID, musiclink);
            lvalues.put(ContentProviderForDb.COLUMN_IMAGEID, imagelink);
            if (name.length() > 0) {
                if (mRowId == null) {
                    Uri uriId = getContentResolver().insert(ContentProviderForDb.PROVIDER_EVENTS, lvalues);//mDbHelper.createTodo(name, type, description, category);
//                Long lId = getContentResolver().acquireContentProviderClient(uriId).;
//                if (lId > 0) {
//                    mRowId = lId;
//                }
                } else {
                    lvalues.put(ContentProviderForDb.COLUMN_ID, mRowId);
                    getContentResolver().update(ContentProviderForDb.PROVIDER_EVENTS, lvalues,
                            mRowId.toString() + "=" + ContentProviderForDb.COLUMN_ID, null);
//                mDbHelper.updateTodo(mRowId, name, type, description, category);
                }
            }
//        mDbHelper.close();
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
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.go_next_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.go_next_out));
                    flipper.showNext();
                } else if ((fromPosition + MOVE_LENGTH) < toPosition) {
                    fromPosition = toPosition;
                    counter--;
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.go_prev_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.go_prev_out));
                    flipper.showPrevious();

                }
                break;
            default:
                break;
        }
        return true;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
            mMusicLink.setText(fileSelected);
//            populateFields();

        }
        if ((requestCode == FILE_CHOOSER_IMAGE) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
            mImageLink.setText(fileSelected);
//            populateFields();

        }
        saveState();
    }

    private void initMediaPlayer(String PATH_TO_FILE)
    {
        TextView textState = (TextView) findViewById(R.id.ef_edit_music_comment);
//        String PATH_TO_FILE = "/sdcard/music.mp3";
        mediaPlayer = new  MediaPlayer();

        try {
            mediaPlayer.setDataSource(PATH_TO_FILE);
            mediaPlayer.prepare();
            Toast.makeText(this, PATH_TO_FILE, Toast.LENGTH_LONG).show();
            stateMediaPlayer = stateMP_NotStarter;
            textState.setText("- IDLE -");
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            stateMediaPlayer = stateMP_Error;
            textState.setText("- ERROR!!! -");
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            stateMediaPlayer = stateMP_Error;
            textState.setText("- ERROR!!! -");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            stateMediaPlayer = stateMP_Error;
            textState.setText("- ERROR!!! -");
        }
    }
}
