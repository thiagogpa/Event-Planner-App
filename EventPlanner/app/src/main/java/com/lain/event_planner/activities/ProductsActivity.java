package com.lain.event_planner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lain.event_planner.common.Base;
import com.lain.event_planner.R;
import com.lain.event_planner.classes.Event;
import com.lain.event_planner.fragments.ProductsFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lain.event_planner.ui.main.TabAdapter;

public class ProductsActivity extends Base {
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //Creates the fragments based on the subcategories
        Fragment foodFragment = new ProductsFragment(Event.SubCategory.FOOD);
        Fragment drinksFragment = new ProductsFragment(Event.SubCategory.DRINKS);
        Fragment othersFragment = new ProductsFragment(Event.SubCategory.OTHERS);

        //Gets the event through the received intent
        Bundle data = getIntent().getExtras();
        currentEvent = data.getParcelable(getString(R.string.INTENT_KEY));

        //Creates a new bundle to be sent to the fragments
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.INTENT_KEY), currentEvent);

        //Set the bundle as an argument to the fragments, so they can be accessed by them
        foodFragment.setArguments(bundle);
        drinksFragment.setArguments(bundle);
        othersFragment.setArguments(bundle);

        //Tab adapter is created to handle the tabs behavior
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(foodFragment, "Food");
        tabAdapter.addFragment(drinksFragment, "Drinks");
        tabAdapter.addFragment(othersFragment, "Others");

        //Sets the loading of the tabs to the max amount necessary, so it doesn't reload the fragment once the user swipes through them
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(tabAdapter.getCount() > 1 ? tabAdapter.getCount() - 1 : 1);
        viewPager.setAdapter(tabAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    //This is called when the user clicks on the Arrow button, to go to the next screen
    public void NextActivity(View view) {
        //Creates a new intent, puts the event object in it, and calls the next activity
        Intent locationSelectionActivity = new Intent(getApplicationContext(), LocationSelectionActivity.class);
        locationSelectionActivity.putExtra(getString(R.string.INTENT_KEY), currentEvent);
        startActivity(locationSelectionActivity);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

}