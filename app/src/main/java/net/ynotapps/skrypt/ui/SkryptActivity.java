package net.ynotapps.skrypt.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.ui.fragments.SkryptFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Captures data
 */
public class SkryptActivity extends ActionBarActivity {

    @InjectView(R.id.navigation_list)
    public ListView navigationList;

    @InjectView(R.id.drawer)
    public DrawerLayout drawerLayout;

    @InjectView(R.id.toolbar)
    public Toolbar toolbar;

    private SkryptFragment skryptFragment;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skrypt);
        ButterKnife.inject(this);

        setupNavigationPanel();
        displaySkryptFragment();
        setupActionBarDrawerToggle();
    }

    private void setupActionBarDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.actionbar_title_open, R.string.actionbar_title_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void displaySkryptFragment() {

        if (skryptFragment == null) {
            skryptFragment = new SkryptFragment();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, skryptFragment, skryptFragment.getFragmentTag())
                .addToBackStack(skryptFragment.getFragmentTag())
                .commit();
    }

    private void setupNavigationPanel() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Test1", "Test2"});
        navigationList.setAdapter(adapter);
        navigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
