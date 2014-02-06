/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.dao;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import hu.holdinarms.authentication.TokenStorage;
import hu.holdinarms.model.Admin;
import org.hibernate.SessionFactory;

/**
 *
 * @author Dgzt
 */
public class AdminDao extends AbstractDAO<Admin>{

    @Inject
    public AdminDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Admin findById(Long id){
        return (Admin) uniqueResult(namedQuery("Admin.findById").setParameter("id", id));
    }

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
    
    public Admin save(Admin admin){
        return persist(admin);
    }
    
}
