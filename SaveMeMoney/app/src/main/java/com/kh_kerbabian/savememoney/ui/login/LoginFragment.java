package com.kh_kerbabian.savememoney.ui.login;

import static com.kh_kerbabian.savememoney.Notifications.Notifications.CHANNEL_1_ID;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_kerbabian.savememoney.DataMinipulationFirebase;
import com.kh_kerbabian.savememoney.IFirebaseMesseginig;
import com.kh_kerbabian.savememoney.OnGetDataListener;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentChartsBinding;
import com.kh_kerbabian.savememoney.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment implements IFirebaseMesseginig {

    private SharedPreferences sharedPref;
    String settingsDarkTheme = "xmlDarkTheme";
    String settingsNotification = "xmlNotification";
    String settingsVibration = "xmlVibration";






    private FragmentLoginBinding binding;
    private NotificationManagerCompat notificationManager;


    public LoginFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mAuth = FirebaseAuth.getInstance();
       // database =  FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notificationManager = NotificationManagerCompat.from(requireContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(DataMinipulationFirebase.getCurrentUser() == null){


        binding.butLogOut.setEnabled(false);
        binding.butLogin.setEnabled(true);
        binding.buttonAnonymous.setEnabled(true);
        binding.butCreateAcc.setEnabled(true);
        }
        else{
            binding.butLogOut.setEnabled(true);
            binding.butLogin.setEnabled(false);
            binding.buttonAnonymous.setEnabled(false);
            binding.butCreateAcc.setEnabled(false);
        }

        binding.buttonAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.idPBLoadinglogin.setVisibility(View.VISIBLE);
                DataMinipulationFirebase.getmAuth().signInAnonymously()
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                    updateUI(DataMinipulationFirebase.getmAuth().getCurrentUser());
                                else
                                    Toast.makeText(requireContext(), "Anonymously Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.butCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.idPBLoadinglogin.setVisibility(View.VISIBLE);
                DataMinipulationFirebase.getmAuth().createUserWithEmailAndPassword(binding.UserNameText.getText().toString(),binding.UserPasswordText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                    updateUI(DataMinipulationFirebase.getmAuth().getCurrentUser());
                                else
                                    Toast.makeText(requireContext(), "Registration failed.", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

        binding.butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.idPBLoadinglogin.setVisibility(View.VISIBLE);
                DataMinipulationFirebase.getmAuth().signInWithEmailAndPassword(binding.UserNameText.getText().toString(),binding.UserPasswordText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                    updateUI(DataMinipulationFirebase.getmAuth().getCurrentUser());
                                else
                                    Toast.makeText(requireContext(), "Login failed.", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

        binding.butLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataMinipulationFirebase.getmAuth().signOut();
                if(DataMinipulationFirebase.getmAuth().getCurrentUser() == null){
                    Toast.makeText(requireContext(), "Sign out is Complete", Toast.LENGTH_SHORT).show();


                    Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_money)
                            .setContentTitle("Save Me Money")
                            .setContentText("Logging out is complete!")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .build();

                    notificationManager.notify(1, notification);



                    DataMinipulationFirebase.setCurrentUser(null);
                    DataMinipulationFirebase.setDatabaseRef(null);


                    binding.butLogOut.setEnabled(false);
                    binding.butLogin.setEnabled(true);
                    binding.buttonAnonymous.setEnabled(true);
                    binding.butCreateAcc.setEnabled(true);
                }
                else
                {
                    Toast.makeText(requireContext(), "Sign out failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void updateUI(FirebaseUser user) {


        if( sharedPref.getBoolean(settingsNotification, false)){
            Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_money)
                    .setContentTitle("Save Me Money")
                    .setContentText("Logging in is complete!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManager.notify(1, notification);
        }


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







        DataMinipulationFirebase.setCurrentUser(user);
        String ref = DataMinipulationFirebase.getCurrentUser().getUid().toString();
        DataMinipulationFirebase.setDatabaseRef(DataMinipulationFirebase.getDatabase().getReference(ref));

        DataMinipulationFirebase.updateArray(new OnGetDataListener() {
            @Override
            public void onSuccess() {
                binding.idPBLoadinglogin.setVisibility(View.INVISIBLE);
                Toast.makeText(requireContext(), "Login is Complete", Toast.LENGTH_SHORT).show();
                binding.butLogOut.setEnabled(true);
                binding.butLogin.setEnabled(false);
                binding.buttonAnonymous.setEnabled(false);
                binding.butCreateAcc.setEnabled(false);

            }

        });


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

    @Override
    public void RunNotification() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
        if( sharedPref.getBoolean(settingsNotification, false)){
            Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_money)
                    .setContentTitle("Save Me Money")
                    .setContentText("Don't Forget to come back!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManager.notify(1, notification);
        }
    }
}