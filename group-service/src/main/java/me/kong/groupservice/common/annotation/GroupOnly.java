package me.kong.groupservice.common.annotation;


import me.kong.groupservice.domain.entity.profile.GroupRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupOnly {
    GroupRole role();
}
