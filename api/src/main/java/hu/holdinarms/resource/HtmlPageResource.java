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
package hu.holdinarms.resource;

import java.io.IOException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Resource for html pages.
 * 
 * @author Dgzt
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HtmlPageResource {
	
	/**
	 * Get the index.html.
	 * 
	 * @return The page content.
	 */
	@GET
	public Response index(){
		String pageContent = "";
		
		try{
			URL clientPage = Resources.getResource("index.html");
			pageContent = Resources.toString(clientPage, Charsets.UTF_8);
		}catch( IOException e ){
			return Response.serverError().build();
		}
		
		return Response.ok(pageContent).build();
	}

}
