package com.example.sawe.scee;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    Camera camera;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    final int CAMERA_REQUEST_CODE = 1;
    int currentCameraId = 0; // it's an arbitrary value right..

    public static CameraFragment newInstance(){
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mSurfaceView = view.findViewById(R.id.surfaceView); // Holder controls surfView, bridging gap btn camera n SView
        mSurfaceHolder = mSurfaceView.getHolder();

        // if permission is not granted, ask app to give permission
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
       // whenever the above line of code (loc) is ran, the onRequestResultPermission method will be called
        } else {
            mSurfaceHolder.addCallback(this); // the 'this' was red underlined prior to importing methods below
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        Button mLogout = view.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        /*mSurfaceView.setOnClickListener(new View.OnClickListener() { // this works for one click
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });*/
        mSurfaceView.setOnClickListener(new DoubleClickListener(500) {

 				public void onDoubleClick() {
                    switchCamera();
                    // double-click code that is executed if the user double-taps
 					// within a span of 500ms (default).
 				}
 			});

        return view;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();

        Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90); //90 degrees
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

/*
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
                //switchCamera();
                return true;
            }

        }
    }*/
    //To implement single touch switching
    public void switchCamera(){
        //Camera.CameraInfo currentCameraId = new Camera.CameraInfo();


        camera.stopPreview();
        camera.release();
        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera = Camera.open(currentCameraId);

        setCameraDisplayOrientation(CameraFragment.this, currentCameraId, camera);
        try {

            camera.setPreviewDisplay(mSurfaceHolder); // replaced previewHolder with this Holder
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

     /*// First method
    //mSurfaceView = view.findViewById(R.id.surfaceView);
     SurfaceView otherCamera = mSurfaceView;
    //SurfaceView otherCamera = (SurfaceView) findViewById(R.id.surfaceView);

    otherCamera.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (inPreview) {
            camera.stopPreview();
        }
        //NB: if you don't release the current camera before switching, you app will crash
        camera.release();

        //swap the id of the camera to be used
        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera = Camera.open(currentCameraId);

        setCameraDisplayOrientation(CameraActivity.this, currentCameraId, camera);
        try {

            camera.setPreviewDisplay(previewHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }
    }
*/
        public static void setCameraDisplayOrientation (Fragment activity, // I had to change Activity to Fragment here
                                                        // in the input parameter
        int cameraId, android.hardware.Camera camera){
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        /*int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();           */      // I'll need to figure this out
            int rotation = activity.getActivity().getWindowManager().getDefaultDisplay()
                    .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

//*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mSurfaceHolder.addCallback(this); // the 'this' was red underlined prior to importing methods below
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }else{
                    Toast.makeText(getContext(), "Please give us permission :(", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private void LogOut() {
        FirebaseAuth.getInstance().signOut(); // to sign out

        //to redirect user to another page after signing out
        Intent intent = new Intent(getContext(), SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }
}
