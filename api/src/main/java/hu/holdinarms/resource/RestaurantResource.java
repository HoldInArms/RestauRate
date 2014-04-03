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

import hu.holdinarms.dao.RestaurantDao;
import hu.holdinarms.model.Admin;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.model.dto.RestaurantDTO;
import hu.holdinarms.model.dto.RestaurantPageDTO;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;

/**
 * The resource for {@file Restaurant}.
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
    public List<RestaurantDTO> getAll( @Auth(required = false) Admin admin ){
        return restaurantDao.findAll( admin );
    }

    @GET
    @UnitOfWork
    @Path("/list")
    public RestaurantPageDTO getRestaurants( @Auth(required = false) Admin admin, @QueryParam("from") Integer from, @QueryParam("to") Integer to,
            @QueryParam("orderby") String orderby, @QueryParam("direction") String direction,
            @QueryParam("filtertext") String filterText){

        RestaurantPageDTO rp = new RestaurantPageDTO();

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
