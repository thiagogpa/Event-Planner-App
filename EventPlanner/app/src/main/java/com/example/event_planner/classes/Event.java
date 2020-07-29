package com.example.event_planner.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

//Class that hold all the info of a given Event
public class Event implements Parcelable {
    private Guests guests = new Guests();
    private List<Product> products = new ArrayList<>();

    //Pre-defined type of events
    public enum EventType {
        BARBECUE,
        BIRTHDAY,
        BUSINESS_MEETING
    }

    //Pre-defined subcategories
    public enum SubCategory {
        FOOD,
        DRINKS,
        OTHERS
    }

    private EventType eventType;

    public Event() {
    }

    //Constructor to be used when object is sent through an intent
    protected Event(Parcel in) {
        guests = in.readParcelable(Guests.class.getClassLoader());
        products = in.createTypedArrayList(Product.CREATOR);
        eventType = EventType.valueOf(in.readString());
    }

    private double calcTotalCost(List<Product> products){
        double totalCost = 0;
        for (Product currentProduct: products) {
            totalCost += currentProduct.getTotalPrice();
        }
        return totalCost;
    }

    public double getSubCategoryPercentage(SubCategory subCategory){
        //return (calcTotalCost(getSelectedProductsBySubcategory(subCategory)) / calcTotalEventCost());
        return (calcTotalCost(getSelectedProductsBySubcategory(subCategory)));
    }

    private double calcTotalEventCost(){
        return calcTotalCost(this.getSelectedProducts());
    }

    public String getTotalEventCostFormatted(){
        return NumberFormat.getCurrencyInstance().format(this.calcTotalEventCost()).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "$");
    }


    //COMPARES THE EVENT TYPE BASED ON A STRING AND RETURNS ITS EQUIVALENT ON ENUM
    public static SubCategory checkValidSubCategory(String str) {
        SubCategory[] allEvents = SubCategory.values();
        for (SubCategory event : allEvents) {
            //Comparing
            if (event.toString().equals(str.toUpperCase())) {
                return event;
            }
        }
        return null;
    }

    //COMPARES THE EVENT TYPE BASED ON A STRING AND RETURNS ITS EQUIVALENT ON ENUM
    public static EventType checkValidEventType(String str) {
        EventType[] allEvents = EventType.values();
        for (EventType event : allEvents) {
            //Comparing
            if (event.toString().equals(str.toUpperCase())) {
                return event;
            }
        }
        return null;
    }

    //List all the products of a given category
    public List<Product> getProducts(SubCategory category) {
        List<Product> selectedProducts = new ArrayList<>();

        for (Product currentProduct : this.products) {
            if (category.toString().equals(currentProduct.getCategory().toUpperCase())){
                selectedProducts.add(currentProduct);
            }
        }
        return selectedProducts;
    }

    //List all the products currently selected
    public List<Product> getSelectedProducts() {
        List<Product> selectedProducts = new ArrayList<>();

        for (Product currentProduct : this.products) {
            if (currentProduct.isSelected()){
                selectedProducts.add(currentProduct);
            }
        }
        return selectedProducts;
    }

    //List all the products currently selected
    public List<Product> getSelectedProductsBySubcategory(SubCategory subCategory) {
        List<Product> selectedProducts = new ArrayList<>();

        for (Product currentProduct : this.products) {
            if (currentProduct.isSelected() && (currentProduct.getCategory().toUpperCase().equals(subCategory.toString().toUpperCase()))){
                selectedProducts.add(currentProduct);
            }
        }
        return selectedProducts;
    }

    //Methods create automatically to turn object into parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(guests, i);
        parcel.writeTypedList(products);
        parcel.writeString(this.eventType.name());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Guests getGuests() {
        return guests;
    }

    public void setGuests(Guests guests) {
        this.guests = guests;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
