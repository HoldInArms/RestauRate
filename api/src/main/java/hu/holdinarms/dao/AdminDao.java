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
package hu.holdinarms.dao;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import hu.holdinarms.authentication.TokenStorage;
import hu.holdinarms.model.Admin;
import org.hibernate.SessionFactory;

/**
 * The DAO for {@file Admin}.
 *
 * @author Dgzt
 */
public class AdminDao extends AbstractDAO<Admin>{

    @Inject
    public AdminDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find the admin by id.
     * 
     * @param id The id of admin.
     * @return The admin.
     */
    public Admin findById(Long id){
        return (Admin) uniqueResult(namedQuery("Admin.findById").setParameter("id", id));
    }

    /**
     * Find the admin by username.
     * 
     * @param username The admin's name.
     * @return The admin.
     */
    public Admin findByUsername(String username){
        return (Admin) uniqueResult(namedQuery("Admin.findByUsername").setParameter("username", username));
    }
    
    /**
     * The authentication of admin.
     * 
     * @param username The admin's username.
     * @param password The admin's password.
     * @return The token.
     */
    public String authenticate(String username, String password){
        Admin admin = uniqueResult(namedQuery("Admin.authenticate").setParameter("username", username).setParameter("password", password));

        if(admin == null){
            return null;
        }

        String tokenValue = TokenStorage.getUserToken(admin.getId());
        if (tokenValue == null) {
            return TokenStorage.generateUserToken(admin.getId());
        }
        TokenStorage.extendExpirationDate(admin.getId());
        
        return tokenValue;
    }
    
    /**
     * Save the new admin.
     * 
     * @param admin The new admin's datas.
     * @return The persisted admin.
     */
    public Admin save(Admin admin){
        return persist(admin);
    }

}
