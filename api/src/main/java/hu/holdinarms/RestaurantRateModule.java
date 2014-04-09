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
package hu.holdinarms;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;

/**
 * This Guice module binds the SessionFactory to be used in dependency injection
 * scenarios.
 * 
 * @author Dgzt
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
