package me.photomap.web.http.interceptors;

import me.photomap.web.data.repo.model.User;
import me.photomap.web.http.filters.UserAwareHttpRequest;
import me.photomap.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UserResolver extends HandlerInterceptorAdapter {

  private
  @Autowired
  UserService userService;

  public static final String USER_ID_HEADER = "x-user-id";
  public static final String USER_SESSION_ID_HEADER = "x-session-id";

  Logger log = LoggerFactory.getLogger(UserResolver.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String sessionHeader = request.getHeader(USER_SESSION_ID_HEADER);

    String userHeader = request.getHeader(USER_ID_HEADER);

    if (request.getMethod().equalsIgnoreCase("get")) {
      sessionHeader = (sessionHeader == null) ? request.getParameter("sessid") : sessionHeader;
      userHeader = (userHeader == null) ? request.getParameter("userid") : userHeader;
    }
    log.info("resolving user from headers userheader " + userHeader + " session " + sessionHeader);
    if (null != sessionHeader && null != userHeader) {
      User reqUser = userService.verifyUserAndSession(userHeader, sessionHeader);
      if (null != reqUser) {
        request.setAttribute(UserAwareHttpRequest.USER_ATTRIBUTE, reqUser);
      }
    }

    return super.preHandle(request, response, handler);
  }
}
