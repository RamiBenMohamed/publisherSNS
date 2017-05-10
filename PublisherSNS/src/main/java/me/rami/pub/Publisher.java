package me.rami.pub;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import me.rami.config.SNSConfig;
@Service
public class Publisher {
	
	@Autowired
	SNSConfig sns;

	public void publier(String msg){
		//publish to an SNS topic
		PublishRequest publishRequest = new PublishRequest(sns.getArn().toString(), msg);
		PublishResult publishResult = sns.getSnsClient().publish(publishRequest);
		//print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());

	}

}