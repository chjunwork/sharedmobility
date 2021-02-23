
package sharedmobility;

public class AccessCanceled extends AbstractEvent {

    private Long id;
    private String status;
    private String mobilityId;
    private Integer useHour;

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
}
