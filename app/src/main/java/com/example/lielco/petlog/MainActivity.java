package com.example.lielco.petlog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.lielco.petlog.Pet.Model.PetFirebase;
import com.example.lielco.petlog.Pet.Pet;
import com.example.lielco.petlog.Pet.PetDetailsActivity;
import com.example.lielco.petlog.Pet.PetGridFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements PetGridFragment.OnFragmentInteractionListener{
    PetGridFragment petGridFrag;
    FirebaseAuth fbAuth;
    static final int REQUEST_WRITE_STORAGE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() == null)
        {
            startLoginAct();
        }
        else {
            petGridFrag = (PetGridFragment) getSupportFragmentManager().findFragmentById(R.id.main_pet_grid_frag);
            fbAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        startLoginAct();
                    }
                }
            });
        }

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onPetSelected(String petId) {
        Log.d("TAG","pet selected: " + petId);
        Intent intent = new Intent(getApplicationContext(),PetDetailsActivity.class);
        intent.putExtra("petId",petId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.main_menu_logout:
                Log.d("TAG","User wants to log out");
                if (fbAuth == null)
                {
                    fbAuth = FirebaseAuth.getInstance();
                }

                fbAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }


    private void startLoginAct(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}