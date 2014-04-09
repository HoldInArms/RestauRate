/***************************************************************************************************
 ***** This file is part of RestauRate.                                                        *****
 *****                                                                                         *****
 ***** Copyright (C) 2014 HoldInArms                                                           *****
 *****                                                                                         *****
 ***** This program is free software: you can redistribute it and/or modify it under the       *****
 ***** terms of the GNU General Public License as published by the Free Software Foundation,   *****
 ***** either version 3 of the License, or (at your option) any later version.                 *****
 *****                                                                                         *****
 ***** This program is distributed in the hope that it will be useful, but WITHOUT ANY         *****
 ***** WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A         *****
 ***** PARTICULAR PURPOSE. See the GNU General Public License for more details.                *****
 *****                                                                                         *****
 ***** You should have received a copy of the GNU General Public License along with this       *****
 ***** program. If not, see <http://www.gnu.org/licenses/>.                                    *****
 ***************************************************************************************************/
package hu.holdinarms.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This object contains a page of the restaurant.
 *
 * @author Dgzt
 */
public class RestaurantPageDTO implements Serializable {
    
	//~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------
	private static final long serialVersionUID = 1L;

	//~-----------------------------------------------------   
    //~ Member fields
    //~-----------------------------------------------------
	/**
	 * The list of the restaurants.
	 */
	List<RestaurantDTO> restaurants = new ArrayList<RestaurantDTO>();
    
	/**
	 * The number of the all restaurants.
	 */
    Long restaurantNumber = new Long(0);

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
    public List<RestaurantDTO> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantDTO> restaurants) {
        this.restaurants = restaurants;
    }

    public Long getRestaurantNumber() {
        return restaurantNumber;
    }

    public void setRestaurantNumber(Long restaurantNumber) {
        this.restaurantNumber = restaurantNumber;
    }
    
}
