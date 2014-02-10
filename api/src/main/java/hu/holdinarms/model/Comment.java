/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dgzt
 */
@Entity
@Table(name = "RR_comments")
@NamedQueries({
    @NamedQuery(name = "Comment.findById", query = "SELECT c FROM Comment c WHERE c.id = :id") 
})
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Restaurant restaurant;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "comment")
    private String comment;

    @Basic(optional = false)
    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "vote")
    private Integer vote;

    @Basic(optional = false)
    @NotNull
    @Column(name = "live")
    private Boolean live;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ordertime")
    private Date orderTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrivetime")
    private Date arriveTime;

    @Size(min = 1, max = 32)
    @Column(name = "foodname")
    private String foodName;

    @Column(name = "foodprice")
    private Float foodPrice;

    @Column(name = "worthmoney")
    private Boolean worthMoney;

    @Column(name="dispatch_behaviour")
    private Boolean dispatchBehaviour;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    private Date createDate;

    @Transient
    @Size(max = 32)
    private String newRestaurantName;

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
