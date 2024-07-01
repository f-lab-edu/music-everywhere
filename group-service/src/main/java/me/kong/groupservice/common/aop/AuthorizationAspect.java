package me.kong.groupservice.common.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.groupservice.common.annotation.GroupId;
import me.kong.groupservice.common.annotation.GroupOnly;
import me.kong.groupservice.common.annotation.UserId;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.service.AuthService;
import me.kong.groupservice.service.ProfileService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final AuthService authService;
    private final ProfileService profileService;


    /**
     * 사용하는 메소드의 매개 변수에 @UserId와 @GroupId를 반드시 등록해야 합니다.
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(me.kong.groupservice.common.annotation.GroupOnly)")
    public Object checkGroupMember(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethodSignature(joinPoint);
        MethodInfo methodInfo = getMethodInfo(joinPoint, method);
        UserAndGroup userAndGroup = hasUserAndGroupAnnotation(methodInfo);
        GroupOnly groupOnly = method.getAnnotation(GroupOnly.class);

        GroupRole role = groupOnly.role();

        Profile profile = profileService.getLoggedInProfile(userAndGroup.userId(), userAndGroup.groupId());

        if (role == GroupRole.MEMBER) {
            if (!authService.isGroupMember(profile.getGroup(), profile)) {
                throw new UnAuthorizedException("그룹 멤버가 아닙니다.");
            }
        } else if (role == GroupRole.MANAGER) {
            if (!authService.isGroupManager(profile.getGroup(), profile)) {
                throw new UnAuthorizedException("그룹 매니저가 아닙니다.");
            }
        }

        return joinPoint.proceed();
    }

    private static Method getMethodSignature(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private static UserAndGroup hasUserAndGroupAnnotation(MethodInfo methodInfo) {
        Long userId = null;
        Long groupId = null;

        for (int i = 0; i < methodInfo.parameterAnnotations().length; i++) {
            for (Annotation annotation : methodInfo.parameterAnnotations()[i]) {
                if (annotation instanceof UserId) {
                    userId = (Long) methodInfo.args()[i];
                } else if (annotation instanceof GroupId) {
                    groupId = (Long) methodInfo.args()[i];
                }
            }
        }
        if (userId == null || groupId == null) {
            throw new IllegalArgumentException("@UserId, @GroupId 어노테이션이 없습니다.");
        }
        return new UserAndGroup(userId, groupId);
    }

    private static MethodInfo getMethodInfo(ProceedingJoinPoint joinPoint, Method method) {
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        return new MethodInfo(args, parameterAnnotations);
    }

    private record UserAndGroup(Long userId, Long groupId) {
    }

    private record MethodInfo(Object[] args, Annotation[][] parameterAnnotations) {
    }
}
