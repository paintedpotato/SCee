package com.example.sawe.scee;

// THE FIRST FaceDetector code from Android - doesn't draw over the fragment/surfaceView.

// THE SECOND FaceDetector code from Firebase - only works for still images (bitmap objects - I
//      could be wrong..) and has the error 'unable to resolve symbol rect/score'

// NOW THE THIRD FaceDetector code from bytefish.de
// ALL OF THE FOLLOWING CODE REPRESENTS THE PHYSICAL PROGRESS I HAVE ATTAINED THUS FAR.
// FOURTH - I will explore my own version of an ML FD built via tf

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.FaceDetector;
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
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.IOException;

import static android.content.ContentValues.TAG;

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

    static int randomi = 0;

 //   private Camera.Face[] mFaces;
 //   private FaceOverlayView mFaceView;
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

        // High-accuracy landmark detection and face classification
   /*     FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();

// Real-time contour detection of multiple faces
        FirebaseVisionFaceDetectorOptions realTimeOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                        .build();*/
        // Wrongly positioned code (supposed to be in surfaceCreated) and it conflicts with the
        // setFocusMode from surfaceCreated
        /*// get Camera parameters
        Camera.Parameters params = camera.getParameters();
        // set the focus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        // set Camera parameters
        camera.setParameters(params);*/

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

        camera.setFaceDetectionListener(new MyFaceDetectionListener());

 //       FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSurfaceView);

        try {
            camera.setPreviewDisplay(surfaceHolder);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }camera.startPreview();

        startFaceDetection(); // start face detection feature

        if (randomi == 1){Toast.makeText(getContext(),"I see a face",Toast.LENGTH_LONG);}
        // For FaceDetection -- mustn't declare two separate error catchers
        /*try {
            camera.setPreviewDisplay(surfaceHolder); // input argument retrieved from function header
            camera.startPreview();

            startFaceDetection(); // start face detection feature

        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
       if (holder.getSurface() == null){
            // preview surface does not exist
            Log.d(TAG, "holder.getSurface() == null");
            return;
        }

        try {
            camera.stopPreview();

        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();


            startFaceDetection(); // re-start face detection feature
            if (randomi == 1){Toast.makeText(getContext(),"I see a face",Toast.LENGTH_LONG);}

        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    //To camera switching
    public void switchCamera(){

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
        camera.setFaceDetectionListener(new MyFaceDetectionListener());

    }

        public static void setCameraDisplayOrientation (Fragment activity, // I had to change Activity to Fragment here
                                                        // in the input parameter
        int cameraId, android.hardware.Camera camera){
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
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

    public void startFaceDetection(){
        // Try starting Face Detection
        Camera.Parameters params = camera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            camera.startFaceDetection();
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
