package me.photomap.web.controller;

import me.photomap.web.annotations.OpenAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class InfoController {


  @Autowired
  private Environment env;

  @RequestMapping(value = "/info/version", method = RequestMethod.GET)
  @OpenAccess
  @ResponseBody
  public Map<String, String> version() {
    HashMap map = new HashMap();
    map.put("version", env.getProperty("app.version"));
    return map;
  }

}
