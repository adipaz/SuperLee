package BusinessLayer.Workers;

import DataAccessLayer.DALDTO.JobDALDTO;

public class Job {
    private int ID ;
    private String name;

    public Job(int ID, String name){
        this.ID=ID;
        this.name=name;
    }

    public Job(JobDALDTO dalJob) {
        ID = dalJob.getID();
        name = dalJob.getName();
    }

    public int getID() {
        return ID;
    }


    public String getName() {
        return name;
    }

public String toString() {
    String description = "Job: ";
    description += "ID: " + getID() + ", ";
    description += "Name: " + getName();
    return description;
}

}
