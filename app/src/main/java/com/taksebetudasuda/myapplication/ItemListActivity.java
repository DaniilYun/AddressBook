package com.taksebetudasuda.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class ItemListActivity extends SingleFragmentActivity
        implements ItemListFragment.Callbacks {

    private long backPressedTime;



    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onItemSelected(Item item) {
        ItemListFragment listFragment = (ItemListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        if (item instanceof Employe) {
            Intent intent = ItemFragment.newIntent(this, item.getId());
            startActivity(intent);
        }else {
            listFragment.updateUI(item);
        }
    }

    @Override
    public void onBackPressed() {
        ItemListFragment listFragment = (ItemListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        if (listFragment.itemStack.isEmpty()){
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
            } else Toast.makeText(this, R.string.exit_toast, Toast.LENGTH_SHORT).show();

            backPressedTime = System.currentTimeMillis();
        }

        else
        listFragment.updateUIBack();

    }
}
