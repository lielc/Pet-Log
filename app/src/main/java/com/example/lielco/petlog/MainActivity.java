package com.example.lielco.petlog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.lielco.petlog.Pet.PetDetailsActivity;
import com.example.lielco.petlog.Pet.PetGridFragment;

public class MainActivity extends AppCompatActivity implements PetGridFragment.OnFragmentInteractionListener{
    PetGridFragment petGridFrag;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.main_menu_add_pet:
//                Log.d("TAG", "Add enu selected");
//                break;
//            default:
//                    return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petGridFrag = (PetGridFragment) getSupportFragmentManager().findFragmentById(R.id.main_pet_grid_frag);
    }

    @Override
    public void onPetSelected(int position) {
        Log.d("TAG","pet selected: " + position);
        Intent intent = new Intent(getApplicationContext(),PetDetailsActivity.class);
        intent.putExtra("petPos",position);
        startActivity(intent);
    }
}
