package DataAccessLayer.DALDTO;

import BusinessLayer.Enums.WorkingTimes;

import java.time.LocalDate;
import java.util.List;

public class WorkerDALDTO implements DALDTO {
    private String ID;
    private String firstName;
    private String lastName;
    private LocalDate startWorkingDate;
    private double salary;
    private int bankAccount;
    private List<Integer> authorityIDs;
    private WorkingTimes[] constrains;
    private List<String> employmentTerms;

    public WorkerDALDTO(String id, String firstName, String lastName, LocalDate startWorkingDate, double salary, int bankAccount, List<Integer> authorityIDs, WorkingTimes[] constrains, List<String> employmentTerms) {
        ID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startWorkingDate = startWorkingDate;
        this.salary = salary;
        this.bankAccount = bankAccount;
        this.authorityIDs = authorityIDs;
        this.constrains = constrains;
        this.employmentTerms = employmentTerms;
    }

    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getStartWorkingDate() {
        return startWorkingDate;
    }

    public double getSalary() {
        return salary;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public List<Integer> getAuthorityIDs() {
        return authorityIDs;
    }

    public WorkingTimes[] getConstrains() {
        return constrains;
    }

    public List<String> getEmploymentTerms() {
        return employmentTerms;
    }

}
