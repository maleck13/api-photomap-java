package me.photomap.web.controller;


import com.rabbitmq.client.QueueingConsumer;
import me.photomap.web.annotations.OpenAccess;
import me.photomap.web.service.AmqpService;
import me.photomap.web.service.exceptions.AmqpMessagingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class JobsController {

    @Autowired private AmqpService amqpService;

    ObjectMapper mapper = new ObjectMapper();

    Logger log = LoggerFactory.getLogger(JobsController.class);

    private static final String STATUS_KEY = "Status";

    @OpenAccess
    @RequestMapping(value = "/job/{jobid}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String,String>> getJobMessage(@PathVariable("jobid") String id)throws AmqpMessagingException{
        int done = 0;
        //put in scheduled executor return better structure
        List<Map<String,String>> ret = new ArrayList<>();
        List<QueueingConsumer.Delivery> d = amqpService.readResponseQue(id);
        TypeReference<HashMap<String,String>> typeRef
                = new TypeReference<HashMap<String,String>>() {};

        for(QueueingConsumer.Delivery ds : d){
            String val = new String(ds.getBody());
            try {
                HashMap<String, String> m = mapper.readValue(val, typeRef);
                ret.add(m);
                String status = m.get(STATUS_KEY);
                if("complete".equalsIgnoreCase(status) || "error".equalsIgnoreCase(status)){
                    // no more messages
                    log.debug("removing que " + id);
                    amqpService.removeQueue(id);
                }
            }catch(IOException e){
                throw new AmqpMessagingException("unparsable message " + e.getMessage(), e);
            }
        }
        return ret;


    }

}
