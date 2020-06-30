package BusinessLayer.Workers;

import DataAccessLayer.DALDTO.ShiftDALDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Shift {
    public static LocalTime start_morning_shift=LocalTime.of(8,0,0);
    public static LocalTime end_morning_start_night=LocalTime.of(14,0,0);
    public static LocalTime end_night_shift=LocalTime.of(20,0,0);

    private int ID;
    private LocalDate date;
    private boolean dayShift;
    private Map<Integer,Integer> jobsNumbers;//key: jobID , value: amountWorkers in this job
    private Map<String,Integer> workersJobs;//key: workerID , value: ID of job the worker does
    private boolean ready;
    private List<String> managersID;


    public  Shift(int ID, LocalDate date , boolean dayShift ){
        this.ID=ID;
        this.date=date;
        this.dayShift=dayShift;
        this.jobsNumbers =new LinkedHashMap<>();
        this.workersJobs=new LinkedHashMap<>();
        this.ready=false;
        this.managersID=new ArrayList<>();
    }

    public Shift(ShiftDALDTO dalShift) {
        ID = dalShift.getID();
        date = dalShift.getDate();
        dayShift = dalShift.isDayShift();
        jobsNumbers = dalShift.getJobsNumbers();
        workersJobs = dalShift.getWorkersJobs();
        ready = dalShift.isReady();
        managersID = dalShift.getManagerIDs();
    }


    public void unready(){this.ready = false;}

    public int getID() {
        return ID;
    }


    public LocalDate getDate() {
        return date;
    }


    public boolean isDayShift() {
        return dayShift;
    }


    public Map<Integer, Integer> getJobsNumbers() {
        return jobsNumbers;
    }


    public Map<String, Integer> getWorkersJobs() {
        return workersJobs;
    }


    public boolean isReady() {
        return ready;
    }


    public List<String> getManagersID() {
        return managersID;
    }




    public void checkReady(){
        boolean flag=true;
        for (Map.Entry<Integer,Integer> entry : jobsNumbers.entrySet()) {
            if(entry.getValue()>0)//more workers required
               flag=false;
        }
        if(this.managersID.size()==0)
            flag= false;
        //till here , have manager and all workers needed
        if(flag)
            this.ready=true;
        }


public String toString(){
    String description="Shift: ";
    description+="Date: "+date+", ";
    description+="is Day Shift: "+dayShift+", ";
    description+="is Ready: "+ready+", ";
    description+="Manager ID's: "+managersID+", ";
    description+="jobID : amount Workers needed: "+managersID+"\n";
    for (Integer jobID : jobsNumbers.keySet()) {
        description+="Job ID : "+jobID+": amount needed: "+jobsNumbers.get(jobID)+"\n";
    }
    description+="Worker ID : job ID : "+managersID+"\n";
    for (String workerID : workersJobs.keySet()) {
        description+="Worker ID : "+workerID+": amount needed: "+workersJobs.get(workerID)+"\n";
    }

return description;
}

    public LocalTime getStartHour() {
        if (this.dayShift)
            return this.start_morning_shift;
        else
            return this.end_morning_start_night;
    }

    public LocalTime getEndHour() {
        if (this.dayShift)
            return this.end_morning_start_night;
        else
            return this.end_night_shift;
    }

    public boolean checkIfAssignedWorker(int jobID) {
        //returns true if there is a worker that was assigned to this job
        return this.workersJobs.values().contains(jobID);
    }

    public boolean isJobExistsInShift(int jobID) {
        return jobsNumbers.keySet().contains(jobID) && jobsNumbers.get(jobID)>0;
    }
}
