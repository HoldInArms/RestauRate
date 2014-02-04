/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms;

import com.fiestacabin.dropwizard.guice.AutoConfigService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import hu.holdinarms.model.Admin;
import hu.holdinarms.model.Comment;
import hu.holdinarms.model.Restaurant;
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

        @Override
        public DatabaseConfiguration getDatabaseConfiguration(RestaurantRateConfiguration configuration) {
            return configuration.getDatabaseConfiguration();
        }
    };
    
    //@Inject
    //private UserDao userDao;
    
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

            @Override
            public DatabaseConfiguration getDatabaseConfiguration(RestaurantRateConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(RestaurantRateConfiguration configuration, Environment environment) throws Exception {
        super.run(configuration, environment);
        //environment.addProvider(new OAuthProvider<User>(new UserAuthenticator(userDao), "SUPER SECRET STUFF"));
    }
}
