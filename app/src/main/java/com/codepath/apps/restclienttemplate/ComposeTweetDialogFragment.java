package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import okhttp3.Headers;

public class ComposeTweetDialogFragment extends DialogFragment {

    public static final String TAG = "ComposeTweetDialog";
    public static final int MAX_TWEET_LENGTH = 280;

    private EditText composeET;
    private Button tweetButton;
    private TextInputLayout textInputLayout;

    TwitterClient client;
    private OnCompleteListener mListener;

    private ComposeTweetDialogFragment(){

    }

    public static ComposeTweetDialogFragment newInstance(String tweet){
        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();
        Bundle args = new Bundle();
        args.putString("tweet", tweet);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.compose_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getRestClient(getActivity());
        composeET = (EditText) view.findViewById(R.id.composeTweetET);
        textInputLayout = view.findViewById(R.id.textContainer);
        textInputLayout.setCounterMaxLength(MAX_TWEET_LENGTH);
        getDialog().setTitle("Compose your tweet");
        composeET.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tweetButton = view.findViewById(R.id.tweetBTN);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = composeET.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(getActivity(), "Whoops, try typing something first", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(getActivity(), "Whoops, that's a bit too long", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getActivity(), tweetContent, Toast.LENGTH_LONG).show();
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.e(TAG,  "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published Tweet says: " +tweet);
                            mListener.onComplete(tweet);
                            dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,  "onFailure to publish tweet", throwable);
                    }
                });

            }
        });
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(Tweet tweet);
    }

    public interface ComposeTweetDialogListener{
        void onFinishComposeDialog(String inputText);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            e.printStackTrace();
        }
    }
}
