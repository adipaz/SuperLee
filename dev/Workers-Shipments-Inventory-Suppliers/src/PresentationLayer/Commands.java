package PresentationLayer;

import Exception.OurException;
import InterfaceLayer.Controller;
import InterfaceLayer.DTO.BranchInterfaceDTO;
import InterfaceLayer.Parser;
import InterfaceLayer.SharedClassSerice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Commands {

    //Main menu
    private final static String SHOW_WORKERS_MENU_STR =  "Show workers menu";
    private final static String SHOW_SHIFT_MENU_STR = "Show shifts menu";
    private final static String SHOW_JOBS_MENU_STR = "Show jobs menu";
    private final static String SHOW_AUTHORITIES_MENU_STR = "Show authorities menu";
    private final static String SHOW_SHIPMENTS_MENU_STR = "Show shipments menu";
    private final static String QUIT_STR = "Quit";

    //Workers menu
    private final static String SHOW_WORKERS_STR = "Show workers list";
    private final static String ADD_WORKER_STR = "Add new worker";
    private final static String EDIT_A_WORKER_STR = "Edit a worker details";
    private final static String ADD_AUTHORITY_TO_WORKER_STR = "Add new authority to worker";
    private final static String ADD_WORKING_TIMES_TO_WORKER_STR = "Add new working time for worker";
    private final static String SHOW_AVAILABLE_JOBS_STR = "Show a worker available jobs";
    private final static String SHOW_WORKER_SHIFTS_HISTORY_STR = "Show a worker shift history";
    private final static String ADD_EMPLOYMENT_TERM_STR = "Add a new employment term to a worker";
    private final static String SHOW_WORKER_EMPLOYMENT_TERMS_STR = "Show a worker employment terms";
    //go back

    //Shifts menu
    private final static String SHOW_SHIFTS_STR = "Show shifts list";
    private final static String ADD_SHIFT_STR = "Create a new shift";
    private final static String ADD_WORKER_TO_SHIFT_STR = "Assign a worker to a shift";
    private final static String ADD_JOB_TO_SHIFT_STR = "Add a job to a shift";
    //go back

    //Jobs menu
    private final static String SHOW_JOBS_STR = "Show jobs list";
    private final static String ADD_JOB_STR = "Add a new job";
    private final static String ADD_AUTHORITY_TO_JOB_STR = "Assign an authority to a job";
    //go back

    //Authorities menu
    private final static String SHOW_AUTHORITIES_STR = "Show authorities list";
    private final static String ADD_AUTHORITY_STR = "Add a new workers authority";

    //Shipments menu
    private final static String SHOW_ALL_SHIPMENT_STR = "Show all shipments";
    private final static String CREATE_NEW_SHIPMENT_STR = "Create a new shipment from request queue";
    //go back

    private static final String SHOW_ALL_BRANCHES_STR = "Show all branches";

    //Natural commands
    private final static String BACK_STR = "Back";

    //Commands arguments
    private final static String ADD_WORKER_ARG = "type: [ID] [First_Name] [Last_Name] [Salary] [Start_Working_Date] [BankAcc]";
    private final static String EDIT_A_WORKER_ARG = "type: [Worker_To_Edit_ID] [First_Name] [Last_Name] [Salary] [Start_Working_Date] [BankAcc]";
    private final static String ADD_AUTHORITY_TO_WORKER_ARG = "type: [WorkerID] [AuthorityID]";
    private final static String ADD_WORKING_TIMES_TO_WORKER_ARG = "type: [WorkerID] [DayInWeek] [Day] [Night]";
    private final static String SHOW_AVAILABLE_JOBS_ARG = "type: [WorkerID]";
    private final static String SHOW_WORKER_SHIFTS_HISTORY_ARG = "type: [WorkerID]";
    private final static String ADD_EMPLOYMENT_TERM_ARG = "type: [WorkerID] [Term]";
    private final static String SHOW_WORKER_EMPLOYMENT_TERMS_ARG = "type: [WorkerID]";

    private final static String ADD_SHIFT_ARG = "type: [Branch_Address] [Date] [Is_Day]";
    private final static String ADD_JOB_TO_SHIFT_ARR = "type: [ShiftID] [JobID]";
    private final static String ADD_WORKER_TO_SHIFT_ARG = "type: [ShiftID] [JobID] [WorkerID]";

    private final static String ADD_JOB_ARG ="type: [Job_Name]";
    private final static String ADD_AUTHORITY_TO_JOB_ARG ="type: [jobID] [AuthorityID]";

    private final static String ADD_AUTHORITY_ARG = "type: [Authority_Name]";


    //Errors
    private final static String NUM_OF_ARG_ERR = "Invalid number of arguments given";

    //Different menus
    private IFunctionalityName[] currentWorkersMenu;
    private IFunctionalityName[] mainMenuCommands;
    private IFunctionalityName[] workersMenuCommands;
    private IFunctionalityName[] jobsMenuCommands;
    private IFunctionalityName[] authoritiesMenuCommands;
    private IFunctionalityName[] shiftsMenuCommands;

    private IFunctionalityName[] currentShipmentsMenu;
    private IFunctionalityName[] shipmentsMenu;


    private String[] commandNamesWorkers;
    private String[] commandNamesShipments;

    private final Scanner scanner = new Scanner(System.in);

    private final Controller controller;
    private final Parser parser;

    //Notify CLI in flow change
    private boolean requestedTerminate = false;
    private boolean requestedNewShipment = false;
    private boolean shouldLogout = false;

    //Constructor
    public Commands(){
        controller = Controller.getInstance();
        parser = new Parser();
        initializeCommands();
        setWorkersMenu(mainMenuCommands);
        setShipmentsMenu(shipmentsMenu);
    }

    //Getters
    public int getLength() {
        return currentWorkersMenu.length;
    }
    public String[] getNames(AccessPermissions permission) {
        if(AccessPermissions.WORKERS_MANAGER.equals(permission))
            return commandNamesWorkers;
        else
            return commandNamesShipments;
    }
    public boolean shouldTerminate(){
        return requestedTerminate;
    }
    public boolean shouldCreateNewShipment() {
        if(requestedNewShipment){
            requestedNewShipment = false;
            return true;
        }
        return false;
    }
    public boolean isShouldLogout() {
        if(shouldLogout){
            shouldLogout = false;
            return true;
        }
        return false;
    }

    public void deployCommand(int input, AccessPermissions permission) throws SQLException {
        if(AccessPermissions.WORKERS_MANAGER.equals(permission)) {
            currentWorkersMenu[input].deployFunction();
        }
        else{
            currentShipmentsMenu[input].deployFunction();
        }
    }

    private void setWorkersMenu(IFunctionalityName[] newMenu){
        currentWorkersMenu = newMenu;
        setNewWorkersCommandNames();
    }
    private void setShipmentsMenu(IFunctionalityName[] newMenu){
        currentShipmentsMenu = newMenu;
        setNewShipmentCommandNames();
    }

    private void initializeCommands(){
        initializeMainCommands();
        initializeWorkersCommands();
        initializeShiftsCommands();
        initializeJobsCommands();
        initializeAuthoritiesCommands();
        initializeShipmentsCommands();
    }

    //Workers module menu
    private void initializeMainCommands() {
        List<IFunctionalityName> commandsList = new ArrayList<>();
        commandsList.add(makeFunction(SHOW_WORKERS_MENU_STR, () -> setWorkersMenu(workersMenuCommands)));
        commandsList.add(makeFunction(SHOW_SHIFT_MENU_STR, () -> setWorkersMenu(shiftsMenuCommands)));
        commandsList.add(makeFunction(SHOW_JOBS_MENU_STR, () -> setWorkersMenu(jobsMenuCommands)));
        commandsList.add(makeFunction(SHOW_AUTHORITIES_MENU_STR, () -> setWorkersMenu(authoritiesMenuCommands)));
        addQuery(commandsList, SHOW_ALL_BRANCHES_STR, "", (args)->controller.getBranches());
        commandsList.add(makeFunction("Advance Day", this::advanceDay));
        commandsList.add(makeFunction("Logout", ()->shouldLogout = true));
        commandsList.add(makeFunction(QUIT_STR, () -> requestedTerminate = true));
        mainMenuCommands = commandsList.toArray(new IFunctionalityName[0]);
    }
    private void initializeWorkersCommands(){
        List<IFunctionalityName> commandsList = new ArrayList<>();

        addQuery(commandsList, SHOW_WORKERS_STR, "", (args)->controller.getWorkers());
        addCommand(commandsList, ADD_WORKER_STR, ADD_WORKER_ARG, parser::addWorker);
        addCommand(commandsList, EDIT_A_WORKER_STR, EDIT_A_WORKER_ARG, parser::editWorkerByID);
        addCommand(commandsList, ADD_AUTHORITY_TO_WORKER_STR, ADD_AUTHORITY_TO_WORKER_ARG, parser::addAuthorityToWorker,
                (args)->toObjectList(controller.getAuthorities())
        );
        addCommand(commandsList, ADD_WORKING_TIMES_TO_WORKER_STR, ADD_WORKING_TIMES_TO_WORKER_ARG, parser::addWorkerWorkingTimes);
        addQuery(commandsList, SHOW_AVAILABLE_JOBS_STR, SHOW_AVAILABLE_JOBS_ARG, parser::getWorkerAvailableJobs);
        addQuery(commandsList, SHOW_WORKER_SHIFTS_HISTORY_STR, SHOW_WORKER_SHIFTS_HISTORY_ARG, parser::getShiftHistory);
        commandsList.add(makeFunction(ADD_EMPLOYMENT_TERM_STR, this::addEmploymentTerm));
        addQuery(commandsList, SHOW_WORKER_EMPLOYMENT_TERMS_STR, SHOW_WORKER_EMPLOYMENT_TERMS_ARG, parser::getEmploymentTerms);

        commandsList.add(makeFunction(BACK_STR, () -> setWorkersMenu(mainMenuCommands)));

        workersMenuCommands = commandsList.toArray(new IFunctionalityName[0]);
    }
    private void initializeShiftsCommands() {
        List<IFunctionalityName> commandsList = new ArrayList<>();

        addQuery(commandsList, SHOW_SHIFTS_STR, "", (args)->controller.getShifts());
        commandsList.add(makeFunction(ADD_SHIFT_STR, this::createShift));
        addCommand(commandsList, ADD_JOB_TO_SHIFT_STR, ADD_JOB_TO_SHIFT_ARR, parser::addJobToShift,
                (args)->toObjectList(controller.getJobs())
        );
        addCommand(commandsList, ADD_WORKER_TO_SHIFT_STR, ADD_WORKER_TO_SHIFT_ARG, parser::addWorkerToShift,
                (args)->toObjectList(controller.getMinimalWorkers()),
                (args)->toObjectList(controller.getJobs())
        );

        commandsList.add(makeFunction(BACK_STR, () -> setWorkersMenu(mainMenuCommands)));

        shiftsMenuCommands = commandsList.toArray(new IFunctionalityName[0]);
    }


    private void initializeJobsCommands() {
        List<IFunctionalityName> commandsList = new ArrayList<>();

        addQuery(commandsList, SHOW_JOBS_STR, "", (args)->controller.getJobs());
        addCommand(commandsList, ADD_JOB_STR, ADD_JOB_ARG, parser::addJob);
        addCommand(commandsList, ADD_AUTHORITY_TO_JOB_STR, ADD_AUTHORITY_TO_JOB_ARG, parser::addAuthorityToJob,
                (args)->toObjectList(controller.getAuthorities())
        );

        commandsList.add(makeFunction(BACK_STR, () -> setWorkersMenu(mainMenuCommands)));

        jobsMenuCommands = commandsList.toArray(new IFunctionalityName[0]);
    }
    private void initializeAuthoritiesCommands() {
        List<IFunctionalityName> commandsList = new ArrayList<>();

        addQuery(commandsList, SHOW_AUTHORITIES_STR, "", (args)->controller.getAuthorities());
        addCommand(commandsList, ADD_AUTHORITY_STR, ADD_AUTHORITY_ARG, parser::addAuthority);
        commandsList.add(makeFunction(BACK_STR, () -> setWorkersMenu(mainMenuCommands)));

        authoritiesMenuCommands = commandsList.toArray(new IFunctionalityName[0]);
    }

    //Shipments module menu
    private void initializeShipmentsCommands() {
        List<IFunctionalityName> commandsList = new ArrayList<>();

        addQuery(commandsList, SHOW_ALL_SHIPMENT_STR, "", controller::getShipments);


        commandsList.add(makeFunction(CREATE_NEW_SHIPMENT_STR, ()-> requestedNewShipment=true));

        commandsList.add(makeFunction("Advance Day", this::advanceDay));
        commandsList.add(makeFunction("Logout", ()->shouldLogout = true));
        commandsList.add(makeFunction(QUIT_STR, () -> requestedTerminate = true));

        shipmentsMenu = commandsList.toArray(new IFunctionalityName[0]);
    }

    //Unique functions
    private void addEmploymentTerm() {
        System.out.println(ADD_EMPLOYMENT_TERM_ARG);
        String[] input = scanner.nextLine().split(" ", 2);
        if(!parser.addEmploymentTerm(input)){
            System.out.println("Couldn't complete action");
        }
        else{
            System.out.println("Successful");
        }
    }
    private void createShift() {
        System.out.println("You can pick from here:");
        showDataInput(controller.getBranches());

        System.out.println(ADD_SHIFT_ARG);
        String input = scanner.nextLine();
        int spaceLIndex = input.lastIndexOf(' ');
        int spaceSecondLIndex = input.substring(0, spaceLIndex).lastIndexOf(' ');
        String address = input.substring(0, spaceSecondLIndex);
        String date = input.substring(spaceSecondLIndex+1, spaceLIndex);
        String isDay = input.substring(spaceLIndex+1);
        try {
            if(!parser.createShift(new String[]{address, date, isDay})){
                System.out.println("Couldn't complete action");
            }
            else{
                System.out.println("Successful");
            }
        }
        catch (OurException e){
            printError(e.getMessage());
        }
    }
    private void advanceDay() throws SQLException {
        SharedClassSerice.nextDay();
        System.out.println("\n~~~~~~~Today is " +SharedClassSerice.getDayString()+"~~~~~~~");
    }

    private void setNewWorkersCommandNames(){
        commandNamesWorkers = new String[currentWorkersMenu.length];
        for (int i = 0; i < currentWorkersMenu.length; i++) {
            commandNamesWorkers[i] = currentWorkersMenu[i].getName();
        }
    }
    private void setNewShipmentCommandNames(){
        commandNamesShipments = new String[currentShipmentsMenu.length];
        for (int i = 0; i < currentShipmentsMenu.length; i++) {
            commandNamesShipments[i] = currentShipmentsMenu[i].getName();
        }
    }

    private boolean isValidArgNum(String[] arguments, String argString){
        return arguments.length == argString.split(" ").length - 1;
    }

    @SafeVarargs
    private final void genericCommand(String argString, ICommand function, IQuery<Object>... toPickFrom){
        printOptions(toPickFrom);

        System.out.println(argString);
        String[] input = scanner.nextLine().split(" ");
        if(isValidArgNum(input, argString)){
            try {
                if(!function.deployCommand(input)){
                    printError("Couldn't complete action");
                }
                else{
                    colorPrint( "Successful");
                }
            }
            catch (OurException e){
                printError(e.getMessage());
            }
        }
        else{
            printError(NUM_OF_ARG_ERR);
        }
    }
    //If the function doesnt take arguments argString == ""
    @SafeVarargs
    private final <T> void genericQuery(String argString, IQuery<T> function, IQuery<Object>... toPickFrom){
        printOptions(toPickFrom);

        String[] input = new String[0];
        boolean valid = true;
        if(!argString.equals("")) {
            System.out.println(argString);
            input = scanner.nextLine().split(" ");
            valid = isValidArgNum(input, argString);
        }
        if(valid){
            try {
                List<T> output = function.deployQuery(input);
                if(output == null){
                    printError("Invalid arguments");
                }
                else {
                    showDataOutput(output);
                }
            }
            catch (OurException e){
                printError(e.getMessage());
            }
        }
        else{
            printError(NUM_OF_ARG_ERR);
        }
    }

    @SafeVarargs
    private final <T> void addQuery(List<IFunctionalityName> functionsList, String queryDesc, String queryArg, IQuery<T> func, IQuery<Object>... toPickFrom){
        functionsList.add(makeFunction(queryDesc,
                () -> genericQuery(queryArg, func, toPickFrom)));
    }
    @SafeVarargs
    private final void addCommand(List<IFunctionalityName> functionsList, String commandDesc, String commandArg, ICommand func, IQuery<Object>... toPickFrom){
        functionsList.add(
                makeFunction(
                            commandDesc,
                            () -> genericCommand(commandArg, func, toPickFrom)
                ));
    }

    private IFunctionalityName makeFunction(String name, IFunctionality toDeploy){
        return new IFunctionalityName() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void deployFunction() throws SQLException {
                toDeploy.deployFunction();
            }
        };
    }

    private <T> void showDataOutput(List<T> output){
        if(output.isEmpty()){
            colorPrint("Nothing to show :(");
        }
        else {
            for (T out : output) {
                colorPrint(out.toString());
            }
        }
    }
    private <T> void showDataInput(List<T> output){
        if(output.isEmpty()){
            colorPrint("Nothing to pick from :(");
        }
        else {
            for (T out : output) {
                colorPrint(out.toString());
            }
        }
    }

    private void printOptions(IQuery<Object>[] toPickFrom) {
        if(toPickFrom.length > 0) {
            System.out.println("You can pick from here:");
            for (IQuery<Object> query : toPickFrom) {
                List<Object> pickList = query.deployQuery();
                showDataInput(pickList);
            }
        }
    }
    private void printError(String errorMsg) {
        colorPrint(errorMsg);
    }
    private void colorPrint(String msg){
        System.out.println(msg);
    }

    private <T> List<Object> toObjectList(List<T> lst){
        return lst.stream().map(object->(Object)object).collect(Collectors.toList());
    }
}
