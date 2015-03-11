package launch;

import me.photomap.web.config.WebConfig;
import me.photomap.web.http.filters.CORSFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

public class AppInitializer implements WebApplicationInitializer {

  Logger logger = LoggerFactory.getLogger(AppInitializer.class);


  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(WebConfig.class);
    ctx.setServletContext(servletContext);
    ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
    dynamic.setAsyncSupported(true);
    dynamic.addMapping("/");
    dynamic.setLoadOnStartup(1);



    FilterRegistration corsFilter = servletContext.addFilter("corsFilter", CORSFilter.class);
    corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, "/*");
    logger.debug("registered cors filter");


  }


}