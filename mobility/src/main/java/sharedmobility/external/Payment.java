package sharedmobility.external;

public class Payment {

    private Long id;
    private Long price;
    private String status;
    private Long accessId;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
