package com.example.event_planner.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.event_planner.R;
import com.example.event_planner.classes.Product;
import com.example.event_planner.customElements.CurrencyEditText;
import com.squareup.picasso.Picasso;

import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    // Store a member variable for the items
    private List<Product> products;
    private Context context;

    public SummaryAdapter(List<Product> products) {
        // generate constructors to initialise the List and Context objects
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout with the card layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_summary_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Resources res = holder.itemView.getContext().getResources();

        //Creates the variables to handle the screen elements
        final Product product = products.get(position);
        holder.txvItemName.setText(product.getName());
        holder.txvQuantity.setText(String.valueOf(product.getQuantity().intValue()));
        holder.txvFinalPrice.setText(String.valueOf(product.getTotalPriceFormatted()));

        //Creates an instance of a Picasso(It is used to load images dynamically into a ImageView)
        Picasso mPicasso = Picasso.get();
        //Sets debugging arrow on the images, RED=Network, BLUE=Disk, GREEN=Memory
        //mPicasso.setIndicatorsEnabled(true);

        //Sets the image to be shown based on the URL received, case it doesn't manage to do it, it loads a default image.
        mPicasso.get()
                .load(product.getImage_url())
                .error(R.drawable.default_food)
                .into(holder.imageUrl)
        ;

        //Binds the Event handlers informing the Product object
        holder.bind(products.get(position));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return products.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Variables for each element on the view
        TextView txvItemName;
        TextView txvQuantity;
        TextView txvFinalPrice;
        ImageView imageUrl;


        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            // initialize the View objects
            this.txvItemName = itemView.findViewById(R.id.txvItemName);
            this.txvQuantity = itemView.findViewById(R.id.txvQuantity);
            this.txvFinalPrice = itemView.findViewById(R.id.txvFinalPrice);
            this.imageUrl = itemView.findViewById(R.id.imageView);
        }

        public void bind(final Product product) {

        }
    }
}
