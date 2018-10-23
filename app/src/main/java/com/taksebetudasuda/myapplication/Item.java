package com.taksebetudasuda.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Daniil on 13.10.2018.
 */
public class Item implements Parcelable,Comparable<Item>{

    private int id;
    private String name;
    private int parentId;
    private boolean hasChild;
    private boolean employe;

    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        parentId = in.readInt();
        hasChild = in.readByte() != 0;
        employe = in.readByte() != 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public boolean isEmploye() {
        return employe;
    }

    public void setEmploye(boolean employe) {
        this.employe = employe;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Item(int id, String name, int parentId, boolean employe) {

        this.employe =employe;
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Item() {

    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(parentId);
        dest.writeByte((byte) (hasChild ? 1 : 0));
        dest.writeByte((byte) (employe ? 1 : 0));
    }

    @Override
    public int compareTo(@NonNull Item o) {
        if(this.getId()<o.getId()){
            return -1;
        }else {
            return 1;
        }
    }
}
