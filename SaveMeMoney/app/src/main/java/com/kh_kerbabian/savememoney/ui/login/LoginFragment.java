package com.kh_kerbabian.savememoney.ui.login;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.databinding.FragmentChartsBinding;
import com.kh_kerbabian.savememoney.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    private FragmentLoginBinding binding;



    public LoginFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.butLogOut.setEnabled(false);
        binding.butLogin.setEnabled(true);
        binding.buttonAnonymous.setEnabled(true);
        binding.butCreateAcc.setEnabled(true);

        binding.buttonAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(requireContext(), "Anonymously Authentication is Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    binding.butLogOut.setEnabled(true);
                                    binding.butLogin.setEnabled(false);
                                    binding.buttonAnonymous.setEnabled(false);
                                    binding.butCreateAcc.setEnabled(false);
                                }
                                else
                                {

                                    Toast.makeText(requireContext(), "Anonymously Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        binding.butCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(binding.UserNameText.getText().toString(),binding.UserPasswordText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(requireContext(), "Registration is Complete", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(requireContext(), "Sign in is Complete", Toast.LENGTH_SHORT).show();
                                    binding.butLogOut.setEnabled(true);
                                    binding.butLogin.setEnabled(false);
                                    binding.buttonAnonymous.setEnabled(false);
                                    binding.butCreateAcc.setEnabled(false);
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Registration failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInWithEmailAndPassword(binding.UserNameText.getText().toString(),binding.UserPasswordText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(requireContext(), "Login is Complete", Toast.LENGTH_SHORT).show();
                                    binding.butLogOut.setEnabled(true);
                                    binding.butLogin.setEnabled(false);
                                    binding.buttonAnonymous.setEnabled(false);
                                    binding.butCreateAcc.setEnabled(false);
                                }
                                else
                                {
                                    Toast.makeText(requireContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.butLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                if(mAuth.getCurrentUser() == null){
                    Toast.makeText(requireContext(), "Sign out is Complete", Toast.LENGTH_SHORT).show();
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
    }
}