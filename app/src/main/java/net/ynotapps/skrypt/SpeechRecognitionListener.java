package net.ynotapps.skrypt;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dougylee on 2/08/15.
 */
public abstract class SpeechRecognitionListener implements RecognitionListener {

    public abstract void handleFeedback(String feedback);

    public abstract void handleResult(String feedback);

    @Override
    public void onReadyForSpeech(Bundle params) {
        handleFeedback("READY...");
        Log.d("Speech", "Ready");
    }

    @Override
    public void onBeginningOfSpeech() {
        handleFeedback("");
        Log.d("Speech", "Listening");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("Speech", "End Speech");
    }

    @Override
    public void onError(int error) {
    }

    @Override
    public void onResults(Bundle results) {
    }

    @Override
    public void onPartialResults(Bundle results) {
        handleResult(getBestResult(results));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }

    public String getBestResult(Bundle results) {

        // Get lists from bundle
        ArrayList<String> resultsList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        float[] confidenceArray = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);

        Log.d("Bundle", Arrays.toString(resultsList.toArray()));
        Log.d("Bundle Confidence", Arrays.toString(confidenceArray));

        if (resultsList.size() > 0) {
            return resultsList.get(0);
        } else {
            return "";
        }
    }
}
