package tech.springboot.productQueryService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import tech.springboot.productQueryService.model.EventType;
import tech.springboot.productQueryService.model.Product;
import tech.springboot.productQueryService.model.SqsEvent;

@Log4j2
@Component
public class SqsEventConsumerService {

  @Autowired
  private ProductService productService;
  @Value ("${aws.sqs.url}")
  private String sqsUrl;
  @Autowired
  private SqsClient sqsClient;

  @Scheduled (fixedDelay = 1000)
  public void pollMessages () {
    log.info ("Polling Messages from SQS:: " + LocalDateTime.now ());
    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder ()
        .queueUrl (sqsUrl)
        .maxNumberOfMessages (10)
        .build ();

    ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage (
        receiveMessageRequest);
    List<Message> messages = receiveMessageResponse.messages ();

    for (Message message : messages) {
      log.info ("Received message: " + message.body ());
      ObjectMapper objectMapper = new ObjectMapper ();
      try {
        SqsEvent sqsEvent = objectMapper.readValue (message.body (), SqsEvent.class);
        String productString = sqsEvent.getEventPayload ();
        productService.saveProductSqsEvent (sqsEvent);
        log.info ("Saved SQS event message: {}", sqsEvent);
        executeDbOperation (EventType.valueOf (sqsEvent.getEventType ()), productString);
      } catch (JsonProcessingException e) {
        log.warn ("Error while parsing json string data to object:: SqsEvent");
        throw new RuntimeException (e);
      }
      log.info ("Deleting Product message: {}", message);
      sqsClient.deleteMessage (
          builder -> builder.queueUrl (sqsUrl).receiptHandle (message.receiptHandle ()));
    }
  }

  private void executeDbOperation (EventType eventType, String productString) {
    switch (eventType) {
      case CREATE_EVENT -> saveProduct (productString);
      case UPDATE_EVENT -> updateProduct (productString);
      case DELETE_EVENT -> deleteProduct (productString);
      default -> {
        log.info ("Nothing to be executed!");
      }
    }
  }

  public void saveProduct (String productString) {
    Product productFetched = null;

    ObjectMapper objectMapper = new ObjectMapper ();
    try {
      productFetched = objectMapper.readValue (productString, Product.class);
      productService.saveProduct (productFetched);
    } catch (JsonProcessingException e) {
      log.warn ("saveProduct: Error while parsing json string data to object:: Product");
      throw new RuntimeException (e);
    }
    log.info ("Saved Product message: {}", productString);
  }

  public void updateProduct (String productString) {
    Product productFetched = null;

    ObjectMapper objectMapper = new ObjectMapper ();
    try {
      productFetched = objectMapper.readValue (productString, Product.class);
      productService.saveProduct (productFetched);
    } catch (JsonProcessingException e) {
      log.warn ("updateProduct: Error while parsing json string data to object:: Product");
      throw new RuntimeException (e);
    }
    log.info ("Updated Product message: {}", productString);
  }

  public void deleteProduct (String productString) {
    Product productFetched = null;

    ObjectMapper objectMapper = new ObjectMapper ();
    try {
      productFetched = objectMapper.readValue (productString, Product.class);
      productService.deleteProduct (productFetched);
    } catch (JsonProcessingException e) {
      log.warn ("deleteProduct: Error while parsing json string data to object:: Product");
      throw new RuntimeException (e);
    }
    log.info ("Deleted Product message: {}", productString);
  }
}
