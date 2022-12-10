package com.kh_kerbabian.savememoney.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentDashboardBinding;
import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment{

    private FragmentDashboardBinding binding;

    private TextView dd;
    private ImageButton forward;
    private ImageButton backward;
    private Calendar currentDate;
    private SimpleDateFormat formateInsance;
    private ArrayList<MoneyDataModel> MylistData ;
    private RowItemsAdapter rowItemAdapter;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDate = Calendar.getInstance();
        formateInsance= new SimpleDateFormat("MMMM, YYYY");
        MylistData = new ArrayList<MoneyDataModel>();

        database =  FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        dd = root.findViewById(R.id.DisplayDate);
        dd.setText(formateInsance.format(currentDate.getTime()));

        forward  = root.findViewById(R.id.action_bar_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate.add(Calendar.MONTH, 1);
                dd.setText(formateInsance.format(currentDate.getTime()));
            }
        });

        backward  = root.findViewById(R.id.action_bar_back);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate.add(Calendar.MONTH, -1);
                dd.setText(formateInsance.format(currentDate.getTime()));
            }
        });




        RecyclerView recyclerView = (RecyclerView) binding.RVmoneyiId;
        rowItemAdapter = new RowItemsAdapter( requireContext(),MylistData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(rowItemAdapter);

        getAllTransactions();


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }










    private void getAllTransactions() {
        MylistData.clear();

        if (mAuth.getCurrentUser() != null) {
            String ref = mAuth.getCurrentUser().getUid().toString();
            databaseRef = database.getReference(ref);

            databaseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    Log.d("snapshotKeys", snapshot.getKey().toString());
                    Log.d("snapshotValue", snapshot.getValue().toString());

                    for (DataSnapshot child : snapshot.getChildren()) {
                        MylistData.add(child.getValue(MoneyDataModel.class));
                        Log.d("childKEY", child.getKey().toString());
                        Log.d("childValue", child.getValue().toString());
                    }
                    rowItemAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    //ReadItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    //ReadItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // ReadItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(requireContext(), "Please Sing in!!", Toast.LENGTH_SHORT).show();
        }

    }



}