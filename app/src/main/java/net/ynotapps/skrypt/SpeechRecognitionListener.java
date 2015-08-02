package net.ynotapps.skrypt;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

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

    private String getBestResult(Bundle results) {

        // Get lists from bundle
        ArrayList<String> resultsList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (resultsList.size() > 0) {
            return resultsList.get(0);
        } else {
            return "";
        }
    }
}