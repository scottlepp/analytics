package org.voyager.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Resources;

@RestController
public class MessageController {

	static KafkaProducer<String, String> producer;
	
	static  {	    
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<String, String>(properties);
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @RequestMapping(value="/message", method=RequestMethod.POST)
    public void send(@RequestParam(value="message") String message, HttpServletRequest request) {
    	
    	JSONObject json = new JSONObject(message);
    	json.append("ip", request.getRemoteAddr());
//    	json.append("ip", "172.217.2.196");
    	
    	ProducerRecord<String, String> data = new ProducerRecord<String, String>("user", "log", json.toString());
    	
    	producer.send(data);
    }
}
