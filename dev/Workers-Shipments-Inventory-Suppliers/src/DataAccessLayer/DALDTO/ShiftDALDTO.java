package DataAccessLayer.DALDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ShiftDALDTO implements DALDTO {
    private int ID;
    private LocalDate date;
    private boolean dayShift;
    private Map<Integer, Integer> jobsNumbers;
    private Map<String, Integer> workersJobs;
    private boolean ready;
    private List<String> managerIDs;
    private String branchAddress;

    public ShiftDALDTO(int id, LocalDate date, boolean dayShift, Map<Integer, Integer> jobsNumbers, Map<String, Integer> workersJobs, boolean ready, List<String> managerIDs, String branchAddress) {
        ID = id;
        this.date = date;
        this.dayShift = dayShift;
        this.jobsNumbers = jobsNumbers;
        this.workersJobs = workersJobs;
        this.ready = ready;
        this.managerIDs = managerIDs;
        this.branchAddress = branchAddress;
    }



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

    public List<String> getManagerIDs() {
        return managerIDs;
    }

    public String getBranchAddress() {
        return branchAddress;
    }
}
