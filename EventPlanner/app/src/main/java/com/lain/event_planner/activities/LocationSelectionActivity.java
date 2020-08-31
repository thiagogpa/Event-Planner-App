package com.lain.event_planner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lain.event_planner.R;
import com.lain.event_planner.classes.Event;
import com.lain.event_planner.common.Base;

public class LocationSelectionActivity extends Base {

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets the event through the received intent
        Bundle data = getIntent().getExtras();
        currentEvent = data.getParcelable(getString(R.string.INTENT_KEY));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

    }

    public void searchByAddress(View view) {
        Intent customAddressActivity = new Intent(getApplicationContext(), CustomAddressActivity.class);
        customAddressActivity.putExtra(getString(R.string.INTENT_KEY), currentEvent);
        startActivity(customAddressActivity);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }

    public void searchByEstablishment(View view) {
        Intent establishmentActivity = new Intent(getApplicationContext(), EstablishmentActivity.class);
        establishmentActivity.putExtra(getString(R.string.INTENT_KEY), currentEvent);
        startActivity(establishmentActivity);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }


}