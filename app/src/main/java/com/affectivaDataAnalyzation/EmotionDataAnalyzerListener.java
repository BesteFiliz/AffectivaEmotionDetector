package com.affectivaDataAnalyzation;

/**
 * This is a helper Interface to the EmotionDataAnalyzer.
 *
 * Contains various methods that will be triggered based on the EmotionDataAnalyzer's data
 */
public interface EmotionDataAnalyzerListener {

    /**
     * This will be called by the EmotionDataAnalyzer when the mean of the data from the time window
     * passes the given threshold.
     * */
    public void thresholdReached();
}
