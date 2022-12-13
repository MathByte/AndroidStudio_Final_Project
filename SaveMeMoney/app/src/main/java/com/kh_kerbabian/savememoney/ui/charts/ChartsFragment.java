package com.kh_kerbabian.savememoney.ui.charts;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kh_kerbabian.savememoney.DataMinipulationFirebase;
import com.kh_kerbabian.savememoney.OnGetDataListener;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentChartsBinding;
import com.kh_kerbabian.savememoney.ui.dashboard.RowItemsAdapter;
import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ChartsFragment extends Fragment {

    private FragmentChartsBinding binding;


    private BarChart barchart;
    private PieChart piechart;


    private TextView TotalExpenses;
    private TextView TotalIncome;


    private TextView displayDate;
    private ImageButton forward;
    private ImageButton backward;
    private Calendar myCalendar;
    private SimpleDateFormat formateInsance;

    private Spinner spinnerdates;
    private String selectedDateRange;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {   

        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();    
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myCalendar = Calendar.getInstance();

        formateInsance= new SimpleDateFormat("yyyy-MM-dd");
        selectedDateRange = "Daily";


        TotalExpenses = view.findViewById(R.id.textTotalEx);
        TotalIncome = view.findViewById(R.id.textTotalIn);
        displayDate = view.findViewById(R.id.DisplayDate);
        barchart = view.findViewById(R.id.barchart);
        piechart = view.findViewById(R.id.piechart1);
        forward  = view.findViewById(R.id.action_bar_forward);
        backward  = view.findViewById(R.id.action_bar_back);
        spinnerdates = view.findViewById(R.id.spinnerDateSelection);


        displayDate.setText(formateInsance.format(myCalendar.getTime()));





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
                        selectedDateRange = "Daily";
                        //formateInsance= new SimpleDateFormat("dd, MMMM");
                        getTotalsExnIN(  myCalendar.getTime(), "Daily");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Weekly": {
                        selectedDateRange = "Weekly";
                        //formateInsance= new SimpleDateFormat("dd, MMMM");
                        getTotalsExnIN(  myCalendar.getTime(), "Weekly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Monthly":{
                        selectedDateRange = "Monthly";
                        //formateInsance= new SimpleDateFormat("MMMM, YYYY");
                        getTotalsExnIN(  myCalendar.getTime(), "Monthly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Yearly":{
                        selectedDateRange = "Yearly";
                        //formateInsance= new SimpleDateFormat("YYYY");
                        getTotalsExnIN(  myCalendar.getTime(), "Yearly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }

                }

                updateBarChart();
                updatePieChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });










        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                displayDate.setText(formateInsance.format(myCalendar.getTime()));
                updateBarChart();
                updatePieChart();
                getTotalsExnIN(myCalendar.getTime(),selectedDateRange);
            }
        };




        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(requireContext(), datePicker, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });











        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (selectedDateRange){
                    case "Daily": {
                        myCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        getTotalsExnIN( myCalendar.getTime(), "Daily");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Weekly": {
                        myCalendar.add(Calendar.DAY_OF_MONTH, 7);
                        getTotalsExnIN( myCalendar.getTime(), "Weekly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Monthly":{
                        myCalendar.add(Calendar.MONTH, 1);
                        getTotalsExnIN( myCalendar.getTime(), "Monthly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Yearly":{
                        myCalendar.add(Calendar.YEAR, 1);
                        getTotalsExnIN( myCalendar.getTime(), "Yearly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                }

                updateBarChart();
                updatePieChart();

            }
        });


        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selectedDateRange){
                    case "Daily": {
                        myCalendar.add(Calendar.DAY_OF_MONTH, -1);
                        getTotalsExnIN(  myCalendar.getTime(), "Daily");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Weekly": {
                        myCalendar.add(Calendar.DAY_OF_MONTH, -7);
                        getTotalsExnIN( myCalendar.getTime(), "Weekly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Monthly":{
                        myCalendar.add(Calendar.MONTH, -1);
                        getTotalsExnIN( myCalendar.getTime(), "Monthly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                    case "Yearly":{
                        myCalendar.add(Calendar.YEAR, -1);
                        getTotalsExnIN( myCalendar.getTime(), "Yearly");
                        displayDate.setText(formateInsance.format(myCalendar.getTime()));
                        break;
                    }
                }

                updateBarChart();
                updatePieChart();
            }
        });









        setupPieChart();





        //updateChart();
    }



    private void setupPieChart(){
        piechart.setDrawHoleEnabled(true);
        piechart.setUsePercentValues(true);
        piechart.setEntryLabelTextSize(12f);
        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setCenterText("Spending by Category");
        piechart.setCenterTextSize(18);
        piechart.getDescription().setEnabled(false);

        Legend l = piechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }


    private void updatePieChart(){
        Date d = myCalendar.getTime();
        ArrayList<PieEntry> pieDataEntry = PieEntries_Expensese_Bycategory(d,selectedDateRange);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color : ColorTemplate.MATERIAL_COLORS)
            colors.add(color);
        for(int color : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(color);

        PieDataSet dataset = new PieDataSet(pieDataEntry, "Expenses Category");
        dataset.setColors(colors);

        PieData data = new PieData(dataset);
        data.setDrawValues(true);
        //data.setValueFormatter(new PercentFormatter(piechart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        piechart.setData(data);
        piechart.invalidate();


    }

    private ArrayList<PieEntry> PieEntries_Expensese_Bycategory(Date startdate, String selectedRange){
            ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startdate);

        if(DataMinipulationFirebase.getCurrentUser() != null){

            try {
                Map<String, Float> map = new HashMap<String, Float>();
                map = DataMinipulationFirebase.getTotalExpenses_byCategory(cal.getTime(), selectedRange);

                for (Map.Entry<String,Float> entry : map.entrySet())
                    pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));

            }
            catch (Exception e)
            {

            }
        }
        else
        {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_SHORT).show();
        }

            return pieEntries;
    }




    private void updateBarChart(){



        Date d = myCalendar.getTime();
        BarDataSet bardatasetExpenses = new BarDataSet(barEntries_Expensese(d, selectedDateRange),"Expenses dara set");
        bardatasetExpenses.setColor(Color.RED);
        BarDataSet bardatasetIncome = new BarDataSet(barEntries_Income(d, selectedDateRange),"Income dara set");
        bardatasetIncome.setColor(Color.BLUE);

        BarData bardata = new BarData(bardatasetExpenses,bardatasetIncome );
        barchart.setData(bardata);

        String [] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        XAxis xaxis = barchart.getXAxis();
        //xaxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xaxis.setCenterAxisLabels(true);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1);
        xaxis.setGranularityEnabled(true);

        barchart.setDragEnabled(true);
        barchart.setVisibleXRangeMaximum(3);

        float barSpace = 0.01f;
        float groupSpace = 0.9f;
        bardata.setBarWidth(0.10f);


        barchart.getXAxis().setAxisMinimum(0);
        barchart.getXAxis().setAxisMaximum(barchart.getBarData().getGroupWidth(groupSpace, barSpace) * 7);
        barchart.getAxisLeft().setAxisMinimum(0);

        barchart.groupBars(0, groupSpace, barSpace);

        barchart.invalidate();

    }













    private ArrayList<BarEntry> barEntries_Expensese(Date startdate, String selectedRange){
        ArrayList<BarEntry> barEntriess = new ArrayList<BarEntry>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startdate);

        if(DataMinipulationFirebase.getCurrentUser() != null){
            switch (selectedRange){
                case "Daily":
                {
                    try {
                        barEntriess.add(new BarEntry(0, (float)DataMinipulationFirebase.getTotalExpenses(cal.getTime(), "Daily")));
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                }
                case "Weekly":
                {

                        try {
                            for(int i = 0; i < 7; i++){
                                barEntriess.add(new BarEntry(i, (float)DataMinipulationFirebase.getTotalExpenses(cal.getTime(), "Daily")));
                                cal.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        catch (Exception e)
                        {

                        }

                    break;
                }
                case "Monthly":
                {

                    try {
                        for(int i = 0; i < 30; i++){
                            barEntriess.add(new BarEntry(i, (float)DataMinipulationFirebase.getTotalExpenses(cal.getTime(), "Daily")));
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                    catch (Exception e)
                    {

                    }

                    break;
                }

            }
        }
        else
        {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_SHORT).show();
        }

        return barEntriess;
    }







    private ArrayList<BarEntry> barEntries_Income(Date startdate, String selectedRange){
        ArrayList<BarEntry> barEntriess = new ArrayList<BarEntry>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startdate);

        if(DataMinipulationFirebase.getCurrentUser() != null){
            switch (selectedRange){
                case "Daily":
                {
                    try {
                        barEntriess.add(new BarEntry(0, (float)DataMinipulationFirebase.getTotalIncomes(cal.getTime(), "Daily")));
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                }
                case "Weekly":
                {
                    try {
                        for(int i = 0; i < 7; i++){
                            barEntriess.add(new BarEntry(i, (float)DataMinipulationFirebase.getTotalIncomes(cal.getTime(), "Daily")));
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                }
                case "Monthly":
                {
                    try {
                        for(int i = 0; i < 30; i++){
                            barEntriess.add(new BarEntry(i, (float)DataMinipulationFirebase.getTotalIncomes(cal.getTime(), "Daily")));
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                }
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_SHORT).show();
        }
        return barEntriess;
    }





    private void getTotalsExnIN(Date sd, String rang) {
        if(DataMinipulationFirebase.getCurrentUser() != null){
            try {
                TotalExpenses.setText(String.valueOf(DataMinipulationFirebase.getTotalExpenses(sd, rang)));
                TotalIncome.setText(String.valueOf(DataMinipulationFirebase.getTotalIncomes(sd, rang)));
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_SHORT).show();
        }
    }








    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }








}