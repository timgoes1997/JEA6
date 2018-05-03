package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.entity.tag.Tag;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Stateless
@Path("tag")
public class TagBean {

    @Inject
    private TagDAO tagDAO;

    @Inject
    private Logger logger;

    @GET
    @Path("create/{tagname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTag(@PathParam("tagname") String tag) {
        try {
            Tag found = tagDAO.findTagByName(tag);
            return Response.ok().entity(found).build();
        }catch (Exception e){
            logger.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create/{tagname}")
    public Response createTag(@PathParam("tagname") String tag) {
        try {
            tagDAO.create(new Tag(tag));
            Tag found = tagDAO.findTagByName(tag);
            return Response.ok().entity(found).build();
        }catch (Exception e){
            logger.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

}
