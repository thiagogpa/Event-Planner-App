package com.example.event_planner.customElements;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.text.NumberFormat;

//This class extend the functionality of a EditText, to format the input as the user types it, into a CURRENCY format
public class CurrencyEditText extends AppCompatEditText {

    private String current = "";
    private CurrencyEditText editText = CurrencyEditText.this;
    private String Currency = "$";
    private double max = 9999.99;
    private String PreviousValue = "";

    public CurrencyEditText(Context context) {
        super(context);
        init();
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        //Listener for when the user types into the textField
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Saves the value of the textField before user input
                if (!charSequence.toString().equals("")) {
                    PreviousValue = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //This method converts the value input into a currency format
                if (!s.toString().equals(current)) {

                    //Removes the handler so the program doesn't loop eternally
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "").replaceAll(Currency, "").replaceAll("\\s+", "");

                    if (cleanString.length() != 0) {
                        try {
                            String currencyFormat = Currency;
                            double parsed;
                            String formatted;

                            parsed = Double.parseDouble(cleanString);
                            formatted = NumberFormat.getCurrencyInstance().format((parsed / 100)).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), currencyFormat);

                            current = formatted;
                            //Converts the value into currency
                            double value = Double.parseDouble(formatted.toString().replaceAll("[$,]", "").replaceAll(Currency, ""));

                            //If the final value is greater than the max value set, it replaces the content with the previous value, so the user doesn't
                            //insert a bigger number than the app can handle
                            if (value > max) {
                                editText.setText(PreviousValue);
                                editText.setSelection(PreviousValue.length());
                            } else {
                                editText.setText(formatted);
                                editText.setSelection(formatted.length());
                            }


                        } catch (NumberFormatException e) {
                        }
                    }
                    //Puts back the event handler
                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    public double getCleanDoubleValue() {
        //Creates a clean value of the current number, formatted into a double
        double value;
        try {
            value = Double.parseDouble(editText.getText().toString().replaceAll("[$,]", "").replaceAll(Currency, ""));
        } catch (NullPointerException e) {
            value = 0.0;
        }

        return value;
    }

}


