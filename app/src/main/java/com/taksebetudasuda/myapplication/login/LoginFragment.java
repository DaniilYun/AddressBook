package com.taksebetudasuda.myapplication.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.taksebetudasuda.myapplication.AddressBookLoader;
import com.taksebetudasuda.myapplication.ItemListActivity;
import com.taksebetudasuda.myapplication.R;
import com.taksebetudasuda.myapplication.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {
    private EditText etUsername;
    private EditText etPassword;


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = (EditText) v.findViewById(R.id.etUsername);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        final Button bLogin = (Button) v.findViewById(R.id.bSignIn);


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return v;


    }
    private void login(){
        final AsyncTask<Void, Void, String> execute = new AsyncTask<Void, Void, String>() {

            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            @Override
            protected String doInBackground(Void... voids) {

                return  new AddressBookLoader().tryLogin(username, password);
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONObject jsonResponse = new JSONObject(s);
                    boolean success = jsonResponse.getBoolean("Success");
                    String message = jsonResponse.getString("Message");

                    if (success) {
                        SharedPref.write(SharedPref.USER_NAME, username);
                        SharedPref.write(SharedPref.PASSWORD, password);
                        SharedPref.write(SharedPref.IS_SELECT, true);
                        Intent intent = new Intent(getActivity(), ItemListActivity.class);
                        LoginFragment.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(message)
                                .setNegativeButton(R.string.retry, null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();
    }

}