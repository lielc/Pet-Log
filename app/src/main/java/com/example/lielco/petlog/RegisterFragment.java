package com.example.lielco.petlog;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lielco.petlog.User.LoginViewModel;
import com.example.lielco.petlog.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    private static EditText userPw, userEmail;
    private static Button btnRegister,btnForgotPw,btnGoToLogin;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private FirebaseAuth fbAuth;
    private LoginViewModel loginVM;

    public RegisterFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        userPw = view.findViewById(R.id.user_password);
        userEmail = view.findViewById(R.id.user_email);
        btnRegister = view.findViewById(R.id.register_btn);
        btnForgotPw = view.findViewById(R.id.forgot_pw_btn);
        btnForgotPw.setVisibility(View.GONE);

        btnGoToLogin = view.findViewById(R.id.go_to_login_btn);
        progressBar = view.findViewById(R.id.register_pb);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginRequest();
            }
        });
//        btnForgotPw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onForgotPw();
//            }
//        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final String email = userEmail.getText().toString().trim();
            String password = userPw.getText().toString().trim();

            //TODO: add validation

            progressBar.setVisibility(View.VISIBLE);
            fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error registering user", Toast.LENGTH_SHORT).show();
                        Log.d("TAG","Signup failed. Exception: " + task.getException().toString());
                    } else {
                        final User user = new User();
                        // Registering successfully automatically signs in the user
                        user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        user.setUserEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        loginVM.addNewUser(user, new LoginViewModel.Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                onSuccessfulLogin();
                            }

                            @Override
                            public void onFailure() {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Error registering user", Toast.LENGTH_SHORT).show();
                                onLoginRequest();
                            }
                        });
                    }
                }
            });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginVM = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onLoginRequest();
        void onForgotPw();
        void onSuccessfulLogin();
    }

    public void onLoginRequest(){
        mListener.onLoginRequest();
    }
    public void onForgotPw(){ mListener.onForgotPw(); }
    public void onSuccessfulLogin(){mListener.onSuccessfulLogin();}
}
