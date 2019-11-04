package com.example.android.planit;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.database.BucketListDao;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.BucketListItem;
import com.example.android.planit.models.PointOfInterestPhoto;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.ui.BucketListFragment;
import com.example.android.planit.ui.MainActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class BucketListFragmentTests {

    private BucketListDao dao;
    private AppDatabase mDb;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeClass() {
        InstrumentationRegistry.getTargetContext().deleteDatabase("PlanIt");

    }

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = mDb.bucketListDao();

        BucketList bucketList = new BucketList("paris");
        dao.insertBucket(bucketList);

        BucketListFragment fragment = new BucketListFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, fragment).commit();
    }

    @Test
    public void AddingNewBucket() {


        onView(withId(R.id.bucketlists_recycler_view)).inRoot(withDecorView(Matchers
                .is(mActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks that the OrderActivity opens with the correct tea name displayed
//        onView(withId(R.id.poi_name)).check(matches(withText("Louvre")));

        onView(withText(R.string.empty_bucketlist))
                .inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }

//    @Test
//    public void addItemInBucket() {
//        BucketList bucketList = new BucketList("amsterdam");
//        dao.insertBucket(bucketList);
//
//        PointOfInterestPhoto photo = new PointOfInterestPhoto(null, 0);
//        ArrayList<PointOfInterestPhoto> pointsOfInterestsPhotoArrayList = new ArrayList<>();
//        pointsOfInterestsPhotoArrayList.add(photo);
//
//        PointsOfInterests item = new PointsOfInterests("Rijksmusuem", "placeId", pointsOfInterestsPhotoArrayList);
//        ArrayList<PointsOfInterests> pointsOfInterestsArrayList = new ArrayList<>();
//        pointsOfInterestsArrayList.add(item);
//
//        BucketListItem bucketListItem = new BucketListItem(pointsOfInterestsArrayList);
//        ArrayList<BucketListItem> items = new ArrayList<>();
//
//        items.add(bucketListItem);
//
//        BucketList mBucketList = new BucketList("paris", items);
//        dao.updateBucket(mBucketList);
//
//        onView(withId(R.id.bucketlists_recycler_view)).inRoot(withDecorView(Matchers
//                .is(mActivityTestRule.getActivity().getWindow().getDecorView())))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//        // Checks that the OrderActivity opens with the correct tea name displayed
//        onView(withId(R.id.poi_name)).check(matches(withText("Rijksmusuem")));
//    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

}
