/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.holdinarms.dao;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import hu.holdinarms.model.Comment;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 *
 * @author zsurot
 */
public class CommentDao extends AbstractDAO<Comment> {

    @Inject
    public CommentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Comment save(Comment comment) {
        comment.setLive(Boolean.TRUE);
        comment.setCreateDate(new Date());
        return persist(comment);
    }

    /*public List<Comment> getCommentsByRestaurantId(Long restaurantId) {
        String queryString = "SELECT id FROM RR_comments where restaurant_id = :restaurantId";

        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);

        List<BigInteger> queryResult = query.list();
        List<Comment> result = new ArrayList<Comment>();
        for (BigInteger bigInteger : queryResult) {
            result.add(get(bigInteger.longValue()));
        }

        return result;
    }*/

    public Integer countComments(Long restaurantId){

        String queryString = "SELECT COUNT(id) FROM RR_comments WHERE restaurant_id = :restaurantId AND live = 1";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return (Integer) query.uniqueResult();
    }

    public List<Comment> getComments(Long restaurantId, Integer from, Integer to){

        if(from == null || to == null){
            return new ArrayList<Comment>();
        }
        
        String queryString = "SELECT id FROM (" +
        "	    SELECT ROW_NUMBER() OVER (ORDER BY createdate DESC ) AS rowNumber, id, restaurant_id, createdate "+
        "         FROM RR_comments WHERE restaurant_id = :restaurantId AND live = 1" +
        "   ) AS rowNumberSelect WHERE rowNumber BETWEEN :from AND :to";
        
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        query.setParameter("from", from);
        query.setParameter("to", to);

        List<BigInteger> queryRestult = query.list();
        List<Comment> result = new ArrayList<Comment>();
        
        for(BigInteger bigInteger: queryRestult){
            result.add( get(bigInteger.longValue()) );
        }
        
        return result;
    }

    public Integer getVotesByRestaurantId(Long restaurantId){
        String queryString = "SELECT COUNT(id) FROM RR_comments WHERE live = 1 AND restaurant_id = :restaurantId";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return (Integer) query.uniqueResult();
    }

    public Double getAvargeByRestaurantId(Long restaurantId){
        String queryString = "select SUM(vote)/convert(float,COUNT(id)) from RR_comments where live = 1 and restaurant_id = :restaurantId";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return (Double) query.uniqueResult();
    }

    public String lastCommentByRestaurantId(Long restaurantId){
        String queryString = "SELECT TOP 1 id from RR_comments WHERE restaurant_id = :restaurantId AND live = 1 order by createdate DESC";

        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        System.out.println(query.uniqueResult());
        return ((Comment) get( ((BigInteger)query.uniqueResult()).longValue() )).getComment();
    }
}
