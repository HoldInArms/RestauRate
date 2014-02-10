/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.model.dto;

import hu.holdinarms.model.Restaurant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zsurot
 */
public class RestaurantPage implements Serializable {
    
    List<Restaurant> restaurants = new ArrayList<Restaurant>();
    
    Integer restaurantNumber = 0;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Integer getRestaurantNumber() {
        return restaurantNumber;
    }

    public void setRestaurantNumber(Integer restaurantNumber) {
        this.restaurantNumber = restaurantNumber;
    }
    
}