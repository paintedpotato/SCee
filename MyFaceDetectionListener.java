package com.example.sawe.scee;

import android.hardware.Camera;
import android.hardware.camera2.params.Face;
//import android.media.FaceDetector;
import android.os.Handler;
import android.util.Log;

import android.graphics.Rect;
import static com.example.sawe.scee.CameraFragment.numFaces;

public class MyFaceDetectionListener implements Camera.FaceDetectionListener {

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {

        if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );

            //Context.startService(FaceDetService);

            // called to alert 1 face spotted
            if (CameraFragment.random2 == 0){
                CameraFragment.random2 = 1;// in the case the same 1 face is still being spotted
            }
        }
        // Resets the whole process when there are no (new) faces spotted
        else{
            CameraFragment.random2 = 0;
        }
        numFaces = faces.length;
    }


}