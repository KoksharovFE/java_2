package com.astra.app.factograph.m_fact;
/**
 * Created by konnor2007 on 07.09.14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                Intent intent = new Intent(MainActivity.this, Overview.class);
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
            case R.id.button2: {
                this.finish();
                System.exit(0);
            }
            case R.id.button3: {
                Intent intent = new Intent(MainActivity.this, Auth.class);
                startActivityForResult(intent, 1);
            }
            case R.id.button4: {
                Intent intent = new Intent(MainActivity.this, Restrate.class);
                startActivityForResult(intent, 1);
            }
            case R.id.button5: {
                Intent intent = new Intent(MainActivity.this, MonteMoiEF.class);
                startActivityForResult(intent, 1);
            }
        }

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
