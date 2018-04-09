package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.entity.tag.Tag;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("tag")
public class TagBean {

    @Inject
    private TagDAO tagDAO;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create/{tagname}")
    public void getTag(@PathParam("tagname") String tag) {
        tagDAO.create(new Tag(tag));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create/{tagname}")
    public void createTag(@PathParam("tagname") String tag) {
        tagDAO.create(new Tag(tag));
    }

}
