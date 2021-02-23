package sharedmobility;

import sharedmobility.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverIssueCanceled_(@Payload IssueCanceled issueCanceled){

        if(issueCanceled.isMe()){
            System.out.println("##### listener  : " + issueCanceled.toJson());

            Payment payment = paymentRepository.findByAccessId(issueCanceled.getAccessId());
            payment.setPrice(0L);
            payment.setStatus("Issue is canceled!");

            paymentRepository.save(payment);
        }
    }

}
