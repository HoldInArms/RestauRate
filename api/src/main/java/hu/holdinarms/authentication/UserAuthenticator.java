/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.authentication;

import hu.holdinarms.dao.AdminDao;
import hu.holdinarms.model.Admin;
import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

/**
 * This is an user authentication class which check
 * 
 * @author Dgzt
 */
public class UserAuthenticator implements Authenticator<String, Admin>{

    private AdminDao adminDao;
    
    public UserAuthenticator(AdminDao adminDao) {
        super();
        this.adminDao = adminDao;
    }

    public Optional<Admin> authenticate(String token) throws AuthenticationException {
        Long adminId = TokenStorage.getUserIdByToken(token);
        if (adminId == null || !TokenStorage.isTokenValid(token)) {
            return Optional.absent();
        }
        Admin user = adminDao.findById(adminId);
        if (user == null) {
            return Optional.absent();
        }
        TokenStorage.extendExpirationDate(adminId);
        
        return Optional.of(user);
    }
}
