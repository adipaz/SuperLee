package InterfaceLayer.DTO;

import BusinessLayer.Enums.WorkingTimes;
import BusinessLayer.Workers.Worker;

import java.time.LocalDate;
import java.util.List;

public class WorkerInterfaceInterfaceDTO extends DriverInterfaceDTO {
    private final double salary;
    private final LocalDate startWorkingDate;
    private final int bankAccID;
    private final WorkingTimes[]  constrains;
    private final List<String> employmentTerms;

    public WorkerInterfaceInterfaceDTO(Worker worker, List<String> authoritiesNames) {
        super(worker, authoritiesNames);
        this.salary = worker.getSalary();
        this.startWorkingDate = worker.getStartWorkingDate();
        this.bankAccID = worker.getBankAccID();
        this.constrains = worker.getConstrains();
        this.employmentTerms = worker.getEmploymentTerms();
    }

    public String toString(){
        String str = super.toString();

        str += "Start Working Date: " + startWorkingDate + "\tBank Account: " + bankAccID + "\tSalary: " + salary + "\n";

        //Working times
        str += workingTimesToString();

        //Employment terms
        str += termsToString();

        return str;
    }

    private String workingTimesToString() {
        StringBuilder str;
        str = new StringBuilder("Days:        \tSunday   \tMonday   \tTuesday  \tWednesday\tThursday \tFriday   \tSaturday \n");
        str.append("Available at:\t");
        for (WorkingTimes workTime:constrains) {
            str.append((workTime == WorkingTimes.day) ? "Day      " :
                    (workTime == WorkingTimes.night) ? "Night    " :
                            (workTime == WorkingTimes.both) ? "Both     " :
                                    "None     ");
            str.append("\t");
        }
        str.append("\n");
        return str.toString();
    }

    private String termsToString() {
        StringBuilder str;
        if(employmentTerms.isEmpty()){
            str = new StringBuilder("No employment terms");
        }
        else {
            str = new StringBuilder("Employment terms:\n");
            for (String employmentTerm : employmentTerms) {
                str.append(employmentTerm).append("\n");
            }
        }
        str.append("\n");

        return str.toString();
    }
}
