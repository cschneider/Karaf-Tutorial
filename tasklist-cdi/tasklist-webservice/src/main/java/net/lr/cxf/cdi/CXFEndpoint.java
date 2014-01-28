package net.lr.cxf.cdi;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Retention(value=RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface CXFEndpoint {
    @Nonbinding
    String url() default "";
}
