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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This DTO object contains the comment information.
 * 
 * @author Dgzt
 */
public class CommentDTO implements Serializable{

	//~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	//~-----------------------------------------------------   
    //~ Member fields
    //~----------------------------------------------------- 
	/**
	 * The comment.
	 */
	@NotNull
    @Size(min = 1, max = 256)
	private String comment;
	
	/**
	 * The number of vote.
	 */
	@NotNull
    @Min(1)
    @Max(5)
	private Integer vote;

    /**
     * The order time.
     */
    private Date orderTime;

    /**
     * The arrive time.
     */
    private Date arriveTime;

    /**
     * The name of the food.
     */
    @Size(min = 1, max = 32)
    private String foodName;

    /**
     * The price of the food.
     */
    private Float foodPrice;

    /**
     * The food worth of money.
     */
    private Boolean worthMoney;

    /**
     * The dispatch's behaviour.
     */
    private Boolean dispatchBehaviour;

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public Float getFoodPrice() {
		return foodPrice;
	}

	public void setFoodPrice(Float foodPrice) {
		this.foodPrice = foodPrice;
	}

	public Boolean getWorthMoney() {
		return worthMoney;
	}

	public void setWorthMoney(Boolean worthMoney) {
		this.worthMoney = worthMoney;
	}

	public Boolean getDispatchBehaviour() {
		return dispatchBehaviour;
	}

	public void setDispatchBehaviour(Boolean dispatchBehaviour) {
		this.dispatchBehaviour = dispatchBehaviour;
	}

}
