package com.thomasoropeza.affectivastarterproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{

    //instance variables for the Surface View and Camera Detector references
    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;

    //max processing rate used by the Camera Detector. (This is in FPS, Frames per Second)
    int maxProcessingRate = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain reference to Surface View in the XML layout to embed theCamera Detector
        cameraDetectorSurfaceView = (SurfaceView) findViewById(R.id.cameraDetectorSurfaceView);

        //initialize the Camera Detector
        cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);

        //set procesing rate (Frames Per Second)
        cameraDetector.setMaxProcessRate(maxProcessingRate);

        //Set this Activity as a listener for callbacks on facial readings
        cameraDetector.setImageListener(this);

        //Set this Activity as a listener for describing Camera Detector size
        cameraDetector.setOnCameraEventListener(this);

        cameraDetector.setDetectAllEmotions(true);

        //start Detector
        cameraDetector.start();
    }

    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotate) {
        //obtain the parameters specifying the layout for the Surface View which embeds the Camera Detector
        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();

        //set the param's height and width the the recommended height and width given by the SDK
        params.height = cameraHeight;
        params.width = cameraWidth;

        //set the Surface view's layout params with the new height/width specification
        cameraDetectorSurfaceView.setLayoutParams(params);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        //check if frame was processed
        if (faces == null)
            return;

        //check if there was a face found
        if (faces.size() == 0)
            return;

        //get the first face
        Face face = faces.get(0);

        //get some of the face's attributes
        float joy = face.emotions.getJoy();
        float anger = face.emotions.getAnger();
        float suprise = face.emotions.getSurprise();

        //print attributes to console
        System.out.println("Joy: " + joy);
        System.out.println("Anger: " + anger);
        System.out.println("Suprise: " + suprise);
    }
}


