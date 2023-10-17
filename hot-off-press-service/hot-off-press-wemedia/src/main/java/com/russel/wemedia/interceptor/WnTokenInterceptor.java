package com.russel.wemedia.interceptor;

import com.russel.model.wemedia.pojos.WmUser;
import com.russel.utils.thread.WmThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Optional;

/**
 * author by Russel
 * created by 2023/10/17.
 */
@Slf4j
@Component
public class WnTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String  userId = request.getHeader("userId");
        Optional.ofNullable(userId).ifPresent(u -> {
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtil.setUser(wmUser);
            log.info("WnTokenInterceptor has accept the userId :{}",u);
        });
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("WnTokenInterceptor postHandle clear the userId");
        WmThreadLocalUtil.clear();
    }
}
