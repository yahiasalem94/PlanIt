package com.ysalem.android.planit;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ysalem.android.planit.database.AppDatabase;
import com.ysalem.android.planit.database.BucketListDao;
import com.ysalem.android.planit.models.BucketList;
import com.ysalem.android.planit.models.BucketListItem;
import com.ysalem.android.planit.models.PointOfInterestPhoto;
import com.ysalem.android.planit.models.PointsOfInterests;
import com.ysalem.android.planit.ui.BucketListFragment;
import com.ysalem.android.planit.ui.MainActivity;

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
    public void init() throws InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = AppDatabase.getBucketListDbInstance(context);
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

    @Test
    public void addItemInBucket() {
        BucketList bucketList = new BucketList("amsterdam");
        dao.insertBucket(bucketList);

        PointOfInterestPhoto photo = new PointOfInterestPhoto(null, 0);
        ArrayList<PointOfInterestPhoto> pointsOfInterestsPhotoArrayList = new ArrayList<>();
        pointsOfInterestsPhotoArrayList.add(photo);

        PointsOfInterests item = new PointsOfInterests("Rijksmusuem", "placeId", pointsOfInterestsPhotoArrayList);
        ArrayList<PointsOfInterests> pointsOfInterestsArrayList = new ArrayList<>();
        pointsOfInterestsArrayList.add(item);

        BucketListItem bucketListItem = new BucketListItem(pointsOfInterestsArrayList);
        ArrayList<BucketListItem> items = new ArrayList<>();

        items.add(bucketListItem);

        BucketList mBucketList = new BucketList("amsterdam", items);
        dao.updateBucket(mBucketList);

        onView(withId(R.id.bucketlists_recycler_view)).inRoot(withDecorView(Matchers
                .is(mActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(withId(R.id.poi_name)).check(matches(withText("Rijksmusuem")));
    }

    @After
    public void closeDb() throws IOException {
        mDb = null;
    }

}
