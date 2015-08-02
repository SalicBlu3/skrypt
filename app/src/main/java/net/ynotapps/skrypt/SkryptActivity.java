package net.ynotapps.skrypt;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 *  Captures data
 */
public class SkryptActivity extends ActionBarActivity implements RecognitionListener {

    @InjectView(R.id.tv_feedback)
    TextView feedback;

    @InjectView(R.id.button_start)
    Button start;

    @InjectView(R.id.toggle)
    ToggleButton toggle;

    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;
    private Intent recognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skrypt);
        ButterKnife.inject(this);

        setupSpeechRecognizer();
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }


    @OnClick(R.id.button_start)
    public void start() {
        if (isListening) {
            speechRecognizer.stopListening();
            isListening = false;
        } else {
            speechRecognizer.startListening(recognizerIntent);
            isListening = true;
        }

        toggle.setChecked(isListening);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skrypt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*******************
     *
     *                  Speech Recognizer
     *
     *******************/

    @Override
    public void onReadyForSpeech(Bundle params) {
        feedback.setText("READY...");
        Log.d("Speech", "Ready");
    }

    @Override
    public void onBeginningOfSpeech() {
        feedback.setText("");
        Log.d("Speech", "Listening");
    }

    @Override
    public void onRmsChanged(float rmsdB) {}

    @Override
    public void onBufferReceived(byte[] buffer) {}

    @Override
    public void onEndOfSpeech() {
        Log.d("Speech", "End Speech");
    }

    @Override
    public void onError(int error) {}

    @Override
    public void onResults(Bundle results) {
        final String bestResult = getBestResult(results);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                feedback.setText(bestResult);
            }
        });
    }

    @Override
    public void onPartialResults(Bundle results) {}

    @Override
    public void onEvent(int eventType, Bundle params) {}

    public String getBestResult(Bundle results) {

        // Get lists from bundle
        ArrayList<String> resultsList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        float[] confidenceArray = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);

        Log.d("Bundle", Arrays.toString(resultsList.toArray()));
        Log.d("Bundle", Arrays.toString(confidenceArray));

        // Iterate through to find peak confidence
        String result = "";
        Float bestConfidence = null;

        for (int i = 0; i < confidenceArray.length; i++) {
            float confidence = confidenceArray[i];
            if (bestConfidence == null) {
                bestConfidence = confidence;
            } else if (bestConfidence < confidence) {
                result = resultsList.get(i);
            }
        }
        return result;
    }
}
