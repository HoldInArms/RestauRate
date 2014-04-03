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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * This entity contains the admin information.
 * 
 * @author Dgzt
 */
@Entity
@Table(name = "rr_admins")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@whoAdded")
@NamedQueries({
    @NamedQuery(name = "Admin.findById", query = "SELECT a FROM Admin a WHERE a.id = :id"),
    @NamedQuery(name = "Admin.findByUsername", query = "SELECT a FROM Admin a where a.username = :username"),
    @NamedQuery(name = "Admin.authenticate", query = "SELECT a FROM Admin a WHERE a.username = :username AND a.password = :password")
})
@SequenceGenerator (name = "admin_seq_gen", sequenceName = "admin_id_seq")
public class Admin implements Serializable {

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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq_gen")
    private Long id;

    /**
     * The admin's name.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "username")
    private String username;

    /**
     * The admin's password.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "password")
    private String password;

    /**
     * The admin who created the admin.
     */
    @Basic(optional = false)
    @JoinColumn(name = "whoadded", referencedColumnName = "id")
    @OneToOne
    private Admin whoAdded;

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin getWhoAdded() {
        return whoAdded;
    }

    public void setWhoAdded(Admin whoAdded) {
        this.whoAdded = whoAdded;
    }
}
