package com.example.android.planit.ui;

import android.app.Fragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.utils.BaseActivity;
import com.example.android.planit.utils.ConnectionReceiver;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.leinardi.android.speeddial.SpeedDialView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        ConnectionReceiver.NetworkStateReceiverListener, CollapsingToolbarListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean isDisconnected = false;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    public Toolbar toolbar;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public AppBarLayout appBarLayout;
    private NavController navController;
    public NestedScrollView nestedScrollView;
    public ImageView imageView;
    private AdView mAdView;
    public SpeedDialView speedDialView;

    private ConnectionReceiver mConnectionReciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713" /*getString(R.string.adMob_appId)*/);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mConnectionReciever = new ConnectionReceiver();
        mConnectionReciever.addListener(this);
        registerReceiver(mConnectionReciever,new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.appbar);
        nestedScrollView = findViewById(R.id.scrollView);
        imageView = findViewById(R.id.header);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view );

        speedDialView = findViewById(R.id.speedDial);

        setupNavigation();
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(BucketListFragment.BUCKET_LIST_NAME)) {
                navController.navigate(R.id.bucketListItemsFragment, getIntent().getExtras());
            }
        }
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
                 R.id.myCalendarFragment, R.id.bestThingsTodoFragment,
                 R.id.noConnectionDialog).setDrawerLayout(drawerLayout).build();

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
            case R.id.settings:
                navController.navigate(R.id.settingsFragment);
                break;
        }
        drawerLayout.closeDrawers();
        return true;

    }

    @Override
    public void networkAvailable(boolean isAvailable) {
        if(!isAvailable) {
            isDisconnected = true;
            getSupportActionBar().hide();
            getSupportActionBar().setHomeButtonEnabled(false);
            navController.navigate(R.id.noConnectionDialog);
        }else{
            if (isDisconnected) {
                navController.navigate(R.id.homeFragment);
                getSupportActionBar().show();
                getSupportActionBar().setHomeButtonEnabled(true);
                isDisconnected = false;
            }
        }
    }

    @Override
    public void unlockAppBarOpen() {

        appBarLayout.setExpanded(true, true);
        appBarLayout.setActivated(true);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        lp.height = (int) getResources().getDimension(R.dimen.app_bar_height);
        appBarLayout.setLayoutParams(lp);
//        nestedScrollView.setNestedScrollingEnabled(true);
        toolbar.setTitle("");
    }

    @Override
    public void lockAppBarClosed() {
        appBarLayout.setExpanded(false, true);
        appBarLayout.setActivated(false);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        lp.height = (int) getResources().getDimension(R.dimen.toolbar_height);

        appBarLayout.setLayoutParams(lp);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle(getString(R.string.app_name));

//        nestedScrollView.setNestedScrollingEnabled(false);
    }
}

interface CollapsingToolbarListener {
   void unlockAppBarOpen();
   void lockAppBarClosed();
}