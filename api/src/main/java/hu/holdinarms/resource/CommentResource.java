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

import hu.holdinarms.dao.CommentDao;
import hu.holdinarms.dao.RestaurantDao;
import hu.holdinarms.model.Admin;
import hu.holdinarms.model.Comment;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.model.dto.CommentPageDTO;
import hu.holdinarms.model.dto.CommentWithNewRestaurantDTO;

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
 * The resouce for {@file Comment}.
 *
 * @author Dgzt
 */
@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource { 


    private CommentDao commentDao;

    @Inject
    private RestaurantDao restaurantDao;

    @Inject
    public CommentResource(CommentDao commentDao){
        this.commentDao = commentDao;
    }
   
    @POST
    @UnitOfWork
    @Path("/saveWithNewRestaurant")
    public Comment saveWithNewRestaurant(CommentWithNewRestaurantDTO commentWithNewRestaurantDTO){
        if(commentWithNewRestaurantDTO.getNewRestaurantName().isEmpty()){
            return null;
        }
        
        Restaurant restaurant = restaurantDao.save(commentWithNewRestaurantDTO.getNewRestaurantName());
        
        return commentDao.saveWithNewRestaurant(commentWithNewRestaurantDTO, restaurant);
    }

    @POST
    @UnitOfWork
    @Path("/saveWithoutNewRestaurant")
    public Comment saveWithoutNewRestaurant(Comment comment){
        if(comment.getRestaurant() == null){
            return null;
        }
        
        return commentDao.save(comment);
    }

    @GET
    @UnitOfWork
    @Path("/list/{restaurantId}")
    public CommentPageDTO getComments(@PathParam("restaurantId") Long restaurantId, 
            @QueryParam("from") Integer from, @QueryParam("to") Integer to)
    {
        CommentPageDTO commentPage = new CommentPageDTO();
        commentPage.setComments(commentDao.getComments(restaurantId, from, to));
        commentPage.setCountComments(commentDao.countComments(restaurantId));
        
        return commentPage;
    }

    @GET
    @UnitOfWork
    @Path("/adminlist")
    public CommentPageDTO getCommentsForAdmin(@Auth Admin admin, 
            @QueryParam("restaurantid") Long restaurantId, 
            @QueryParam("from") Integer from, @QueryParam("to") Integer to,
            @QueryParam("orderby") String orderby, @QueryParam("direction") String direction){

        
        CommentPageDTO commentPage = new CommentPageDTO();

        commentPage.setComments(commentDao.getCommentsForAdmin(restaurantId, from, to, orderby, direction));
        commentPage.setCountComments(commentDao.countCommentsForAdmin(restaurantId));
        
        return commentPage;
    }

    @POST
    @UnitOfWork
    @Path("/delete/{commentId}")
    public Comment delete(@Auth Admin admin, @PathParam("commentId")Long commentId){
        Comment comment = commentDao.findById(commentId);
        if(comment != null){
            comment.setLive(Boolean.FALSE);
            commentDao.update(comment);
        }

        return comment;
    }

    @POST
    @UnitOfWork
    @Path("/reinstate/{commentId}")
    public Comment reinstate(@Auth Admin admin, @PathParam("commentId")Long commentId){
        Comment comment = commentDao.findById(commentId);
        if(comment != null){
            comment.setLive(Boolean.TRUE);
            commentDao.update(comment);
        }

        return comment;
    }

    @POST
    @UnitOfWork
    @Path("/move/{commentId}/{restaurantId}")
    public Comment move(@Auth Admin admin, @PathParam("commentId") Long commentId, @PathParam("restaurantId") Long restaurantId){
        Comment comment = commentDao.findById(commentId);
        if(comment == null){
            return null;
        }

        Restaurant restaurant = restaurantDao.findById(restaurantId);
        if(restaurant == null){
            return null;
        }

        comment.setRestaurant(restaurant);
        
        return commentDao.update(comment);
    }
    
}
