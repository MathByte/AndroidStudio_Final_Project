package com.kh_kerbabian.savememoney;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_kerbabian.savememoney.ui.dashboard.RowItemsAdapter;
import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DataMinipulationFirebase {

    public static ArrayList<MoneyDataModel> dataArray = new ArrayList<MoneyDataModel>();



    private static FirebaseUser currentUser;

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseRef;


    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(FirebaseUser currentUser) {
        DataMinipulationFirebase.currentUser = currentUser;
    }
    public static DatabaseReference getDatabaseRef() {
        return databaseRef;
    }

    public static void setDatabaseRef(DatabaseReference databaseRef) {
        DataMinipulationFirebase.databaseRef = databaseRef;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static ArrayList<MoneyDataModel> getAllExpenses(){
        ArrayList<MoneyDataModel> re = new ArrayList<MoneyDataModel>();

        for( MoneyDataModel m : dataArray)
            if(m.getType().equals("Expenses"))
                re.add(m);
        return  re;
    }


    public static Map<String, Float> getTotalExpenses_byCategory(Date sd, String type) throws ParseException {

        Map<String, Float> dic = new HashMap<String, Float>();


        Calendar calendarIndex = Calendar.getInstance();
        Calendar calendarModel = Calendar.getInstance();
        calendarIndex.setTime(sd);

        for (MoneyDataModel m : dataArray)
            if (m.getType().equals("Expenses") ){
                calendarModel.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m.getDate()));


                if(type.equals("Daily")) {
                    if (calendarIndex.get(Calendar.DAY_OF_MONTH) == calendarModel.get(Calendar.DAY_OF_MONTH))
                        if(dic.containsKey(m.getCategory())){
                            float re = dic.get(m.getCategory());
                            re += (float)m.getAmmount();
                            dic.put(m.getCategory(), re);
                        }
                    else
                        {
                            dic.put(m.getCategory(), (float)m.getAmmount());
                        }

                }

                else
                if(type.equals("Weekly")){
                    Calendar range = Calendar.getInstance();
                    range.setTime(sd);
                    range.add(Calendar.DATE, 7);

                    if(calendarModel.after(calendarIndex) && calendarModel.before(range))
                        if(dic.containsKey(m.getCategory())){
                            float re = dic.get(m.getCategory());
                            re += (float)m.getAmmount();
                            dic.put(m.getCategory(), re);
                        }
                        else
                        {
                            dic.put(m.getCategory(), (float)m.getAmmount());
                        }



                }
                else
                if(type.equals("Monthly")){
                    Calendar range = Calendar.getInstance();
                    range.setTime(sd);
                    range.add(Calendar.MONTH, 1);
                    if(calendarModel.after(calendarIndex) && calendarModel.before(range))
                        if(dic.containsKey(m.getCategory())){
                            float re = dic.get(m.getCategory());
                            re += (float)m.getAmmount();
                            dic.put(m.getCategory(), re);
                        }
                        else
                        {
                            dic.put(m.getCategory(), (float)m.getAmmount());
                        }


                }



            }

        return dic;
    }






    public static double getTotalExpenses(Date sd, String type) throws ParseException {
        double re = 0;
        Calendar calendarIndex = Calendar.getInstance();
        Calendar calendarModel = Calendar.getInstance();
        calendarIndex.setTime(sd);

        for (MoneyDataModel m : dataArray)
            if (m.getType().equals("Expenses") ){
                calendarModel.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m.getDate()));


                if(type.equals("Daily")) {
                    if (calendarIndex.get(Calendar.DAY_OF_MONTH) == calendarModel.get(Calendar.DAY_OF_MONTH))
                        re += m.getAmmount();
                }

                else
                    if(type.equals("Weekly")){
                        Calendar range = Calendar.getInstance();
                        range.setTime(sd);
                        range.add(Calendar.DATE, 7);

                        if(calendarModel.after(calendarIndex) && calendarModel.before(range))
                            re += m.getAmmount();


                    }
                    else
                        if(type.equals("Monthly")){
                            Calendar range = Calendar.getInstance();
                            range.setTime(sd);
                            range.add(Calendar.MONTH, 1);
                            if(calendarModel.after(calendarIndex) && calendarModel.before(range))
                                re += m.getAmmount();


                        }



            }

        return re;
    }


    public static double getTotalIncomes(Date sd, String type) throws ParseException {
        double re = 0;
        Calendar calendarIndex = Calendar.getInstance();
        Calendar calendarModel = Calendar.getInstance();
        calendarIndex.setTime(sd);

        for (MoneyDataModel m : dataArray)
            if (m.getType().equals("Income") ){
                calendarModel.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m.getDate()));


                if(type.equals("Daily")) {
                    if (calendarIndex.get(Calendar.DAY_OF_MONTH) == calendarModel.get(Calendar.DAY_OF_MONTH))
                        re += m.getAmmount();
                }
                else
                    if(type.equals("Weekly")){
                        Calendar range = Calendar.getInstance();
                        range.setTime(sd);
                        range.add(Calendar.DATE, 7);
                        if(calendarModel.after(calendarIndex) && calendarModel.before(range))
                            re += m.getAmmount();


                    }
                    else
                    if(type.equals("Monthly")){
                        Calendar range = Calendar.getInstance();
                        range.setTime(sd);
                        range.add(Calendar.MONTH, 1);
                        if(calendarModel.after(calendarIndex) && calendarModel.before(range))
                            re += m.getAmmount();


                    }

            }

        return re;
    }









    public static void updateArray(  OnGetDataListener  listener){

            dataArray.clear();


            if (currentUser != null) {


                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("snapshotKeys", snapshot.getKey().toString());
                        Log.d("snapshotValue", snapshot.getValue().toString());

                        for (DataSnapshot child : snapshot.child("Transactions").getChildren()) {
                            dataArray.add(child.getValue(MoneyDataModel.class));
                            Log.d("childKEY", child.getKey().toString());
                            Log.d("childValue", child.getValue().toString());
                        }

                        listener.onSuccess();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

    }

}
