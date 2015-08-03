package net.ynotapps.skrypt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.model.dto.Skrypt;
import net.ynotapps.skrypt.utils.SpeechRecognitionListener;
import net.ynotapps.skrypt.utils.SpeechRecognitionListener.ResultHandler;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 *  Captures data
 */
public class SkryptActivity extends AppCompatActivity implements ResultHandler {

    @InjectView(R.id.button_start)
    public Button start;

    @InjectView(R.id.tv_feedback)
    public TextView feedbackView;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private long timeStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skrypt);
        ButterKnife.inject(this);

        setupSpeechRecognizer();
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);

        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener(this));
    }


    @OnClick(R.id.button_start)
    public void start() {
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skrypt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {
            Skrypt skrypt = new Skrypt(Calendar.getInstance().getTime(), feedbackView.getText().toString());
            skrypt.save();
            Log.d("Skrypt saved", skrypt.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //    Handle results from speech recognition

    /**
     *
     * @param feedback
     */
    @Override
    public void onResult(String feedback) {}

    /**
     * Displays to the user a real time feed of their skrypt
     * @param feedback
     */
    @Override
    public void handlePartialResult(String feedback) {
        feedbackView.setText(feedback);
    }

    @Override
    public void onComplete() {
        long timeFinished = Calendar.getInstance().getTimeInMillis();
        int duration = (int) ((timeFinished - timeStarted) / 1000);
        feedbackView.append(String.format("\n\nFlow length: %ss", duration));
    }

    @Override
    public void onStartSpeech() {
        timeStarted = Calendar.getInstance().getTimeInMillis();
    }
}
