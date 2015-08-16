package net.ynotapps.skrypt.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.ui.adapters.HomepagePagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Captures data
 */
public class SkryptActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    public Toolbar toolbar;

    @InjectView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skrypt);
        ButterKnife.inject(this);

        // Set up ToolBar
        setSupportActionBar(toolbar);

        // Set up Tabs
        pager.setAdapter(new HomepagePagerAdapter(getSupportFragmentManager()));

    }

}
