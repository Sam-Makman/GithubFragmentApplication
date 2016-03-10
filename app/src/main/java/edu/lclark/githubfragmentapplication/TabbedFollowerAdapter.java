package edu.lclark.githubfragmentapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import edu.lclark.githubfragmentapplication.fragments.UserFragment;
import edu.lclark.githubfragmentapplication.models.GithubUser;


public class TabbedFollowerAdapter extends FragmentPagerAdapter {

    ArrayList<GithubUser> mUsers;

    public TabbedFollowerAdapter(ArrayList<GithubUser> users, FragmentManager fm){
        super(fm);
        mUsers = users;
    }

    @Override
    public Fragment getItem(int position) {
        return UserFragment.newInstance(mUsers.get(position));
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mUsers.get(position).getLogin();
    }
}
