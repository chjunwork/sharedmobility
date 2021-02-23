package sharedmobility;

import sharedmobility.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MypageViewHandler {


    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAccessed_then_CREATE_1 (@Payload Accessed accessed) {
        try {
            if (accessed.isMe()) {
                // view 객체 생성
                Mypage mypage  = new Mypage();
                // view 객체에 이벤트의 Value 를 set 함
                mypage.setAccessId(accessed.getId());
                mypage.setMobilityId(accessed.getMobilityId());
                mypage.setUseHour(accessed.getUseHour());
                mypage.setStatus(accessed.getStatus());
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenKeyGenerated_then_UPDATE_1(@Payload KeyGenerated keyGenerated) {
        try {
            if (keyGenerated.isMe()) {

                System.out.println("##### Mypage  KeyGenerated: " + keyGenerated.toJson());
                // view 객체 조회
                List<Mypage> mypageList = mypageRepository.findByAccessId(keyGenerated.getAccessId());
                for(Mypage mypage  : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set
                    mypage.setIssueId(keyGenerated.getId());
                    mypage.setStatus(keyGenerated.getStatus());
                    mypage.setKey(keyGenerated.getKey());
                    mypage.setPaymentId(keyGenerated.getPaymentId());
                    mypage.setPrice(keyGenerated.getPrice());
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentApproved_then_UPDATE_2(@Payload PaymentApproved paymentApproved) {
        try {
            if (paymentApproved.isMe()) {

                System.out.println("##### Mypage  PaymentApproved: " + paymentApproved.toJson());
                // view 객체 조회
                List<Mypage> mypageList = mypageRepository.findByAccessId(paymentApproved.getAccessId());
                for(Mypage mypage  : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setPaymentId(paymentApproved.getId());
                    mypage.setPrice(paymentApproved.getPrice());
                    mypage.setStatus(paymentApproved.getStatus());
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenIssueCanceled_then_UPDATE_3(@Payload IssueCanceled issueCanceled) {
        try {
            if (issueCanceled.isMe()) {

                System.out.println("##### Mypage  IssueCanceled: " + issueCanceled.toJson());
                // view 객체 조회
                List<Mypage> mypageList = mypageRepository.findByAccessId(issueCanceled.getAccessId());
                for(Mypage mypage  : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setStatus(issueCanceled.getStatus());
                    mypage.setPaymentId(issueCanceled.getPaymentId());
                    mypage.setKey(issueCanceled.getKey());
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentCanceled_then_UPDATE_4(@Payload PaymentCanceled paymentCanceled) {
        try {
            if (paymentCanceled.isMe()) {

                System.out.println("##### Mypage  PaymentCanceled: " + paymentCanceled.toJson());
                // view 객체 조회
                List<Mypage> mypageList = mypageRepository.findByAccessId(paymentCanceled.getAccessId());
                for(Mypage mypage  : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setStatus(paymentCanceled.getStatus());
                    mypage.setPrice(paymentCanceled.getPrice());
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}