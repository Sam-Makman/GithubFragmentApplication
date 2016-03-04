package edu.lclark.githubfragmentapplication.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.lclark.githubfragmentapplication.GithubUserAsyncTask;
import edu.lclark.githubfragmentapplication.R;
import edu.lclark.githubfragmentapplication.models.GithubUser;


public class LoginFragment extends Fragment implements GithubUserAsyncTask.OnLoginFailedListener {

    private static final String TAG = LoginFragment.class.getSimpleName() ;
    GithubUser mUser;
    @Bind(R.id.login_fragment_enter_text)
    EditText mEditText;
    @Bind(R.id.login_fragment_button)
    Button mButton;

    @Bind(R.id.login_fragment_image)
    ImageView mImage;

    public static final String IMAGEURL = "https://s-media-cache-ak0.pinimg.com/236x/2d/8e/e8/2d8ee815146390d567706f2c7b5c2916.jpg";

    GithubUserAsyncTask mAsyncTask;

    @Override
    public void onStop() {
        super.onStop();
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        Picasso.with(getContext()).load(IMAGEURL).fit().centerCrop().into(mImage);

        Log.d(TAG, "onCreateView");

        return rootView;
    }

    @Override
    public void onLoginFailed() {
            mButton.setEnabled(true);
            Toast.makeText(getActivity(), R.string.user_error, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.login_fragment_button)
    public void onFindUserClick() {
        ConnectivityManager manager =(ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        if(network == null || !network.isConnected()){
            Toast.makeText(getContext(), R.string.toast_no_internet, Toast.LENGTH_SHORT).show();
        }else {
            mAsyncTask = new GithubUserAsyncTask(this,
                    (GithubUserAsyncTask.OnLoginCompleteListener) getActivity(),
                    mEditText.getText().toString());
            mAsyncTask.execute(mEditText.getText().toString());
            mButton.setEnabled(false);
        }
        View v = getActivity().getCurrentFocus();
        if(v != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
