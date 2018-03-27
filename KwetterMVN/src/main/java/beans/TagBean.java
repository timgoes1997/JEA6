package beans;

import dao.interfaces.TagDAO;
import entity.tag.Tag;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("tag")
public class TagBean {

    @Inject
    private TagDAO tagDAO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{tagname}")
    public void createTag(@PathParam("tagname") String tag) {
        tagDAO.create(new Tag(tag));
    }

}
