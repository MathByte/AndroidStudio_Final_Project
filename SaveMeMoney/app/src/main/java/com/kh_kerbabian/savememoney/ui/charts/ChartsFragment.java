package com.kh_kerbabian.savememoney.ui.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentChartsBinding;

import java.util.ArrayList;


public class ChartsFragment extends Fragment {

    private FragmentChartsBinding binding;
    private BarChart barchart;
    private BarData bardata;
    private BarDataSet bardataset;
    private ArrayList barentries;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // ChartsViewModel notificationsViewModel =
        //        new ViewModelProvider(this).get(ChartsViewModel.class);

        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textNotifications;
       // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();
        barchart = view.findViewById(R.id.barchart);
        bardataset = new BarDataSet(barentries,"dara set");
        bardata = new BarData(bardataset);

        barchart.setData(bardata);

        bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
        bardataset.setValueTextColor(Color.BLACK);
        bardataset.setValueTextSize(18f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }













    private void getData(){
        barentries = new ArrayList<>();
        barentries.add(new BarEntry(1f, 2));
    }
}