package net.ynotapps.skrypt.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.ynotapps.skrypt.ui.fragments.SkryptFragment;

public class HomepagePagerAdapter extends FragmentStatePagerAdapter {

    public static final int FRAGMENT_COUNT = 2;

    public HomepagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new SkryptFragment();
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
