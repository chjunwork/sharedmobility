package sharedmobility;

import sharedmobility.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PolicyHandler{

    @Autowired
    IssueRepository issueRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_(@Payload PaymentApproved paymentApproved){

        if(paymentApproved.isMe()){
            System.out.println("##### listener  paymentApproved: " + paymentApproved.toJson());

            Random rand = new Random();
            int key = rand.nextInt(10000);

            System.out.println("##### key value  : " + key);

            Issue issue = new Issue();

            issue.setAccessId(paymentApproved.getAccessId());
            issue.setPaymentId(paymentApproved.getId());
            issue.setStatus("Key is Generated!!");
            issue.setKey(key);
            issue.setPrice(paymentApproved.getPrice());

            issueRepository.save(issue);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverAccessCanceled_(@Payload AccessCanceled accessCanceled){

        if(accessCanceled.isMe()){
            System.out.println("##### listener  accessCanceled: " + accessCanceled.toJson());

            Issue issue = issueRepository.findByAccessId(accessCanceled.getId());

            issue.setStatus("Access is canceled!");
            issue.setKey(0);

            issueRepository.save(issue);
        }
    }

}
