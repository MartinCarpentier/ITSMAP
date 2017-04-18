package com.example.martin.developercv.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martin on 16-04-2017.
 */

public class DeveloperInfo implements Parcelable {
    public DeveloperInfo(String name, String id, String android)
    {
        this.name = name;
        this.id = id;
        this.android = Boolean.valueOf(android);
    }

    private String name;

    private String id;

    private boolean android;

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected DeveloperInfo(Parcel in) {
        name = in.readString();
        id = in.readString();
        android = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeByte((byte) (android ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeveloperInfo> CREATOR = new Parcelable.Creator<DeveloperInfo>() {
        @Override
        public DeveloperInfo createFromParcel(Parcel in) {
            return new DeveloperInfo(in);
        }

        @Override
        public DeveloperInfo[] newArray(int size) {
            return new DeveloperInfo[size];
        }
    };
}
