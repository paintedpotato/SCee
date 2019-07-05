package com.example.sawe.scee;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.hardware.camera2.params.Face;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    // Declarations for bytefish tutorial
    public static final String TAG = CameraFragment.class.getSimpleName();
    private int mOrientation;
    private int mOrientationCompensation;
    private OrientationEventListener mOrientationEventListener;

    private int mDisplayRotation;
    private int mDisplayOrientation;
    private Camera.Face[] mFaces;

    //bytefishtest
    //public FaceOverlayView mFaceView;         // CODE 1
    //public FrameLayout mFaceView;             // ..

    // Normal Declarations
    Camera camera;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    final int CAMERA_REQUEST_CODE = 1;
    int currentCameraId = 0; // it's an arbitrary value right..

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();

        return fragment;
    }

    public static int randomi = 0;

    // bytefish FDListener                      // CODE 2
    /**
     * Sets the faces for the overlay view, so it can be updated
     * and the face overlays will be drawn again.
     */
    /*
    private Camera.FaceDetectionListener faceDetectionListener = new Camera.FaceDetectionListener() {
    //private Camera.FaceDetectionListener faceDetectionListener = new MyFaceDetectionListener() {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            Log.d("onFaceDetection", "Number of Faces:" + faces.length);
            // Update the view now!
            mFaceView.setFaces(faces);

            //Toast.makeText(CameraFragment.this, "I see a face", Toast.LENGTH_LONG).show();
        }
    };
//*///test
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mSurfaceView = view.findViewById(R.id.surfaceView); // Holder controls surfView, bridging gap btn camera n SView
        mSurfaceHolder = mSurfaceView.getHolder();

        //mFaceView = view.findViewById(R.id.faceOverlay);  // CODE 3
        //mFaceView = (FrameLayout)findViewById(R.id.faceOverlay);

        // if permission is not granted, ask app to give permission
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
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


        // bytefish - pas de necessarie             // CODE 4
        /*mView = new SurfaceView(this);

        setContentView(mView);
        // Now create the OverlayView:
        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Create and Start the OrientationListener:
        /*mOrientationEventListener = new SimpleOrientationEventListener(this);
        mOrientationEventListener.enable();*/


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

        // commented code


//bytefishtest                                      // CODE 5
        //camera.setFaceDetectionListener(faceDetectionListener);
        //camera.startFaceDetection();

        //       FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSurfaceView);

        try {
            camera.setPreviewDisplay(surfaceHolder);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
        camera.startPreview();


        //camera.setFaceDetectionListener(faceDetectionListener);
        //camera.startFaceDetection();

        camera.setFaceDetectionListener(new MyFaceDetectionListener());
        startFaceDetection();
        //while(randomi==0){Toast.makeText(getContext(), "I don't see a face", Toast.LENGTH_LONG).show();}
        //while(randomi==1){Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();}

        /*if (randomi == 1) {
            Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();
        }else{Toast.makeText(getContext(), "I don't see a face", Toast.LENGTH_LONG).show();}
*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            // preview surface does not exist
            Log.d(TAG, "holder.getSurface() == null");
            return;
        }

        try {
            camera.stopPreview();

        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();


            //camera.startFaceDetection(); // re-start face detection feature
            startFaceDetection();
            //while(randomi==0){Toast.makeText(getContext(), "I don't see a face", Toast.LENGTH_LONG).show();}
            //while(randomi==1){Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();}

            /*if (randomi == 1) {
                Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();
            }else{Toast.makeText(getContext(), "I don't see a face", Toast.LENGTH_LONG).show();}*/

        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    //To camera switching
    public void switchCamera() {

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

        //camera.setFaceDetectionListener(faceDetectionListener);
        //camera.startFaceDetection();

        camera.setFaceDetectionListener(new MyFaceDetectionListener());
        startFaceDetection();
        //while(randomi==0){Toast.makeText(getContext(), "I don't see a face", Toast.LENGTH_LONG).show();}
        //while(randomi==1){Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();}

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                if(randomi==1){Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();}
                else{Toast.makeText(getContext(), "I see space", Toast.LENGTH_LONG).show();}

            }
        }, 5000);
//*/
        /*if (randomi == 1) {
            Toast.makeText(getContext(), "I see a face", Toast.LENGTH_LONG).show();
        }else{Toast.makeText(getContext(), "I don't see a face", Toast.LENGTH_LONG).show();}
*/

    }

    public static void setCameraDisplayOrientation(Fragment activity, // I had to change Activity to Fragment here
                                                   // in the input parameter
                                                   int cameraId, android.hardware.Camera camera) {
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
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mSurfaceHolder.addCallback(this); // the 'this' was red underlined prior to importing methods below
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                } else {
                    Toast.makeText(getContext(), "Please give us permission :(", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void startFaceDetection() {
        // Try starting Face Detection
        Camera.Parameters params = camera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0) {
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

    // bytefish - not necessary

    /**
     * We need to react on OrientationEvents to rotate the screen and
     * update the views.
     *
    private class SimpleOrientationEventListener extends OrientationEventListener {

        public SimpleOrientationEventListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            // We keep the last known orientation. So if the user first orient
            // the camera then point the camera to floor or sky, we still have
            // the correct orientation.
            if (orientation == ORIENTATION_UNKNOWN) return;
            mOrientation = roundOrientation(orientation, mOrientation);
            // When the screen is unlocked, display rotation may change. Always
            // calculate the up-to-date orientationCompensation.
            int orientationCompensation = mOrientation
                    + getDisplayRotation(CameraFragment.this);         // THERE IS A PROBLEM WITH THIS CODE
            if (mOrientationCompensation != orientationCompensation) {
                mOrientationCompensation = orientationCompensation;
                mFaceView.setOrientation(mOrientationCompensation);
            }
        }
    }

        // Orientation hysteresis amount used in rounding, in degrees
        private static final int ORIENTATION_HYSTERESIS = 5;

        public int getDisplayRotation(Fragment activity) { // Replaced Activity with Fragment
            //int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int rotation = activity.getActivity().getWindowManager().getDefaultDisplay()
                    .getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    return 0;
                case Surface.ROTATION_90:
                    return 90;
                case Surface.ROTATION_180:
                    return 180;
                case Surface.ROTATION_270:
                    return 270;
            }
            return 0;
        }

        public static int getDisplayOrientation(int degrees, int cameraId) {
            // See android.hardware.Camera.setDisplayOrientation for
            // documentation.
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
            return result;
        }

        public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                         int viewWidth, int viewHeight) {
            // Need mirror for front camera.
            matrix.setScale(mirror ? -1 : 1, 1);
            // This is the value for android.hardware.Camera.setDisplayOrientation.
            matrix.postRotate(displayOrientation);
            // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
            // UI coordinates range from (0, 0) to (width, height).
            matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
            matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
        }

        public static int roundOrientation(int orientation, int orientationHistory) {
            boolean changeOrientation = false;
            if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
                changeOrientation = true;
            } else {
                int dist = Math.abs(orientation - orientationHistory);
                dist = Math.min(dist, 360 - dist);
                changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
            }
            if (changeOrientation) {
                return ((orientation + 45) / 90 * 90) % 360;
            }
            return orientationHistory;
        }
    //}
//*/
}