package com.example.lielco.petlog;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements RegisterFragment.OnFragmentInteractionListener,LoginFragment.OnFragmentInteractionListener{
    LoginFragment LoginFrag;
    RegisterFragment registerFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        LoginFrag = new LoginFragment();
        tran.add(R.id.login_frag_container,LoginFrag);
        tran.commit();
    }

    @Override
    public void onLoginRequest() {
        Log.d("TAG","User asked to login");
        if (LoginFrag == null) {
            LoginFrag = new LoginFragment();
        }

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        LoginFrag = new LoginFragment();
        tran.replace(R.id.login_frag_container,LoginFrag);
        tran.commit();
    }

    @Override
    public void onForgotPw() {
        Log.d("TAG","User forgot p/w");

        // TODO: add support for password reset
    }

    @Override
    public void onRegisterRequest() {
        Log.d("TAG","User asked to register");
        if (registerFrag == null) {
            registerFrag = new RegisterFragment();
        }

        FragmentTransaction registerTran = getSupportFragmentManager().beginTransaction();
        registerTran.remove(LoginFrag);
        registerTran.replace(R.id.login_frag_container,registerFrag);
        registerTran.commit();
    }

    @Override
    public void onSuccessfulLogin() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}