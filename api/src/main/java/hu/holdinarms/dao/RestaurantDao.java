/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.dao;

import hu.holdinarms.model.Admin;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.model.dto.RestaurantDTO;

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
    
    public List<RestaurantDTO> findAll( Admin admin ){
        String queryString = "SELECT id FROM rr_restaurants";
        if( admin == null ){
            queryString += " WHERE live = TRUE";
        }
        Query query = currentSession().createSQLQuery(queryString);

        List<BigInteger> queryResult = query.list();
        List<RestaurantDTO> results = new ArrayList<RestaurantDTO>();
        for(BigInteger bigInteger : queryResult){
            Restaurant restaurant = get(bigInteger.longValue());
            RestaurantDTO restaurantDto = new RestaurantDTO();
            restaurantDto.setName(restaurant.getName());
            //restaurant.setVotes(commentDao.getVotesByRestaurantId(bigInteger.longValue()));
            //restaurant.setAvarge(commentDao.getAvargeByRestaurantId(bigInteger.longValue()));
            results.add(restaurantDto);
        }
        
        return results;
    }

    public Long countRestaurants( Admin admin, String filterText){
        StringBuilder filterBuilder = new StringBuilder();

        addFilter(filterBuilder, admin, filterText);

        String queryString = "select COUNT(id) from rr_restaurants where true=true ";

        if(!filterBuilder.toString().isEmpty()){
            queryString += filterBuilder.toString();
        }

        Query query = currentSession().createSQLQuery(queryString);

        addFilterQueryParameters(query, filterText);
        
        return ((BigInteger)query.uniqueResult()).longValue();
    }

    public List<RestaurantDTO> getRestaurants(Admin admin, Integer from, Integer to, String orderby, String direction, String filterText){
        if(from == null || to == null){
            return new ArrayList<RestaurantDTO>();
        }

        if(direction == null || direction.isEmpty()){
            direction = "DESC";
        }

        StringBuilder filterBuilder = new StringBuilder();
        addFilter(filterBuilder, admin, filterText);
        
        String queryString = "select id from (" +
        "	select row_number() over (order by :orderby :direction ) as rowNumber, * from (" +
        "		select restaurants.id, restaurants.name ," +
        "		(select comments.createdate from rr_comments as comments" +
        "			where comments.live = true and comments.restaurant_id = restaurants.id" +
        "			order by comments.createdate DESC LIMIT 1" +
        "		) as latest_comment," +
        "		(select SUM(comments.vote)/CAST(count(comments.id) AS float4) " +
        "			from rr_comments as comments " +
        "			where comments.live = true and comments.restaurant_id = restaurants.id" +
        "		) as avarge," +
        "		(select COUNT(id) from rr_comments as comments " +
        "			where comments.live = true and" +
        "			comments.restaurant_id = restaurants.id" +
        "		) as votes" +
        "		from rr_restaurants as restaurants where true=true ";
        
        if(!filterBuilder.toString().isEmpty()){
            queryString += filterBuilder.toString();
        }

        queryString += " ) AS rowNumberSelect ) AS rowNumberFilter WHERE rowNumber BETWEEN :from AND :to";
        
        queryString = queryString.replace(":direction", direction);
        queryString = queryString.replace(":orderby", getOrderByProperty(orderby));
       
        Query query = currentSession().createSQLQuery(queryString);
        query.setParameter("from", from);
        query.setParameter("to", to);

        addFilterQueryParameters(query, filterText);

        @SuppressWarnings("unchecked")
		List<BigInteger> queryResult = query.list();
        List<RestaurantDTO> result = new ArrayList<RestaurantDTO>();
        System.out.println(queryString);
        for(BigInteger bigInteger : queryResult){
            Restaurant restaurant = get(bigInteger.longValue());
            
            RestaurantDTO restaurantDto = new RestaurantDTO();
            restaurantDto.setName(restaurant.getName());
            restaurantDto.setLastComment(commentDao.lastCommentByRestaurantId(bigInteger.longValue()));
            restaurantDto.setAverage(commentDao.getAvargeByRestaurantId(bigInteger.longValue()));
            restaurantDto.setVotes(commentDao.getVotesByRestaurantId(bigInteger.longValue()));
            
            result.add(restaurantDto);
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

    private void addFilter(StringBuilder filterBuilder, Admin admin, String filterText){
        if( admin == null ){
            filterBuilder.append("and live = true ");
        }
        
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
