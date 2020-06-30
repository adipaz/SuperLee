package BusinessLayer.Shipments;

import DataAccessLayer.DALDTO.ShipmentDALDTO;
import DataStructures.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class Shipment {
    private int ID;
    private int nextSiteIndex;
    private LocalDate date;
    private LocalTime departureTime;
    private String truckNumber;
    private String driverName;
    private String bnNumber;
    private int shiftID;

    private String source;
    private List<Integer> certificationIDs;

    private double overallWeight;
    private List<String> comments;
    private String driverID;

    public Shipment(int ID){
        this.ID = ID;
        certificationIDs = new Vector<>();
        comments = new Vector<>();
        nextSiteIndex = 0;
    }

    public Shipment(ShipmentDALDTO dalShipment) {
        ID = dalShipment.getID();
        date = dalShipment.getDate();
        departureTime = dalShipment.getDepartureTime();
        truckNumber = dalShipment.getTruckNumber();
        driverName = dalShipment.getDriverName();
        source = dalShipment.getSource();
        certificationIDs = dalShipment.getCertificateIDs();
        overallWeight = dalShipment.getOverallWeight();
        comments = dalShipment.getComments();
        driverID = dalShipment.getDriverID();
        shiftID = dalShipment.getShiftID();
    }
    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public List<Integer> getCertificationIDs() {
        return certificationIDs;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDriverName() {
        return driverName;
    }

    public List<String> getComments() {
        return comments;
    }

    public double getOverallWeight() {
        return overallWeight;
    }

    public void addCertificate(int certificationID){
        certificationIDs.add(certificationID);
    }

    public void  calculateOverallWeight(List<Double> certificateWeight, double truckWeight){
        double weight=0;
        for (Double certificate_weight:certificateWeight) {
            weight+=certificate_weight;
        }
       overallWeight=weight+ truckWeight;
    }
    public boolean validateWeight(List<Double> certificateWeight, double truckWeight, double maxCarryWeight){
        calculateOverallWeight(certificateWeight, truckWeight);
        double carryWeight=overallWeight- truckWeight;
        return (maxCarryWeight>=carryWeight);
    }

    public boolean checkNextSiteIndex(){
        return (nextSiteIndex<certificationIDs.size());
    }

    public int getNextSiteCertificationID() {
        return certificationIDs.get(nextSiteIndex);
    }

    public Map<Integer, List<Pair<String, Integer>>> selectAllProducts() {
        return null;
    }

    public void removeAddress(int certificateID) {
        certificationIDs.remove(new Integer(certificateID));
    }
    public void addComment(String comment) {
        comments.add(comment);
    }
    public void sortByAreas(List<Pair<Integer,Character>> certificateToAreaList) {
        certificateToAreaList.sort((x, y) -> x.getSecond() - y.getSecond());
        certificationIDs=new Vector<>();
        certificateToAreaList.forEach((x)->certificationIDs.add(x.getFirst()));
    }

    public void setSource(String source) {
        this.source = source;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return nextSiteIndex == shipment.nextSiteIndex &&
                overallWeight == shipment.overallWeight &&
                Objects.equals(date, shipment.date) &&
                Objects.equals(departureTime, shipment.departureTime) &&
                Objects.equals(truckNumber, shipment.truckNumber) &&
                Objects.equals(driverName, shipment.driverName) &&
                Objects.equals(source, shipment.source) &&
                Objects.equals(certificationIDs, shipment.certificationIDs) &&
                Objects.equals(comments, shipment.comments);
    }

    public void removeCertificate(int certificateID) {
        for(int i=0;i<certificationIDs.size();i++){
            if(certificationIDs.get(i)==certificateID) {
                certificationIDs.remove(i);
                break;
            }
        }
    }
    public void setDriverName(String driverName) {
        this.driverName=driverName;
    }

    public void setShipmentDateAndTime(LocalTime departureTime, LocalDate date) {
        this.departureTime=departureTime;
        this.date=date;
    }

    public String getCurrentShipmentDriverID() {
        return this.driverID;
    }

    public void setDriverID(String id) {
        this.driverID=id;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public int getShiftID() {
        return shiftID;
    }
}
