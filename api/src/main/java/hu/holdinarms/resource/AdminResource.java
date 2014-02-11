/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 *
 * @author zsurot
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

    private AdminDao adminDao;
    
    @Inject
    AdminResource(AdminDao adminDao){
        this.adminDao = adminDao;
    }
    
    @GET
    @UnitOfWork
    @Path("/login/{username}/{password}")
    public String login(@PathParam("username")String username, @PathParam("password")String password){
        return adminDao.authenticate(username, DigestUtils.sha256Hex(password));
    }
    
    @POST
    @UnitOfWork
    @Path("/logout")
    public void logout(@Auth Admin admin){
        TokenStorage.removeUsertoken(admin.getId());
    }

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
        
        return adminDao.save(newAdmin);
    }

    @PUT
    @UnitOfWork
    @Path("/changepassword/{newPassword}")
    public Admin changePassword(@Auth Admin admin, @PathParam("newPassword") String newPassword){
        admin.setPassword(DigestUtils.sha256Hex(newPassword));
        return adminDao.save(admin);
    }
}
