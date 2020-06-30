package BusinessLayer.Workers;

import BusinessLayer.Enums.SpecialAuthorities;
import BusinessLayer.Enums.WorkingTimes;
import DataAccessLayer.DALDTO.WorkerDALDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Worker {
    private String ID;
    private String firstName;
    private String lastName;
    private double salary;
    private LocalDate startWorkingDate;
    private int bankAccID;
    private List<Integer> authoritiesID;
    private WorkingTimes[]  constrains;
    private List<String> employmentTerms;

    public Worker(String ID , String firstName , String lastName , double salary , LocalDate startWorkingDate , int bankAccID ) {
        this.ID=ID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.salary=salary;
        this.startWorkingDate=startWorkingDate;
        this.bankAccID=bankAccID;
        this.authoritiesID=new ArrayList<>();
        this.constrains=new WorkingTimes[7];

        this.employmentTerms =new ArrayList<>();

    }

    public Worker(WorkerDALDTO dalWorker) {
        ID = dalWorker.getID();
        firstName = dalWorker.getFirstName();
        lastName = dalWorker.getLastName();
        salary = dalWorker.getSalary();
        startWorkingDate = dalWorker.getStartWorkingDate();
        bankAccID = dalWorker.getBankAccount();
        authoritiesID = dalWorker.getAuthorityIDs();
        constrains = dalWorker.getConstrains();
        employmentTerms = dalWorker.getEmploymentTerms();
    }

    public void addAuthority(int authorityID){
        this.getAuthoritiesID().add(authorityID);
    }


        public String getID(){
        return this.ID;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public double getSalary(){
        return this.salary;
    }

    public LocalDate getStartWorkingDate(){
        return this.startWorkingDate;
    }

    public int getBankAccID(){
        return this.bankAccID;
    }

    public List<Integer> getAuthoritiesID(){
        return this.authoritiesID;
    }

    public WorkingTimes[] getConstrains(){
        return this.constrains;
    }

    public List<String> getEmploymentTerms(){return this.employmentTerms;}

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public void setSalary(double salary){
        this.salary=salary;
    }

    public void setStartWorkingDate(LocalDate startWorkingDate){
        this.startWorkingDate=startWorkingDate;
    }


    public void setBankAccID(int bankAccID){
        this.bankAccID=bankAccID;
    }

    public void addEmploymentTerm(String term){
        this.getEmploymentTerms().add(term);
    }

    public String toString(){
        String description="";
        description+="Worker: ";
        description+="First name: "+firstName+", ";
        description+="Last name: "+lastName+", ";
        description+="Salary: "+salary+", ";
        description+="Start Working Date: "+startWorkingDate+", ";
        description+="Bank Account ID: "+bankAccID+", ";
        description+="Constrains: "+constrains+", ";
        description+="Authorities ID's: "+authoritiesID;
        return description;

    }

    public int getDriverLicenseNumber() {

        if(!authoritiesID.contains(SpecialAuthorities.License.getNumVal()))
            return -1;
        else{
            if(authoritiesID.contains(SpecialAuthorities.LicenseD.getNumVal()))
                return SpecialAuthorities.LicenseD.getNumVal();
            if(authoritiesID.contains(SpecialAuthorities.LicenseC.getNumVal()))
                return SpecialAuthorities.LicenseC.getNumVal();
            if(authoritiesID.contains(SpecialAuthorities.LicenseB.getNumVal()))
                return SpecialAuthorities.LicenseB.getNumVal();
            if(authoritiesID.contains(SpecialAuthorities.LicenseA.getNumVal()))
                return SpecialAuthorities.LicenseA.getNumVal();
        }
        return -1;
    }
}
