package InterfaceLayer.DTO;

import BusinessLayer.Workers.Worker;

import java.util.List;

public class DriverInterfaceDTO {
    protected final String ID;
    protected final String firstName;
    protected final String lastName;
    protected final List<String> authoritiesNames;


    public DriverInterfaceDTO(Worker worker, List<String> authoritiesNames) {
        this.ID = worker.getID();
        this.firstName = worker.getFirstName();
        this.lastName = worker.getLastName();
        this.authoritiesNames = authoritiesNames;
    }

    public String toString(){
        String str = "ID" + ": " + ID + "\t"+"Name"+": " + firstName + " " + lastName + "\n";

        //Returns the list of the workers authorities
        str += authoritiesToString();

        return str;
    }

    private String authoritiesToString() {
        StringBuilder str;
        if(authoritiesNames.isEmpty()){
            str = new StringBuilder("No authorities");
        }
        else {
            str = new StringBuilder("Authorities"+": ");
            str.append(authoritiesNames);
        }
        str.append("\n");
        return str.toString();
    }

}
