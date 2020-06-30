package InterfaceLayer.DTO;

import BusinessLayer.Workers.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ShiftInterfaceDTO {

    private final int ID;
    private final LocalDate date;
    private final boolean dayShift;
    private final String branchAddress;
    private final Map<String,Integer> jobsNumbers;//key: jobName , value: amountWorkers in this job
    private final Map<String,String> workersJobs;//key: workerString , value: Name of job the worker does
    private final List<String> managersID;
    private final boolean ready;

    public ShiftInterfaceDTO(Shift shift, String branchAddress, Map<String, Integer> jobsNumbers, Map<String,String> workersJobs) {
        this.ID = shift.getID();
        this.date = shift.getDate();
        this.dayShift = shift.isDayShift();
        this.jobsNumbers = jobsNumbers;
        this.workersJobs = workersJobs;
        this.managersID = shift.getManagersID();
        this.ready = shift.isReady();
        this.branchAddress = branchAddress;
    }

    public String toString(){
        StringBuilder str = new StringBuilder("ID: " + ID + "\tDate: " + date.toString() + "\t" + (dayShift? "Day Shift" : "Night shift") + "\n");
        str.append("Branch Address: " + branchAddress + "\n");

        //Returns for each job the workers working in that job and the number of jobs unassigned from each kind.
        for (String job:jobsNumbers.keySet()) {
            str.append("Job: ").append(job).append("\tWorkers: [");
            List<String> jobWorkers = workingInJob(job);
            for (int i =0; i<jobWorkers.size(); i++){
                str.append(jobWorkers.get(i));
                if(i != jobWorkers.size()-1){
                    str.append(", ");
                }
            }
            if(jobsNumbers.get(job) > 0) {
                str.append((jobWorkers.size() > 0)? ", " : "").append("Not Assigned: ").append(jobsNumbers.get(job));
            }
            str.append("]\n");
        }

        //List of all the managers
        str.append("Managers: [");
        for (int i =0; i<managersID.size(); i++){
            str.append(managersID.get(i));
            if(i != managersID.size()-1){
                str.append(", ");
            }
        }
        str.append("]");
        str.append("\n");

        //IIIMMMMM READDDDYYYY
        str.append((ready) ? "SHIFT IS READY! :)" : "Shift is not ready :(");
        str.append("\n");

        return str.toString();
    }

    //Returns a list of all the workers id in the shift that working the job @job;
    private List<String> workingInJob(String job) {
        List<String> workers = new Vector<>();
        for (Map.Entry<String, String> workerJob: workersJobs.entrySet()) {
            if(workerJob.getValue().equals(job)){
                workers.add((workerJob.getKey()));
            }
        }
        return  workers;
    }
}
