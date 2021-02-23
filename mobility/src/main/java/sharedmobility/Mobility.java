package sharedmobility;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Mobility_table")
public class Mobility {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String mobilityId;
    private Integer useHour;
    private String status;

    @PostPersist
    public void onPostPersist(){
        Accessed accessed = new Accessed();
        BeanUtils.copyProperties(this, accessed);
        accessed.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        sharedmobility.external.Payment payment = new sharedmobility.external.Payment();
        // mappings goes here
        payment.setAccessId(this.getId());
        payment.setPrice(this.getUseHour()*5000L);
        payment.setStatus("Access Mobility!");

        MobilityApplication.applicationContext.getBean(sharedmobility.external.PaymentService.class)
            .pay(payment);


    }

    @PostUpdate
    public void onPostUpdate(){
        AccessCanceled accessCanceled = new AccessCanceled();
        BeanUtils.copyProperties(this, accessCanceled);
        accessCanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMobilityId() {
        return mobilityId;
    }

    public void setMobilityId(String mobilityId) {
        this.mobilityId = mobilityId;
    }
    public Integer getUseHour() {
        return useHour;
    }

    public void setUseHour(Integer useHour) {
        this.useHour = useHour;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
