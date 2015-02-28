package me.photomap.web.http.filters;

import me.photomap.web.data.repo.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by craigbrookes on 21/12/14.
 */
public class UserAwareHttpRequest extends HttpServletRequestWrapper {

  public static final String USER_ATTRIBUTE = "user";

  public UserAwareHttpRequest(HttpServletRequest request) {
    super(request);
  }


  public User getUser() {
    return (User) this.getAttribute(USER_ATTRIBUTE);
  }


}
