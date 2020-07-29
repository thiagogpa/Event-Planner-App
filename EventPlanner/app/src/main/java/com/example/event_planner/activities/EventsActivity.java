package com.example.event_planner.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.event_planner.common.Base;
import com.example.event_planner.R;
import com.example.event_planner.classes.Event;

public class EventsActivity extends Base {

    private Event currentEvent = new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
    }

    public void imgButtonClicked(View view) {
        //Gets the event of the button clicked
        Event.EventType eventType = Event.checkValidEventType(view.getTag().toString());

        //In case it`s null, it means the button has a tag which is not a valid event
        if (eventType != null) {
            currentEvent.setEventType(eventType);

            Intent guestsActivity = new Intent(this, GuestActivity.class);
            guestsActivity.putExtra(getString(R.string.INTENT_KEY), currentEvent);
            startActivity(guestsActivity);
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        checkOrientation(newConfig);
    }

    private void checkOrientation(Configuration newConfig) {
        LinearLayout linearAllEvents = findViewById(R.id.linearAllEvents);
        LinearLayout linearInnerAllEvents = findViewById(R.id.linearInnerAllEvents);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("OrientationMyApp", "Current Orientation : Landscape");
            linearInnerAllEvents.setOrientation(LinearLayout.HORIZONTAL);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("OrientationMyApp", "Current Orientation : Portrait");
            linearInnerAllEvents.setOrientation(LinearLayout.VERTICAL);
        }
    }

}
