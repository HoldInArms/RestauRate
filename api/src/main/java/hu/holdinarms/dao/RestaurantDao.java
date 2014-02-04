/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.dao;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import hu.holdinarms.model.Restaurant;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 *
 * @author Dgzt
 */
public class RestaurantDao extends AbstractDAO<Restaurant> {

    private static final String[] ORDERBY_COLUMN_NAMES = new String[]{"name", "latest_comment", "avarge", "votes" };

    @Inject
    private CommentDao commentDao;
    
    @Inject
    public RestaurantDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Restaurant findById(Long id){
        return uniqueResult(namedQuery("Restaurant.findById").setParameter("id", id));
    }

    public Restaurant update(Restaurant restaurant){
        return persist(restaurant);
    }
    
    public List<Restaurant> findAll(){
//        return list(namedQuery("Restaurant.findAll"));
        String queryString = "SELECT id FROM RR_restaurants WHERE live = 1";
        Query query = currentSession().createSQLQuery(queryString);

        List<BigInteger> queryResult = query.list();
        List<Restaurant> results = new ArrayList<Restaurant>();
        for(BigInteger bigInteger : queryResult){
            Restaurant restaurant = get(bigInteger.longValue());
            restaurant.setVotes(commentDao.getVotesByRestaurantId(bigInteger.longValue()));
            restaurant.setAvarge(commentDao.getAvargeByRestaurantId(bigInteger.longValue()));
            results.add(restaurant);
        }
        
        return results;
    }

    public Integer countRestaurants(String filterText){
        StringBuilder filterBuilder = new StringBuilder();

        addFilter(filterBuilder, filterText);

        String queryString = "select COUNT(id) from RR_restaurants where 1=1 ";

        if(!filterBuilder.toString().isEmpty()){
            queryString += filterBuilder.toString();
        }

        Query query = currentSession().createSQLQuery(queryString);

        addFilterQueryParameters(query, filterText);
        
        return (Integer) query.uniqueResult();
    }

    public List<Restaurant> getRestaurants(Integer from, Integer to, String orderby, String direction, String filterText){
        if(from == null || to == null){
            return new ArrayList<Restaurant>();
        }

        if(direction == null || direction.isEmpty()){
            direction = "DESC";
        }

        StringBuilder filterBuilder = new StringBuilder();
        addFilter(filterBuilder,filterText);
        
        String queryString = "select id from (" +
        "	select ROW_NUMBER() over (order by :orderby :direction ) as rowNumber, * from (" +
        "		select restaurants.id, restaurants.name ," +
        "		(select TOP 1 comments.createdate from RR_comments as comments" +
        "			where comments.live = 1 and comments.restaurant_id = restaurants.id" +
        "			order by comments.createdate DESC" +
        "		) as latest_comment," +
        "		(select SUM(comments.vote)/CONVERT(float,count(comments.id)) " +
        "			from RR_comments as comments " +
        "			where comments.live = 1 and comments.restaurant_id = restaurants.id" +
        "		) as avarge," +
        "		(select COUNT(id) from RR_comments as comments " +
        "			where comments.live = 1 and" +
        "			comments.restaurant_id = restaurants.id" +
        "		) as votes" +
        "		from RR_restaurants as restaurants where 1=1 ";
        
        if(!filterBuilder.toString().isEmpty()){
            queryString += filterBuilder.toString();
        }

        queryString += " ) AS rowNumberSekect ) AS rowNumberFilter WHERE rowNumber BETWEEN :from AND :to ";
        
        queryString = queryString.replace(":direction", direction);
        queryString = queryString.replace(":orderby", getOrderByProperty(orderby));
       
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("from", from);
        query.setParameter("to", to);

        addFilterQueryParameters(query, filterText);

        List<BigInteger> queryResult = query.list();
        List<Restaurant> result = new ArrayList<Restaurant>();
        System.out.println(queryString);
        for(BigInteger bigInteger : queryResult){
            Restaurant restaurant = get(bigInteger.longValue());
            restaurant.setVotes(commentDao.getVotesByRestaurantId(bigInteger.longValue()));
            restaurant.setAvarge(commentDao.getAvargeByRestaurantId(bigInteger.longValue()));
            restaurant.setLastComment(commentDao.lastCommentByRestaurantId(bigInteger.longValue()));
            
            result.add(restaurant);
        }
        
        return result;
    }
    
//    public Restaurant save(Restaurant restaurant){
//        restaurant.setLive(Boolean.TRUE);
//        restaurant.setCreateDate(new Date());
//        return persist(restaurant);
//    }

    public Restaurant save(String name){
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(name);
        newRestaurant.setLive(Boolean.TRUE);
        newRestaurant.setCreateDate(new Date());

        return persist(newRestaurant);
    }

    private void addFilter(StringBuilder filterBuilder, String filterText){
        if(filterText != null && !filterText.isEmpty()){
            filterBuilder.append("and name like :filterText ");
        }
    }

    private String getOrderByProperty(String orderby){
        if(Arrays.asList(ORDERBY_COLUMN_NAMES).contains(orderby)){
            return orderby;
        }

        return "latest_comment";
    }

    private void addFilterQueryParameters(Query query, String filterText){
        if(filterText != null && !filterText.isEmpty()){
            query.setParameter("filterText", "%"+filterText+"%");
        }
    }
}
