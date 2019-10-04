package com.example.android.planit.ui;

import android.app.Fragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android.planit.R;
import com.example.android.planit.utils.ConnectionReceiver;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ConnectionReceiver.NetworkStateReceiverListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    public Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public AppBarLayout appBarLayout;
    private NavController navController;
    public NestedScrollView nestedScrollView;

    private ConnectionReceiver mConnectionReciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectionReciever = new ConnectionReceiver();
        mConnectionReciever.addListener(this);
        registerReceiver(mConnectionReciever,new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.appbar);
        nestedScrollView = findViewById(R.id.scrollView);

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view );

        setupNavigation();

        checkConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnectionReciever.removeListener(this);
        unregisterReceiver(mConnectionReciever);
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

        switch (id) {

            case R.id.home:
                navController.navigate(R.id.homeFragment);
                break;

            case R.id.near_me:
                navController.navigate(R.id.nearByFragment);
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

    private void checkConnection() {

    }

    @Override
    public void networkAvailable(boolean isAvailable) {
        NoConnectionDialog dialogFragment = new NoConnectionDialog();
        if(!isAvailable) {

            //show a No Internet Alert or Dialog
            dialogFragment.setCancelable(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.addToBackStack(null);

            dialogFragment.show(ft, "dialog");
        }else{
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                // dismiss the dialog or refresh the activity
                dialogFragment.dismiss();
            }
        }
    }
}