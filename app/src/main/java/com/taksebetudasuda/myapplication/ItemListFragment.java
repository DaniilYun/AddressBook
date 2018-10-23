package com.taksebetudasuda.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taksebetudasuda.myapplication.login.LoginFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String SAVED_ID = "idd";
    private static final String SAVED_PARENT_ID = "parent_id";
    private static final String SAVED_SUBTITLE_NAME = "subtitle_name";

    private static final String EXTRA_ITEM_ID = "item_id";
    private static final String EXTRA_ITEM_PARENT_NAME = "item_name";

    private RecyclerView mCrimeRecyclerView;
    private ItemAdapter mAdapter;
    private Callbacks mCallbacks;
    private Item currentItem;
    List<Item> itemList= new ArrayList<>();

    public List<Item>itemStack= new ArrayList<>();


    public interface Callbacks {
        void onItemSelected(Item item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentItem = new Item();
        if (savedInstanceState != null) {
             currentItem.setParentId(savedInstanceState.getInt(SAVED_ID));
             currentItem.setId(savedInstanceState.getInt(SAVED_ID));
            currentItem.setName(savedInstanceState.getString(SAVED_SUBTITLE_NAME));
            itemStack = savedInstanceState.getParcelableArrayList(SAVED_SUBTITLE_VISIBLE);
            Collections.sort(itemStack);
        }else {
            currentItem.setName("");
            currentItem.setId(-1);
            currentItem.setParentId(-1);
        }
        new FetchItemsTask().execute();
        updatesSubtitle(currentItem.getName());
        return view;
    }

public  void updateUIBack(){
    currentItem=itemStack.get(itemStack.size()-1);
    itemStack.remove(itemStack.size()-1);
    List list = ItemLab.get(getActivity()).getItemsByParent(itemList,currentItem.getParentId());

    if (mAdapter!=null) {
        mAdapter.setItemList(list);
        mAdapter.notifyDataSetChanged();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (currentItem.getParentId()==-1){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        actionBar.setSubtitle(currentItem.getName());
    }else{
        setupAdapter();
    }
}


    public  void updateUI(Item item){
        currentItem.setId(item.getId());
        currentItem.setParentId(item.getParentId());
        List list = ItemLab.get(getActivity()).getItemsByParent(itemList,item.getId());

        if (mAdapter!=null) {
            mAdapter.setItemList(list);
            mAdapter.notifyDataSetChanged();
            itemStack.add(new Item(currentItem.getId(),currentItem.getName(),currentItem.getParentId(),item.isEmploye()));
            updatesSubtitle(currentItem.getName()+"/"+item.getName());
        }else{
            setupAdapter();
        }
    }


    public void setupAdapter() {

            if (isAdded()) {
                List list= ItemLab.get(getActivity()).getItemsByParent(itemList, currentItem.getParentId());
                mAdapter = new ItemAdapter(list);
                mCrimeRecyclerView.setAdapter(mAdapter);
            }


    }
    private void updatesSubtitle(String s) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if (currentItem.getId() != -1) {

                actionBar.setDisplayHomeAsUpEnabled(true);

                if (!currentItem.getName().equals(s)) {
                    currentItem.setName(s);
                }
                actionBar.setSubtitle(currentItem.getName());
            } else {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }

        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PARENT_ID, currentItem.getParentId());
        outState.putInt(SAVED_ID, currentItem.getId());
        outState.putString(SAVED_SUBTITLE_NAME, currentItem.getName());
        outState.putParcelableArrayList(SAVED_SUBTITLE_VISIBLE, (ArrayList<? extends Parcelable>) itemStack);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_item_list, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!itemStack.isEmpty()) {
                    updateUIBack();
                }else {
                   super.onOptionsItemSelected(item);
                }
                return true;
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Item item;


        private TextView mTitleTextView;
        private ImageView mDateTextView;
        private ImageView mSolvedImageView;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_item, parent, false));

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.employe_name);
            mDateTextView = (ImageView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Item crime) {
            item = crime;
            mTitleTextView.setText(item.getName());
            if (item instanceof Employe){
                mDateTextView.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));
                mSolvedImageView.setVisibility(View.GONE);
            }else {
                mSolvedImageView.setVisibility(View.VISIBLE);
                mDateTextView.setImageDrawable(getResources().getDrawable(R.drawable.ic_folder_black_24dp));
            }
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onItemSelected(item);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        private List<Item> itemList;

        private ItemAdapter(List<Item> items) {
            itemList = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item crime = itemList.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public void setItemList(List<Item> crimes) {
            itemList = crimes;
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ItemListActivity.class);
        return intent;
    }

    public static ItemListFragment newInstance(Item item) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ITEM_ID, item.getId());

        args.putSerializable(EXTRA_ITEM_PARENT_NAME,"");

        ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<Item>> {


        @Override
        protected List<Item> doInBackground(Void... voids) {

                itemList=  new AddressBookLoader().downloadItems();

            return itemList;
        }

        @Override
        protected void onPostExecute(List<Item> itemsList) {
            itemList=itemsList;
            setupAdapter();
        }
    }
}
