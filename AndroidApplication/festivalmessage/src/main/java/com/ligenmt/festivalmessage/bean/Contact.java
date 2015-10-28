package com.ligenmt.festivalmessage.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenov0 on 2015/10/7.
 */
public class Contact implements Parcelable{
    private String name;
    private String number;
    private boolean isSelected;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Parcelable.Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel parcel) {
            String name = parcel.readString();
            String number = parcel.readString();
            Contact contact = new Contact(name, number);
            return contact;
        }

        @Override
        public Contact[] newArray(int i) {
            return new Contact[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        String[] arr = new String[2];
//        arr[0] = name;
//        arr[1] = number;
//        parcel.writeStringArray(arr);
        parcel.writeString(name);
        parcel.writeString(number);
    }
}
