package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.interceptor.UserAuthorization;
import com.github.timgoes1997.java.authentication.token.TokenProvider;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import org.eclipse.persistence.internal.libraries.antlr.runtime.Token;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@Path("user")
public class UserBean {

    @Inject
    private UserDAO userDAO;

    @Inject
    private Logger logger;

    @Inject
    private TokenProvider tokenProvider;

    @Resource(name="mail/kwetter")
    private Session session;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("username") String username, @FormParam("password") String password){
        try{
            User user = userDAO.authenticate(username, password); //TODO: In future encrypt password using salt and secret key.

            Set<String> authorities = new HashSet<>(); //TODO: Better authorities implementation.
            authorities.add(user.getRole().toString());

            String token = tokenProvider.issueToken(user.getUsername(), authorities, false); //TODO: remember me implementation

            return Response.ok().header(HttpHeaders.AUTHORIZATION, Constants.BEARER + token).build();

        }catch(Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(@FormParam("username") String username,
                             @FormParam("password") String password,
                             @FormParam("email") String email,
                             @FormParam("firstName") String firstName,
                             @FormParam("middleName") String middleName,
                             @FormParam("lastName") String lastName,
                             @FormParam("telephone") String telephone){
        try{
            userDAO.create(new User(username, password, UserRole.User, firstName, middleName, lastName, email, telephone, false));

            logger.info("Created new account for user: " + username);

            User user = userDAO.findByUsername(username);
            user.setVerifyLink(generateVerificationLink(user));
            userDAO.edit(user);

            sendVerificationMail(user);

            return authenticate(username, password);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Path("/notoken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response noTokenGetRequest(){
        return Response.ok("Hello").build();
    }

    @GET
    @Path("/token")
    @UserAuthorization
    @Produces(MediaType.APPLICATION_JSON)
    public Response tokenGetRequest(){
        return Response.ok("Hello").build();
    }

    private String generateVerificationLink(User user){
        Random random = new SecureRandom();
        random.setSeed(user.getId());
        String token = new BigInteger(130, random).toString(32);
        if(userDAO.verificationLinkExists(token)){
            return generateVerificationLink(user);
        }else{
            return token;
        }
    }

    private void sendVerificationMail(User user) throws MessagingException {
        Message message = new MimeMessage(session);

        logger.info("Set subject");
        message.setSubject("Verify email for Kwetter");

        logger.info("Set from");
        message.setFrom();

        logger.info("Set body");
        BodyPart messageGreeting = new MimeBodyPart();
        messageGreeting.setText("Hello " + user.getFirstName() + ",");

        BodyPart messageBody = new MimeBodyPart();
        messageBody.setText(System.lineSeparator()
                            + "Please use this verification link to verify your email:"
                            + System.lineSeparator()
                            + "http://localhost:8080/Kwetter/api/user/verify/" + user.getVerifyLink());

        BodyPart signatureBody = new MimeBodyPart();
        signatureBody.setText(System.lineSeparator()
                            + "Team Kwetter");

        logger.info("Set multipart");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageGreeting, 0);
        multipart.addBodyPart(messageBody, 1);
        multipart.addBodyPart(signatureBody, 2);

        logger.info("Set content message");
        message.setContent(multipart);

        logger.info("Set header mail");
        message.setHeader("X-mailer", "Java Mail API");

        logger.info("Set send date");
        message.setSentDate(new Date());

        logger.info("Set recipient");
        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false)[0]);

        logger.info("Send mail");
        Transport.send(message);
    }


}
