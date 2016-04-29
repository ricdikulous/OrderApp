package com.dikulous.ric.orderapp.model;

import java.util.List;

/**
 * Created by ric on 31/03/16.
 */
public class OrderItem {
    Long pk;
    Long orderFk;
    Long menuItemFk;
    int amount;
    String menuItemKeyString;
    String specialRequest;
    List<String> ingredientsExcluded;

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getMenuItemKeyString() {
        return menuItemKeyString;
    }

    public void setMenuItemKeyString(String menuItemKeyString) {
        this.menuItemKeyString = menuItemKeyString;
    }

    public List<String> getIngredientsExcluded() {
        return ingredientsExcluded;
    }

    public void setIngredientsExcluded(List<String> ingredientsExcluded) {
        this.ingredientsExcluded = ingredientsExcluded;
    }

    public Long getOrderFk() {
        return orderFk;
    }

    public void setOrderFk(Long orderFk) {
        this.orderFk = orderFk;
    }

    public Long getMenuItemFk() {
        return menuItemFk;
    }

    public void setMenuItemFk(Long menuItemFk) {
        this.menuItemFk = menuItemFk;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
