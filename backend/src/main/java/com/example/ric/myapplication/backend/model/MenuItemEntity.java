package com.example.ric.myapplication.backend.model;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by ric on 30/03/16.
 */
public class MenuItemEntity {
    String servingUrl;
    String name;
    String description;
    String keyString;
    Long price;
    Long type;
    ArrayList<String> ingredients;
    ArrayList<String> allergens;

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getServingUrl() {
        return servingUrl;
    }

    public void setServingUrl(String servingUrl) {
        this.servingUrl = servingUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}