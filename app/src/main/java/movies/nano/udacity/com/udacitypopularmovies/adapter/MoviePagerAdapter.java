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

        switch(position){

            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);
            case 2:
                return fragmentList.get(2);
            default:
                return null;
        }


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
