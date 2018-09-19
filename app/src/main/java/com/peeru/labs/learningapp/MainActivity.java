package com.peeru.labs.learningapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.peeru.labs.learningapp.ui.learn.LetterListFragment;
import com.peeru.labs.learningapp.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private boolean LEARN_SHOWN = true;
    private Fragment shownFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState ==   null) {
            showFragment(LetterListFragment.newInstance());
        }
    }

    private void initView() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_learn:
                        LEARN_SHOWN = true;
                        showFragment(LetterListFragment.newInstance());
                        return true;
                    case R.id.navigation_profile:
                        LEARN_SHOWN = false;
                        showFragment(ProfileFragment.newInstance());
                        return true;
                }
                return false;
            }
        });


    }

    private void showFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, fragment);
        fragmentTransaction.commitNow();
        shownFragment = fragment;
    }

}
