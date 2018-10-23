package com.taksebetudasuda.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taksebetudasuda.myapplication.login.LoginFragment;

public class ItemFragment extends Fragment {

    private static final String ARG_EMPLOYE_ID = "item_id";

    private int currentId;
    private Employe employe;
    private TextView nameTextView;
    private TextView titleTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private ImageView photoImageView;
    Bitmap bitmap;

    public static Intent newIntent(Context packageContext, int itemId) {
        Intent intent = new Intent(packageContext, ItemActivity.class);
        intent.putExtra(ARG_EMPLOYE_ID, itemId);
        return intent;
    }


    public static ItemFragment newInstance(int employeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EMPLOYE_ID, employeId);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentId = (int) getActivity().getIntent().getSerializableExtra(ARG_EMPLOYE_ID);
        employe = ItemLab.get(getActivity()).getEmployeById(currentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employe, container, false);


        nameTextView = (TextView) v.findViewById(R.id.employe_name);
        nameTextView.setText(employe.getName());
        titleTextView = (TextView) v.findViewById(R.id.employe_title);
        titleTextView.setText(employe.getTitle());
        phoneTextView = (TextView) v.findViewById(R.id.employe_phone);
        phoneTextView.setText(employe.getPhone());
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneTextView.getText().toString()));
                startActivity(intent);
            }
        });
        emailTextView = (TextView) v.findViewById(R.id.employe_email);
        emailTextView.setText(employe.getEmail());
        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + emailTextView.getText().toString());
                intent.setData(data);
                startActivity(intent);
            }
        });
        new FetchItemsTask().execute();
        setTitle();

        photoImageView = (ImageView) v.findViewById(R.id.employe_photo);
        updatePhotoView();

        return v;
    }

    private void updatePhotoView() {
        if (bitmap != null) {
            photoImageView.setImageBitmap(bitmap);
        }
    }

    private void setTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(employe.getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_employe, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.logout_message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPref.write(SharedPref.IS_SELECT, false);
                                Intent intent = LoginFragment.newIntent(getActivity());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create()
                        .show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(Void... voids) {

            bitmap = new AddressBookLoader().photoLoad(String.valueOf(currentId));

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bbitmap) {
            bitmap = bbitmap;
            updatePhotoView();

        }
    }
}
