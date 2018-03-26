package beans;

import entity.message.InitialMessage;
import entity.message.Message;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
@Path("message")
public class MessageBean {

    @PersistenceContext(unitName = "Kwetter")
    private EntityManager em;

    private List<Message> messageList;

    @PostConstruct
    public void init(){
        messageList = new ArrayList<>();
        messageList.add(new InitialMessage("Hello"));
        messageList.add(new InitialMessage("Hello"));
        messageList.add(new InitialMessage("Hello"));
        messageList.add(new InitialMessage("Hello"));
        messageList.add(new InitialMessage("Hello"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getMessageByID(@PathParam("id") int id){
        if(messageList.size() > id){
            Message test = new InitialMessage("Hello");
            em.persist(test);
            return Response.ok(messageList.get(id)).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        }
    }

}
