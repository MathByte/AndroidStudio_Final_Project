package com.kh_kerbabian.savememoney;

import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;

import java.util.ArrayList;

public interface OnGetDataListener {
    void onSuccess(ArrayList<MoneyDataModel> dataSnapshotValue);
}
