

package me.photomap.web.controller;

import java.util.HashMap;
import java.util.Map;

import me.photomap.web.annotations.OpenAccess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {


  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  @OpenAccess
  public Map<String, String> showIndex() {
    Map<String, String> res = new HashMap<String, String>();
    res.put("message", "Hello world");
    return res;
  }
}
