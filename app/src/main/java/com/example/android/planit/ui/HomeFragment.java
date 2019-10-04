package com.example.android.planit.ui;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.example.android.planit.R;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.databinding.FragmentHomeBinding;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.PopularDestinations;
import com.example.android.planit.utils.AppExecutors;
import com.google.android.material.appbar.AppBarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    public static final String CITY_NAME = "city_name";

    private NavController navController;

    SimpleDateFormat dateFormatter;
    Date departDate;
    Date arrivalDate;
    private final Calendar calendar;
    private int year;
    private int month;
    private int day;

    boolean isDeparture = false;
    boolean isDepartureSet = false;
    /* Views */
    private FragmentHomeBinding binding;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private DatePickerDialog datePickerDialog;

    private AppDatabase mDb;

    public HomeFragment() {
        // Required empty public constructor
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        //Add one to month {0 - 11}
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getPopularDestinationsDbInstance((getActivity()).getApplicationContext());
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        populateDB();
    }

    @Override
    public void onStart() {
        super.onStart();
        appBarLayout = ((MainActivity) getActivity()).appBarLayout;
        nestedScrollView = ((MainActivity) getActivity()).nestedScrollView;
        appBarLayout.setExpanded(false);
        nestedScrollView.setNestedScrollingEnabled(false);;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.searchButton.setOnClickListener(this);

        binding.departureDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
                isDeparture = true;
            }
        });

        binding.arrivalDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
                isDeparture = false;
            }
        });


        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Navigation.setViewNavController(binding.searchButton, navController);

        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {

        if (!TextUtils.isEmpty(binding.simpleSearchView.getText())) {
            Bundle bundle = new Bundle();
            bundle.putString(CITY_NAME, binding.simpleSearchView.getText().toString());
//            appBarLayout.setExpanded(true);
            navController.navigate(R.id.bestThingsTodoFragment, bundle);
        } else {
            Toast.makeText(getActivity(), "Please enter a City", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet (DatePicker view,int year, int month, int dayOfMonth){
        String date = day + "/" + (month + 1) + "/" + year;
        if (isDeparture) {
            try {
                departDate = dateFormatter.parse(date);
                binding.departureDate.setText(date);
                isDepartureSet = true;
            } catch (ParseException e) {
                Toast.makeText(getActivity(), "Please enter a valid Date", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            if (isDepartureSet) {
                try {
                    arrivalDate = dateFormatter.parse(date);
                    if (arrivalDate.after(departDate)) {
                        binding.arrivalDate.setText(date);
                    } else {
                        Toast.makeText(getActivity(), "Please enter a valid Date", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                datePickerDialog.cancel();
                Toast.makeText(getActivity(), "Please enter a Departure date first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateDB() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                mDb.popularDestinationDao().insertAll(new PopularDestinations[] {
                        new PopularDestinations("Paris"),
                        new PopularDestinations("Amsterdam"),
                        new PopularDestinations("Barcelona"),
                        new PopularDestinations("Berlin"),
                        new PopularDestinations("Rome")
                });


            }
        });
    }

    private void retrieveTasks() {
        Log.d(TAG, "Actively retrieving the bucketLists from the DataBase");
        LiveData<List<PopularDestinations>> destinations = mDb.popularDestinationDao().loadAllDestinations();

        destinations.observe(this, new Observer<List<PopularDestinations>>() {
            @Override
            public void onChanged(List<PopularDestinations> popularDestinations) {

            }
        });
    }
}