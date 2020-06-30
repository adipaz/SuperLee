package BusinessLayer.Workers;
import BusinessLayer.Enums.LicenseTypes;
import BusinessLayer.Enums.SpecialAuthorities;
import BusinessLayer.Enums.WorkingTimes;
import DataAccessLayer.DALDTO.WorkerDALDTO;
import DataAccessLayer.Handlers.WorkerHandler;
import  Exception.OurException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkerManager {
    private static WorkerManager instance = null;
    private List<Worker> allWorkers;
    private WorkerHandler workerHandler;
    private WorkerManager() {
        this.allWorkers = new ArrayList<>();
        workerHandler = WorkerHandler.getInstance();

//        addWorker("318773439","Sean","Pikulin",200,LocalDate.now(),1234);
//        addWorker("211905641","Ravid","Rom",200,LocalDate.now(),5678);
//        addWorker("206368722","Tali","Malenboim",1000,LocalDate.now(),456);
//        addWorker("211111111","Rami","Puzis",2000,LocalDate.now(),012);
//        addWorker("208084004","Inon","Katz",300,LocalDate.now(),890);
    }

    public static WorkerManager getInstance() {
        if (instance == null)
            instance = new WorkerManager();
        return instance;
    }
    public List<Worker> getAllWorkers(){
        for (WorkerDALDTO dalWorker : workerHandler.loadAll()) {
            isWorkerExists(dalWorker.getID());
        }
        return this.allWorkers;
    }

    public boolean isWorkerExists(String workerID) {
        for (Worker worker : allWorkers) {
            if (worker.getID() .equals( workerID))
                return true;
        }
        WorkerDALDTO dalWorker = workerHandler.loadByString(workerID);
        if (dalWorker != null) {
            allWorkers.add(new Worker(dalWorker));
            return true;
        }
        return false;
    }

    private boolean isWorkerHasAuthority(String workerID, int authorityID){
        for (Worker worker:this.allWorkers) {
            if(worker.getID().equals(workerID))
                if(worker.getAuthoritiesID().contains(authorityID))
                    return true;
        }
        return false;
    }


    public boolean addAuthorityToWorker(String workerID , int authorityID){
        if(!isWorkerExists(workerID))
            return false;
        if(isWorkerHasAuthority(workerID, authorityID))
            return false;
        for (Worker worker:this.allWorkers) {
            if(workerID.equals(worker.getID())) {
                worker.addAuthority(authorityID);
                if(authorityID>=3 && authorityID<=6 && !worker.getAuthoritiesID().contains(SpecialAuthorities.License))//license type authority
                {
                    if(isWorkerHasLicense(worker)){
                        OurException exception = new OurException("driver already has license");
                        throw exception;
                    }
                    worker.addAuthority(7);//adding the global license authority
                    workerHandler.saveNewAuthority(workerID, SpecialAuthorities.License.getNumVal());
                }
                workerHandler.saveNewAuthority(workerID, authorityID);
            }
        }
        return true;
    }

    private boolean isWorkerHasLicense(Worker worker) {
        for (Integer authorityID:worker.getAuthoritiesID()) {
            if(authorityID>=3 && authorityID<=6)
                return true;
        }
        return false;
    }

    public List<Integer> getAuthoritiesOfWorker(String workerID) {
        for (Worker worker : allWorkers) {
            if (worker.getID() .equals( workerID))
                return worker.getAuthoritiesID();
        }
        return null;
    }

    public void addWorker(String id, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAccID) {
        Worker worker = new Worker(id,firstName,lastName,salary,startWorkingDate,bankAccID);
        this.allWorkers.add(worker);
        workerHandler.save(toDTO(worker));
    }

    public Worker getWorkerByID(String id) {
        for (Worker worker : allWorkers) {
            if (worker.getID() .equals(id))
                return worker;
        }
        return null;
    }

    public boolean editWorkerByID(String id, String firstName, String lastName, double salary, LocalDate startWorkingDate, int bankAccountID) {

        if (!isWorkerExists(id))//checking if there exists such worker
            throw new OurException("Worker with the id " + id + " doesn't exist.");
        else {
            for (Worker worker : this.allWorkers) {
                if (worker.getID() .equals(id)) {//this is the worker
                    //setting worker's new details
                    worker.setFirstName(firstName);
                    worker.setLastName(lastName);
                    worker.setSalary(salary);
                    worker.setStartWorkingDate(startWorkingDate);
                    worker.setBankAccID(bankAccountID);
                }
            }
            workerHandler.update(toDTO(getWorkerByID(id)));
            return true;
        }
    }

    public boolean addWorkerWorkingTimes(String workerID, int day, WorkingTimes times) {
        if(!isWorkerExists(workerID))
            return false;
        for (Worker worker:this.allWorkers) {
            if(worker.getID().equals(workerID)){
                worker.getConstrains()[day-1]=times;
            }
        }
        boolean isDay = times == WorkingTimes.day | times == WorkingTimes.both;
        boolean isNight = times == WorkingTimes.night | times == WorkingTimes.both;
        workerHandler.saveWorkingTime(isDay, isNight, day, workerID);
        return true;
    }
    public List<String> getEmploymentTerms(String workerID) {
        if(!isWorkerExists(workerID))
            return  null;
        for (Worker worker:this.allWorkers) {
            if(worker.getID().equals(workerID))
                return worker.getEmploymentTerms();
        }
        return null;
    }

    public boolean addEmploymentTermToWorker(String workerID, String term) {
        if(!isWorkerExists(workerID))
            return  false;
        for (Worker worker:this.allWorkers) {
            if(worker.getID().equals(workerID)) {
                if (worker.getEmploymentTerms().contains(term))
                    return false;//if worker already has this term return false
                worker.addEmploymentTerm(term);
                workerHandler.saveTerm(term, workerID);
            }
        }
        return true;
    }
    public List<Integer> getAuthoritiesIDOfWorker(String workerID){
        Worker worker = getWorkerByID(workerID);
        return worker.getAuthoritiesID();
    }

    public List<Worker> getWorkersByAuthorityID(int authorityIDToCheck){

        List<Worker> workersByAuthority=new ArrayList<>();
        for (Worker worker:this.allWorkers) {
            for (Integer authorityID:worker.getAuthoritiesID()) {
                if(authorityIDToCheck <= authorityID)
                    workersByAuthority.add(worker);
            }
        }
        return workersByAuthority;
    }

    public List<Worker> getAppropriateDrivers(int licenseAuthority){
        List<Worker> drivers=new ArrayList<>();
        for (WorkerDALDTO dalWorker : workerHandler.loadDriversByLicense(licenseAuthority)) {
            isWorkerExists(dalWorker.getID());
        }
        for(Worker worker:this.allWorkers){
            if(worker.getDriverLicenseNumber()>=licenseAuthority  )
                drivers.add(worker);
        }
        return drivers;
    }

    public boolean canWorkInTime(String workerID , LocalDate shiftDate, boolean dayShift) {
        Worker worker = getWorkerByID(workerID);
        int dayOfWeek = shiftDate.getDayOfWeek().getValue();
        if(dayOfWeek>=1 && dayOfWeek<=6){/*format like USA , monday = day of week 1*/
            dayOfWeek++;
        }
        else{/* 7 day of week is sunday*/
            dayOfWeek=1;
        }
        WorkingTimes constraint = worker.getConstrains()[dayOfWeek-1];
        if(constraint == WorkingTimes.both)
            return true;
        else if (constraint == WorkingTimes.none)
            return false;
        else if (dayShift && constraint==WorkingTimes.day)
            return true;
        else if(!dayShift && constraint==WorkingTimes.night)
            return true;
        return false;
    }

    private WorkerDALDTO toDTO(Worker worker) {
        return new WorkerDALDTO(worker.getID(), worker.getFirstName(), worker.getLastName(), worker.getStartWorkingDate(),
                worker.getSalary(), worker.getBankAccID(), worker.getAuthoritiesID(), worker.getConstrains(),
                worker.getEmploymentTerms());
    }

    public List<Worker> getAvailableDriversInDate(LocalDate date, int workingTime) {
        List<Worker> drivers=new ArrayList<>();
        for (WorkerDALDTO dalWorker : workerHandler.loadDriversByLicense(SpecialAuthorities.LicenseA.getNumVal())) {
            isWorkerExists(dalWorker.getID());
        }
        for(Worker worker:this.allWorkers){
            if(worker.getDriverLicenseNumber()!=-1)
                drivers.add(worker);
        }

        List<Worker> driversAvailableInTime= new ArrayList<>();
        for(Worker driver: drivers){
            boolean isDay= workingTime == WorkingTimes.day.getNumVal();
            if(canWorkInTime(driver.getID(),date,isDay))
                driversAvailableInTime.add(driver);
        }

        return driversAvailableInTime;
    }
}
