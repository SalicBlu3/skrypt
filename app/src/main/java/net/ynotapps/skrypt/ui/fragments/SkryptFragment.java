package net.ynotapps.skrypt.ui.fragments;

import android.content.Intent;
import android.media.MediaRecorder;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.ui.SkryptActivity;
import net.ynotapps.skrypt.ui.controllers.PointController;
import net.ynotapps.skrypt.ui.controllers.PromptController;
import net.ynotapps.skrypt.ui.controllers.SkryptController;
import net.ynotapps.skrypt.utils.PromptUtils;
import net.ynotapps.skrypt.utils.SkryptSpeechRecognitionListener;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;

public class SkryptFragment extends BaseFragment implements SkryptSpeechRecognitionListener.ResultHandler {

    public static final String FRAGMENT_ID = "Skrypt";
    private static final String PROMPT_USER = "Start your flow...";
    private static final String PROMPT_HAS_BEGUN = "Go!";
    private static final String LOG_TAG = "SkryptFragment";

    @InjectView(R.id.button_start)
    public ToggleButton start;

    @InjectView(R.id.tv_skrypt)
    public TextView skryptView;

    @InjectView(R.id.tv_result)
    public TextView resultView;

    @InjectView(R.id.scroll_skrypt)
    public ScrollView scrollView;

    @InjectView(R.id.tally)
    public TextView pointView;

    @InjectView(R.id.prompt)
    public TextView promptView;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private long timeStarted;
    private String skryptText = "";
    private MediaRecorder mRecorder;

    private PromptUtils promptUtils = new PromptUtils();
    private String prompt = "";

    private PointController pointController;
    private PromptController promptController;
    private SkryptController skryptController;


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

        pointController = new PointController(pointView);
        promptController = new PromptController(promptView);
        skryptController = new SkryptController(skryptView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupSpeechRecognizer();
        pointController.reset();
        promptController.reset();
        skryptController.reset();
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
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);

        // Set up listener
        speechRecognizer.setRecognitionListener(new SkryptSpeechRecognitionListener(this, getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        speechRecognizer.destroy();
    }

    /**
     * Set up Actionbar Menu
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
            skryptController.saveSkrypt();

            // Give feedback to user
            Toast.makeText(getActivity(), "Skrypt Saved", Toast.LENGTH_SHORT).show();

            // Update List
            ((SkryptActivity) getActivity()).updateList();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnCheckedChanged(R.id.button_start)
    public void onChecked(boolean checked) {
        if (checked) {
            reset();
            startDictating();
        } else {
            stopDictating();
        }
    }

    private void reset() {
        // Reset Visibility of Result View
        resultView.setVisibility(View.GONE);

        // Clear out skryptText
        if (!skryptText.trim().isEmpty()) {
            skryptView.setText("");
        }

        pointController.reset();
    }

    private void startDictating() {
        // Set up starting time
        timeStarted = Calendar.getInstance().getTimeInMillis();

        // Start listening
        speechRecognizer.startListening(recognizerIntent);
    }

    private void stopDictating() {
        speechRecognizer.stopListening();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    /**
     * Handle results from speech recognition
     */

    @Override
    public void onResult(String feedback) {
    }

    /**
     * Displays to the user a real time feed of their skryptText
     */
    @Override
    public void handlePartialResult(String feedback) {

        // Process Skrypt
        skryptController.updateSkrypt(feedback);

        // Check if we hit the prompt

        // Ignore empty prompts
        String currentPrompt = promptController.getCurrentPrompt();
        if (!currentPrompt.trim().isEmpty() && skryptController.contains(currentPrompt)) {
            prompt = promptController.showNext();
            pointController.tallyUp();
        }

        // Ensure that ScrollView text displays newest text
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.fullScroll(View.FOCUS_DOWN);

        // Debug
        Log.d("Partial Results", feedback);
    }



    @Override
    public void onStartSpeech() {
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

        // Result covers skrypt so auto scroll the skrypt view to display
        scrollView.fullScroll(View.FOCUS_DOWN);

        // Debug
        Log.d("Skrypt - Complete", flowLengthText);

    }

}