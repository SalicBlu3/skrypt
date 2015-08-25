package net.ynotapps.skrypt.ui.controllers;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.ToggleButton;

import net.ynotapps.skrypt.utils.SkryptSpeechRecognitionListener;

public class SpeechController implements SkryptSpeechRecognitionListener.ResultHandler{

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private Activity activity;

    private ToggleButton operateButton;
    private SkryptController skryptController;
    private PointController pointController;
    private PromptController promptController;
    private TimeController timeController;
    private ResultController resultController;

    public SpeechController(Activity activity, ToggleButton operateButton, SkryptController skryptController,
                            PointController pointController, PromptController promptController,
                            TimeController timeController, ResultController resultController) {
        this.activity = activity;
        this.operateButton = operateButton;
        this.skryptController = skryptController;
        this.pointController = pointController;
        this.promptController = promptController;
        this.timeController = timeController;
        this.resultController = resultController;

        initSpeechRecognizer();
    }

    public void initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);

        // Add flags to speech recognizer
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                activity.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);

        // Set up listener
        speechRecognizer.setRecognitionListener(new SkryptSpeechRecognitionListener(this, activity));
    }

    public void startDictating() {
        speechRecognizer.startListening(recognizerIntent);
    }


    /**
     *  Handle Speech Recognition Result
     */

    @Override
    public void onResult(String feedback) {

    }

    @Override
    public void handlePartialResult(String feedback) {

        // Process Skrypt
        skryptController.updateSkrypt(feedback);

        // Check if we hit the prompt
        if (promptController.getPoint(feedback)) {
            promptController.showNext();
            pointController.tallyUp();
        }

    }

    @Override
    public void onStartSpeech() {

    }

    @Override
    public void onComplete() {

        // Calculate metrics
        stopAndDestroy();

        // Get Duration
        timeController.stop();

        // Display Results
        resultController.setDisplayResultView(true);
    }

    public void stopAndDestroy() {
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        speechRecognizer.destroy();

        operateButton.setChecked(false);
    }
}
