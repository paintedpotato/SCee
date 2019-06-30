package com.example.sawe.scee;

import android.content.Context;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;

// Gesture View is becoming complicated for implementing Double Touch Gesture
public class MyView extends View {

    GestureDetector gestureDetector;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // creating new gesture detector
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

// skipping measure calculation and drawing

    // delegate the event to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //Single Tap
        return gestureDetector.onTouchEvent(e);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            /*FragmentManager fm = getFragmentManager();
            MainFragment fragm = (CameraFragment)fm.findFragmentById(R.id.camera_fragment);
            fragm.otherList();switchCamera();*/

            /*
            Fragment cls2 = new CameraFragment();
            cls2.switchCamera();*/
            return true;
        }

    }

}