package com.lain.event_planner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lain.event_planner.R;
import com.lain.event_planner.adapters.ProductAdapter;
import com.lain.event_planner.adapters.SummaryAdapter;
import com.lain.event_planner.classes.Event;

import java.text.NumberFormat;

//Each fragment represents a subcategory of Product
public class SummaryFragment extends Fragment {
    private Event currentEvent;

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Defines the RecyclerView and inflates it with the necessary content
        View root = inflater.inflate(getLayoutId(), container, false);
        RecyclerView recyclerView = root.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        TextView txvTotalNumber = root.findViewById(R.id.txvTotalNumber);

        //Gets the argument with the event to be used in order to fill the recycler
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentEvent = bundle.getParcelable(getString(R.string.INTENT_KEY));
        }

        recyclerView.setAdapter(new SummaryAdapter(currentEvent.getSelectedProducts()));

        txvTotalNumber.setText(currentEvent.getTotalEventCostFormatted());

        return root;
    }


    protected int getLayoutId() {
        return R.layout.fragment_summary;
    }
}