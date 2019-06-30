package com.example.sawe.scee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoryFragment extends Fragment {

        public static StoryFragment newInstance(){
        StoryFragment fragment = new StoryFragment();
        return fragment;
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        return view;
    }
    }

