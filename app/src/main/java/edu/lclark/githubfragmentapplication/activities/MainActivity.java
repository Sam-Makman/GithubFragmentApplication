package edu.lclark.githubfragmentapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.lclark.githubfragmentapplication.GithubUserAsyncTask;
import edu.lclark.githubfragmentapplication.NetworkAsyncTask;
import edu.lclark.githubfragmentapplication.R;
import edu.lclark.githubfragmentapplication.fragments.LoginFragment;
import edu.lclark.githubfragmentapplication.fragments.UserFragment;
import edu.lclark.githubfragmentapplication.models.GithubUser;

public class MainActivity extends AppCompatActivity implements  UserFragment.UserListener,
        GithubUserAsyncTask.OnLoginCompleteListener, NetworkAsyncTask.GithubListener {

    @Bind(R.id.activity_main_framelayout)
    FrameLayout mFrameLayout;

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    NetworkAsyncTask mNeworkTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mFab.setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_main_framelayout, new LoginFragment());
                transaction.commit();
            }
        });


        mFab.setVisibility(View.GONE);
        mFab.setImageResource(android.R.drawable.ic_input_add);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_framelayout, new LoginFragment());
        transaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUserFollowerButtonClicked(GithubUser user) {
        ConnectivityManager manager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        if(network == null || !network.isConnected()){
            Toast.makeText(this, R.string.toast_no_internet, Toast.LENGTH_SHORT).show();
        }else {
            mNeworkTask = new NetworkAsyncTask(this);
            mNeworkTask.execute(user.getLogin());
        }
    }


    @Override
    public void onLoginComplete(GithubUser user) {
        mFab.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_framelayout, UserFragment.newInstance(user));
        transaction.commit();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!mNeworkTask.isCancelled()){
            mNeworkTask.cancel(true);
        }
    }

    @Override
    public void onGithubFollowersRetrieved(@Nullable ArrayList<GithubUser> followers) {
        if(followers == null){
            Toast.makeText(this, R.string.cant_retrieve_followers, Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(MainActivity.this, TabbedUserActivity.class);
            intent.putParcelableArrayListExtra(TabbedUserActivity.ARG_USERS, followers);
            startActivity(intent);
        }
    }
}
