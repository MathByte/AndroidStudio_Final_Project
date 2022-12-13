package com.kh_kerbabian.savememoney.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_kerbabian.savememoney.DataMinipulationFirebase;
import com.kh_kerbabian.savememoney.OnGetDataListener;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentDashboardBinding;
import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;


import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment{

    private FragmentDashboardBinding binding;



    private RowItemsAdapter rowItemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.idPBLoadinghomedashboard.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) binding.RVmoneyiId;
        rowItemAdapter = new RowItemsAdapter( requireContext(), DataMinipulationFirebase.dataArray);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(rowItemAdapter);


        if(DataMinipulationFirebase.getCurrentUser() == null)
            Toast.makeText(requireContext(), "Please Login!", Toast.LENGTH_SHORT).show();
        else
        {
            rowItemAdapter.notifyDataSetChanged();

            //rowItemAdapter.notifyDataSetChanged();
            binding.idPBLoadinghomedashboard.setVisibility(View.INVISIBLE);

        }








    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }













}