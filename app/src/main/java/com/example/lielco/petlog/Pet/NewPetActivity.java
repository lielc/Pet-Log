package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lielco.petlog.R;

import java.util.Random;

public class NewPetActivity extends AppCompatActivity {
    static NewPetViewModel NewPetVM;
            private Integer[] mThumbIds = {
                R.drawable.cat_001,
                R.drawable.dog_001,
                R.drawable.dog_002,
                R.drawable.rabbit_001};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);

        NewPetVM = ViewModelProviders.of(this).get(NewPetViewModel.class);

        final EditText etPetName = findViewById(R.id.new_pet_name_field);

        Button btnConfirm = findViewById(R.id.new_pet_confirm_btn);

        //TODO: add cancel functionality
        Button btnCancel = findViewById(R.id.new_pet_cancel_btn);

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Pet newPet = new Pet(etPetName.getText().toString(),String.valueOf(mThumbIds[new Random().nextInt(mThumbIds.length)]));
                NewPetVM.addNewPet(newPet);
                setResult(RESULT_OK);
                finish();

//                // get the text from the EditText
//                EditText editText = (EditText) findViewById(R.id.editText);
//                String stringToPassBack = editText.getText().toString();
//
//                // put the String to pass back into an Intent and close this activity
//                Intent intent = new Intent();
//                intent.putExtra("keyName", stringToPassBack);
//                setResult(RESULT_OK, intent);
//                finish();

            }
        });
    }
}