package com.example.android.planit.ui;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.planit.R;
import com.example.android.planit.adapters.BucketListAdapter;
import com.example.android.planit.adapters.PopularDestinationsAdapter;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.databinding.FragmentHomeBinding;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.PopularDestinations;
import com.example.android.planit.utils.AppExecutors;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener,
        PopularDestinationsAdapter.PopularDestinationsAdapterOnClickHandler {

    private static final String TAG = HomeFragment.class.getSimpleName();
    public static final String CITY_NAME = "city_name";

    private NavController navController;

    /* Views */
    private FragmentHomeBinding binding;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private NestedScrollView nestedScrollView;

    private PopularDestinationsAdapter mAdapter;
    private ArrayList<PopularDestinations> mPopularDestinations;
    private GridLayoutManager gridLayoutManager;

    private AppDatabase mDb;
    private Boolean isTablet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        mDb = AppDatabase.getPopularDestinationsDbInstance((getActivity()).getApplicationContext());

        mAdapter = new PopularDestinationsAdapter(this, getActivity());

        populateDB();
    }

    @Override
    public void onStart() {
        super.onStart();
        NavigationView navigation = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        int size = menu.size();

        for (int i = 0; i < size; i++) {
            menu.getItem(i).setChecked(false);
        }

        MenuItem menuItem;
        menuItem = menu.findItem(R.id.home);
        if(!menuItem.isChecked()) {
            menuItem.setChecked(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getContext()).lockAppBarClosed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.searchButton.setOnClickListener(this);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Navigation.setViewNavController(binding.searchButton, navController);

        setupRecyclerView();

        return binding.getRoot();
    }




    private void setupRecyclerView() {

        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), calculateNoOfColumns(getActivity()));
        binding.homeRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        binding.homeRecyclerView.setAdapter(mAdapter);

        retrieveTasks();
    }

    public int calculateNoOfColumns(@NonNull Context context) {

        if (isTablet) {
            return 2;
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;

        return noOfColumns;
    }

    @Override
    public void onClick(View v) {

        if (!TextUtils.isEmpty(binding.simpleSearchView.getText())) {
            Bundle bundle = new Bundle();
            bundle.putString(CITY_NAME, binding.simpleSearchView.getText().toString());
            navController.navigate(R.id.bestThingsTodoFragment, bundle);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.enter_city), Toast.LENGTH_SHORT).show();
        }
    }

    private void populateDB() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (mDb.popularDestinationDao().numberOfRows() == 0) {
                    mDb.popularDestinationDao().insertAll(new PopularDestinations[]{
                            new PopularDestinations("Paris", R.drawable.paris),
                            new PopularDestinations("Amsterdam", R.drawable.amsterdam),
                            new PopularDestinations("Barcelona", R.drawable.barcelona),
                            new PopularDestinations("Berlin", R.drawable.berlin),
                            new PopularDestinations("Rome", R.drawable.rome)
                    });
                }
            }
        });
    }

    private void retrieveTasks() {
        Log.d(TAG, "Actively retrieving the bucketLists from the DataBase");
        LiveData<List<PopularDestinations>> destinations = mDb.popularDestinationDao().loadAllDestinations();

        destinations.observe(this, new Observer<List<PopularDestinations>>() {
            @Override
            public void onChanged(List<PopularDestinations> popularDestinations) {
                mPopularDestinations = (ArrayList) popularDestinations;
                binding.homeRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.setData(mPopularDestinations);
            }
        });
    }

    @Override
    public void onClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME, mPopularDestinations.get(position).getName());
        navController.navigate(R.id.bestThingsTodoFragment, bundle);
        Log.d(TAG, "Item Clicked");
    }
}