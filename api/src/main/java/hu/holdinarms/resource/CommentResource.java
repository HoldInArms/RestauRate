/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.resource;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import hu.holdinarms.dao.CommentDao;
import hu.holdinarms.dao.RestaurantDao;
import hu.holdinarms.model.Admin;
import hu.holdinarms.model.Comment;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.model.dto.CommentPage;
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
 * @author zsurot
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
    public Comment saveWithNewRestaurant(Comment comment){
        if(comment.getNewRestaurantName().isEmpty()){
            return null;
        }
        
        Restaurant restaurant = restaurantDao.save(comment.getNewRestaurantName());
        comment.setRestaurant(restaurant);
        
        return commentDao.save(comment);
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

    /*@GET
    @UnitOfWork
    @Path("/get/{restaurantId}")
    public List<Comment> getCommentsByRestaurant(@PathParam("restaurantId") Long restaurantId){
        return commentDao.getCommentsByRestaurantId(restaurantId);
    }*/

    @GET
    @UnitOfWork
    @Path("/list/{restaurantId}")
    public CommentPage getComments(@PathParam("restaurantId") Long restaurantId, 
            @QueryParam("from") Integer from, @QueryParam("to") Integer to)
    {
        CommentPage commentPage = new CommentPage();
        commentPage.setComments(commentDao.getComments(restaurantId, from, to));
        commentPage.setCountComments(commentDao.countComments(restaurantId));
        
        return commentPage;
    }

    @GET
    @UnitOfWork
    @Path("/adminlist")
    public CommentPage getCommentsForAdmin(@Auth Admin admin, 
            @QueryParam("restaurantid") Long restaurantId, 
            @QueryParam("from") Integer from, @QueryParam("to") Integer to,
            @QueryParam("orderby") String orderby, @QueryParam("direction") String direction){

        
        CommentPage commentPage = new CommentPage();

        commentPage.setComments(commentDao.getCommentsForAdmin(restaurantId, from, to, orderby, direction));
        commentPage.setCountComments(commentDao.countCommentsForAdmin(restaurantId));
        
        return commentPage;
    }
    
}
