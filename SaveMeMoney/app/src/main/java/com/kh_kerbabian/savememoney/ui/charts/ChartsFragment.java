package com.kh_kerbabian.savememoney.ui.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kh_kerbabian.savememoney.DataMinipulationFirebase;
import com.kh_kerbabian.savememoney.OnGetDataListener;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentChartsBinding;
import com.kh_kerbabian.savememoney.ui.dashboard.RowItemsAdapter;
import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChartsFragment extends Fragment {

    private FragmentChartsBinding binding;
    private BarChart barchart;
    private BarData bardata;
    private BarDataSet bardataset;
    private ArrayList barentries;

    private TextView TotalExpenses;
    private TextView TotalIncome;


    private TextView dd;
    private ImageButton forward;
    private ImageButton backward;
    private Calendar currentDate;
    private SimpleDateFormat formateInsance;

    private Spinner spinnerdates;
    private String selectedDateRange;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currentDate = Calendar.getInstance();

        formateInsance= new SimpleDateFormat("MMMM, YYYY");
        selectedDateRange = "Monthly";

        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textNotifications;
       // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dd = view.findViewById(R.id.DisplayDate);
        dd.setText(formateInsance.format(currentDate.getTime()));

        forward  = view.findViewById(R.id.action_bar_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (selectedDateRange){
                    case "Daily": {
                        currentDate.add(Calendar.DAY_OF_MONTH, 1);
                        getTotals( currentDate.getTime(), "Daily");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Weekly": {
                        currentDate.add(Calendar.DAY_OF_MONTH, 7);
                        getTotals( currentDate.getTime(), "Weekly");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Monthly":{
                        currentDate.add(Calendar.MONTH, 1);
                        getTotals( currentDate.getTime(), "Monthly");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Yearly":{
                        currentDate.add(Calendar.YEAR, 1);
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                }

            }
        });

        backward  = view.findViewById(R.id.action_bar_back);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selectedDateRange){
                    case "Daily": {
                        currentDate.add(Calendar.DAY_OF_MONTH, -1);
                        getTotals( currentDate.getTime(), "Daily");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Weekly": {
                        currentDate.add(Calendar.DAY_OF_MONTH, -7);
                        getTotals( currentDate.getTime(), "Weekly");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Monthly":{
                        currentDate.add(Calendar.MONTH, -1);
                        getTotals( currentDate.getTime(), "Monthly");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Yearly":{
                        currentDate.add(Calendar.YEAR, -1);
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                }


            }
        });

        spinnerdates = view.findViewById(R.id.spinnerDateSelection);

        String[] dateselect = getResources().getStringArray(R.array.DatesSelection);
        ArrayAdapter<String> dateselectionAdapter = new ArrayAdapter<String>(requireContext(),
                androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                dateselect);
        dateselectionAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerdates.setAdapter(dateselectionAdapter);


        spinnerdates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDateRange = adapterView.getSelectedItem().toString();
                switch (selectedDateRange){
                    case "Daily": {
                        formateInsance= new SimpleDateFormat("dd, MMMM");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }

                    case "Weekly": {
                        formateInsance= new SimpleDateFormat("dd, MMMM");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Monthly":{
                        formateInsance= new SimpleDateFormat("MMMM, YYYY");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }
                    case "Yearly":{
                        formateInsance= new SimpleDateFormat("YYYY");
                        dd.setText(formateInsance.format(currentDate.getTime()));
                        break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });















        getData();
        barchart = view.findViewById(R.id.barchart);


        bardataset = new BarDataSet(barentries,"dara set");
        bardata = new BarData(bardataset);

        barchart.setData(bardata);

        bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
        bardataset.setValueTextColor(Color.BLACK);
        bardataset.setValueTextSize(18f);


        TotalExpenses = view.findViewById(R.id.textTotalEx);
        TotalIncome = view.findViewById(R.id.textTotalIn);

        getTotals(new Date(), "Daily");


    }


    private void getTotals(Date sd, String rang){
        if(DataMinipulationFirebase.getCurrentUser() != null){

            DataMinipulationFirebase.updateArray(new OnGetDataListener() {
                @Override
                public void onSuccess(ArrayList<MoneyDataModel> dataSnapshotValue) {
                    try {
                        //new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-10")
                        TotalExpenses.setText(String.valueOf(DataMinipulationFirebase.getTotalExpenses(sd, rang)));
                        TotalIncome.setText(String.valueOf(DataMinipulationFirebase.getTotalIncomes(sd, rang)));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
        else
        {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<BarEntry> barEntries(){
        ArrayList<BarEntry> barEntriess = new ArrayList<BarEntry>();

        //barEntriess.add();


        return barEntriess;
    }
    private void getData(){
        barentries = new ArrayList<>();
        barentries.add(new BarEntry(1f, 2));
        barentries.add(new BarEntry(1.5f, 4));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }








}