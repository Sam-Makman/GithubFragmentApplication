package edu.lclark.githubfragmentapplication;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.lclark.githubfragmentapplication.models.GithubUser;

/**
 * Created by sam on 3/2/16.
 */
public class GithubUserAsyncTask extends AsyncTask<String, Integer, GithubUser> {

    public static final String TAG = GithubUserAsyncTask.class.getSimpleName();

    OnLoginFailedListener mFailureListener;
    OnLoginCompleteListener mSucessListener;

    public interface OnLoginFailedListener {
        void onLoginFailed();
    }

    public interface OnLoginCompleteListener{
        void onLoginComplete(GithubUser user);
    }

    public GithubUserAsyncTask(OnLoginFailedListener listener, OnLoginCompleteListener sucessListener, String s){
        mFailureListener = listener;
        mSucessListener = sucessListener;
    }
    @Override
    protected GithubUser doInBackground(String... params) {



        StringBuilder responseBuilder = new StringBuilder();
        GithubUser user = null;
        if (params.length == 0) {
            return null;
        }

        String userId = params[0];

        try {
            URL url = new URL("https://api.github.com/users/" + userId );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            String line;

            if (isCancelled()) {
                return null;
            }
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);

                if (isCancelled()) {
                    return null;
                }
            }

            user = new Gson().fromJson(responseBuilder.toString(), GithubUser.class);

            if (isCancelled()) {
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        return user;
    }

    @Override
    protected void onPostExecute(GithubUser githubUser) {
        super.onPostExecute(githubUser);
        if(githubUser == null){
            mFailureListener.onLoginFailed();
            Log.d(TAG, "Failure");
        }else {
            mSucessListener.onLoginComplete(githubUser);
            Log.d(TAG, "Success");
        }

    }
}
