package net.ynotapps.skrypt.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.model.dto.Skrypt;
import net.ynotapps.skrypt.utils.SpeechRecognitionListener;

import java.util.Calendar;

import butterknife.InjectView;
import butterknife.OnClick;

public class SkryptFragment extends BaseFragment implements SpeechRecognitionListener.ResultHandler {

    public static final String FRAGMENT_ID = "Skrypt";
    @InjectView(R.id.button_start)
    public Button start;

    @InjectView(R.id.tv_feedback)
    public TextView feedbackView;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private long timeStarted;

    @Override
    public String getFragmentTag() {
        return FRAGMENT_ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_skrypt, container, false);


        return view;
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());

        // Add flags to speech recognizer
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);

        // Set up listener
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener(this));
    }


    @OnClick(R.id.button_start)
    public void start() {
        timeStarted = Calendar.getInstance().getTimeInMillis();
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        speechRecognizer.destroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_skrypt, menu);
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


    /**
     *   Handle results from speech recognition
     */

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
        Log.d("Partial Results", feedback);
    }

    @Override
    public void onComplete() {
        long timeFinished = Calendar.getInstance().getTimeInMillis();
        int duration = (int) ((timeFinished - timeStarted) / 1000);
        String flowLengthText = String.format("\n\nFlow length: %ss", duration);
        feedbackView.append(flowLengthText);
        Log.d("Complete", flowLengthText);
    }

    @Override
    public void onStartSpeech() {}
}
