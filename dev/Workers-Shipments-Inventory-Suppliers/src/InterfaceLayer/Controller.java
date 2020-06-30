package InterfaceLayer;

import BusinessLayer.Enums.WorkingTimes;
import BusinessLayer.IManager;
import BusinessLayer.Manager;
import BusinessLayer.Shipments.Certificate;
import BusinessLayer.SuppliersAndInventory.*;
import BusinessLayer.Workers.Authority;
import BusinessLayer.Workers.Job;
import DataStructures.Pair;
import InterfaceLayer.DTO.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements IShipmentController{
    //Fields
    private final IManager manager;

    /* Singleton-------------------------------------------------------------------------------------------------------- */
    private static Controller instance = null;
    private Controller() {
        manager = Manager.getInstance();
    }
    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }
    /* ----------------------------------------------------------------------------------------------------------------- */


    /* Shipment module commands----------------------------------------------------------------------------------------- */
    //Initiates a new shipment with a given source site address @Param source
//    public boolean initiateShipment(String source) {
//        return manager.initiateShipment(source);
//    }
    public ShipmentRequestDTO popNextShipmentRequest(){
        ShipmentRequest sr = manager.initiateShipment();
        if(sr != null)
            return new ShipmentRequestDTO(sr);
        return null;
    }
    //After filling a full certificate add it to the business
//    public void acceptCertificate(String destinationAddress, Map<String, Integer> productIDQuantity) {
//        manager.acceptCertificate(destinationAddress, productIDQuantity);
//    }
    //Choose a truck by its license number @Param for the current built shift
    public boolean chooseTruck(String truckLicenseNumber) {
        return manager.chooseTruck(truckLicenseNumber);
    }
    //Choose a driver by his id for the current shipment
    public boolean chooseDriver(String id) {
        return manager.chooseDriver(id);
    }
    //Choose a new truck for the current shipment
    public boolean chooseOtherTruck(String licenseNum) {
        return manager.chooseOtherTruck(licenseNum);
    }
    //Remove branch from destinations by its address from the current shipment
    public boolean removeAddress(String address) {
        return manager.removeAddress(address);
    }
    //Remove products from current shipment by its catalogID from the current shipment
    //Returns the number of products removed
    public int removeProducts(String catalogID, int quantity,String address) {
        return manager.removeProductsFromSite(catalogID, quantity,address);
    }
    public void createShipment() {
        manager.completeShipment();
    }

    public Pair<LocalDate, LocalTime> setShipmentDepartureTime() {
        return manager.setShipmentDateAndTime();
    }


    /* Shipment module queries------------------------------------------------------------------------------------------ */
    public List<TruckInterfaceDTO> getOtherTrucks() {
        return manager.getOtherTrucks().stream()
                    .map(TruckInterfaceDTO::new)
                    .collect(Collectors.toList());
    }
    public List<ShipmentInterfaceDTO> getShipments(String [] args) {
        return manager.getShipmentsReports().stream()
                .map((shipment -> {
                    List<Certificate> certificates =  manager.getCertificates(shipment.getCertificationIDs());
                    List<CertificateInterfaceDTO> certificatesDTO = certificatesToDTO(certificates);
                    return new ShipmentInterfaceDTO(shipment, certificatesDTO);
                }))
                .collect(Collectors.toList());
    }
//    public List<ProductS_DTO> getProducts() {
//        return manager.getProducts().stream()
//                .map(ProductS_DTO::new)
//                .collect(Collectors.toList());
//    }
    public List<TruckInterfaceDTO> getTrucks() {
        return manager.getTrucks().stream()
                .map(TruckInterfaceDTO::new)
                .collect(Collectors.toList());
    }

//    public List<SiteInterfaceDTO> getSourceSites() {
//        return manager.getSourceSites().stream()
//                .map(SiteInterfaceDTO::new)
//                .collect(Collectors.toList());
//    }
//    //Available means the current shipment's shift has a storekeeper
//    public List<SiteInterfaceDTO> getAvailableDestinationSites() {
//        return manager.getAvailableDestinationSites().stream()
//                .map(SiteInterfaceDTO::new)
//                .collect(Collectors.toList());
//    }
//
    public List<DriverInterfaceDTO> getDriversForTruck() {
        return manager.getAvailableDriversForTruck().stream()
                .map((driver -> {
                    List<String> authoritiesNames = authoritiesToNames(manager.getAuthorities(), driver.getAuthoritiesID());
                    return new DriverInterfaceDTO(driver, authoritiesNames);
                }))
                .collect(Collectors.toList());
    }
    public List<CertificateInterfaceDTO> getCurrentShipmentCertificates() {
        List<Certificate> certificates = manager.getCurrentShipmentCertificates();
        return certificatesToDTO(certificates);
    }

    @Override
    public void abortShipment() {
        manager.abortShipment();
    }

    //Checks if there are other trucks in the system not includes the current shipment's truck
    public boolean otherTrucksExists() {
        return manager.containsOtherTrucks();
    }
    //Validate the current shipment weight
    public boolean validateWeight() {
        return manager.validateWeight();
    }
    //Check if the given quantity is valid
    public boolean checkValidQuantity(int quantity){
        return manager.checkValidQuantity(quantity);
    }
    //Check if the given catalog id is valid
    public boolean checkCatalogID(String catalogID){
        return manager.checkCatalogID(catalogID);
    }

    /* Workers module commands------------------------------------------------------------------------------------------*/
    public boolean addWorker(String id, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAcc) {
        return manager.addWorker(id, firstName, lastName, salary, startWorkingDate, bankAcc);
    }
    public boolean editWorkerByID(String id, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAcc) {
        return manager.editWorkerByID(id, firstName, lastName, salary, startWorkingDate, bankAcc);
    }
    public boolean addJob(String jobName) {
        return manager.addJob(jobName);
    }
    public boolean addAuthority(String authorityName) {
        return manager.addAuthority(authorityName);
    }
    public boolean addAuthorityToJob(int jobID, int authorityID) {
        return manager.addAuthorityToJob(jobID, authorityID);
    }
    public boolean addAuthorityToWorker(String workerID, int authorityID) {
        return manager.addAuthorityToWorker(workerID, authorityID);
    }
    public boolean addWorkerWorkingTimes(String workerID, int dayInWeek, boolean day, boolean night) {
        return manager.addWorkerWorkingTimes(workerID, dayInWeek,
                (day&night)? WorkingTimes.both :
                (day)? WorkingTimes.day :
                (night)? WorkingTimes.night :
                WorkingTimes.none
        );
    }
    public boolean createShift(String branchAddress, LocalDate date, boolean isDay) {
        return manager.createShift(branchAddress, date, isDay);
    }
    public boolean addJobToShift(int shiftID, int jobID) {
        return manager.addJobToShift(shiftID, jobID);
    }
    public boolean addWorkerToShift(int shiftID, int jobID, String workerID) {
        return manager.addWorkerToShift(shiftID, jobID, workerID);
    }
    public boolean addEmploymentTerm(String workerID, String term) {
        return manager.addEmploymentTermToWorker(workerID, term);
    }

    /* Workers module queries-------------------------------------------------------------------------------------------*/
    public List<WorkerInterfaceInterfaceDTO> getWorkers() {
        return manager.getWorkers().stream()
                .map((worker -> {
                    List<String> authoritiesNames = authoritiesToNames(manager.getAuthorities(), worker.getAuthoritiesID());
                    return new WorkerInterfaceInterfaceDTO(worker, authoritiesNames);
                }))
                .collect(Collectors.toList());

    }
    public List<DriverInterfaceDTO> getMinimalWorkers(){
        return manager.getWorkers().stream()
                .map((worker -> {
                    List<String> authoritiesNames = authoritiesToNames(manager.getAuthorities(), worker.getAuthoritiesID());
                    return new DriverInterfaceDTO(worker, authoritiesNames);
                }))
                .collect(Collectors.toList());
    }
    public List<ShiftInterfaceDTO> getShifts() {
        return manager.getShifts().stream()
                .map((shift -> {
                    Map<String, Integer> nameQuantity = idNumTonNameNum(manager.getJobs(), shift.getJobsNumbers());
                    Map<String, String> workerJob = workerIDToWorkerName(manager.getJobs(), shift.getWorkersJobs());
                    return new ShiftInterfaceDTO(shift, manager.getBranchByShiftID(shift.getID()), nameQuantity, workerJob);
                }))
                .collect(Collectors.toList());
    }
    public List<JobInterfaceDTO> getJobs() {
        List<Job> jobs = manager.getJobs();
        return getJobInterfaceDTOS(jobs);
    }
    public List<AuthorityInterfaceDTO> getAuthorities() {
        return manager.getAuthorities().stream()
                .map(AuthorityInterfaceDTO::new)
                .collect(Collectors.toList());
    }
    public List<String> getEmploymentTerms(String workerID) {
        return manager.getEmploymentTerms(workerID);
    }
    public List<JobInterfaceDTO> getWorkerAvailableJobs(String workerID) {
        List<Job> availableJobs = manager.jobsAvailableToWorker(workerID);
        return getJobInterfaceDTOS(availableJobs);
    }
    public List<ShiftInterfaceDTO> getShiftHistory(String workerID) {
        return manager.getShiftHistory(workerID).stream()
                .map((branchShift -> {
                    Map<String, Integer> nameQuantity = idNumTonNameNum(manager.getJobs(), branchShift.getSecond().getJobsNumbers());
                    Map<String, String> workerJob = workerIDToWorkerName(manager.getJobs(), branchShift.getSecond().getWorkersJobs());
                    return new ShiftInterfaceDTO(branchShift.getSecond(), branchShift.getFirst().getAddress(), nameQuantity, workerJob);
                }))
                .collect(Collectors.toList());
    }
    public List<BranchInterfaceDTO> getBranches() {
        return manager.getBranches().stream()
                .map(BranchInterfaceDTO::new)
                .collect(Collectors.toList());
    }

    private List<CertificateInterfaceDTO> certificatesToDTO(List<Certificate> certificates) {
        return certificates.stream()
                .map((certificate -> {
                    Map<String, Integer> productQuantity = certificate.getProductsQuantities();
                    Map<ProductForShipmentDTO, Integer> dummyQuantity = new HashMap<>();

                    Map<String, Pair<String, Double>> nameAndWeightPerID = SharedClass.getSharedClass().getProductsMap();
                    productQuantity.keySet().forEach((productID)->dummyQuantity.put(
                            getProduct(nameAndWeightPerID, productID),

                            productQuantity.get(productID)
                    ));

                    return new CertificateInterfaceDTO(certificate.getAddress(), dummyQuantity);
                }))
                .collect(Collectors.toList());

    }
    private ProductForShipmentDTO getProduct(Map<String, Pair<String, Double>> products, String productID) {
        return new ProductForShipmentDTO(products.get(productID).getFirst(), productID, products.get(productID).getSecond());
    }
    private List<String> authoritiesToNames(List<Authority> authorities) {
        return authorities.stream()
                .map((Authority::getName))
                .collect(Collectors.toList());
    }
    private List<String> authoritiesToNames(List<Authority> authorities, List<Integer> authoritiesID) {
        return authorities.stream()
                .filter(authority -> authoritiesID.contains(authority.getID()))
                .map((Authority::getName))
                .collect(Collectors.toList());

    }
    private Map<String, String> workerIDToWorkerName(List<Job> jobs, Map<String, Integer> workersJobs) {
        Map<String, String> workerName = new HashMap<>();
        for (Map.Entry<String, Integer> workerJob:workersJobs.entrySet()) {
            for (Job job:jobs) {
                if(job.getID() == workerJob.getValue()){
                    workerName.put(workerJob.getKey(), job.getName());
                }
            }
        }
        return workerName;


    }
    private Map<String, Integer> idNumTonNameNum(List<Job> jobs, Map<Integer, Integer> jobsNumbers) {
        Map<String, Integer> nameQuantity = new HashMap<>();
        for (Map.Entry<Integer, Integer> jobIDQuantity:jobsNumbers.entrySet()) {
            for (Job job:jobs) {
                if(job.getID() == jobIDQuantity.getKey()){
                    nameQuantity.put(job.getName(), jobIDQuantity.getValue());
                }
            }
        }
        return  nameQuantity;
    }
    private List<JobInterfaceDTO> getJobInterfaceDTOS(List<Job> availableJobs) {
        return availableJobs.stream()
                .map((job)->{
                    List<String> authoritiesName = authoritiesToNames(manager.getAuthorities(job.getID()));
                    return new JobInterfaceDTO(job, authoritiesName);
                })
                .collect(Collectors.toList());
    }

    public List<ShipmentInterfaceDTO> getShipmentsReports() {
        return manager.getShipmentsReports().stream()
                .map((shipment -> {
                    List<Certificate> certificates =  manager.getCertificates(shipment.getCertificationIDs());
                    List<CertificateInterfaceDTO> certificatesDTO = certificatesToDTO(certificates);
                    return new ShipmentInterfaceDTO(shipment, certificatesDTO);
                }))
                .collect(Collectors.toList());
    }
}


