package me.rami.config;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;


@Configuration
@Component
public class SNSConfig {
	
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SNSConfig.class);
    
   
	AmazonSNSClient snsClient ;
	CreateTopicResult createTopicResult;
	CreateTopicRequest createTopicRequest;

	
	
    @SuppressWarnings("deprecation")
	@PostConstruct

    public void init()  {
    	
        LOGGER.info("initialisation du client SNS ..............");
        LOGGER.info("mes credentials "+getCredential());
		//create a new SNS client and set endpoint
        snsClient = new AmazonSNSClient(getCredential());
		snsClient.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
		//create a new SNS topic
		createTopicRequest = new CreateTopicRequest("MyNewTopic");
		 createTopicResult= snsClient.createTopic(createTopicRequest);
		//print TopicArn
	    LOGGER.info("createTopicResult .: ." + createTopicResult);
		//get request id for CreateTopicRequest from SNS metadata		
        LOGGER.info("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
   
    }
	
	

	public String getArn(){
		 final Logger LOGGER = LoggerFactory.getLogger(SNSConfig.class);
		 LOGGER.info("return Arn: "+createTopicResult.getTopicArn());
		return createTopicResult.getTopicArn();
		
	}
	
	public AmazonSNSClient getSnsClient(){
		return snsClient;
	}
	
	@SuppressWarnings("deprecation")

	public AWSCredentials getCredential() {
		 final Logger LOGGER = LoggerFactory.getLogger(SNSConfig.class);
		
		
		
		AWSCredentials credentials;
	    try {
	        credentials = new ProfileCredentialsProvider().getCredentials();
	       
	    } catch (Exception e) {
	        throw new AmazonClientException(
	                "Cannot load the credentials from the credential profiles file. " +
	                "Please make sure that your credentials file is at the correct " +
	                "location (~/.aws/credentials), and is in valid format.",
	                e);
	        
	    }
	    
	   
		AWSSecurityTokenServiceClient stsClient = new  AWSSecurityTokenServiceClient(credentials);
	    AssumeRoleRequest assumeRequest = new AssumeRoleRequest()
	            .withRoleArn("arn:aws:iam::347970623729:role/dae_from_support")
	            .withDurationSeconds(3600)
	            .withRoleSessionName("blabla");

	    AssumeRoleResult assumeResult = stsClient.assumeRole(assumeRequest);
	    BasicSessionCredentials temporaryCredentials =
	            new BasicSessionCredentials(
	                        assumeResult.getCredentials().getAccessKeyId(),
	                        assumeResult.getCredentials().getSecretAccessKey(),
	                        assumeResult.getCredentials().getSessionToken());
	    LOGGER.info("create temporary Credentials");
	    return temporaryCredentials;
	    
	    
	
	
};
}