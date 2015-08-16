package net.ynotapps.skrypt.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.ynotapps.skrypt.ui.fragments.SkryptFragment;

public class HomepagePagerAdapter extends FragmentStatePagerAdapter {

    public static final int FRAGMENT_COUNT = 2;
    public static final String FLOW_TITLE = "Work on your Flow";

    public HomepagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return FLOW_TITLE;
            default:
                return "Something Else";
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new SkryptFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
