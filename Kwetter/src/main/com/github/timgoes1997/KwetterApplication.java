package com.github.timgoes1997;

import com.github.timgoes1997.java.base.CORSFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Test
 * @author tim_g
 */
@ApplicationPath("/api")
public class KwetterApplication extends ResourceConfig{


    public KwetterApplication() {
        packages("com.github.timgoes1997");
        register(MultiPartFeature.class);
        register(CORSFilter.class);
    }
}
