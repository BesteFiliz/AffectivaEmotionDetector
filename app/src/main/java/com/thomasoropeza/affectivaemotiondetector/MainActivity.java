package com.thomasoropeza.affectivaemotiondetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectivaDataAnalyzation.EmotionDataAnalyzer;
import com.affectivaDataAnalyzation.EmotionDataAnalyzerListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener, EmotionDataAnalyzerListener {

    //Table Layout displaying emotion readings
    TableLayout emotionReadingsTableLayout;
    SurfaceView cameraDetectorSurfaceView;

    //Labels displaying each emotion reading
    TextView joyLevel;
    TextView angerLevel;
    TextView supriseLevel;

    //Main Affectiva Detector for Realtime Emotion tracking
    CameraDetector cameraDetector;

    //Video Frames processed per second
    int maxProcessingRate = 20;

    //Data Analyzer
    EmotionDataAnalyzer emotionDataAnalyzer;

    //Parameters for Data Analyzer
    int dataAnalyzerWindow = 50;
    int dataAnalyzerThreshold = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emotionDataAnalyzer = new EmotionDataAnalyzer(this, dataAnalyzerWindow, dataAnalyzerThreshold);

        //Initializing views
        joyLevel = new TextView(this);
        angerLevel = new TextView(this);
        supriseLevel = new TextView(this);
        emotionReadingsTableLayout = (TableLayout) findViewById(R.id.emotionDataTableView);
        setTableLayoutData(emotionReadingsTableLayout);
        cameraDetectorSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);

        //recommended procesing rate (Frames Per Second)
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
    protected void onDestroy() {
        super.onDestroy();
        cameraDetector.stop();
    }

    @Override
    public void thresholdReached() {
        System.out.println("Threshold reached for Emotion!!");
    }

    /**
     * Sets the data within the TableLayout to describe the level of emotions
     * */
    public void setTableLayoutData(TableLayout tableLayout){
        TableRow happinessRow = new TableRow(this);
        TextView joy = new TextView(this);
        joy.setText("Joy");
        happinessRow.addView(joy);
        happinessRow.addView(joyLevel);
        tableLayout.addView(happinessRow);

        TableRow angerRow = new TableRow(this);
        TextView anger = new TextView(this);
        anger.setText("Anger");
        angerRow.addView(anger);
        angerRow.addView(angerLevel);
        tableLayout.addView(angerRow);

        TableRow supriseRow = new TableRow(this);
        TextView suprise = new TextView(this);
        suprise.setText("Suprise");
        supriseRow.addView(suprise);
        supriseRow.addView(supriseLevel);
        tableLayout.addView(supriseRow);
    }

    /**
     * Process image results from Affectiva SDK
     * */
    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
        if (faces == null)
            return; //frame was not processed

        if (faces.size() == 0)
            return; //no face found

        Face face = faces.get(0);

        //Some Expressions
        float joy = face.emotions.getJoy();
        float anger = face.emotions.getAnger();
        float suprise = face.emotions.getSurprise();
        joyLevel.setText(String.valueOf(joy));
        angerLevel.setText(String.valueOf(anger));
        supriseLevel.setText(String.valueOf(suprise));

        //analyzing the "joy" emotion
        emotionDataAnalyzer.addEmotionData(joy, timeStamp);
    }

    /**
     * Sizing the Camera Detector container for what works best with the Affectiva SDK
     */
    @Override
    public void onCameraSizeSelected(int cameraWidth, int cameraHeight, Frame.ROTATE rotation) {

        cameraHeight = cameraWidth*2;
        cameraWidth = cameraHeight*2;

        //size the SurfaceView which contains the Camera Detector as a child view
        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();
        params.height = cameraHeight;
        params.width = cameraWidth;
        cameraDetectorSurfaceView.setLayoutParams(params);
    }
}
