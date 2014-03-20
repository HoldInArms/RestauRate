/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This class encapsulates the database configuration. It reads from
 * the YML config file passed in on the command line.
 * 
 * @author Tibor Zsuro
 */
public class RestaurantRateConfiguration extends Configuration{
    
    //~-----------------------------------------------------   
    //~ Member fields
    //~-----------------------------------------------------      

    @Valid
    @NotNull
    private DatabaseConfiguration database = new DatabaseConfiguration();
    
    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------  
    
    @JsonProperty("database")
    public DatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabaseConfiguration(DatabaseConfiguration databaseConfiguration) {
        this.database = databaseConfiguration;
    }  
    
}
