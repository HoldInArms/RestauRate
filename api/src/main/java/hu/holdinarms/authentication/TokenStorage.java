/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
