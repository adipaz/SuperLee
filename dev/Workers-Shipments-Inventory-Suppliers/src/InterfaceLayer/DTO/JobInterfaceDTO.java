package InterfaceLayer.DTO;

import BusinessLayer.Workers.Job;

import java.util.List;

public class JobInterfaceDTO {

    private final int ID ;
    private final String name;
    private final List<String> requiredAuthorities;

    public JobInterfaceDTO(Job job, List<String> requiredAuthorities) {
        this.ID = job.getID();
        this.name = job.getName();
        this.requiredAuthorities = requiredAuthorities;
    }

    public String toString(){
        StringBuilder str = new StringBuilder(
                "ID"+": " + ID +
                "\t"+"Name"+": " + name + "\n");

        str.append((requiredAuthorities.isEmpty()) ? "No required authorities" : "Required authorities"+": ");

        str.append(requiredAuthorities);

        return str.toString();
    }
}
