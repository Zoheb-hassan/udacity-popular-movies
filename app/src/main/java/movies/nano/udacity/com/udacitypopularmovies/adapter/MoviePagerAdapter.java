package movies.nano.udacity.com.udacitypopularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Suhel on 19-04-2016.
 */
public class MoviePagerAdapter extends FragmentPagerAdapter{

    List<Fragment> fragmentList;
    String [] titleTabs = {"Synopsis", "Reviews", "Trailers"};

    public MoviePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleTabs[position];
    }
}
