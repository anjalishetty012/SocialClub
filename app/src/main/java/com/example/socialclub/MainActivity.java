
package com.example.socialclub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView btm_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getFragment(new home());

        


        btm_view=findViewById(R.id.bottom_view);
        btm_view.getMenu().getItem(0).setCheckable(false);
        btm_view.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                item.setCheckable(true);
                MainActivity.this.getFragment(new home());

            } else if (item.getItemId() == R.id.search_interests) {
                item.setCheckable(true);
                MainActivity.this.getFragment(new interests());
            } else if (item.getItemId() == R.id.profile) {
                item.setCheckable(true);
                MainActivity.this.getFragment(new Profile());
            }else if (item.getItemId() == R.id.chat) {
                item.setCheckable(true);
                startActivity(new Intent(MainActivity.this, Friendslist.class));
            }


            return true;
        });





    }

    private void getFragment(Fragment fragment) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=manager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();

    }










}

