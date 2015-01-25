package me.photomap.web.http.interceptors;

import me.photomap.web.annotations.OpenAccess;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.http.filters.UserAwareHttpRequest;
import me.photomap.web.service.exceptions.NoAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by craigbrookes on 23/12/14.
 */
public class AccessInterceptor extends HandlerInterceptorAdapter {

    Logger log = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserAwareHttpRequest req = (UserAwareHttpRequest) request;
        User u = req.getUser();

        if (handler instanceof HandlerMethod) {
            log.debug("checking access on handler " + ((HandlerMethod) handler).getBean() + " " + ((HandlerMethod) handler).getMethod());
            HandlerMethod methodHandler = (HandlerMethod) handler;
            OpenAccess access = methodHandler.getMethodAnnotation(OpenAccess.class);
            if(null != access){
                log.debug("access is open on handler  " + ((HandlerMethod) handler).getBean() + " " + ((HandlerMethod) handler).getMethod());
                return true;
            }
            else if(null == u){
                log.debug("no user present throwing on handler " + ((HandlerMethod) handler).getBean() + " " + ((HandlerMethod) handler).getMethod());
                throw new NoAccessException();
            }
        }
        return true;
    }
}
