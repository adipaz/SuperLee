package BusinessLayer.Workers;

import BusinessLayer.Enums.SpecialAuthorities;
import BusinessLayer.Enums.LicenseTypes;
import DataAccessLayer.DALDTO.AuthorityDALDTO;
import DataAccessLayer.Handlers.AuthorityHandler;
import DataAccessLayer.DALDTO.JobDALDTO;
import DataAccessLayer.Handlers.JobHandler;

import java.util.*;

public class JobsAuthoritiesData {
    private static JobsAuthoritiesData instance=null;
    private int jobTempCounter;
    private int authorityTempCounter;

    private List<Job> allJobs;
    private List<Authority> allAuthorities;
    private Map<Job, List<Authority>> jobPerAuthority;
    private Map <Integer,Integer> truckWeightAuthorityID;
    private JobHandler jobsHandler;
    private AuthorityHandler authorityHandler;

    private JobsAuthoritiesData(){
        this.allJobs=new ArrayList<>();
        this.allAuthorities=new ArrayList<>();
        this.jobPerAuthority=new LinkedHashMap<>();
        truckWeightAuthorityID=new HashMap<>();
        jobsHandler = JobHandler.getInstance();
        jobTempCounter = jobsHandler.loadAutoID();
        authorityHandler = AuthorityHandler.getInstance();
        authorityTempCounter = authorityHandler.loadAutoID();


        truckWeightAuthorityID.put(LicenseTypes.A.getNumVal(), SpecialAuthorities.LicenseA.getNumVal());
        truckWeightAuthorityID.put(LicenseTypes.B.getNumVal(),SpecialAuthorities.LicenseB.getNumVal());
        truckWeightAuthorityID.put(LicenseTypes.C.getNumVal(),SpecialAuthorities.LicenseC.getNumVal());
        truckWeightAuthorityID.put(LicenseTypes.D.getNumVal(),SpecialAuthorities.LicenseD.getNumVal());

    }
    public static JobsAuthoritiesData getInstance(){
        if(instance==null)
            instance=new JobsAuthoritiesData();
        return instance;
    }


    public List<Job> getAllJobs() {
        for (JobDALDTO dalJob : jobsHandler.loadAll()) {
            isJobExists(dalJob.getID());
        }
        return allJobs;
    }

    public Map<Job, List<Authority>> getJobPerAuthority() {
        return jobPerAuthority;
    }

    public List<Authority> getAllAuthorities() {
        for (AuthorityDALDTO dalAuthority : authorityHandler.loadAll()) {
            isAuthorityExists(dalAuthority.getID());
        }

        return allAuthorities;
    }

    private boolean isJobNameExists(String jobName){
        for (Job job:this.allJobs) {
            if (jobName.equals(job.getName()))
                return true;
        }
        JobDALDTO dalJob = jobsHandler.loadByString(jobName);
        if (dalJob != null) {
            allJobs.add(new Job(dalJob));
            return true;
        }
        return false;
    }

    private boolean isAuthorityNameExists(String authorityName){
        for (Authority authority:this.allAuthorities) {
            if (authorityName.equals(authority.getName()))
                return true;
        }
        AuthorityDALDTO dalAuthority = authorityHandler.loadByString(authorityName);
        if (dalAuthority != null) {
            allAuthorities.add(new Authority(dalAuthority));
            return true;
        }
        return false;
    }

    public boolean addJob(String jobName){
        if(!isJobNameExists(jobName)) {
            Job newJob = new Job(jobTempCounter, jobName);
            jobTempCounter++;
            this.allJobs.add(newJob);
            this.jobPerAuthority.put(newJob, new ArrayList<>());
            jobsHandler.save(toDTO(newJob));
            return true;
        }
        return false;
    }

    public boolean addAuthority(String authorityName){
        if(!isAuthorityNameExists(authorityName)) {
            Authority newAuthority = new Authority(authorityTempCounter, authorityName);
            authorityTempCounter++;
            this.allAuthorities.add(newAuthority);
            authorityHandler.save(toDTO(newAuthority));
            return true;
        }
        return false;
    }


    public boolean isJobExists(int jobID){
        for (Job job:this.allJobs) {
            if(job.getID()==jobID)
                return true;
        }
        JobDALDTO dalJob = jobsHandler.loadByInt(jobID);
        if (dalJob != null) {
            allJobs.add(new Job(dalJob));
            return true;
        }
        return false;
    }

    public boolean isAuthorityExists(int authorityID){
        for (Authority authority:this.allAuthorities) {
            if(authority.getID()==authorityID)
                return true;
        }
        AuthorityDALDTO dalAuthority = authorityHandler.loadByInt(authorityID);
        if (dalAuthority != null) {
            allAuthorities.add(new Authority(dalAuthority));
            return true;
        }
        return false;
    }

    public boolean addAuthorityToJob(int jobID , int authorityID){
        if(isJobExists(jobID) && isAuthorityExists(authorityID)) {
            for (Job job :this.allJobs) {
                if (job.getID() == jobID) {//found the requested job
                    for (Authority authority : this.allAuthorities) {//searching the authority
                        if (authority.getID() == authorityID) {//found the requested authority
                            getAuthoritiesByJob(jobID).add(authority);//add authority to job
                            jobsHandler.saveNewAuthority(jobID, authorityID);
                            return true;
                         }
                    }
                }
            }
        }
    return false;
    }



    public List<Authority> getAuthoritiesByJob(int jobID){
        for (Job job:this.jobPerAuthority.keySet()) {
            if(job.getID()==jobID){
                return jobPerAuthority.get(job);
            }
        }
        List<AuthorityDALDTO> dalAuthorities = authorityHandler.loadJobsPerAuthorities((jobID));
        if (dalAuthorities != null) {
            List<Authority> authorities = new ArrayList<>();
            for (AuthorityDALDTO dalAuthority : dalAuthorities) {
                if (isAuthorityExists(dalAuthority.getID()))
                    authorities.add(getAuthorityByID(dalAuthority.getID()));
                else
                    authorities.add(new Authority(dalAuthority));
            }
            for (int i = 0; i < allJobs.size(); i++) {
                if (allJobs.get(i).getID() == jobID) {
                    jobPerAuthority.put(allJobs.get(i), authorities);
                    return authorities;
                }
            }
        }
        return new Vector<>();
    }

    public Authority getAuthorityByID(int authorityID) {
        for (Authority authority : allAuthorities) {
            if (authority.getID() == authorityID)
                return authority;
        }
        return null;
    }

    public Integer getJobIDByName(String jobName){
        for (Job job:allJobs) {
            if (job.getName().equals(jobName))
                return job.getID();
        }
        return  null;
    }

    public Integer getAuthorityIDByName(String authorityName){
        for (Authority authority:allAuthorities) {
            if (authority.getName().equals(authorityName))
                return authority.getID();
        }
        return  null;
    }

    public List<Job> getAvailableJobsByAuthorities(List<Integer> workerAuthoritiesID) {
        List<Job> jobs=new Vector<>();
        for (Job job:this.allJobs) {
            if(isAuthoritiesEnough(job.getID() , workerAuthoritiesID))
                jobs.add(job);
        }
        return jobs;
    }

    private boolean isAuthoritiesEnough(int jobID, List<Integer> workerAuthoritiesID) {
        List<Authority> jobAuthorities = getAuthoritiesByJob(jobID);
        boolean canWork=true;
        for (Authority authority:jobAuthorities) {
            if(!workerAuthoritiesID.contains(authority.getID()))
                canWork=false;
        }
        if(canWork)
            return true;
        return false;
    }

    public int getLicenseIDByWeight(int weight){
        return this.truckWeightAuthorityID.get(weight);
    }

    public JobDALDTO toDTO(Job job) {
        return new JobDALDTO(job.getID(), job.getName());
    }

    public AuthorityDALDTO toDTO(Authority authority) {
        return new AuthorityDALDTO(authority.getID(), authority.getName());
    }
}
