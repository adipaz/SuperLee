package PresentationLayer;

import DataStructures.Pair;
import InterfaceLayer.Controller;
import InterfaceLayer.DTO.*;
import InterfaceLayer.IShipmentController;
import Exception.OurException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ErrorHandlingController implements IShipmentController {

    IShipmentController controller = Controller.getInstance();


    @Override
    public ShipmentRequestDTO popNextShipmentRequest() {
        try{
            return controller.popNextShipmentRequest();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  null;
        }
    }

    @Override
    public boolean chooseTruck(String truckLicenseNumber) {
        try{
            return controller.chooseTruck(truckLicenseNumber);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public boolean checkValidQuantity(int quantity) {
        try{
            return controller.checkValidQuantity(quantity);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public boolean checkCatalogID(String catalogID) {
        try{
            return controller.checkCatalogID(catalogID);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public boolean chooseDriver(String id) {
        try{
            return controller.chooseDriver(id);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public boolean validateWeight() {
        try{
            return controller.validateWeight();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public boolean chooseOtherTruck(String licenseNum) {
        try{
            return controller.chooseOtherTruck(licenseNum);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public boolean removeAddress(String address) {
        try{
            return controller.removeAddress(address);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    @Override
    public int removeProducts(String catalogID, int quantity, String address) {
        try{
            int ret = controller.removeProducts(catalogID, quantity, address);
            if(ret == -1){
                System.out.println("Couldn't remove product");
            }
            return ret;
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    @Override
    public boolean otherTrucksExists() {
        try{
            return controller.otherTrucksExists();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public void createShipment() {
        try{
            controller.createShipment();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Pair<LocalDate, LocalTime> setShipmentDepartureTime() {
        try{
            return controller.setShipmentDepartureTime();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }


    @Override
    public List<TruckInterfaceDTO> getOtherTrucks() {
        try{
            return controller.getOtherTrucks();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return new Vector<>();
        }
    }

    @Override
    public List<ShipmentInterfaceDTO> getShipments(String[] args) {
        try{
            return controller.getShipments(args);
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return new Vector<>();
        }
    }


    @Override
    public List<TruckInterfaceDTO> getTrucks() {
        try{
            return controller.getTrucks();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return new Vector<>();
        }
    }

//    @Override
//    public List<SiteInterfaceDTO> getSourceSites() {
//        try{
//            return controller.getSourceSites();
//        }
//        catch (OurException ex){
//            System.out.println(ex.getMessage());
//            return new Vector<>();
//        }
//    }
//
//    @Override
//    public List<SiteInterfaceDTO> getAvailableDestinationSites() {
//        try{
//            return controller.getAvailableDestinationSites();
//        }
//        catch (OurException ex){
//            System.out.println(ex.getMessage());
//            return new Vector<>();
//        }
//    }

    @Override
    public List<DriverInterfaceDTO> getDriversForTruck() {
        try{
            return controller.getDriversForTruck();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return new Vector<>();
        }
    }

    @Override
    public List<CertificateInterfaceDTO> getCurrentShipmentCertificates() {
        try{
            return controller.getCurrentShipmentCertificates();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
            return new Vector<>();
        }
    }

    @Override
    public void abortShipment() {
        try{
            controller.abortShipment();
        }
        catch (OurException ex){
            System.out.println(ex.getMessage());
        }
    }
}
