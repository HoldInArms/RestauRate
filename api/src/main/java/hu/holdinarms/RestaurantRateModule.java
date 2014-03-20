/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;

/**
 * This Guice module binds the SessionFactory to be used in dependency injection
 * scenarios.
 * 
 * @author Tibor Zsuro
 */
public class RestaurantRateModule extends AbstractModule{
    
    /**
     * The Hibernate session factory, provided by the main service class.
     */
    private SessionFactory sessionFactory;
    
    /**
     * The holdinarms configuration class instance, provided by the main service class.
     */
    private RestaurantRateConfiguration restaurantBlacklistConfiguration;
    
    public RestaurantRateModule(SessionFactory sessionFactory, RestaurantRateConfiguration restaurantBlacklistConfiguration) {
        this.sessionFactory = sessionFactory;
        this.restaurantBlacklistConfiguration = restaurantBlacklistConfiguration;
    }

    @Override
    protected void configure() {
        //Use the default implementation in this module.
    }
    
    @Provides
    @Singleton
    SessionFactory provideSessionFactory() {
        return sessionFactory;
    }
    
    @Provides
    @Singleton
    RestaurantRateConfiguration provideRestaurantBlacklistConfiguration(){
        return restaurantBlacklistConfiguration;
    }
}
