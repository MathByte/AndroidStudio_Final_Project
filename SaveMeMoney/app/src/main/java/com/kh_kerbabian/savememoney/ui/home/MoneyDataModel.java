package com.kh_kerbabian.savememoney.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class MoneyDataModel implements Parcelable {
    private String category;
    private String account;
    private double ammount;
    private String date;
    private String type;

    public MoneyDataModel(){

    }

    public MoneyDataModel( String category, String account, String ammount, String datee, String TyPE) {
        this.category = category;
        this.account = account;
        this.ammount = Double.valueOf(ammount);
        this.date = datee;
        this.type = TyPE;
    }

    protected MoneyDataModel(Parcel in) {
        category = in.readString();
        account = in.readString();
        ammount = Double.valueOf(in.readString());
        date = in.readString();
        type = in.readString();
    }

    public static final Creator<MoneyDataModel> CREATOR = new Creator<MoneyDataModel>() {
        @Override
        public MoneyDataModel createFromParcel(Parcel in) {
            return new MoneyDataModel(in);
        }

        @Override
        public MoneyDataModel[] newArray(int size) {
            return new MoneyDataModel[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(account);
        parcel.writeDouble(ammount);
        parcel.writeString(date);
        parcel.writeString(type);
    }
}
