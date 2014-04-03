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
