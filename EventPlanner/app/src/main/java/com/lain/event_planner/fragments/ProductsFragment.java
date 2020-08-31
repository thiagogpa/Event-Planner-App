package com.lain.event_planner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lain.event_planner.R;
import com.lain.event_planner.adapters.ProductAdapter;
import com.lain.event_planner.classes.Event;

//Each fragment represents a subcategory of Product
public class ProductsFragment extends Fragment {
    private Event currentEvent;
    private Event.SubCategory subCategory;
    public ProductsFragment(Event.SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public ProductsFragment() {
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

        //Gets the argument with the event to be used in order to fill the recycler
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentEvent = bundle.getParcelable(getString(R.string.INTENT_KEY));
        }

        recyclerView.setAdapter(new ProductAdapter(currentEvent.getProducts(this.subCategory)));

        return root;
    }

    //When the instance is retrieved, in case the user flips the device, it needs to get the type of event that was informed on the last time
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieving logic
        if (savedInstanceState != null) {
            this.subCategory = Event.checkValidSubCategory(savedInstanceState.getString(getString(R.string.INSTANCE_RETRIEVE)));
        }
    }

    //Saves the info necessary to get back to the point it left
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.INSTANCE_RETRIEVE), this.subCategory.toString()); //<-- Saving operation
    }

    protected int getLayoutId() {
        return R.layout.fragment_products;
    }
}