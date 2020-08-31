package com.lain.event_planner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lain.event_planner.classes.Product;
import com.lain.event_planner.common.Base;
import com.lain.event_planner.R;
import com.lain.event_planner.classes.Event;
import com.lain.event_planner.fragments.SummaryFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class SummaryActivity extends Base implements OnChartValueSelectedListener {

    private PieChart chart;
    private Event currentEvent;
    private ImageView eventImage;
    private SummaryFragment summaryFragment;
    private TextView txtaddress, txtestablishmentName;
    private LinearLayout linearEstablishment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Gets the event through the received intent
        Bundle data = getIntent().getExtras();
        currentEvent = data.getParcelable(getString(R.string.INTENT_KEY));

        eventImage = findViewById(R.id.imgEvent);

        txtaddress = findViewById(R.id.txtaddress);
        txtestablishmentName = findViewById(R.id.txtestablishmentName);

        linearEstablishment = findViewById(R.id.linearEstablishment);

        if (currentEvent.getEventLocation().getName() != null){
            txtestablishmentName.setText(currentEvent.getEventLocation().getName());
            linearEstablishment.setVisibility(View.VISIBLE);
        } else {
            linearEstablishment.setVisibility(View.GONE);
        }

        txtaddress.setText(currentEvent.getEventLocation().getFormatted_address());

        Drawable drawable;
        switch (currentEvent.getEventType()){
            case BARBECUE:
                drawable = getDrawable(R.drawable.barbecue);
                break;
            case BIRTHDAY:
                drawable = getDrawable(R.drawable.birthday);
                break;
            case BUSINESS_MEETING:
                drawable = getDrawable(R.drawable.business);
                break;
            default:
                drawable = null;
        }
        
        eventImage.setImageDrawable(drawable);

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfLight);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);
        chart.animateY(1400, Easing.EaseInOutQuad);

        chart.getLegend().setEnabled(false);

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);

        setData();
        setSummaryFragment();
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(new PieEntry((float) currentEvent.getSubCategoryPercentage(Event.SubCategory.FOOD),Event.SubCategory.FOOD.toString()));
        entries.add(new PieEntry((float) currentEvent.getSubCategoryPercentage(Event.SubCategory.DRINKS),Event.SubCategory.DRINKS.toString()));
        entries.add(new PieEntry((float) currentEvent.getSubCategoryPercentage(Event.SubCategory.OTHERS),Event.SubCategory.OTHERS.toString()));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
    

    public void setSummaryFragment() {
        summaryFragment = new SummaryFragment();

        //Creates a new bundle to be sent to the fragments
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.INTENT_KEY), currentEvent);

        //Set the bundle as an argument to the fragments, so they can be accessed by them
        summaryFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, summaryFragment)
                .commitNow();
    }

    public void sendEvent(View view) {

        StringBuilder content = new StringBuilder();
        content.append("Hello,\n\nMy event will be taking place on this location:\n");

        if(currentEvent.getEventLocation().getName() != null)
            content.append("Place name: ").append(currentEvent.getEventLocation().getName());

        content.append("Address: ").append(currentEvent.getEventLocation().getFormatted_address());

        content.append("\n\nThe list of items to be bought are:\n\n");
        for (Product product:currentEvent.getSelectedProducts()) {
            content.append(product.getQuantity() + " x " + product.getName() + " = " + product.getTotalPriceFormatted() + "\n");
        }

        content.append("\nI hope to see you there !");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hey! I just created an event, here`s the list of items and the location");
        intent.putExtra(Intent.EXTRA_TEXT, content.toString());

        Intent shareIntent = Intent.createChooser(intent, "Share event");
        startActivity(shareIntent);

    }
}
