/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.authentication;

import java.io.Serializable;
import java.util.Date;

/**
 * This model class represents the token value and the expiration date of the token.
 * 
 * @author Dgzt
 */
public class Token implements Serializable{
    
    //~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------     
    
    private static final long serialVersionUID = 1L;
    
    public Token(String token, Date expirationDate){
        this.token = token;
        this.expirationDate = expirationDate;
    }
    
    //~-----------------------------------------------------   
    //~ Member fields
    //~-----------------------------------------------------  
    /**
     * The token value.
     */
    private String token;
    
    /**
     * The expiration date of the token.
     */
    private Date expirationDate;

    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
