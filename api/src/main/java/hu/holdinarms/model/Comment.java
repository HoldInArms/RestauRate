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
package hu.holdinarms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The entity contains the comment information.
 *
 * @author Dgzt
 */
@Entity
@Table(name = "rr_comments")
@NamedQueries({
    @NamedQuery(name = "Comment.findById", query = "SELECT c FROM Comment c WHERE c.id = :id") 
})
@SequenceGenerator (name = "comment_seq_gen", sequenceName = "comment_id_seq")
public class Comment implements Serializable {

	//~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------
    private static final long serialVersionUID = 1L;

    //~-----------------------------------------------------   
    //~ Member fields
    //~----------------------------------------------------- 
    /**
     * The primary key.
     */
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_gen")
    private long id;

    /**
     * The restaurant what contains the comment.
     */
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Restaurant restaurant;

    /**
     * The comment.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "comment")
    private String comment;

    /**
     * The vote number.
     */
    @Basic(optional = false)
    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "vote")
    private Integer vote;

    /**
     * If the this value is false then
     * the comment was removed.
     */
    @Basic(optional = false)
    @NotNull
    @Column(name = "live")
    private Boolean live;

    /**
     * The order time.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ordertime")
    private Date orderTime;

    /**
     * The arrive time.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrivetime")
    private Date arriveTime;

    /**
     * The name of the food.
     */
    @Size(min = 1, max = 32)
    @Column(name = "foodname")
    private String foodName;

    /**
     * The price of the food.
     */
    @Column(name = "foodprice")
    private Float foodPrice;

    /**
     * The food worth of money.
     */
    @Column(name = "worthmoney")
    private Boolean worthMoney;

    /**
     * The dispatch's behaviour.
     */
    @Column(name="dispatch_behaviour")
    private Boolean dispatchBehaviour;

    /**
     * The created date.
     */
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    private Date createDate;

    /**
     * The name of the new restaurant.
     */
    @Transient
    @Size(max = 32)
    private String newRestaurantName;

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

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

    public Boolean isLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
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

    public Boolean isWorthMoney() {
        return worthMoney;
    }

    public void setWorthMoney(Boolean worthMoney) {
        this.worthMoney = worthMoney;
    }
        
    public Boolean isDispatchBehaviour() {
        return dispatchBehaviour;
    }

    public void setDispatchBehaviour(Boolean dispatchBehaviour) {
        this.dispatchBehaviour = dispatchBehaviour;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNewRestaurantName() {
        return newRestaurantName;
    }

    public void setNewRestaurantName(String newRestaurantName) {
        this.newRestaurantName = newRestaurantName;
    }

}
