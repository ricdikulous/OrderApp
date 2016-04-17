package com.example.ric.myapplication.backend.model;


import java.util.List;

/**
 * Created by ric on 7/04/16.
 */
public class OrderItemEntity {
    String menuItemKeyString;
    Integer amount;
    List<String> ingredientsExcluded;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuItemKeyString() {
        return menuItemKeyString;
    }

    public void setMenuItemKeyString(String menuItemKeyString) {
        this.menuItemKeyString = menuItemKeyString;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<String> getIngredientsExcluded() {
        return ingredientsExcluded;
    }

    public void setIngredientsExcluded(List<String> ingredientsExcluded) {
        this.ingredientsExcluded = ingredientsExcluded;
    }
}
