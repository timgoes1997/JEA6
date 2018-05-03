package com.github.timgoes1997.java.authentication.interceptor;

import com.github.timgoes1997.java.entity.user.UserRole;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface UserAuthorization {
    UserRole[] value() default {};
}
