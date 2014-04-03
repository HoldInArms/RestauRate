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

import hu.holdinarms.model.Comment;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.model.dto.CommentDTO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.AbstractDAO;

/**
 * The DAO for {@file Comment}.
 *
 * @author Dgzt
 */
public class CommentDao extends AbstractDAO<Comment> {

    private static final String[] ORDERBY_COLUMN_NAMES = new String[]{"vote", "createdate" };

    @Inject
    public CommentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Comment findById(Long id){
        return uniqueResult(namedQuery("Comment.findById").setParameter("id", id));
    }
    
    /**
     * Save the new comment.
     * 
     * @param commentDto The new comment.
     * @param restaurant The restaurant of comment.
     * @return Ther persisted comment.
     */
    public Comment save( CommentDTO commentDto, Restaurant restaurant ){
    	Comment comment = new Comment();
    	comment.setRestaurant(restaurant);
    	comment.setComment(commentDto.getComment());
    	comment.setVote(commentDto.getVote());
    	comment.setOrderTime(commentDto.getOrderTime());
    	comment.setArriveTime(commentDto.getArriveTime());
    	comment.setFoodName(commentDto.getFoodName());
    	comment.setFoodPrice(commentDto.getFoodPrice());
    	comment.setWorthMoney(commentDto.getWorthMoney());
    	comment.setDispatchBehaviour(commentDto.getDispatchBehaviour());
    	comment.setLive(Boolean.TRUE);
    	comment.setCreateDate(new Date());
    	
    	return persist(comment);
    }

    public Comment update(Comment comment){
        return persist(comment);
    }

    public Integer countComments(Long restaurantId){

        String queryString = "SELECT COUNT(id) FROM rr_comments WHERE restaurant_id = :restaurantId AND live = true";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return ((BigInteger) query.uniqueResult()).intValue();
    }

    public List<CommentDTO> getComments(Long restaurantId, Integer from, Integer to){

        if(from == null || to == null){
            return new ArrayList<CommentDTO>();
        }
        
        String queryString = "SELECT id FROM (" +
        "	    SELECT ROW_NUMBER() OVER (ORDER BY createdate DESC ) AS rowNumber, id, restaurant_id, createdate "+
        "         FROM rr_comments WHERE restaurant_id = :restaurantId AND live = true" +
        "   ) AS rowNumberSelect WHERE rowNumber BETWEEN :from AND :to";
        
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        query.setParameter("from", from);
        query.setParameter("to", to);

        @SuppressWarnings("unchecked")
		List<BigInteger> queryRestult = query.list();
        List<CommentDTO> result = new ArrayList<CommentDTO>();
        
        for(BigInteger bigInteger: queryRestult){
        	Comment comment = get(bigInteger.longValue());
        	
        	CommentDTO commentDto = new CommentDTO();
        	commentDto.setComment(comment.getComment());
        	commentDto.setVote(comment.getVote());
        	commentDto.setOrderTime(comment.getOrderTime());
        	commentDto.setArriveTime(comment.getArriveTime());
        	commentDto.setFoodName(comment.getFoodName());
        	commentDto.setFoodPrice(comment.getFoodPrice());
        	commentDto.setWorthMoney(comment.isWorthMoney());
        	commentDto.setDispatchBehaviour(comment.isDispatchBehaviour());
        	
            result.add( commentDto );
        }
        
        return result;
    }

    public Integer getVotesByRestaurantId(Long restaurantId){
        String queryString = "SELECT COUNT(id) FROM rr_comments WHERE live = true AND restaurant_id = :restaurantId";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return ((BigInteger) query.uniqueResult()).intValue();
    }

    public Double getAvargeByRestaurantId(Long restaurantId){
        String queryString = "select SUM(vote)/CAST(COUNT(id) AS float4) from rr_comments where live = true and restaurant_id = :restaurantId";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return (Double) query.uniqueResult();
    }

    public String lastCommentByRestaurantId(Long restaurantId){
        String queryString = "SELECT id from rr_comments WHERE restaurant_id = :restaurantId AND live = true order by createdate DESC LIMIT 1";

        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        if(query.uniqueResult() == null){
            return "";
        }
        return ((Comment) get( ((BigInteger)query.uniqueResult()).longValue() )).getComment();
    }

    public List<CommentDTO> getCommentsForAdmin(Long restaurantId, Integer from, Integer to, String orderby, String direction){
        
        if(from == null || to == null){
            return new ArrayList<CommentDTO>();
        }
        
        if(direction == null || direction.isEmpty()){
            direction = "DESC";
        }
        
        String queryString = "SELECT id FROM ("+
        "   SELECT ROW_NUMBER() OVER ( ORDER BY :orderby :direction ) AS rowNumber, id, restaurant_id, createdate, vote "+
        "   FROM rr_comments WHERE true=true ";
        
        if(restaurantId != null){
            queryString += "AND restaurant_id = :restaurantId";
        }

        queryString += " ) as rowNumberSelect WHERE rowNumber BETWEEN :from AND :to ";
        
        queryString = queryString.replace(":direction", direction);
        queryString = queryString.replace(":orderby", getOrderByProperty(orderby));
        
        
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("from", from);
        query.setParameter("to", to);
        if(restaurantId != null){
            query.setParameter("restaurantId", restaurantId);
        }
        
        @SuppressWarnings("unchecked")
		List<BigInteger> resultList = query.list();
        List<CommentDTO> result = new ArrayList<CommentDTO>();
        for(BigInteger bigInteger : resultList){
        	Comment comment = get(bigInteger.longValue());
        	
        	CommentDTO commentDto = new CommentDTO();
        	commentDto.setComment(comment.getComment());
        	commentDto.setVote(comment.getVote());
        	commentDto.setOrderTime(comment.getOrderTime());
        	commentDto.setArriveTime(comment.getArriveTime());
        	commentDto.setFoodName(comment.getFoodName());
        	commentDto.setFoodPrice(comment.getFoodPrice());
        	commentDto.setWorthMoney(comment.isWorthMoney());
        	commentDto.setDispatchBehaviour(comment.isDispatchBehaviour());
        	
            result.add( commentDto );
        }
        
        return result;
    }

    public Integer countCommentsForAdmin(Long restaurantId){
        String queryString = "SELECT COUNT(id) FROM rr_COMMENTS WHERE true=true ";
        
        if(restaurantId != null){
            queryString += "AND restaurant_id = :restaurantId";
        }

        Query query = currentSession().createSQLQuery(queryString);
        if(restaurantId != null){
            query.setParameter("restaurantId", restaurantId);
        }

        return ((BigInteger) query.uniqueResult()).intValue();
    }

    public String getOrderByProperty(String orderBy){
       if(Arrays.asList(ORDERBY_COLUMN_NAMES).contains(orderBy)){
            return orderBy;
        }

        return "createdate"; 
    }
}
