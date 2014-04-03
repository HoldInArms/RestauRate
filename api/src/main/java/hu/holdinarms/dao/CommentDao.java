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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 *
 * @author zsurot
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

    public Comment save(Comment comment) {
        comment.setLive(Boolean.TRUE);
        comment.setCreateDate(new Date());
        return persist(comment);
    }

    public Comment update(Comment comment){
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

        String queryString = "SELECT COUNT(id) FROM rr_comments WHERE restaurant_id = :restaurantId AND live = true";
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("restaurantId", restaurantId);
        
        return ((BigInteger) query.uniqueResult()).intValue();
    }

    public List<Comment> getComments(Long restaurantId, Integer from, Integer to){

        if(from == null || to == null){
            return new ArrayList<Comment>();
        }
        
        String queryString = "SELECT id FROM (" +
        "	    SELECT ROW_NUMBER() OVER (ORDER BY createdate DESC ) AS rowNumber, id, restaurant_id, createdate "+
        "         FROM rr_comments WHERE restaurant_id = :restaurantId AND live = true" +
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

    public List<Comment> getCommentsForAdmin(Long restaurantId, Integer from, Integer to, String orderby, String direction){
        
        if(from == null || to == null){
            return new ArrayList();
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
        
        List<BigInteger> resultList = query.list();
        List<Comment> result = new ArrayList<Comment>();
        for(BigInteger bigInteger : resultList){
            result.add(get(bigInteger.longValue()));
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
