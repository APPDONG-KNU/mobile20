package com.example.senior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.senior.community.CommunityFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navbar;
    private Fragment communityFragment = new CommunityFragment();
    private Fragment talkFragment = new TalkFragment();

    private Fragment messengerFragment = new MessengerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fix light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        AppCenter.start(getApplication(), "bc13ebca-f95b-4d11-9048-7866024e5104", Analytics.class, Crashes.class);

        changeFragment(R.id.main_fragment, communityFragment);
        navbar = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        navbar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId ();
            if (itemId == R.id.community) {
                changeFragment(R.id.main_fragment, communityFragment);
                return true;
            } else if (itemId == R.id.talk) {
                changeFragment(R.id.main_fragment, talkFragment);
                return true;
            } else if(itemId==R.id.messenger){
                changeFragment(R.id.main_fragment,messengerFragment);
                return true;
            }
            return false;
        });
    }

    // change fragment on main_fragment slot
    private void changeFragment(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.commit();
    }
}