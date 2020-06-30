package InterfaceLayer.DTO;

import DataAccessLayer.DALDTO.DALReport;

public class ReportDTO {

    private String creationDate;
    private String type;
    private String address;
    private String description;



    public ReportDTO(String creationDate, String type, String description,String address) {
        this.creationDate = creationDate;
        this.type = type;
        this.address = address;
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
