package net.ynotapps.skrypt.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.ynotapps.skrypt.ui.fragments.SkryptFragment;
import net.ynotapps.skrypt.ui.fragments.SkryptListFragment;

public class HomepagePagerAdapter extends FragmentStatePagerAdapter {

    public static final int FRAGMENT_COUNT = 2;
    public static final String FLOW_TITLE = "Work on your Flow";
    private SkryptFragment skryptFragment;
    private SkryptListFragment skryptListFragment;

    public HomepagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return FLOW_TITLE;
            default:
                return "Saved Flows";
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                skryptFragment = new SkryptFragment();
                return skryptFragment;
            default:
                skryptListFragment = new SkryptListFragment();
                return skryptListFragment;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    public void updateSavedSkryptList() {
        skryptListFragment.update();
    }
}
