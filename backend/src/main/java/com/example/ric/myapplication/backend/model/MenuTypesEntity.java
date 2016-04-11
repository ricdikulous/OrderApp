package com.example.ric.myapplication.backend.model;

import java.util.HashMap;

/**
 * Created by ric on 3/04/16.
 */
public class MenuTypesEntity {
    HashMap<Long, String> menuTypes;

    public HashMap<Long, String> getMenuTypes() {
        return menuTypes;
    }

    public void setMenuTypes(HashMap<Long, String> menuTypes) {
        this.menuTypes = menuTypes;
    }
}
