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

import com.fiestacabin.dropwizard.guice.AutoConfigService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;

import hu.holdinarms.authentication.UserAuthenticator;
import hu.holdinarms.model.Admin;
import hu.holdinarms.dao.AdminDao;
import hu.holdinarms.model.Comment;
import hu.holdinarms.model.Restaurant;
import hu.holdinarms.resource.HtmlPageResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the main Service class, the entry point of the API.
 * 
 * @author Dgzt
 */
public class RestaurantRateService extends AutoConfigService<RestaurantRateConfiguration>{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantRateService.class);
    
    //~-----------------------------------------------------   
    //~ Private fields
    //~----------------------------------------------------- 
    private final HibernateBundle<RestaurantRateConfiguration> hibernateBundle = 
            new HibernateBundle<RestaurantRateConfiguration>(Restaurant.class, Comment.class,Admin.class) {

        public DatabaseConfiguration getDatabaseConfiguration(RestaurantRateConfiguration configuration) {
            return configuration.getDatabaseConfiguration();
        }
    };
    
    @Inject
    private AdminDao adminDao;
    
    //~-----------------------------------------------------   
    //~ Main method
    //~-----------------------------------------------------      
    /**
     * The main entry point of the server.
     *
     * @param args The command line arguments passed to the server
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new RestaurantRateService().run(args);
    }
    
    public RestaurantRateService(){
        super("hu.holdinarms","hu.holdinarms.resource");
    }
    
    //~-----------------------------------------------------   
    //~ AutoConfigService implementation
    //~-----------------------------------------------------      
    @Override
    protected Injector createInjector(RestaurantRateConfiguration configuration) {
        LOGGER.debug("*** createInjector called...");
        RestaurantRateModule restaurantBlacklistModule = new RestaurantRateModule(hibernateBundle.getSessionFactory(), configuration);
        return Guice.createInjector(restaurantBlacklistModule);
    }
    
    //~-----------------------------------------------------   
    //~ Service implementation
    //~-----------------------------------------------------   
    @Override
    public void initialize(Bootstrap<RestaurantRateConfiguration> bootstrap) {
        LOGGER.debug("*** initialize called...");
        bootstrap.addBundle(new MigrationsBundle<RestaurantRateConfiguration>() {

            public DatabaseConfiguration getDatabaseConfiguration(RestaurantRateConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
        
        bootstrap.addBundle(new AssetsBundle("/assets/"));
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(RestaurantRateConfiguration configuration, Environment environment) throws Exception {
        super.run(configuration, environment);
        environment.addProvider(new OAuthProvider<Admin>(new UserAuthenticator(adminDao), "SUPER SECRET STUFF"));
    }
}
