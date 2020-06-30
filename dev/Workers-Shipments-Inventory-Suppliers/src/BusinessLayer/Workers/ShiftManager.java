package BusinessLayer.Workers;

import BusinessLayer.Enums.WorkingTimes;
import BusinessLayer.Shipments.Shipment;
import DataAccessLayer.DALDTO.ShiftDALDTO;
import DataAccessLayer.Handlers.ShiftHandler;
import DataStructures.Pair;
import  Exception.OurException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ShiftManager {
    private static ShiftManager instance=null;
    private List<Shift> allShifts;
    private ShiftHandler shiftHandler;
    private int tempCounter;

    private ShiftManager(){
        this.allShifts = new ArrayList<>();
        shiftHandler = ShiftHandler.getInstance();
        tempCounter = shiftHandler.loadAutoID();
    }
    public static ShiftManager getInstance(){
        if(instance==null)
            instance=new ShiftManager();
        return instance;
    }

    public List<Shift> getBranchShifts(String branchAddress) {
        List<Shift> shifts = new ArrayList<>();
        for (ShiftDALDTO dalShift : shiftHandler.loadBranchShifts(branchAddress)) {
            if (isShiftExists(dalShift.getID()))
                shifts.add(getShiftByID(dalShift.getID()));
            else
                shifts.add(new Shift(dalShift));
        }
        return shifts;
    }

    public List<Shift> getAllShifts() {
        return this.allShifts;
    }


//checking if shiftID is in the system
    public boolean isShiftExists(int shiftID){

        for (Shift shift:this.allShifts) {
            if (shift.getID() == shiftID) {
                return true;
            }
        }
        ShiftDALDTO dalShift = shiftHandler.loadByInt(shiftID);
        if (dalShift != null) {
            Shift shift = new Shift(dalShift);
            allShifts.add(shift);
            return true;
        }
        return false;
    }
    //create new shift with the details were given
    public Shift createShift(LocalDate date , boolean isDayShift){
        Shift newShift=new Shift(tempCounter, date,isDayShift);
        tempCounter++;
        this.allShifts.add(newShift);
        //TODO: resolve the issue about hardCoded jobs
        //Hard coded the manager constraint. There is always at least 1 manager in a shift.
        addJobToShift(newShift.getID(), 0);
        return newShift;
    }

    //adding job to shift by ID
    public boolean addJobToShift(int shiftID , int jobID) {
        if (!isShiftExists(shiftID))
            return false;
        for (Shift shift : this.allShifts) {
            if (shift.getID() == shiftID) {
                //getting map of job and required workers
                Map<Integer, Integer> jobsNumber = shift.getJobsNumbers();
                if (jobsNumber.containsKey(jobID)) {//there is such job in the shift
                    jobsNumber.replace(jobID, jobsNumber.get(jobID) + 1);//required one more
                } else {//new job is needed to the shift
                    jobsNumber.put(jobID, 1);
                }
                shift.unready();
                shiftHandler.saveNewJob(shiftID, jobID);
                shiftHandler.update(toDTO(shift, ""));
            }
        }
        return true;
    }


    //adding worker to shift was given
    public boolean addWorkerToShift(int shiftID , int jobID , String workerID) {
        Shift shift = getShiftByID(shiftID);
        if (shift.getWorkersJobs().containsKey(workerID))
            //trying to assign same worker to another job
            throw new OurException("The worker is already assigned to a job");
        if (shift.getJobsNumbers().containsKey(jobID)) {//such job needed
            int oldRequired = shift.getJobsNumbers().get(jobID);//number of workers were needed before to this job
            if (oldRequired > 0) {
                //needed worker in this job
                shift.getJobsNumbers().replace(jobID, oldRequired - 1);//worker is now in this job

                shift.getWorkersJobs().put(workerID, jobID);
                shift.checkReady();//updating the readiness of the shift
                shiftHandler.saveNewWorker(shiftID, jobID, workerID);
                shiftHandler.update(toDTO(shift, ""));
                return true;
            }
        }
        return false;
    }

    public void addManagerIDToShift(int shiftID , String managerID) {
        Shift shift = getShiftByID(shiftID);
        shift.getManagersID().add(managerID);
        //shiftHandler.saveNewWorker(shiftID, SpecialJobs.Manager.getNumVal(), managerID);
    }

    public Shift getShiftByID(int shiftID){
        for (Shift shift:this.allShifts) {
            if(shift.getID()==shiftID)
                return shift;
        }
        return null;
    }


    public List<Shift> getShiftsWithStoreKeeper(LocalDate localDate, int workingTime,int storeKeeperID) {
        for (ShiftDALDTO dalShift : shiftHandler.loadShiftsByDate(localDate)) {
            isShiftExists(dalShift.getID());
        }
        List<Shift> shiftsWithStoreKeeper=new ArrayList<>();
        for (Shift shift : allShifts) {
                if(shift.getDate().isEqual(localDate) && (shift.isDayShift() && workingTime == WorkingTimes.day.getNumVal()) || (!shift.isDayShift() && workingTime == WorkingTimes.night.getNumVal()))//hour fits
                    if(checkIfHasAssignedWorker(shift,storeKeeperID))
                        shiftsWithStoreKeeper.add(shift);
        }

        return shiftsWithStoreKeeper;
    }

    private boolean checkIfHasAssignedWorker(Shift shift,int storeKeeperID) {
        return shift.checkIfAssignedWorker(storeKeeperID);
    }

    public int getShiftIDAtDateTime(LocalDate date, LocalTime hour) {
        int returnID=-1;
        for(Shift shift: allShifts){
            if(shift.getDate().compareTo(date)==0) {//first shift that fits to this time and date - return
                if (hour.compareTo(shift.getStartHour()) > -1 && hour.compareTo(shift.getEndHour()) < 0) {
                    returnID=shift.getID();
                }
            }
        }
        return returnID;
    }

    public ShiftDALDTO toDTO(Shift shift, String branchAddress) {
        return new ShiftDALDTO(shift.getID(), shift.getDate(), shift.isDayShift(), shift.getJobsNumbers(),
                shift.getWorkersJobs(), shift.isReady(), shift.getManagersID(), branchAddress);
    }

    public List<Pair<String, Shift>> loadShiftHistory(String workerID) {
        List<Pair<String, Shift>> workerShiftHistory = new ArrayList<>();
        for (ShiftDALDTO dalShift: shiftHandler.loadShiftHistory(workerID)) {
            if (isShiftExists(dalShift.getID()))
                workerShiftHistory.add(new Pair<>(dalShift.getBranchAddress(), getShiftByID(dalShift.getID())));
            else
                workerShiftHistory.add(new Pair<>(dalShift.getBranchAddress(), new Shift(dalShift)));
        }
        return workerShiftHistory;
    }

    public ShiftHandler getShiftHandler() {
        return shiftHandler;
    }

    public List<Shift> getShiftsInDateTime(LocalDate date, boolean dayShift) {
        List<Shift> shiftsInDateTime=new ArrayList<>();
        for (Shift shift:this.allShifts) {
            if(shift.getDate().equals(date) && shift.isDayShift() == dayShift){
                shiftsInDateTime.add(shift);
            }
        }
        return shiftsInDateTime;
    }

    public boolean isJobExistsInShift(int shiftID, int jobID) {
        for(Shift shift: allShifts){
            if(shift.getID()==shiftID){
                return shift.isJobExistsInShift(jobID);
            }
        }
        return false; //couldn't find the ship
    }

    public void releaseWorker(String driverID, int shiftID,int jobID) {
        shiftHandler.releaseWorker(driverID,shiftID,jobID);
    }
}
