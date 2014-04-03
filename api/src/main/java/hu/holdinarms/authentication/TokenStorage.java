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
package hu.holdinarms.authentication;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.time.DateUtils;

/**
 * This is a storage class which stores the tokens for the users.
 * 
 * @author Dgzt
 */
public class TokenStorage {
    
    /**
     * The map where the tokens are storing.
     */
    private static ConcurrentHashMap<Long, Token> userTokenHashMap = new ConcurrentHashMap<Long, Token>();
    
    /**
     * The number of the extended minutes.
     */
    private static final int timeExtendedMinutes = 60;
    
    /**
     * Get the token by the user id.
     * 
     * @param userId The user id.
     * @return 
     */
    public static String getUserToken(Long userId){
        Token token = userTokenHashMap.get(userId);
        if (token == null) {
            return null;
        }
        return token.getToken();
    }
    
    /**
     * Generate new token to the user. The system uses this method when the user makes the first authentication.
     * 
     * @param userId The user id.
     * @return 
     */
    public static String generateUserToken(Long userId){
        String tokenValue = UUID.randomUUID().toString();
        Date expirationDate = DateUtils.addMinutes(new Date(), timeExtendedMinutes);
        userTokenHashMap.put(userId, new Token(tokenValue, expirationDate));
        
        return tokenValue;
    }
    
    /**
     * Extend the expiration date of token of the user.
     * 
     * @param userId The user id.
     */
    public static void extendExpirationDate(Long userId){
        Token token = userTokenHashMap.get(userId);
        token.setExpirationDate(DateUtils.addMinutes(token.getExpirationDate(), timeExtendedMinutes));
    }
    
    /**
     * Get the user id by the token value.
     * 
     * @param token The token value
     * @return The user id
     */
    public static Long getUserIdByToken(String token){
        for(Map.Entry<Long, Token> entry : userTokenHashMap.entrySet()){
            if (entry.getValue().getToken().equals(token)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * This method checks that the given token expiration date is valid (after the current date)
     * 
     * @param token
     * @return 
     */
    public static boolean isTokenValid(String token){
        for(Map.Entry<Long, Token> entry : userTokenHashMap.entrySet()){
            if (entry.getValue().getToken().equals(token) && entry.getValue().getExpirationDate().after(new Date())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Remove user token from the storage. This method is used by the logout process.
     * 
     * @param userId 
     */
    public static void removeUsertoken(Long userId){
        userTokenHashMap.remove(userId);
    }
}
