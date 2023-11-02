package com.russel.search.interceptor;

import com.russel.model.user.pojos.ApUser;
import com.russel.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * author by Russel
 * created by 2023/10/17.
 */
@Slf4j
@Component
public class AppTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String  userId = request.getHeader("userId");
        Optional.ofNullable(userId).ifPresent(u -> {
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtil.setUser(apUser);
            log.info("AppTokenInterceptor has accept the userId :{}",u);
        });
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AppTokenInterceptor postHandle clear the userId");
        AppThreadLocalUtil.clear();
    }
}
