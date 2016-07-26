package com.example.user.mana_livechatv2;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.example.user.mana_livechatv2.app.MyApplication;

/**
 * Created by user on 19/07/2016.
 */
public class TabbedActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        TabHost tabHost = getTabHost();

        // Tab for Photos
        TabHost.TabSpec photospec = tabHost.newTabSpec("Chats");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Chats", getResources().getDrawable(R.drawable.bg_bubble_white));
        Intent chatIntent = new Intent(this, MainActivity.class);
        photospec.setContent(chatIntent);
        tabHost.addTab(photospec); // Adding photos tab

        // Tab for Songs
        TabHost.TabSpec songspec = tabHost.newTabSpec("Search");
        songspec.setIndicator("Search", getResources().getDrawable(R.drawable.bg_bubble_gray));
        Intent mapIntent = new Intent(this, Pencarian_Narasumber.class);
        songspec.setContent(mapIntent);
        tabHost.addTab(songspec); // Adding songs tab

        // Tab for Videos
//        TabHost.TabSpec videospec = tabHost.newTabSpec("Videos");
//        videospec.setIndicator("Videos", getResources().getDrawable(R.drawable.icon_videos_tab));
//        Intent videosIntent = new Intent(this, VideosActivity.class);
//        videospec.setContent(videosIntent);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.action_logout:
//                MyApplication.getInstance().logout();
//                break;
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

}
