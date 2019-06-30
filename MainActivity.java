package com.example.sawe.scee;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);
    }
        // Adapter
        public static class MyPagerAdapter extends FragmentPagerAdapter {

            public MyPagerAdapter(FragmentManager fm){
                super(fm);
            }
            @Override
            public Fragment getItem(int position){  // will return fragment for cam, chat, storage
                switch(position){
                    case 0:
                        // return fragment chat
                        return ChatFragment.newInstance();
                    case 1:
                        // return fragment camera
                        return CameraFragment.newInstance();
                    case 2:
                        // return fragment story
                        return StoryFragment.newInstance();
                }

                return null;
            }

            @Override
            public int getCount(){ // returns number of pages in our ViewPager
                return 3;           // chat, camera, storage.
            }
        }

}
