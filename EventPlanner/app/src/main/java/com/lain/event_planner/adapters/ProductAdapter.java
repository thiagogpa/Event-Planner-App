package com.lain.event_planner.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lain.event_planner.R;
import com.lain.event_planner.classes.Product;
import com.lain.event_planner.customElements.CurrencyEditText;
import com.lain.event_planner.customElements.InputFilterMinMax;
import com.squareup.picasso.Picasso;

import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    // Store a member variable for the items
    private List<Product> products;
    private Context context;

    public ProductAdapter(List<Product> products) {
        // generate constructors to initialise the List and Context objects
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout with the card layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cards, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Creates the variables to handle the screen elements
        final Product product = products.get(position);
        holder.textViewName.setText(product.getName());
        holder.switchSelected.setChecked((product.isSelected()));
        holder.edtPrice.setText((product.getPrice().toString()));
        holder.edtQuantity.setText((product.getQuantity().toString()));


        holder.edtQuantity.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "9999")});

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

        holder.setIsRecyclable(false);
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
        TextView textViewName;
        ImageView imageUrl;
        Switch switchSelected;
        CurrencyEditText edtPrice;
        EditText edtQuantity;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            // initialize the View objects
            this.textViewName = itemView.findViewById(R.id.textItemName);
            this.imageUrl = itemView.findViewById(R.id.imageView);
            this.switchSelected = itemView.findViewById(R.id.switchSelected);
            this.edtPrice = itemView.findViewById((R.id.edtPrice));
            this.edtQuantity = itemView.findViewById((R.id.edtQuantity));

        }

        public void bind(final Product product) {

            // Set a checked change listener for the switch button
            switchSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setSelected(!product.isSelected());
                }
            });

            edtPrice.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                //Once the value was changed, it sets the product price based on the content of the editField
                @Override
                public void afterTextChanged(Editable s) {
                    product.setPrice(edtPrice.getCleanDoubleValue());
                }
            });


            edtQuantity.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                //Once the value was changed, it sets the product price based on the content of the editField
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        product.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
                    } catch (Exception e){
                        product.setQuantity(0);
                    }
                }

            });
        }
    }
}
