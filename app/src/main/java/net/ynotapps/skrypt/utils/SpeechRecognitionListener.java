package net.ynotapps.skrypt.utils;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class SpeechRecognitionListener implements RecognitionListener {

    private ResultHandler resultHandler;

    public SpeechRecognitionListener(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("Speech", "Ready");
    }

    @Override
    public void onBeginningOfSpeech() {
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
        resultHandler.onComplete();
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
        resultHandler.handlePartialResult(getBestResult(results));
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

    public interface ResultHandler {

        void onResult(String feedback);

        void handlePartialResult(String feedback);

        void onStartSpeech();

        void onComplete();
    }
}
