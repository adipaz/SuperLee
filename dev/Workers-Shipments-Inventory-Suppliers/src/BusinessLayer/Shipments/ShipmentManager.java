package BusinessLayer.Shipments;
import BusinessLayer.SuppliersAndInventory.SharedClass;
import BusinessLayer.SuppliersAndInventory.ShipmentRequest;
import DataAccessLayer.DALDTO.ShipmentDALDTO;
import DataAccessLayer.Handlers.CertificateHandler;
import DataAccessLayer.Handlers.ShipmentHandler;
import DataStructures.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ShipmentManager {
    private static ShipmentManager instance=null;
    private int autoID;
    private final Map<Integer, Shipment> shipments = new HashMap<>();
    private ShipmentHandler shipmentHandler;
    private List<ShipmentRequest> requestsForShipments;
    private CertificateHandler certificateHandler;

    private ShipmentManager(){
        shipmentHandler = ShipmentHandler.getInstance();
        autoID = shipmentHandler.loadAutoID();
        requestsForShipments= new Vector<>();
        certificateHandler = CertificateHandler.getInstance();
    }

    public static ShipmentManager getInstance(){
        if(instance==null)
            instance=new ShipmentManager();
        return instance;
    }

    public ShipmentRequest getNextRequest(){
        if(requestsForShipments.size()>0)
            return requestsForShipments.remove(0);
        return null;
    }

    public Shipment getShipmentToExecute(int shipmentID) {
        return shipments.get(shipmentID);
    }

    public void initiateShipment(ShipmentRequest request){
        autoID++;
        Shipment currentShipment = new Shipment(autoID);
        currentShipment.setSource(request.getSupplierAddress());
        currentShipment.setBnNumber(request.getBnNumber());
        shipments.put(autoID,currentShipment);
    }


    public void addCertificate(int currentCertificateID) {
        shipments.get(autoID).addCertificate(currentCertificateID);
    }

    public void sortByArea(List<Pair<Integer,Character>> certificateToAreaList ) {
        shipments.get(autoID).sortByAreas(certificateToAreaList);
    }

    public void addTruckToCurrentShipment(String truckLicenseNumber) {
        shipments.get(autoID).setTruckNumber(truckLicenseNumber);
    }

    public boolean validateWeight(List<Double> certificateWeights , double truckWeight, double maxCarryWeight) {
        return shipments.get(autoID).validateWeight(certificateWeights , truckWeight , maxCarryWeight);
    }

    public String getCurrentTruckNum() {
        return shipments.get(autoID).getTruckNumber();
    }

    public void addComment(String s) {
        shipments.get(autoID).addComment(s);
    }

    public List<Integer> showCerificateIdsCurrentShipment() {
        return shipments.get(autoID).getCertificationIDs();
    }

    public void removeAddress(int certificateID){
        shipments.get(autoID).removeAddress(certificateID);
    }

    public List<Integer> getAllCertificateIDs() {
        return shipments.get(autoID).getCertificationIDs();
    }

    public LocalDate getCurrentShipmentDate() {
        return shipments.get(autoID).getDate();
    }

    public LocalTime getCurrentShipmentHour() {
        return shipments.get(autoID).getDepartureTime();
    }

    public void setDriverName(String driverName) {
        shipments.get(autoID).setDriverName(driverName);
    }

    public List<Shipment> getAllShipments() {
        for (ShipmentDALDTO dalShipment : shipmentHandler.loadAll()) {
            if (!shipments.containsKey(dalShipment.getID()))
                shipments.put(dalShipment.getID(), new Shipment(dalShipment));
        }
        List<Shipment> allShipments=new Vector<>();
        allShipments.addAll(shipments.values());
        return allShipments;
    }

    public void setShipmentDateAndTime(LocalTime departureTime, LocalDate date) {
        shipments.get(autoID).setShipmentDateAndTime(departureTime,date);
    }

    public String getCurrentShipmentDriverID() {
        return shipments.get(autoID).getCurrentShipmentDriverID();
    }

    public void setDriverID(String id) {
        shipments.get(autoID).setDriverID(id);
    }

    public int getAutoID() { return autoID; }

    public void saveShipment(int shiftID) {
        shipmentHandler.save(toDTO(shipments.get(autoID), shiftID));
    }

    public ShipmentDALDTO toDTO(Shipment shipment, int shiftID) {
        return new ShipmentDALDTO(autoID, shipment.getDate(), shipment.getDepartureTime(),
                shipment.getTruckNumber(), shipment.getDriverName(), shipment.getDriverID(), shipment.getSource(),
                shipment.getCertificationIDs(), shipment.getOverallWeight(), shipment.getComments(), shiftID);
    }

    public void abortCurrentShipment() {
        shipments.remove(autoID);
    }

    public void addRequestForShipment(ShipmentRequest shipmentRequest){
        for(ShipmentRequest request: requestsForShipments){
            if(request.getSupplierAddress().equals(shipmentRequest.getSupplierAddress())){
                request.combine(shipmentRequest);
                return;
            }
        }
        requestsForShipments.add(shipmentRequest);
    }

    public List<Shipment> getAllShipmentsForExecution() {
        shipmentHandler.loadAll().forEach(shipmentDALDTO -> shipments.put(shipmentDALDTO.getID(),new Shipment(shipmentDALDTO)));
        List<Shipment> shipmentsToExecute= new Vector<>();
        for(Shipment shipment: shipments.values()){
            if(shipment.getDate().equals(SharedClass.getSharedClass().getDATE()))
                shipmentsToExecute.add(shipment);
        }
        return shipmentsToExecute;
    }

    public String getCurrentShipmentSupplierAddress() {
        return shipments.get(autoID).getSource();
    }

    public List<Shipment> getShipmentsReports(LocalDate date) {
        List<Shipment> shipmentsToReport= new Vector<>();
        shipmentHandler.loadToReport().forEach(shipmentDALDTO -> shipmentsToReport.add(new Shipment(shipmentDALDTO)));

        return shipmentsToReport;
    }

    public String getCurrentShipmentBnNumber() {
        return shipments.get(autoID).getBnNumber();
    }

    public void update(Shipment shipmentToExecute) {
        shipmentHandler.update(toDTO(shipmentToExecute,0));
    }
}
