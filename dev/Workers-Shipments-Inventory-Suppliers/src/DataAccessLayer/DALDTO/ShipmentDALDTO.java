package DataAccessLayer.DALDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ShipmentDALDTO implements DALDTO {
    private int ID;
    private LocalDate date;
    private LocalTime departureTime;
    private String truckNumber;
    private String driverName;
    private String source;
    private List<Integer> certificateIDs;
    private double overallWeight;
    private List<String> comments;
    private int shiftID;
    private String driverID;

    public ShipmentDALDTO(int id, LocalDate date, LocalTime departureTime, String truckNumber,
                          String driverName, String driverID, String source,
                          List<Integer> certificateIDs, double overallWeight, List<String> comments, int shiftID) {
        ID = id;
        this.date = date;
        this.departureTime = departureTime;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        this.driverID = driverID;
        this.source = source;
        this.certificateIDs = certificateIDs;
        this.overallWeight = overallWeight;
        this.comments = comments;
        this.shiftID = shiftID;
    }

    public int getID() {
        return ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getSource() {
        return source;
    }

    public List<Integer> getCertificateIDs() {
        return certificateIDs;
    }

    public double getOverallWeight() {
        return overallWeight;
    }

    public List<String> getComments() {
        return comments;
    }

    public int getShiftID() {
        return shiftID;
    }

    public String getDriverName() {
        return driverName;
    }
}
