package net.ynotapps.skrypt.tasks;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import net.ynotapps.skrypt.utils.SkryptSpeechRecognitionListener;

public abstract class SpeechTask extends Service {

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private long timeStarted;
    private String skryptText = "";
    private Context context;
    private SkryptSpeechRecognitionListener.ResultHandler handler;

    protected SpeechTask(Context context, SkryptSpeechRecognitionListener.ResultHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onPreExecuteUi();
        speechSetUp();
    }


    private void speechSetUp() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

        // Add flags to speech recognizer
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        recognizerIntent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        recognizerIntent.putExtra("android.speech.extra.GET_AUDIO", true);

        // Set up listener
        speechRecognizer.setRecognitionListener(new SkryptSpeechRecognitionListener(handler, context));
    }

    public abstract void onPreExecuteUi();


    @Override
    public void onDestroy() {
        super.onDestroy();
        speechTearDown();
    }

    private void speechTearDown() {
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        speechRecognizer.destroy();
    }
}
