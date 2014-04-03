/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.holdinarms.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This DTO contains the comment page information.
 *
 * @author Dgzt
 */
public class CommentPageDTO implements Serializable {
    
	//~-----------------------------------------------------   
    //~ Static fields
    //~-----------------------------------------------------
	private static final long serialVersionUID = 1L;

	//~-----------------------------------------------------   
    //~ Member fields
    //~----------------------------------------------------- 
	private List<CommentDTO> comments = new ArrayList<CommentDTO>();

    private Integer countComments = 0;
    
    //~-----------------------------------------------------   
    //~ Getters / setters
    //~-----------------------------------------------------
    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Integer getCountComments() {
        return countComments;
    }

    public void setCountComments(Integer countComments) {
        this.countComments = countComments;
    }

}
