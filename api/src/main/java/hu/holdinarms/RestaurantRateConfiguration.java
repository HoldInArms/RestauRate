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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This class encapsulates the database configuration. It reads from
 * the YML config file passed in on the command line.
 * 
 * @author Dgzt
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
