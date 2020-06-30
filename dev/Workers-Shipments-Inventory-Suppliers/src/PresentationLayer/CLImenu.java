package PresentationLayer;

import InterfaceLayer.SharedClassSerice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import static PresentationLayer.SupplierMenu.SupplierOptionsForStoreManager;
import static java.lang.System.exit;


public class CLImenu {
    //Singleton---
    private static CLImenu instance = null;
    private AccessPermissions permission = AccessPermissions.DEFAULT;
    private Boolean logout = false;
    //------------
    private static final String SHALOM =
            "|      |   *         ---                             \n" +
            "|      |   |          |   |/-----    ______  |/----- \n" +
            " ------    |          |   |      |  |      | |      |\n" +
            "|      |   |          |   |      |  |      | |      |\n" +
            "|      |   |         ---  |      |   ------  |      |\n";
    private static final String SuperLee =
            " /-----                                       |                                        \n" +
            " |                                            |                                        \n" +
            " \\____     |     |    _____   r----ר  | /     |           r----ר  r----ר              \n" +
            "      \\    |     |   |     \\  |____   |/      |           |____   |____              \n" +
            "       |   |     |   |      | |       |       |           |       |                    \n" +
            " \\____/    \\ ___ /   |-----/  \\----/  |       |________   \\----/  \\----/          \n" +
            "                     |                                                                 \n" +
            "                     |                                                                 \n";

    private final static String GREETING_MESSAGE = SuperLee + "Hey champ :)";
    private final static String INVALID_INPUT_ERR = "%s is an invalid input, try a number from the list\n";

    private CLImenu(){ }

    public static CLImenu getInstance() {
        if(instance == null){ instance = new CLImenu(); }
        return instance;
    }

    private final Scanner scanner = new Scanner(System.in);
    private Commands commands;
    private ShipmentFlow shipFlow;
    boolean shouldQuit = false;

    public void StartProgram() throws IOException, SQLException {
        SharedClassSerice.createNewDataBase();
        SharedClassSerice.initProducts();
        SupplierMenu.loadRepository();

        commands = new Commands();
        shipFlow = new ShipmentFlow();

        showGreetingMessage();

        mainMenu();
    }

    public void mainMenu() throws IOException, SQLException {
        int input;

        while(!shouldTerminate()){

            if(shouldLogout()){
                this.permission = AccessPermissions.DEFAULT;
                logout = false;
            }

            switch (permission){
                case DEFAULT:
                    identify();
                    break;
                case SHIPMENTS_MANAGER:
                case WORKERS_MANAGER:
                    showMenu(permission);
                    input = getInput();
                    commands.deployCommand(input, permission);
                    break;
                case PRODUCTS_MANAGER:
                    MainMenu.MainMenu();
                    break;
                case SUPPLIERS_MANAGER:
                    SupplierOptionsForStoreManager();
                    break;
                default:
                    throw new RuntimeException("Forgot a permission at StartProgram()");
            }
            if(commands.shouldCreateNewShipment()){
                shipFlow.createNewShipment();
            }
        }
        exit(0);
    }

    private boolean shouldLogout() {
        return commands.isShouldLogout() ||
                logout;
    }

    public void logout(){
        logout = true;
    }

    private void identify() {
        System.out.print("Enter a username or type 'quit': ");
        String permissionStr = takeInput();
        if(permissionStr.toLowerCase().equals("quit")){
            shouldQuit = true;
        }
        else {
            permission = AccessPermissions.permissionFromStr(permissionStr);
            if (permission.equals(AccessPermissions.DEFAULT)) {
                System.out.println("Wrong user name");
            }
        }
    }

    private int getInput() {
        String input = "Default error message";
        int index;
        boolean invalid = false;
        do {
            if(invalid )showInvalidInputMessage(input);

            input = takeInput();
            try{
                index = Integer.parseInt(input) - 1;
            }
            catch (NumberFormatException ex){
                index = -1;
            }
        }
        while(invalid = !(inputInRange(index)));
        return index;
    }

    private void showInvalidInputMessage(String input){
        System.out.printf(INVALID_INPUT_ERR, input);
    }

    private boolean inputInRange(int input){
        return input < commands.getLength() && input >= 0;
    }

    private String takeInput(){
        return scanner.nextLine();
    }

    private boolean shouldTerminate() {
        return commands.shouldTerminate() || shouldQuit;
    }

    private void showGreetingMessage(){
        System.out.println(GREETING_MESSAGE);
    }

    private void showMenu(AccessPermissions permission){
        String[] commandsNames = commands.getNames(permission);
        System.out.println("\nWhat do you want to do?");
        for (int i = 0; i < commandsNames.length; i++){
            System.out.println((i + 1) + ". " + commandsNames[i]);
        }
    }
}
