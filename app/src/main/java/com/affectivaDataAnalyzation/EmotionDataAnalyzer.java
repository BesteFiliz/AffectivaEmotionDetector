package com.affectivaDataAnalyzation;

/**
 * The Emotion Data Analyzer class can be fed the Data coming in from the Affectiva SDK and
 * observe different emotional trends
 *
 * @author Thomas Oropeza (thomasoropeza@gmail.com)
 */
public class EmotionDataAnalyzer {

    //listener whose method will be called on a thresholdReached Event
    EmotionDataAnalyzerListener listener;

    //window of time to analyze (Seconds)
    int window;

    //mean threshold that will trigger listener method (On a scale of 0-100)
    int threshold;

    //data within the Window
    DataEntry[] data;

    //index for data that will circulate through the data array to add new and replace old values
    int currentDataIndex;

    /**
     * @param listener listener whose method will be called on a thresholdReached Event
     * @param window window of time to analyze (Seconds)
     * @param threshold mean threshold that will trigger listener method (On a scale of 0-100)
     * */
    public EmotionDataAnalyzer(EmotionDataAnalyzerListener listener, int window, int threshold){
        this.listener = listener;
        this.window = window;
        this.threshold = threshold;
        this.data = new DataEntry[window];
        this.currentDataIndex = 0;
    }

    /**
     * Add the Emotion Reading to the Data Analyzer.
     *
     * If the mean of the data within the current window passes the Threshold,
     * the EmotionDataAnalyzerListener method will be called
     *
     * @param emotionReading The Emotion Reading data
     * @param timeStamp The time the data was recorded
     * */
    public void addEmotionData(float emotionReading, float timeStamp){
        DataEntry entry = new DataEntry(emotionReading, timeStamp);
        addDataEntry(entry);
        if (thresholdReached()){
            listener.thresholdReached();
        }
    }

    /**
     * Returns whether or not the data within the current window reached the Threshold
     * */
    private boolean thresholdReached() {
        boolean thresholdReached = false;
        float mean = calculateMean();
        if (mean >= threshold){
            thresholdReached = true;
        }
        return thresholdReached;
    }

    /**
     * Calculates the mean of the Data Set
     * */
    private float calculateMean() {
        //calculate Sum of numbers in Window
        float sum = 0;
        for (DataEntry dataEntry : data) {
            sum += dataEntry.data;
        }
        //mean = sum/size
        return sum/data.length;
    }

    /**
     * Adds a Data Entry to the Data set
     * */
    private void addDataEntry(DataEntry dataEntry) {
        //end of the data array
        if (currentDataIndex == window){
            currentDataIndex = 0;
        }
        data[currentDataIndex] = dataEntry;
        currentDataIndex++;
    }

    /**
     * Private Helper class that wraps the properties of a Data Entry
     * */
    private class DataEntry{

        float data;
        float timeStamp;

        /**
         * @param data The Data from the Emotion Reading
         * @param timeStamp The timestamp from the Emotion Reading
         * */
        public DataEntry(float data, float timeStamp) {
            this.data = data;
            this.timeStamp = timeStamp;
        }
    }
}
