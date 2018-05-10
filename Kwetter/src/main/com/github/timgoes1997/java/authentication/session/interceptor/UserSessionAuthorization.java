package com.github.timgoes1997.java.authentication.session.interceptor;

import com.github.timgoes1997.java.entity.user.UserRole;

import javax.interceptor.InterceptorBinding;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface UserSessionAuthorization {
    UserRole[] value() default {};
}
