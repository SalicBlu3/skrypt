package net.ynotapps.skrypt.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import net.ynotapps.skrypt.ui.controllers.ResultController;
import net.ynotapps.skrypt.ui.controllers.SkryptController;
import net.ynotapps.skrypt.ui.controllers.SpeechController;
import net.ynotapps.skrypt.ui.controllers.TimeController;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;

public class SkryptFragment extends BaseFragment {

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

    private PointController pointController;
    private PromptController promptController;
    private SkryptController skryptController;
    private TimeController timeController;
    private SpeechController speechController;
    private ResultController resultController;

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
        skryptController = new SkryptController(skryptView, scrollView);
        timeController = new TimeController();
        resultController = new ResultController(resultView, skryptController, timeController);
        speechController = new SpeechController(getActivity(), start, skryptController, pointController, promptController, timeController, resultController);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pointController.reset();
        promptController.reset();
        skryptController.reset();
        speechController.initSpeechRecognizer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        speechController.stopAndDestroy();
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

    /**
     *  Start Button
     */

    @OnCheckedChanged(R.id.button_start)
    public void onChecked(boolean checked) {
        if (checked) {
            resetForNewSkrypt();
            startDictating();
        } else {
            stopDictating();
        }
    }

    private void resetForNewSkrypt() {
        // Reset Visibility of Result View
        resultController.setDisplayResultView(false);

        // Clear out skryptText
        skryptController.reset();
        pointController.reset();
        promptController.reset();
        timeController.reset();
    }

    private void startDictating() {
        // Set up starting time
        timeController.start();

        // Set up prompt
        promptController.showNext();

        // Start listening
        speechController.initSpeechRecognizer();
        speechController.startDictating();
    }

    private void stopDictating() {
        speechController.stopAndDestroy();
    }

}