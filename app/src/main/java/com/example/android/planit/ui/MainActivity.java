package com.example.android.planit.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android.planit.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    public Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public AppBarLayout appBarLayout;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.detail_toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view );

        setupNavigation();
    }


    // Setting Up One Time Navigation
    private void setupNavigation() {

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

         appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.homeFragment, R.id.bucketListFragment,
                 R.id.myCalendarFragment).setDrawerLayout(drawerLayout).build();


//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(collapsingToolbarLayout, toolbar, navController, appBarConfiguration);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);


    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        if ( navController.getCurrentDestination().getId() == R.id.bestThingsTodoFragment) {
//            appBarLayout.setExpanded(false);
//        }
//        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), appBarConfiguration);
//    }


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

        int id = menuItem.getItemId();
        appBarLayout.setExpanded(false);

        switch (id) {

            case R.id.home:
                navController.navigate(R.id.homeFragment);
                break;

            case R.id.my_bucket_lists:
                navController.navigate(R.id.bucketListFragment);
                break;

            case R.id.my_calendar:
                navController.navigate(R.id.myCalendarFragment);
                break;
        }
        drawerLayout.closeDrawers();
        return true;

    }
}