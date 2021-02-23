package sharedmobility;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Mypage_table")
public class Mypage {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String mobilityId;
        private Integer useHour;
        private Integer key;
        private Long issueId;
        private Long paymentId;
        private Long price;
        private String status;
        private Long accessId;


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
        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }
        public Long getIssueId() {
            return issueId;
        }

        public void setIssueId(Long issueId) {
            this.issueId = issueId;
        }
        public Long getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
        }
        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public Long getAccessId() {
            return accessId;
        }

        public void setAccessId(Long accessId) {
            this.accessId = accessId;
        }

}
