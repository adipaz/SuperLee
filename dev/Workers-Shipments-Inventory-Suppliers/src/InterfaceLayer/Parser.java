package InterfaceLayer;

import DataStructures.Validator;
import InterfaceLayer.DTO.JobInterfaceDTO;
import Exception.InvalidArgumentsNumberException;
import InterfaceLayer.DTO.ShiftInterfaceDTO;
import PresentationLayer.ErrorHandlingController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private static final int ID_LENGTH = 9;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
    private final Controller controller;
    private final IShipmentController shipmentController;
    private static final List<String> TrueString = Arrays.asList("True", "true", "turu", "Tru", "t", "T");
    private static final List<String> FalseString = Arrays.asList("False", "false", "fals", "f", "F");

    public Parser(){
        controller = Controller.getInstance();
        shipmentController = new ErrorHandlingController();
    }
    public int checkValidQuantity(String quantity_str) {
        int quantity = parseInt(quantity_str);
        return (shipmentController.checkValidQuantity(quantity))? quantity : -1;
    }

//    public boolean setShipmentDepartureTime(String departureTime_str, String date_str) {
//        Validator validator = new Validator();
//
//        LocalDate date = parseDate(date_str, validator);
//        LocalTime departureTime = parseTime(departureTime_str, validator);
//
//        return (validator.isValid()) && shipmentController.setShipmentDepartureTime(departureTime, date);
//    }
    //ID, firstName, lastName, salary, startWorkingDate, bankAcc
    public boolean addWorker(String[] workerDetails){
        Validator validator = new Validator();
        String ID, firstName, lastName;
        double salary; LocalDate startWorkingDate; int bankAcc;

        try {
            ID               = parseWorkerID(workerDetails[0], validator);
            firstName        = workerDetails[1];
            lastName         = workerDetails[2];
            salary           = parseSalary(workerDetails[3], validator);
            startWorkingDate = parseDate(workerDetails[4], validator);
            bankAcc          = parseBankAcc(workerDetails[5], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("Add worker got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.addWorker(ID, firstName, lastName, salary, startWorkingDate, bankAcc);
    }
    //ID, firstName, lastName, salary, startWorkingDate, bankAcc
    public boolean editWorkerByID(String[] workerDetails){
        Validator validator = new Validator();
        String ID, firstName, lastName; double salary; LocalDate startWorkingDate; int bankAcc;

        try {
            ID = parseWorkerID(workerDetails[0], validator);
            firstName = workerDetails[1];
            lastName = workerDetails[2];
            salary = parseSalary(workerDetails[3], validator);
            startWorkingDate = parseDate(workerDetails[4], validator);
            bankAcc = parseBankAcc(workerDetails[5], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("Edit worker got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.editWorkerByID(ID, firstName, lastName, salary, startWorkingDate, bankAcc);
    }
    //String jobName
    public boolean addJob(String[] args){
        String jobName;

        try {
            jobName = args[0];
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("Add job got wrong number of exceptions");
        }

        return controller.addJob(jobName);
    }
    //String[] authorityName
    public  boolean addAuthority(String[] args){
        String authorityName;

        try {
            authorityName = args[0];
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("Add authority got wrong number of exceptions");
        }

        return controller.addAuthority(authorityName);
    }
    //String jobID, String authorityID
    public boolean addAuthorityToJob(String[] args){
        Validator validator = new Validator();

        int jobID, authorityID;
        try {
            jobID = parseDefaultID(args[0], validator);
            authorityID = parseDefaultID(args[1], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("Add authority to job got wrong number of exceptions");
        }


        return (validator.isValid()) && controller.addAuthorityToJob(jobID, authorityID);
    }
    //String workerID, String authorityID
    public boolean addAuthorityToWorker(String[] args){
        Validator validator = new Validator();
        String workerID; int authorityID;

        try {
            workerID = parseWorkerID(args[0], validator);
            authorityID = parseDefaultID(args[1], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("Add authority to worker got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.addAuthorityToWorker(workerID, authorityID);
    }
    //String WorkerID,String Day, String startHour, String endHour
    public boolean addWorkerWorkingTimes(String[] args){
        Validator validator = new Validator();
        String workerID; int dayInWeek; boolean day, night;

        try {
            workerID = parseWorkerID(args[0], validator);
            dayInWeek = parseDayInWeek(args[1], validator);
            day = parseBoolean(args[2], validator);
            night = parseBoolean(args[3], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("addWorkerWorkingTimes got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.addWorkerWorkingTimes(workerID, dayInWeek, day, night);
    }
    //String branchAddress, String date, String isDay
    public boolean createShift(String[] args){
        Validator validator = new Validator();
        String branchAddress; LocalDate date; boolean isDay;

        try {
            branchAddress = args[0];
            date = parseDate(args[1], validator);
            isDay = parseBoolean(args[2], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("createShift got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.createShift(branchAddress, date, isDay);
    }
    //String shiftID, String jobID
    public boolean addJobToShift(String[] args){
        Validator validator = new Validator();
        int shiftID, jobID;

        try {
            shiftID = parseDefaultID(args[0], validator);
            jobID = parseDefaultID(args[1], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("addJobToShift got wrong number of exceptions");
        }


        return (validator.isValid()) && controller.addJobToShift(shiftID, jobID);
    }
    //String shiftID, String jobID, String workerID
    public boolean addWorkerToShift(String[] args){
        Validator validator = new Validator();
        int shiftID, jobID; String workerID;

        try {
            shiftID = parseDefaultID(args[0], validator);
            jobID = parseDefaultID(args[1], validator);
            workerID = parseWorkerID(args[2], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("addWorkerToShift got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.addWorkerToShift(shiftID, jobID, workerID);
    }
    //int workerIDm String Term
    public boolean addEmploymentTerm(String[] args) {
        Validator validator = new Validator();
        String workerID, term;

        try {
            workerID = parseWorkerID(args[0], validator);
            term = args[1];
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("addEmploymentTerm got wrong number of exceptions");
        }

        return (validator.isValid()) && controller.addEmploymentTerm(workerID, term);
    }
    //String workerID
    public List<String> getEmploymentTerms(String[] args) {
        Validator validator = new Validator();
        String workerID;

        try {
            workerID = parseWorkerID(args[0], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("getEmploymentTerms got wrong number of exceptions");
        }

        return (validator.isValid())? controller.getEmploymentTerms(workerID) : null;
    }
    //String workerID
    public List<JobInterfaceDTO> getWorkerAvailableJobs(String[] args) {
        Validator validator = new Validator();
        String workerID;

        try {
            workerID = parseWorkerID(args[0], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("getWorkerAvailableJobs got wrong number of exceptions");
        }

        return (validator.isValid())? controller.getWorkerAvailableJobs(workerID) : null;
    }
    //String workerID
    public List<ShiftInterfaceDTO> getShiftHistory(String[] args) {
        Validator validator = new Validator();
        String workerID;

        try {
            workerID = parseWorkerID(args[0], validator);
        }
        catch (IndexOutOfBoundsException ex){
            throw new InvalidArgumentsNumberException("getShiftHistory got wrong number of exceptions");
        }

        return (validator.isValid())? controller.getShiftHistory(workerID) : null;
    }

    //region Parsers for specific arguments
    private int parseDayInWeek(String arg, Validator validator) {
        int day = parseInt(arg);
        if(intNotInRange(day, 1, 7)){
            validator.inValid();
        }
        return day;
    }
    private int parseDefaultID(String input, Validator validator){
        int ID = parseInt(input);
        if(ID < 0) validator.inValid();
        return ID;
    }
    private double parseSalary (String input, Validator validator){
        double salary = parseDouble(input);
        if(salary <= 0) validator.inValid();
        return salary;
    }
    private int parseBankAcc (String input, Validator validator){
        int bankAcc = parseInt(input);
        if(bankAcc <= 0) validator.inValid();
        return bankAcc;
    }
    private String parseWorkerID(String workerDetail, Validator validator) {
        if(workerDetail.length() != ID_LENGTH) validator.inValid();
        for (char c : workerDetail.toCharArray()) {
            if(!isDigit(c)) validator.inValid();
        }
        return workerDetail;
    }
    //endregion

    //region Wrappers for type parsers
    //Returns -1 if the input is not an integer
    private int parseInt(String input){
        try{
            return Integer.parseInt(input);
        }
        catch (Exception ex){
            return -1;
        }
    }
    //Returns -1 if the input is not a double
    private double parseDouble(String input){
        try{
            return Double.parseDouble(input);
        }
        catch (Exception ex){
            return -1;
        }
    }
    //Returns null if the input is invalid
    private LocalDate parseDate(String input, Validator validator){
        try{
            return LocalDate.parse(input, dateFormatter);
        }
        catch (Exception ex){
            validator.inValid();
            return null;
        }
    }
    //1 = true, 0 = false
    private boolean parseBoolean (String input, Validator validator){
        int booleanNumber = parseInt(input);
        if(intNotInRange(booleanNumber, -1, 1)) validator.inValid();

        if(booleanNumber == -1){
            if(TrueString.contains(input)) return true;
            else if(FalseString.contains(input)) return false;
            else{
                validator.inValid();
                return false;
            }
        }
        else{
            return booleanNumber == 1;
        }
    }
    //Returns null if invalid
    //Returns null if invalid
    private LocalTime parseTime(String input, Validator validator){
        try {
            return LocalTime.parse(input,timeFormatter);
        }
        catch (Exception e){
            validator.inValid();
            return null;
        }
    }
    //endregion

    private boolean intNotInRange(int num, int lowerBound, int upperBound) {
        return num > upperBound || num < lowerBound;
    }
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
