package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.token.interceptor.UserTokenAuthorization;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.services.beans.interfaces.TagService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("tag")
public class TagBean {

    @Inject
    private TagService tagService;

    @GET
    @Path("{tagname}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getTagMessages(@PathParam("tagname") String tag) {
        return tagService.getTagMessages(tag);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization(requiresUser = true,
            allowed = {UserRole.User, UserRole.Moderator, UserRole.Admin},
            onlySelf = true)
    @Path("create/{tagname}")
    public Tag createTag(@PathParam("tagname") String tag) {
        return tagService.createTag(tag);
    }

}
