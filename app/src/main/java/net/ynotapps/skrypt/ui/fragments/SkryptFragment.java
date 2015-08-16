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
import android.widget.Toast;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.model.dto.Skrypt;
import net.ynotapps.skrypt.utils.SpeechRecognitionListener;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SkryptFragment extends BaseFragment implements SpeechRecognitionListener.ResultHandler {

    public static final String FRAGMENT_ID = "Skrypt";
    private static final String PROMPT_USER = "Start your flow...";
    private static final String PROMPT_HAS_BEGUN = "Go!";
    @InjectView(R.id.button_start)
    public Button start;
    @InjectView(R.id.tv_feedback)
    public TextView feedbackView;
    @InjectView(R.id.tv_skrypt)
    public TextView skryptView;
    @InjectView(R.id.tv_result)
    public TextView resultView;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private long timeStarted;
    private String skryptText = "";
    private boolean isStarted = false;

    @Override
    public String getFragmentTag() {
        return FRAGMENT_ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_skrypt, container, false);
        ButterKnife.inject(this, view);
        feedbackView.setText(PROMPT_USER);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupSpeechRecognizer();
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

        // Avoid double start
        if (isStarted) {
            return;
        }

        // Clear out skryptText
        if (!skryptText.trim().isEmpty()) {
            skryptView.setText("");
        }

        // Reset Visibility of Result View
        resultView.setVisibility(View.GONE);

        // Set up variables
        timeStarted = Calendar.getInstance().getTimeInMillis();
        isStarted = true;

        // Atart listening
        speechRecognizer.startListening(recognizerIntent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        speechRecognizer.destroy();
    }


    /**
     *   Set up Actionbar Menu
     */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_skrypt, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {

            // Save skrypt
            Skrypt skrypt = new Skrypt(Calendar.getInstance().getTime(), skryptText);
            skrypt.save();

            // Give feedback to user
            Toast.makeText(getActivity(), "Skrypt Saved", Toast.LENGTH_SHORT).show();

            // Debug
            Log.d("Skrypt saved", skrypt.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *   Handle results from speech recognition
     */

    @Override
    public void onStartSpeech() {
        feedbackView.setText(PROMPT_HAS_BEGUN);
    }

    @Override
    public void onResult(String feedback) {}

    /**
     * Displays to the user a real time feed of their skryptText
     * @param feedback
     */
    @Override
    public void handlePartialResult(String feedback) {

        // User Feedback
        skryptView.setText(feedback);

        // Store skryptText
        skryptText = feedback;

        // Debug
        Log.d("Partial Results", feedback);
    }

    @Override
    public void onComplete() {

        // Calculate metrics

        // Get Duration
        long timeFinished = Calendar.getInstance().getTimeInMillis();
        int duration = (int) ((timeFinished - timeStarted) / 1000);

        // Get Number of words
        int numberOfWords = skryptText.split(" ").length;

        // Display result
        String flowLengthText = String.format("You had a %d second flow.\nYou spun up %d words", duration, numberOfWords);
        resultView.setText(flowLengthText);
        resultView.setVisibility(View.VISIBLE);

        // Enable start button
        isStarted = false;

        // Debug
        Log.d("Skrypt - Complete", flowLengthText);
    }

}
