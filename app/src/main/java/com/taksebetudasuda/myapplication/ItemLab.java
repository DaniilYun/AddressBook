package com.taksebetudasuda.myapplication;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ItemLab {

    private static ItemLab itemLab;
    private Context mContext;
    private  List<Item> items;

    public static ItemLab get(Context context) {
        if (itemLab == null) {
            itemLab = new ItemLab(context);
        }

        return itemLab;
    }

    private ItemLab(Context context) {
        mContext = context.getApplicationContext();
    }



     public List<Item> getItemsByParent(List<Item> list,int parentId){
        if (items==null){
            items=list;
        }
        List<Item> result = new ArrayList<>();
        for( Item s: list){
            if (s.getParentId()==parentId){
                result.add(s);
            }
        }
        return result;
    }

    public Employe getEmployeById(int id){
        Employe e = new Employe();
        for (Item employe: items){
            if (employe.getId()==id){
                e = (Employe) employe;
            }
        }
        return e;
    }

}
