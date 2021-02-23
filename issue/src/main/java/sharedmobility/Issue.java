package sharedmobility;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Issue_table")
public class Issue {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String status;
    private Integer key;
    private Long paymentId;
    private Long accessId;
    private Long price;

    @PostPersist
    public void onPostPersist(){
        KeyGenerated keyGenerated = new KeyGenerated();
        BeanUtils.copyProperties(this, keyGenerated);
        keyGenerated.publishAfterCommit();


    }

    @PostUpdate
    public void onPostUpdate(){
        IssueCanceled issueCanceled = new IssueCanceled();
        BeanUtils.copyProperties(this, issueCanceled);
        issueCanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }




}
