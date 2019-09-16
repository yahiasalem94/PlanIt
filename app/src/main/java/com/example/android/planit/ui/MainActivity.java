package com.example.android.planit.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.android.planit.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);

//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = findViewById(R.id.nav_view );
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch(id)
//                {
//                    case R.id.home:
//                        Toast.makeText(MainActivity.this, "Home",Toast.LENGTH_SHORT).show();break;
//                    case R.id.near_me:
//                        Toast.makeText(MainActivity.this, "Near me",Toast.LENGTH_SHORT).show();break;
//                    case R.id.ar:
//                        Toast.makeText(MainActivity.this, "AR",Toast.LENGTH_SHORT).show();break;
//                    case R.id.my_bucket_lists:
//                        Toast.makeText(MainActivity.this, "Bucket Lists",Toast.LENGTH_SHORT).show();break;
//                    case R.id.my_calendar:
//                        Toast.makeText(MainActivity.this, "My Calendar",Toast.LENGTH_SHORT).show();break;
//                    case R.id.settings:
//                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
//                    default:
//                        return true;
//                }
//
//                return true;
//            }
//        });

        setupNavigation();
    }



    // Setting Up One Time Navigation
    private void setupNavigation() {


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        int id = menuItem.getItemId();

        switch (id) {

            case R.id.home:
                navController.navigate(R.id.homeFragment);
                break;

            case R.id.my_bucket_lists:
                navController.navigate(R.id.bucketListFragment);
                break;

        }
        return true;

    }


}