package BusinessLayer;

import BusinessLayer.Enums.WorkingTimes;
import BusinessLayer.Shipments.*;
import BusinessLayer.SuppliersAndInventory.ProductS;
import BusinessLayer.SuppliersAndInventory.ShipmentRequest;
import BusinessLayer.Workers.*;
import DataStructures.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public interface IManager {

    /**
     * section of workers
     */
    boolean addWorker(String id, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAccID);
    boolean editWorkerByID(String ID , String firstName, String lastName , double salary, LocalDate startWorkingDate, int bankAccountID);
    boolean addJob(String jobName);
    boolean addAuthority(String authorityName);
    boolean addAuthorityToJob(int jobID, int authorityID);
    boolean addAuthorityToWorker(String workerID , int authorityID);
    boolean addWorkerWorkingTimes(String workerID , int day , WorkingTimes times);
    boolean createShift(String branchAddress , LocalDate date , boolean isDayShift);
    boolean addJobToShift(int shiftID , int jobID);
    boolean addWorkerToShift(int shiftID , int jobID , String workerID);
    List<Job> jobsAvailableToWorker(String workerID);
    List<Authority> getAuthorities();
    List<Worker> getWorkers();
    List<Job> getJobs();
    List<Shift> getShifts();
    List<Branch> getBranches();
    List<Pair<Branch,Shift>> getShiftHistory(String workerID);
    List<String> getEmploymentTerms(String workerID);
    boolean addEmploymentTermToWorker(String workerID , String term);
    //check if worker qualified to the job - has all authorities

    /**
     * section of shipments
     */

    //List<Site> getSourceSites();
    //List<Branch> getAvailableDestinationSites();
    ShipmentRequest initiateShipment();
    //void acceptCertificate(String address, Map<String, Integer> productIDQuantity);
    List<Truck> getTrucks();
    boolean chooseTruck(String truckLicenseNumber);
    Pair<LocalDate,LocalTime> setShipmentDateAndTime();

    //boolean checkDestination(String addressDestination); maybe not needed
    boolean checkValidQuantity(int quantity);
    boolean checkCatalogID(String catalogID);
    List<Worker> getAvailableDriversForTruck();
    boolean chooseDriver(String ID);
    List<Truck> getOtherTrucks();
    boolean chooseOtherTruck(String licenseNumber);
    //List<Site> showAddressesForCurrentShipment(); saved locally -not needed
    boolean removeAddress(String address);

    int removeProductsFromSite(String catalogID, int quantity, String address);
    //boolean checkCatalogIDInShipment(String catalogNum);
    boolean validateWeight();
    boolean containsOtherTrucks();
    List<Shipment> getShipments();
    List<Certificate> getCurrentShipmentCertificates();
    //Announce that the current shipment is ready and saves it
    void completeShipment();
    //Checks if the departure time is valid and sets it if valid
    //void setShipmentDateAndTime(LocalTime departureTime,LocalDate date);
    List<Certificate> getCertificates(List<Integer> certificationIDs);

    List<Authority> getAuthorities(int id);

    String getBranchByShiftID(int id);

    void abortShipment();

    void executeShipment(Shipment shipment);

    List<Shipment> getShipmentsReports();
}

