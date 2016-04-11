package com.dikulous.ric.orderapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderActivity extends AppCompatActivity {

    protected Spinner mMenuSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        mMenuSpinner = (Spinner) findViewById(R.id.menu_spinner);
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Burger");
        menuItems.add("Kebab");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, menuItems);
        mMenuSpinner.setAdapter(adapter);
    }

    public void handlePlaceOrderButtonClick(View view){
        Intent intent = new Intent(this, OrderPlacedActivity.class);
        startActivity(intent);
    }
}
