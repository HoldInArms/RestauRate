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

import javax.validation.constraints.NotNull;

/**
 * The DTO for comment when add new comment for a restaurant.
 * 
 * @author Dgzt
 */
public class CommentWithoutNewRestaurantDTO extends CommentDTO {

	//~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	//~-----------------------------------------------------   
    //~ Member fields
    //~----------------------------------------------------- 
	/**
	 * The id of the restaurant.
	 */
	@NotNull
	private Long restaurantId;

	//~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
	
}
