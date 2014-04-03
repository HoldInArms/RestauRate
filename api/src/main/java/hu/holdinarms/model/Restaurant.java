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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The entity contains the restaurant information.
 *
 * @author Dgzt
 */
@Entity
@Table(name = "rr_restaurants")
@NamedQueries({
    @NamedQuery(name="Restaurant.findById", query = "SELECT r FROM Restaurant r WHERE id = :id")
})
@SequenceGenerator (name = "restaurant_seq_gen", sequenceName = "restaurant_id_seq")
public class Restaurant implements Serializable {
    
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
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "restaurant_seq_gen")
    private long id;

    /**
     * The name of the restaurant.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "name")
    private String name;

    /**
     * The status.
     */
    @Basic(optional = false)
    @NotNull
    @Column(name = "live")
    private Boolean live;

    /**
     * The created date.
     */
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    private Date createDate;

    @Transient
    private Integer votes;

    @Transient
    private Double avarge;

    @Transient
    private String lastComment;

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
    
    public Double getAvarge() {
        return avarge;
    }

    public void setAvarge(Double avarge) {
        this.avarge = avarge;
    }
    
    public String getLastComment() {
        return lastComment;
    }
    
    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }
}
