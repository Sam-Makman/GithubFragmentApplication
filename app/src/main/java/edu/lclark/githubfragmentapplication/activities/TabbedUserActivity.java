package edu.lclark.githubfragmentapplication.activities;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.lclark.githubfragmentapplication.NetworkAsyncTask;
import edu.lclark.githubfragmentapplication.R;
import edu.lclark.githubfragmentapplication.TabbedFollowerAdapter;
import edu.lclark.githubfragmentapplication.fragments.UserFragment;
import edu.lclark.githubfragmentapplication.models.GithubUser;

public class TabbedUserActivity extends AppCompatActivity implements UserFragment.UserListener, NetworkAsyncTask.GithubListener
{
    public static final String TAG = TabbedUserActivity.class.getSimpleName();
    public static final String ARG_USERS = "USER_ARGUMENT";

    @Bind(R.id.activity_tabbed_user_tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.activity_tabbed_user_viewPager)
    ViewPager mViewPager;

    private NetworkAsyncTask mNetworkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_user);
        ButterKnife.bind(this);

        ArrayList<GithubUser> users = getIntent().getParcelableArrayListExtra(ARG_USERS);


        FragmentPagerAdapter adapter = new TabbedFollowerAdapter(users, getSupportFragmentManager());

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onUserFollowerButtonClicked(GithubUser user) {
        mNetworkTask = new NetworkAsyncTask(this);
        mNetworkTask.execute(user.getLogin());
    }

    @Override
    public void onGithubFollowersRetrieved(@Nullable ArrayList<GithubUser> followers) {
        Toast.makeText(this, "WHAT AM I DOING", Toast.LENGTH_SHORT).show();
    }
}
