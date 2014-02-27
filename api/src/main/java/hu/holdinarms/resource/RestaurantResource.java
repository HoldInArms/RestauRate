/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.resource;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import hu.holdinarms.dao.RestaurantDao;
import hu.holdinarms.model.Admin;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.model.dto.RestaurantPage;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Dgzt
 */
@Path("/restaurant")
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantResource {
    
    private final RestaurantDao restaurantDao;
    
    @Inject
    public RestaurantResource(RestaurantDao restaurantDao){
        this.restaurantDao = restaurantDao;
    }
    
    @GET
    @UnitOfWork
    @Path("/all")
    public List<Restaurant> getAll( @Auth(required = false) Admin admin ){
        return restaurantDao.findAll( admin );
    }

    @GET
    @UnitOfWork
    @Path("/list")
    public RestaurantPage getRestaurants( @Auth(required = false) Admin admin, @QueryParam("from") Integer from, @QueryParam("to") Integer to,
            @QueryParam("orderby") String orderby, @QueryParam("direction") String direction,
            @QueryParam("filtertext") String filterText){

        RestaurantPage rp = new RestaurantPage();

        rp.setRestaurants(restaurantDao.getRestaurants(admin, from, to, orderby, direction, filterText));
        rp.setRestaurantNumber(restaurantDao.countRestaurants(admin, filterText));
        
        return rp;
    }
    
//    @POST
//    @UnitOfWork
//    @Path("/save")
//    public Restaurant save(Restaurant restaurant){
//        return restaurantDao.save(restaurant);
//    }

    @POST
    @UnitOfWork
    @Path("/delete/{restaurantId}")
    public void delete(@Auth Admin admin, @PathParam("restaurantId") Long restaurantId ){
        Restaurant restaurant = restaurantDao.findById(restaurantId);
        if(restaurant != null){
            restaurant.setLive(Boolean.FALSE);
            
            restaurantDao.update(restaurant);
        }
    }

    @POST
    @UnitOfWork
    @Path("/reinstate/{restaurantId}")
    public void reinstate(@Auth Admin admin, @PathParam("restaurantId") Long restaurantId){
        Restaurant restaurant = restaurantDao.findById(restaurantId);
        if(restaurant != null){
            restaurant.setLive(Boolean.TRUE);

            restaurantDao.update(restaurant);
        }
    }
    
}
