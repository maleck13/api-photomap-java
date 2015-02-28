package me.photomap.web.http.filters;

import me.photomap.web.http.interceptors.UserResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

  Logger logger = LoggerFactory.getLogger(CORSFilter.class);

  @PostConstruct
  public void setUp() {
    logger.info("cors filter called");
    System.out.println("cors filter setup");
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    logger.info("cors filter doFilter");
    HttpServletResponse response = (HttpServletResponse) res;
    UserAwareHttpRequest request = new UserAwareHttpRequest((HttpServletRequest) req);
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, " + UserResolver.USER_ID_HEADER + ", " + UserResolver.USER_SESSION_ID_HEADER + "");

    chain.doFilter(request, res);
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }
}