package com.zuzubots.healthapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String user_name, user_sex_age, user_age, user_sex, user_lat, user_lon;

    SessionManager name_sex_session;
    LocationSessionManager location_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // these session managers have users input data!
        name_sex_session = new SessionManager(getApplicationContext());
        location_session = new LocationSessionManager(getApplicationContext());

        // check whethe ruser has logged in or filled details
        name_sex_session.checkLogin();  // if he hasnt, user will be automatically redirected to login activity
        location_session.checkLogin();

        // get user data from SessionManager : data : name and sex_age
        HashMap<String, String> user = name_sex_session.getUserDetails();
        user_name = user.get(SessionManager.KEY_NAME);
        user_sex_age = user.get(SessionManager.KEY_EMAIL);

        // get user data from LocationSessionManager : data : lat and lon
        HashMap<String, String> user_2 = location_session.getUserDetails();
        user_lat = user_2.get(LocationSessionManager.KEY_NAME);
        user_lon = user_2.get(LocationSessionManager.KEY_EMAIL);

        //Toast.makeText(MainActivity.this, "Welcome " + user_name, Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TwoFragment(), "Nearby Diseases");
        adapter.addFragment(new OneFrangment(), "Symptoms & Diseases");
        viewPager.setAdapter(adapter);
    }


    public void maps(View view) {
        Log.e("Maps", "Am intrat");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.ro/maps/place/Microsoft+România/@44.4774295,26.0695857,17z"));
        startActivity(intent);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
        // In caz ca ai un item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_logout)  //Logout
        {
            name_sex_session.logoutUser();
            location_session.logoutUser();
        }

        if(id == R.id.action_feedback)  //Feedback
        {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        if(id == R.id.action_share)  //Share
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        if(id == R.id.action_about)  //About
        {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("About");

            // Setting Dialog Message
            alertDialog.setMessage(" Health App is your personal healthcare assistant, that brings you the location of nearby diseases along with their details. App also helps you to track or map a disease based on the various symptoms." +
                    "\n\nDevelopers:\n\nChera Robinson\nrobi10101298@yahoo.com" +
                    "\n\nGutuMarius\ngutumarius@yahoo.com" +
                    "\n\nFabian Alexandru\nfabianalexandru@yahoo.com" +
                    "\n\nFuture:\n\nUnder future prospects of this project, we would like to harness Health App with Big Data Analysis. We believe that by collecting large chunks of data from  App related to various diagnosis that it performs, and then analysing this valuable data on the grounds of health related issues according to geographical location, sex and age, could possibly emerge as a major asset in the field of health care Industry."
                    );

            // if User clicks Accept
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

        if (id == R.id.action_disclaimer)  //Disclaimer
        {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            //  Dialog Title
            alertDialog.setTitle("Disclaimer. Legal Notice.");

            //  Dialog Message
            alertDialog.setMessage("\n" +
                    "Prima noastra aplicatie de IoT.Sorry for multiple bugs :D.\n");

            // daca apasa accept
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

}