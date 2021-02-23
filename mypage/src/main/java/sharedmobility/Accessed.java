package sharedmobility;

public class Accessed extends AbstractEvent {

    private Long id;
    private String mobilityId;
    private Integer useHour;
    private String status;

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