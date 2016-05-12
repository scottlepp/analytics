package org.voyager.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Resources;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

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
    public void send(@RequestParam(value="message") String message) {
    	ProducerRecord<String, String> data = new ProducerRecord<String, String>("user", "log", message);
    	
    	//KeyedMessage<String, String> data = new KeyedMessage<String, String>("user", "log", message);
    	producer.send(data);
    }
}
