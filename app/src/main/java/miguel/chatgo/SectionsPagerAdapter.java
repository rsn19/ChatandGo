package miguel.chatgo;

/**
 * Created by Miguel on 1/8/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Resources resources;

    public SectionsPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext=context;
        resources=context.getResources();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
            default:
                return null;
        }

        //return new InboxFragment();
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.title_messages_tab);
            case 1:
                return resources.getString(R.string.title_friends_tab);
        }
        return null;
    }
}