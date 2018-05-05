package com.github.timgoes1997.java.base;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class CORSFilter implements ContainerResponseFilter {
    @Context
    private HttpServletRequest request;

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        // "http://domain.com/path/to/url
        String r = request.getHeader("Referer");

        /*
        if (r != null) {
            r = request.getScheme() + "://" +   // "http" + "://
                    request.getServerName() + ":" + request.getServerPort();       // "myhost:port"
        }
        else {
            r = "*";
        }*/

        headers.add("Access-Control-Allow-Origin", "http://localhost:4200");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        headers.add("Access-Control-Allow-Headers", HttpHeaders.AUTHORIZATION + ", X-Requested-With, " +
                "X-Frame-Options, X-Powered-By, Content-Type, Content-Length, Server");
        headers.add("Access-Control-Expose-Headers", "Authorization");
        headers.add("Access-Control-Allow-Credentials", "true");

    }

}
