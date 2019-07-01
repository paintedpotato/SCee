package com.example.sawe.scee;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.params.Face;
//import android.media.FaceDetector;
import android.util.Log;
import android.widget.Toast;

public class MyFaceDetectionListener implements Camera.FaceDetectionListener {


    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {

        if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );

            //return Toast.makeText(this, "I see a face(s) :)", Toast.LENGTH_SHORT).show();
            CameraFragment.randomi = 1;
        }
    }


}