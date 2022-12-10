package com.kh_kerbabian.savememoney.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {


    private Calendar calender;
    private SimpleDateFormat formateInsance;

    private String currentTmedate;

    private FragmentHomeBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database =  FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        calender = Calendar.getInstance();
        formateInsance= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //  NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        String[] accounts = getResources().getStringArray(R.array.AccountsStrings);
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(requireContext(),
                androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                accounts);
        accountAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.spinnerAccount.setAdapter(accountAdapter);


        String[] category = getResources().getStringArray(R.array.CategoryStrings);
        ArrayAdapter<String>categoryAdapter = new ArrayAdapter<String>(requireContext(),
                androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                category);
        categoryAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(categoryAdapter);





        binding.fabIncome.hide();
        binding.fabExpence.hide();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();

                binding.fabIncome.show();
                binding.fabExpence.show();
            }
        });

        binding.fabExpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();

                binding.fabIncome.hide();
                binding.fabExpence.hide();
                binding.spinnerCategory.setEnabled(true);
                binding.textViewEI.setText("Expenses");
            }
        });

        binding.fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                binding.fabIncome.hide();
                binding.fabExpence.hide();
                binding.spinnerCategory.setEnabled(false);

                binding.textViewEI.setText("Income");

            }
        });



        binding.ButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.idPBLoading.setVisibility(View.VISIBLE);

                if(mAuth.getCurrentUser() != null) {
                    currentTmedate = formateInsance.format(new Date()).toString();


                    if (!binding.spinnerAccount.getSelectedItem().toString().isEmpty()
                        && !binding.spinnerCategory.getSelectedItem().toString().isEmpty()
                        && !binding.editTextNumber.getText().toString().isEmpty()
                    )
                    {
                        String ref = mAuth.getCurrentUser().getUid().toString();
                        databaseRef = database.getReference(ref);


                        MoneyDataModel moneydata = new MoneyDataModel(
                                binding.spinnerCategory.getSelectedItem().toString(),
                                binding.spinnerAccount.getSelectedItem().toString(),
                                binding.editTextNumber.getText().toString(),
                                currentTmedate,
                                binding.textViewEI.getText().toString());

                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseRef
                                        .child("Transactions")
                                        .child(moneydata.getDate())
                                        .setValue(moneydata);
                                Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(requireContext(), "Cant add data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else

                    {
                        binding.idPBLoading.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireContext(), "Must not be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    binding.idPBLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), "Must be logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}