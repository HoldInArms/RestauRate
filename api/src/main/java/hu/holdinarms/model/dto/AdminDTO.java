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
import java.util.Date;

/**
 * The DTO object for {@file Admin}.
 * 
 * @author Dgzt
 */
public class AdminDTO implements Serializable{

	//~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------
	private static final long serialVersionUID = 1L;
	
    //~-----------------------------------------------------   
    //~ Member fields
    //~----------------------------------------------------- 
	/**
	 * The username.
	 */
	private String username;
	
	/**
	 * The username who added the current admin.
	 */
	private String whoAdded;
	
	/**
	 * The created date.
	 */
	private Date createDate;

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWhoAdded() {
		return whoAdded;
	}

	public void setWhoAdded(String whoAdded) {
		this.whoAdded = whoAdded;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
