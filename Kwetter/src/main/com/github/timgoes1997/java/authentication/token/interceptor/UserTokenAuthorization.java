package com.github.timgoes1997.java.authentication.token.interceptor;

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
public @interface UserTokenAuthorization {
    boolean requiresUser() default false;
    UserRole[] allowed() default {};
    boolean onlySelf() default false;
    UserRole[] onlySelfExceptions() default {};
    //UserRole only self exceptions
}
