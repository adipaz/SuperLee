package BusinessLayer;

import BusinessLayer.Enums.SpecialJobs;
import BusinessLayer.Enums.SpecialAuthorities;
import BusinessLayer.Enums.WorkingTimes;
import BusinessLayer.Shipments.*;
import BusinessLayer.SuppliersAndInventory.*;
import BusinessLayer.Workers.*;
import DataAccessLayer.DALDTO.BranchDALDTO;
import DataStructures.Pair;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import Exception.OurException;

public class Manager implements IManager{
    private static Manager instance;
    /**
     *The manager singletons of the system
     */
    private final ShiftManager shiftManager;
    private final WorkerManager workerManager;
    private final JobsAuthoritiesData jobsAuthoritiesData;
    private final CertificateManager certificateManager;
    private final ShipmentManager shipmentManager;
    private final BranchManager branchManager;
    private final TrucksManager trucksManager;
    private final Store store;

    //shift history holds for every worker , list of the shifts he worked in and their branches
    private final Map<Worker, List<Pair<Branch, Shift>>> shiftsHistory;
    private final Map<Branch,List<Shift>> branchShifts;
    //TODO: when accepting shipment , add it to shift

    private Manager(){
        this.shiftManager=ShiftManager.getInstance();
        this.workerManager=WorkerManager.getInstance();
        this.jobsAuthoritiesData=JobsAuthoritiesData.getInstance();
        certificateManager=CertificateManager.getInstance();
        shipmentManager=ShipmentManager.getInstance();
        trucksManager=TrucksManager.getInstance();
        branchManager= BranchManager.getInstance();
        store=Store.getInstance();
        shiftsHistory=new HashMap<>();
        branchShifts=new HashMap<>();



        for (Branch branch:  branchManager.getAllBranches()) {
            List<Shift> shifts = shiftManager.getBranchShifts(branch.getAddress());
            branchShifts.put(branch, shifts);
        }
//
//        //adding workers to the map- for test purposes
//        for (Worker worker: workerManager.getAllWorkers()){
//            List<Pair<Branch,Shift>> list=new Vector<>();
//            shiftsHistory.put(worker,list);
//        }
//
//        LocalDate date= LocalDate.of(2020,12,12);
//        createShift("Remez",date,true);
//        createShift("Alef",date,true);
//        for (Map.Entry<Branch,List<Shift>> pair:branchShifts.entrySet()) {
//            if(pair.getKey().getAddress().equals("Remez")){
//                pair.getValue().add(shiftManager.getAllShifts().get(0));
//                break;
//            }
//        }
//
//        for (Map.Entry<Branch,List<Shift>> pair:branchShifts.entrySet()) {
//            if(pair.getKey().getAddress().equals("Alef")){
//                pair.getValue().add(shiftManager.getAllShifts().get(0));
//                break;
//            }
//        }
//
//        addWorkerWorkingTimes("208084004",7,WorkingTimes.both);
//        addWorkerWorkingTimes("206368722",7,WorkingTimes.both);
//        addWorkerWorkingTimes("318773439",7,WorkingTimes.both);
//        addWorkerWorkingTimes("211905641",7,WorkingTimes.both);
//        addWorkerWorkingTimes("211111111",7,WorkingTimes.both);
//
////        addAuthorityToWorker("208084004",SpecialAuthorities.License.getNumVal()); *
//
//        addAuthorityToWorker("208084004",SpecialAuthorities.LicenseD.getNumVal());
//        addAuthorityToWorker("206368722",SpecialAuthorities.Cancel_card_holder.getNumVal());
//        addAuthorityToWorker("206368722", SpecialAuthorities.ManageTeam.getNumVal());
//        addAuthorityToWorker("211111111",SpecialAuthorities.Cancel_card_holder.getNumVal());
//        addAuthorityToWorker("211111111", SpecialAuthorities.ManageTeam.getNumVal());
//        addAuthorityToWorker("318773439",SpecialAuthorities.StoreKeeping.getNumVal());
//        addAuthorityToWorker("211905641",SpecialAuthorities.StoreKeeping.getNumVal());
//
//        addJobToShift(0,SpecialJobs.Driver.getNumVal());
////        addJobToShift(0,SpecialJobs.Manager.getNumVal()); *
//        addJobToShift(0,SpecialJobs.StoreKeeper.getNumVal());
//
////        addJobToShift(1,SpecialJobs.Manager.getNumVal());
//        addJobToShift(1,SpecialJobs.StoreKeeper.getNumVal());
//
//        addWorkerToShift(0,SpecialJobs.Driver.getNumVal(),"208084004");
//        addWorkerToShift(0,SpecialJobs.Manager.getNumVal(),"206368722");
//        addWorkerToShift(0,SpecialJobs.StoreKeeper.getNumVal(),"318773439");
//
//        addWorkerToShift(1,SpecialJobs.Manager.getNumVal(),"211111111");
//        addWorkerToShift(1,SpecialJobs.StoreKeeper.getNumVal(),"211905641");


    }
    public static Manager getInstance(){
        if(instance==null)
            instance=new Manager();
        return instance;
    }

    @Override
    public boolean addWorker(String id, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAccID) {
        if(this.workerManager.isWorkerExists(id)) {
            OurException exception = new OurException("Worker ID already exists");
            throw exception;
        }
        this.workerManager.addWorker(id, firstName,lastName,salary, startWorkingDate,  bankAccID );
        Worker worker = this.workerManager.getWorkerByID(id);
        List<Pair<Branch , Shift>> workerShiftHistory = new ArrayList<>();
        shiftsHistory.put(worker , workerShiftHistory);
        return true;
    }

    @Override
    public boolean editWorkerByID(String ID, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAccountID) {
        return this.workerManager.editWorkerByID(ID ,firstName,lastName , salary, startWorkingDate,bankAccountID);
    }

    public boolean addJob(String jobName){
        return this.jobsAuthoritiesData.addJob(jobName);

    }

    public boolean addAuthority(String authorityName){
        return this.jobsAuthoritiesData.addAuthority(authorityName);
    }

    public boolean addAuthorityToJob(int jobID, int authorityID){
        return this.jobsAuthoritiesData.addAuthorityToJob(jobID, authorityID);
    }

    public boolean addAuthorityToWorker(String workerID , int authorityID){
        //@TODO right now its a bit hard coded, another solution will be great (user can't add the generic license authority)
        if(authorityID == SpecialAuthorities.License.getNumVal() || !this.jobsAuthoritiesData.isAuthorityExists(authorityID)) {
            OurException exception = new OurException("Authority ID doesn't exist");
            throw exception;
        }
        return this.workerManager.addAuthorityToWorker(workerID,authorityID);
    }

    public boolean addWorkerWorkingTimes(String workerID , int day , WorkingTimes times) {
        return this.workerManager.addWorkerWorkingTimes(workerID, day , times);
    }

    public boolean createShift(String branchAddress , LocalDate date , boolean isDayShift) {
        Branch branch = getBranchByAddress(branchAddress);
        List<Shift> shiftsByBranch = branchShifts.get(branch);
        for (Shift shift:shiftsByBranch) {
            if(shift.getDate().equals(date) && shift.isDayShift()==isDayShift){//exists shift in the same branch
                //in the same date and time - return exception
                throw new OurException("ERROR: Other shift already exists in branch , with these date and time");
            }
        }
        Shift shift =  shiftManager.createShift(date,isDayShift);
        shiftManager.getShiftHandler().save(shiftManager.toDTO(shift, branchAddress));
        if(branch==null){
            OurException exception = new OurException("Branch address doesn't exist");
            throw exception;
        }
        if(branchShifts.keySet().contains(branch)) {
            branchShifts.get(branch).add(shift);
            return true;
        }
        else
            throw new RuntimeException("ERROR: Branch was found in SiteManager but not in branchShifts in manager");
    }

    public boolean addJobToShift(int shiftID , int jobID) {
        if(!this.jobsAuthoritiesData.isJobExists(jobID)) {
            OurException exception = new OurException("Job ID doesn't exist");
            throw exception;
        }
        return this.shiftManager.addJobToShift(shiftID,jobID);
    }

    public boolean addWorkerToShift(int shiftID , int jobID , String workerID) {
        if(!this.workerManager.isWorkerExists(workerID)) {
            OurException exception = new OurException("Worker ID doesn't exist");
            throw exception;
        }
        if(!this.jobsAuthoritiesData.isJobExists(jobID)) {
            OurException exception = new OurException("Job ID doesn't exist");
            throw exception;
        }
        if(!this.shiftManager.isJobExistsInShift(shiftID,jobID))
            throw new OurException("The job doesn't exist or has the max amount for this job");

        if(!this.shiftManager.isShiftExists(shiftID)) {
            OurException exception = new OurException("Shift ID doesn't exist");
            throw exception;
        }
        if(!checkQualified(workerID , jobID)) {
            OurException exception = new OurException("Worker is not qualified to this job");
            throw exception;
        }
        Shift shift = this.shiftManager.getShiftByID(shiftID);
        if(!isAvailableWorkerInDate(shift.getDate() ,shift.isDayShift() , workerID)) {
            OurException exception = new OurException("Worker is not available in this time and date");
            throw exception;
        }
        if(!canWorkInShiftTime(workerID,shiftID)) {
            OurException exception = new OurException("Worker can't work in shift time");
            throw exception;
        }
        //adding worker to current shift
        this.shiftManager.addWorkerToShift(shiftID, jobID , workerID);
        addShiftToHistory(workerID,shiftID);
        if (jobID== SpecialJobs.Manager.getNumVal())
            this.shiftManager.addManagerIDToShift(shiftID , workerID);
        return true;
    }

    private boolean isAvailableWorkerInDate(LocalDate date, boolean dayShift , String workerID) {
        List<Shift> shiftsInDateTime  = this.shiftManager.getShiftsInDateTime(date, dayShift);
        for (Shift shift:shiftsInDateTime) {
            if(shift.getWorkersJobs().keySet().contains(workerID))//worker already works in shift
                //in this date and this time
                return false;
        }
        return true;
    }

    public List<Job> jobsAvailableToWorker(String workerID) {
        List<Integer> workerAuthoritiesID= this.workerManager.getAuthoritiesIDOfWorker(workerID);
        return this.jobsAuthoritiesData.getAvailableJobsByAuthorities(workerAuthoritiesID);
    }

    public List<Authority> getAuthorities(){//not returning authority license - user should not know
        return this.jobsAuthoritiesData.getAllAuthorities().stream()
                .filter((authority -> authority.getID() != SpecialAuthorities.License.getNumVal()))
                .collect(Collectors.toList());
    }

    public List<Worker> getWorkers(){
        return this.workerManager.getAllWorkers();
    }

    public List<Job> getJobs(){
        return this.jobsAuthoritiesData.getAllJobs();
    }

    public List<Shift> getShifts(){
        for (Branch branch : branchManager.getAllBranches()) {
            List<Shift> shifts = shiftManager.getBranchShifts(branch.getAddress());
            if (branchShifts.containsKey(branch))
                branchShifts.get(branch).addAll(shifts);
            else
                branchShifts.put(branch, shifts);
        }
        return this.shiftManager.getAllShifts();
    }

    public List<Branch> getBranches(){
        return this.branchManager.getAllBranches();
    }
    //TODO: INON load the data from DB
    public List<Pair<Branch,Shift>> getShiftHistory(String workerID) {
        if(!this.workerManager.isWorkerExists(workerID)) {
            OurException exception = new OurException("Worker ID doesn't exist");
            throw exception;
        }
        for (Worker worker:this.shiftsHistory.keySet()) {
            if(worker.getID().equals(workerID))
                return this.shiftsHistory.get(worker);
        }
        List<Pair<Branch, Shift>> history = new ArrayList<>();
        List<Pair<String, Shift>> historyAddresses = shiftManager.loadShiftHistory(workerID);
        for (Pair<String, Shift> historyAddress : historyAddresses) {
            shiftManager.isShiftExists(historyAddress.getSecond().getID());
            history.add(new Pair(getBranchByAddress(historyAddress.getFirst()), shiftManager.getShiftByID(historyAddress.getSecond().getID())));
        }

        shiftsHistory.put(workerManager.getWorkerByID(workerID), history);
        return history;
    }

    public List<String> getEmploymentTerms(String workerID) {
        return this.workerManager.getEmploymentTerms(workerID);
    }

    public boolean addEmploymentTermToWorker(String workerID , String term) {
        return this.workerManager.addEmploymentTermToWorker(workerID , term);
    }
    //TODO: load worker , jobs . authorities
    //check if worker qualified to the job - has all authorities
    private boolean checkQualified(String workerID , int jobID) {

        List<Integer> workerAuthorities = this.workerManager.getAuthoritiesOfWorker(workerID);
        List<Authority> jobAuthorities = jobsAuthoritiesData.getAuthoritiesByJob(jobID);

        if(jobID == SpecialJobs.Driver.getNumVal()){
            return workerAuthorities.contains(SpecialAuthorities.License.getNumVal());
        }

        for (Authority jobAuthority : jobAuthorities) {
            if (!workerAuthorities.contains(jobAuthority.getID()))
                return false;
        }
        return true;

    }

    private boolean isBranchExists(String branchAddress){
        return branchManager.isBranchExists(branchAddress);
    }

    private Branch getBranchByAddress(String branchAddress){
        if(!isBranchExists(branchAddress))
            return null;
        return branchManager.getBranchByAddress(branchAddress);
    }

    private void addShiftToHistory(String workerID, int shiftID) {
        Worker worker = this.workerManager.getWorkerByID(workerID);
        Shift shift = this.shiftManager.getShiftByID(shiftID);
        Branch branch=getBranchByShift(shift);//check if branch exists
        Pair<Branch, Shift> branch_shift= new Pair<>(branch , shift);
        // shiftsHistory.put(worker, getShiftHistory(workerID));
        // this.shiftsHistory.get(worker).add(branch_shift);
    }
    private boolean canWorkInShiftTime(String workerID, int shiftID) {
        Shift shift;
        if (shiftManager.isShiftExists(shiftID))
            shift = this.shiftManager.getShiftByID(shiftID);
        else
            throw new OurException("ShiftID " + shiftID + " not exist");
        return this.workerManager.canWorkInTime(workerID , shift.getDate(),shift.isDayShift());
    }

    private Branch getBranchByShift(Shift shift){
        BranchDALDTO dalSite = branchManager.getBranchByShift(shift.getID());
        if (branchManager.isBranchExists(dalSite.getAddress())) {
            return branchManager.getBranchByAddress(dalSite.getAddress());
        }
        return null;
    }

    public ShipmentRequest initiateShipment(){
        ShipmentRequest request = shipmentManager.getNextRequest();
        if(request!=null) {
            shipmentManager.initiateShipment(request);
            for(Map.Entry<String,Map<ProductForShipment,Integer>> productsQuantities : request.getProductsForShipment().entrySet()){
                acceptCertificate(productsQuantities.getKey(), productsQuantities.getValue());
            }
            return request;
        }
        return null;
    }

    private void acceptCertificate(String address, Map<ProductForShipment, Integer> productIDQuantity) {
        Map<String,Integer> catalogIDsQuantities = new HashMap<>();
        productIDQuantity.forEach(((productForShipment, quantity) -> catalogIDsQuantities.put(productForShipment.getCatalogID(), quantity)));
        certificateManager.createCertificate(address,catalogIDsQuantities);
        shipmentManager.addCertificate(certificateManager.getCurrentCertificateID());
    }
    public void sortByArea(){
        List<Pair<Integer,Character>> certificateToAreaList=new Vector<>();
        List<Integer> certificationIDs= shipmentManager.getAllCertificateIDs();
        for(Integer certificateID:certificationIDs){
            String address=certificateManager.getAddressByCertificateID(certificateID);
            char area=branchManager.getAreaOfSite(address);
            Pair<Integer,Character> certificateToArea=new Pair(certificateID,area);
            certificateToAreaList.add(certificateToArea);
        }
        shipmentManager.sortByArea(certificateToAreaList);
    }
    public List<Truck> getTrucks(){
        return trucksManager.getAllTrucks();
    }
    public boolean chooseTruck(String truckLicenseNumber){
        if(!trucksManager.isTruckExists(truckLicenseNumber)) {
            OurException exception = new OurException("Truck license number doesn't exist");
            throw exception;
        }
        shipmentManager.addTruckToCurrentShipment(truckLicenseNumber);
        return true;
    }

    public boolean checkValidQuantity(int quantity){
        return ProductSManager.checkValidQuantity(quantity);
    }

    //TODO: clarify the unique id for productS
    public boolean checkCatalogID(String catalogID){
        return ProductSManager.checkCatalogID(catalogID);
    }

    public List<Worker> getAvailableDriversForTruck(){
        String licenseNumber = this.shipmentManager.getCurrentTruckNum();
        int maxWeight = this.trucksManager.getMaxWeightByLicenseNum(licenseNumber);
        int licenseAuthorityID = this.jobsAuthoritiesData.getLicenseIDByWeight(maxWeight);
        List<Worker> drivers=workerManager.getAppropriateDrivers(licenseAuthorityID);
        List<Worker> driversAvailableInTime=new ArrayList<>();
        for(Worker driver: drivers){
            boolean isDay= castHourToIsDay(shipmentManager.getCurrentShipmentHour());
            LocalDate date=shipmentManager.getCurrentShipmentDate();
            if(isAvailableWorkerInDate(date,isDay,driver.getID()) && workerManager.canWorkInTime(driver.getID(),date,isDay))
                driversAvailableInTime.add(driver);
        }
        return driversAvailableInTime;
    }

    private boolean castHourToIsDay(LocalTime hour){
        return Shift.start_morning_shift.compareTo(hour)<=0 && Shift.end_morning_start_night.compareTo(hour) >0;
    }

    public boolean chooseDriver(String ID){
        List<Worker> availableDrivers= getAvailableDriversForTruck();
        if(!availableDrivers.contains(workerManager.getWorkerByID(ID))) {
            OurException exception = new OurException("Worker is not an available driver");
            throw exception;
        }
        String driverName=workerManager.getWorkerByID(ID).getFirstName();
        driverName=driverName+" "+workerManager.getWorkerByID(ID).getLastName();
        shipmentManager.setDriverName(driverName);
        shipmentManager.setDriverID(ID);
        return true;
    }

    public List<Truck> getOtherTrucks(){
        String currentTruckNum=shipmentManager.getCurrentTruckNum();
        return trucksManager.getOtherTrucks(currentTruckNum);
    }
    public boolean chooseOtherTruck(String licenseNumber){
        if(!trucksManager.isTruckExists(licenseNumber)) {
            OurException exception = new OurException("Truck license number doesn't exist");
            throw exception;
        }
        shipmentManager.addTruckToCurrentShipment(licenseNumber);
        String model=trucksManager.getTruckModelByLicenseNum(shipmentManager.getCurrentTruckNum());
        shipmentManager.addComment("Truck changed to " + model);
        return true;
    }
    public boolean removeAddress(String address){
        int certificateID=certificateManager.removeCertificate(address);
        if(certificateID>=0){
            shipmentManager.removeAddress(certificateID);
            shipmentManager.addComment("address " + address + " removed from shipment.");
            return true;
        }
        OurException exception = new OurException("Address is not valid");
        throw exception;
    }

    private void checkDestinationInShipment(String address){
        List<Integer> certificateIDs = shipmentManager.getAllCertificateIDs();
        List<String> addresses = certificateManager.getAddressesByCertificateIDs(certificateIDs);
        if(!addresses.contains(address))
            throw new OurException("There is no such destination in the current shipment");
    }

    private void checkProductInDestinationInShipment(String catalogID,String address){
        List<Integer> certificateIDs=shipmentManager.getAllCertificateIDs();
        List<Certificate> certificates=certificateManager.getCertificatesByIDS(certificateIDs);
        for(Certificate certificate:certificates){
            if(certificate.getAddress().equals(address)) {
                if (!certificate.getProductsQuantities().keySet().contains(catalogID))
                    throw new OurException("There is no such product in the chosen address in the shipment");
            }
        }
    }

    public int removeProductsFromSite(String catalogID, int quantity, String address) {
        checkDestinationInShipment(address);
        checkProductInDestinationInShipment(catalogID,address);
        int howMuchRemoved=certificateManager.removeProducts(address, catalogID, quantity);
        String nameOfProduct=ProductSManager.getProductNameByCatalogID(catalogID);
        shipmentManager.addComment(howMuchRemoved + " removed from product " + nameOfProduct);
        return howMuchRemoved;
    }


    public boolean validateWeight()
    {
        String currentLicenseNumber=this.shipmentManager.getCurrentTruckNum();
        double truckWeight = this.trucksManager.getWeightOfTruck(currentLicenseNumber);
        double maxCarryWeight=this.trucksManager.getMaxCarryWeight(currentLicenseNumber);
        List<Integer> certifcateIDs = shipmentManager.getAllCertificateIDs();
        List<Pair<Integer,Double>> productQuantityAndWeight=new ArrayList<>();
        List<Double> certificateWeights=new ArrayList<>();
        for(Integer i: certifcateIDs){
            Map<String,Integer> productIDQuantities = certificateManager.getCertificate(i).getProductsQuantities();
            for (Map.Entry<String,Integer> productQuantityEntry:productIDQuantities.entrySet()) {
                double prodWeight =ProductSManager.getProductWeightByID(productQuantityEntry.getKey());
                Pair<Integer,Double> newPair=new Pair(productQuantityEntry.getValue() , prodWeight);
                productQuantityAndWeight.add(newPair);
            }
            double certificateWeight = certificateManager.calculateWeight(i, productQuantityAndWeight);
            certificateWeights.add(certificateWeight);
        }
        return shipmentManager.validateWeight(certificateWeights , truckWeight , maxCarryWeight);
    }


    public boolean containsOtherTrucks() {
        return trucksManager.containsOtherTrucks();
    }

    public List<Shipment> getShipments(){
        return shipmentManager.getAllShipments();
    }


    public Pair<LocalDate,LocalTime> setShipmentDateAndTime() {
        LocalDate currentDate = SharedClass.getSharedClass().getDATE();
        LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth());
        LocalDate dateAfterWeek = date.plusDays(7);
        int workingTimeInDay = WorkingTimes.day.getNumVal();
//&& workingTimeInDay != WorkingTimes.night.getNumVal()
        while (date.compareTo(dateAfterWeek) != 0 ) {
            List<Worker> availableDrivers = workerManager.getAvailableDriversInDate(date, workingTimeInDay);
            if (availableDrivers.size() == 0) {
                if (workingTimeInDay == WorkingTimes.night.getNumVal()) {
                    workingTimeInDay = WorkingTimes.day.getNumVal();
                    date = date.plusDays(1);
                } else
                    workingTimeInDay = WorkingTimes.night.getNumVal();
                continue;
            }

            int storeKeeperID = SpecialJobs.StoreKeeper.getNumVal();
            List<Shift> shiftsStoreKeeper = this.shiftManager.getShiftsWithStoreKeeper(date, workingTimeInDay, storeKeeperID);
            List<String> addressesOfBranches = new ArrayList<>();
            shiftsStoreKeeper.forEach((shift -> addressesOfBranches.add(getBranchByShift(shift).getAddress())));
            List<Integer> shipmentCertificates = shipmentManager.getAllCertificateIDs();
            List<String> shipmentAddresses = certificateManager.getAddressesByCertificateIDs(shipmentCertificates);
            boolean allAddressesHaveStoreKeeper = true;
            for (String address : shipmentAddresses) {
                if (!addressesOfBranches.contains(address)) {
                    allAddressesHaveStoreKeeper = false;
                    break;
                }
            }
            if (!allAddressesHaveStoreKeeper) {
                if (workingTimeInDay == WorkingTimes.night.getNumVal()) {
                    workingTimeInDay = WorkingTimes.day.getNumVal();
                    date = date.plusDays(1);
                } else
                    workingTimeInDay = WorkingTimes.night.getNumVal();
                continue;
            }

            if(!SupplierManager.checkIfSupplierWorksInDate(((date.getDayOfWeek().getValue() % 7)+1),shipmentManager.getCurrentShipmentBnNumber()))
            {
                if (workingTimeInDay == WorkingTimes.night.getNumVal()) {
                    workingTimeInDay = WorkingTimes.day.getNumVal();
                    date = date.plusDays(1);
                } else
                    workingTimeInDay = WorkingTimes.night.getNumVal();
                continue;
            }

            //if we won't continue- all details are good

            if(workingTimeInDay== WorkingTimes.day.getNumVal())
                shipmentManager.setShipmentDateAndTime(Shift.start_morning_shift, date);
            else
                shipmentManager.setShipmentDateAndTime(Shift.end_morning_start_night,date);
            return new Pair<>(shipmentManager.getCurrentShipmentDate(),shipmentManager.getCurrentShipmentHour());
        }
        return null;
    }

    public List<Certificate> getCurrentShipmentCertificates(){
        List<Integer> certificateIDs=shipmentManager.getAllCertificateIDs();
        return certificateManager.getCertificatesByIDS(certificateIDs);
    }

    public List<Certificate> getCertificates(List<Integer> certificationIDs){
        List<Certificate> certificates=new Vector<>();
        for(Integer certificateID: certificationIDs){
            certificates.add(certificateManager.getCertificate(certificateID));
        }
        return certificates;
    }

    public void completeShipment(){
        sortByArea();
        LocalDate date=shipmentManager.getCurrentShipmentDate();
        LocalTime hour=shipmentManager.getCurrentShipmentHour();
        int shiftID=shiftManager.getShiftIDAtDateTime(date,hour); //need to put driver in a shift
        addJobToShift(shiftID,SpecialJobs.Driver.getNumVal());
        assignDriverToShift(shiftID,SpecialJobs.Driver.getNumVal());
        shipmentManager.saveShipment(shiftID);
        shipmentManager.getAllCertificateIDs().forEach((certificateID) -> certificateManager.save(certificateID, shipmentManager.getAutoID()));
    }


    //getting shiftID and returns the branch that connected to this shift
    @Override
    public String getBranchByShiftID(int shiftID) {
        for (Branch branch:branchShifts.keySet()) {
            List<Shift> shiftsOfBranch=branchShifts.get(branch);
            for (Shift shift:shiftsOfBranch) {
                if(shift.getID()==shiftID){
                    return branch.getAddress();
                }
            }
        }
        throw new OurException("shiftID doesn't exist in the system");
    }

    //this function is needed for making a driver not available to another work in the time of his shipment
    private void assignDriverToShift(int shiftID,int jobID){
        String driverID=shipmentManager.getCurrentShipmentDriverID();
        addWorkerToShift(shiftID,jobID,driverID);
    }

    public void abortShipment(){
        shipmentManager.abortCurrentShipment();
    }

    @Override
    public List<Authority> getAuthorities(int jobID) {
        return jobsAuthoritiesData.getAuthoritiesByJob(jobID);
    }

    /**
     * added functions for updating the inventory after shipment's execution
     */
    private Map<String,Map<String,Integer>> createProductsIncomeDocument( Shipment shipmentToExecute){
        Map<String,Map<String,Integer>> productsFromShipmentDocument= new HashMap<>();
        List<Integer> certificateIDs=shipmentToExecute.getCertificationIDs();
        for(Integer certificateID: certificateIDs){
            String address= certificateManager.getAddressByCertificateID(certificateID);
            Map<String,Integer> productsIDsQuantities=certificateManager.getProductsIDsQuantitites(certificateID);
            productsFromShipmentDocument.put(address,productsIDsQuantities);
        }
        return productsFromShipmentDocument;
    }
    public void executeShipment(Shipment shipmentToExecute) {
        Map<String, Map<String, Integer>> productsDocument = createProductsIncomeDocument(shipmentToExecute);
        try {
            store.updateInventoryFromShipment(productsDocument);
            shipmentManager.update(shipmentToExecute);
            shiftManager.releaseWorker(shipmentToExecute.getDriverID(),shipmentToExecute.getShiftID(),SpecialJobs.Driver.getNumVal());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Shipment> getShipmentsReports() {
        return shipmentManager.getShipmentsReports(SharedClass.getSharedClass().getDATE());
    }

    public void checkForShipmentsExecutions() {
        List<Shipment> shipmentsToGo= shipmentManager.getAllShipmentsForExecution();
        shipmentsToGo.forEach(shipment -> shipment.getCertificationIDs().forEach(id -> certificateManager.loadCertificate(id)));
        shipmentsToGo.forEach(shipment -> executeShipment(shipment));
    }
}
