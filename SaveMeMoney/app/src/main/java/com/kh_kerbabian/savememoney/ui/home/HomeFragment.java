package com.kh_kerbabian.savememoney.ui.home;

import static com.kh_kerbabian.savememoney.Notifications.Notifications.CHANNEL_1_ID;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_kerbabian.savememoney.DataMinipulationFirebase;
import com.kh_kerbabian.savememoney.OnGetDataListener;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {


    private Calendar calender;
    private SimpleDateFormat formateInsance;

    private String currentTmedate;
    private String currentTransaction = "Expenses";
    private NotificationManagerCompat notificationManager;



    private SharedPreferences sharedPref;
    String settingsDarkTheme = "xmlDarkTheme";
    String settingsNotification = "xmlNotification";
    String settingsVibration = "xmlVibration";








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
        notificationManager = NotificationManagerCompat.from(requireContext());
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //  NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        String[] accounts = getResources().getStringArray(R.array.AccountsStrings);
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(requireContext(),
                R.layout.spinner_list,
                accounts);
        accountAdapter.setDropDownViewResource(R.layout.spinner_list);
        binding.spinnerAccount.setAdapter(accountAdapter);


        String[] category = getResources().getStringArray(R.array.CategoryStrings);
        ArrayAdapter<String>categoryAdapter = new ArrayAdapter<String>(requireContext(),
                R.layout.spinner_list,
                category);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_list);

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
                currentTransaction = "Expenses";
                binding.textViewEI.setText(currentTransaction);
            }
        });

        binding.fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                binding.fabIncome.hide();
                binding.fabExpence.hide();
                binding.spinnerCategory.setEnabled(false);


                currentTransaction = "Income";
                binding.textViewEI.setText(currentTransaction);

            }
        });



        binding.ButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.idPBLoadinghomedashboard.setVisibility(View.VISIBLE);
                if(DataMinipulationFirebase.getCurrentUser() != null)
                {
                    currentTmedate = formateInsance.format(new Date()).toString();


                    if (!binding.spinnerAccount.getSelectedItem().toString().isEmpty()
                        && !binding.spinnerCategory.getSelectedItem().toString().isEmpty()
                        && !binding.editTextNumber.getText().toString().isEmpty()
                    )
                    {
                       /// String ref = DataMinipulationFirebase.getCurrentUser().getUid().toString();
                       /// DataMinipulationFirebase.setDatabaseRef(DataMinipulationFirebase.getDatabase().getReference(ref));


                        MoneyDataModel moneydata = new MoneyDataModel(
                                binding.spinnerCategory.getSelectedItem().toString(),
                                binding.spinnerAccount.getSelectedItem().toString(),
                                binding.editTextNumber.getText().toString(),
                                currentTmedate,
                                currentTransaction);

                        DataMinipulationFirebase.getDatabaseRef().addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DataMinipulationFirebase.getDatabaseRef()
                                        .child("Transactions")
                                        .child(moneydata.getDate())
                                        .setValue(moneydata);
                                Snackbar snackbar = Snackbar
                                        .make(view, "Record has been added!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                binding.idPBLoadinghomedashboard.setVisibility(View.INVISIBLE);
                                DataMinipulationFirebase.updateArray(new OnGetDataListener() {
                                    @Override
                                    public void onSuccess() {
                                        binding.idPBLoadinghomedashboard.setVisibility(View.INVISIBLE);


                                        if( sharedPref.getBoolean(settingsVibration, false)){

                                            AppCompatActivity activity = (AppCompatActivity) getActivity();

                                            Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //deprecated in API 26
                                                v.vibrate(500);
                                            }
                                        }




                                        if( sharedPref.getBoolean(settingsNotification, false))
                                            if(currentTransaction.equals("Expenses")) {
                                                Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)

                                                        .setContentTitle("Save Me Money")
                                                        .setContentText(currentTransaction + " added successfully!")
                                                            .setSmallIcon(R.drawable.ic_down_icon)
                                                        .setColor(Color.RED)
                                                        .setPriority(NotificationCompat.PRIORITY_LOW)
                                                        .build();

                                                notificationManager.notify(1, notification);
                                            }
                                            else
                                            {
                                                Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)

                                                        .setContentTitle("Save Me Money")
                                                        .setContentText(currentTransaction + " added successfully!")
                                                              .setSmallIcon(R.drawable.ic_up_icon)
                                                        .setColor(Color.GREEN)

                                                        .setPriority(NotificationCompat.PRIORITY_LOW)
                                                        .build();

                                                notificationManager.notify(1, notification);
                                            }





                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Snackbar snackbar = Snackbar
                                        .make(view, "Can't add data!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                             
                                binding.idPBLoadinghomedashboard.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                    else

                    {
                        binding.idPBLoadinghomedashboard.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    binding.idPBLoadinghomedashboard.setVisibility(View.INVISIBLE);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
        int[] values = new int[2];
        values[0] = sharedPref.getBoolean(settingsDarkTheme, false) == true ? 1 : 0;
        values[1] = sharedPref.getBoolean(settingsNotification, false) == true ? 1 : 0;
        if( sharedPref.getBoolean(settingsDarkTheme, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



    }

}