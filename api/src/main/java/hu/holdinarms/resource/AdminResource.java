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
package hu.holdinarms.resource;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import hu.holdinarms.authentication.TokenStorage;
import hu.holdinarms.dao.AdminDao;
import hu.holdinarms.model.Admin;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * The resource for {@file Admin}.
 *
 * @author Dgzt
 */
@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

	//~-----------------------------------------------------   
    //~ Member fields
    //~----------------------------------------------------- 
	/**
	 * The dao for admin.
	 */
	@Inject
    private AdminDao adminDao;
//    
//    @Inject
//    AdminResource(AdminDao adminDao){
//        this.adminDao = adminDao;
//    }
    
	//~-----------------------------------------------------   
    //~ Services
    //~----------------------------------------------------- 
	/**
	 * Login for admin.
	 * 
	 * @param username The admin's username.
	 * @param password The admin's password.
	 * @return The token.
	 */
    @GET
    @UnitOfWork
    @Path("/login/{username}/{password}")
    public String login(@PathParam("username")String username, @PathParam("password")String password){
        return adminDao.authenticate(username, DigestUtils.sha256Hex(password));
    }
    
    /**
     * The logout.
     * 
     * @param admin The admin.
     */
    @POST
    @UnitOfWork
    @Path("/logout")
    public void logout(@Auth Admin admin){
        TokenStorage.removeUsertoken(admin.getId());
    }

    /**
     * Add new admin.
     * 
     * @param admin The admin who add the new admin.
     * @param username The new admin's username.
     * @param password The new admin's password.
     * @return The new admin.
     */
    @POST
    @UnitOfWork
    @Path("/add/{username}/{password}")
    public Admin addAdmin(@Auth Admin admin, @PathParam("username") String username, @PathParam("password") String password){
        if(adminDao.findByUsername(username) != null){
            return null;
        }
        
        Admin newAdmin = new Admin();
        newAdmin.setUsername(username);
        newAdmin.setPassword(DigestUtils.sha256Hex(password));
        newAdmin.setWhoAdded(admin);
        
        return adminDao.save(newAdmin);
    }

    /**
     * Change password.
     * 
     * @param admin The admin.
     * @param newPassword The new password.
     * @return The admin with new password.
     */
    @PUT
    @UnitOfWork
    @Path("/changepassword/{newPassword}")
    public Admin changePassword(@Auth Admin admin, @PathParam("newPassword") String newPassword){
        admin.setPassword(DigestUtils.sha256Hex(newPassword));
        return adminDao.save(admin);
    }
}
