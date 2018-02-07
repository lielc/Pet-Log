package com.example.lielco.petlog.Pet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lielco.petlog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class PetDetailsActivity extends AppCompatActivity implements PetInfoFragment.OnFragmentInteractionListener,PetDetailsHeaderFragment.onFragmentInteractionListener,PetDetailsNavFragment.onFragmentInteractionListener{
    private final String PET_ID = "petId";
    private final int EDIT_PET_REQUEST_CODE = 0;
    private static String petId;
    private Pet displayedPet;
    PetDetailsHeaderFragment headerFrag;
    PetDetailsNavFragment navFrag;
    private PetDetailsViewModel petDetailsVM;
    private  ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        progressBar = findViewById(R.id.pet_details_pb);

        Intent intent = getIntent();
        petId = intent.getExtras().getString(PET_ID);

        PetDetailsViewModel.Factory factory = new PetDetailsViewModel.Factory(petId);
        petDetailsVM = ViewModelProviders.of(this,factory).get(PetDetailsViewModel.class);

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        headerFrag = PetDetailsHeaderFragment.newInstance(petId);
        navFrag = PetDetailsNavFragment.newInstance(petId);

        tran.add(R.id.details_header_frag,headerFrag);
        tran.add(R.id.details_content_frag,navFrag);
        tran.commit();
    }

    @Override
    public void showInfoFrag(String petId) {
        PetInfoFragment infoFragment = PetInfoFragment.newInstance(petId);
        //headerFrag.changeInfoVisibility(false);
        FragmentTransaction showInfoTran = getSupportFragmentManager().beginTransaction();
        showInfoTran.replace(R.id.details_content_frag,infoFragment);
        showInfoTran.addToBackStack("NavToInfo");
        showInfoTran.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pet_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.details_menu_logout:
                Log.d("TAG","User wants to log out");
                FirebaseAuth.getInstance().signOut();
                break;
            case R.id.details_menu_delete_pet:
                Log.d("TAG","User wants to delete pet");
                break;
            case R.id.details_menu_edit_pet:
                Log.d("TAG","User wants to edit pet");
                Intent intent = new Intent(this,EditPetActivity.class);
                intent.putExtra("petId",petId);
                startActivityForResult(intent,EDIT_PET_REQUEST_CODE);
                break;
            case R.id.details_menu_share_pet:
                showShareDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShareDialog(){
        final EditText emailAddress = new EditText(this);
        emailAddress.setHint("Email Address");

        new AlertDialog.Builder(this)
                .setTitle("Pet Sharing")
                .setMessage("Enter E-mail address to share with")
                .setView(emailAddress)
                .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        Log.d("TAG", "pet share: user wants to share with " + emailAddress.getText());
                        if (!(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(emailAddress.getText().toString()))) {
                            FirebaseAuth.getInstance().fetchProvidersForEmail(emailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        ProviderQueryResult result = task.getResult();
                                        if (result != null
                                                && result.getProviders() != null
                                                && result.getProviders().size() > 0) {
                                            sharePet(emailAddress.getText().toString(),petId);
                                        } else {
                                            Log.d("TAG", "task fetchProvidersForEmail successful. user doesn't exist");
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(PetDetailsActivity.this, "Requested user does not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("TAG", "task fetchProvidersForEmail unsuccessful. " + task.getException().getMessage());
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(PetDetailsActivity.this, "Problem looking for user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(PetDetailsActivity.this, "Cannot use current user's email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("TAG","user canceled");
                    }
                })
                .show();
    }

    private void sharePet(String emailAddress, final String petId){
        petDetailsVM.getUserIdByEmail(emailAddress, new PetDetailsViewModel.ResultsCallback() {
            @Override
            public void onSuccess(Object data) {
                petDetailsVM.addPetPermissions(data.toString(), petId, new PetDetailsViewModel.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PetDetailsActivity.this, "Shared pet successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Log.d("TAG","Failed sharing pet");
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PetDetailsActivity.this, "Failed sharing pet", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PET_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
            }

            //TODO: handle failed
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
