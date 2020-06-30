package PresentationLayer;


import DataStructures.Pair;
import InterfaceLayer.DTO.DriverInterfaceDTO;
import InterfaceLayer.DTO.ShipmentRequestDTO;
import InterfaceLayer.IShipmentController;
import InterfaceLayer.Parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class ShipmentFlow {

    private static final Scanner scanner = new Scanner(System.in);
    private static final IShipmentController controller = new ErrorHandlingController();
    private static final Parser parser = new Parser();

//    private final List<String> DONE_STRINGS = Arrays.asList("finish", "Finish", "done", "Done");
    private final List<String> QUIT_STRINGS = Arrays.asList("quit", "Quit", "return", "Return", "abort", "Abort");

    private boolean quit;

    public void createNewShipment(){
        quit = false;
        /*Choosing:
         * -Source site
         * -Departure time
         * -Destination branches
         * -products and their quantities for each destination
         */
        println("Creating a new shipment from request queue...");
        println("You can quit at any time by typing " + QUIT_STRINGS.get(0));

        ShipmentRequestDTO request;
        if((request=controller.popNextShipmentRequest()) == null){
            println("There is no more request for shipments in the queue.\nCome back again at another time :)");
            quit = true;
        }
        else {
            Pair<LocalDate, LocalTime> timePair = controller.setShipmentDepartureTime();
            if(timePair == null){
                println("Couldn't find a valid time for your shipment request :(");
                quit = true;
            }
            else{
                println("Here is your shipment request--------");
                println(request);

                println("Your shipment departure date: " + timePair.getFirst().toString());
                println("Your shipment departure time: " + timePair.getSecond().toString());

                //List<String> destinations = createRequest();

                //Choosing the truck and the truck driver
                setTruckAndDriver();

                //Created request successfully, now we are validating the weight and if there is a problem the user may act to solve it
                println("\nAll the details are complete. Now wait for the validation of your request.");
                validateAndSolveWeight(new ArrayList<>(request.getProductsPerDestination().keySet()));

                println("\nYour shipment is valid and is ready to go!");

                if (!quit) controller.createShipment();
            }
        }
        if(quit){
            controller.abortShipment();
        }
    }

    //region Starting new shipment with: source, departure time, destinations,products
//    private List<String> createRequest() {
//        List<String> destinations = new Vector<>();

        //Telling the business layer the user is starting a new shipment with the given source site
        //setSourceForShipment();

        //setDepartureTimeAndDate();

//        while (!quit) {
//            //Starting new certificate
//            Pair<String, Map<String, Integer>> certificate = new Pair<>();
//
//            //Setting a new destination
//            boolean check;
//            do {
//                check = setNewDestination(certificate, destinations);
//                if(check){
//                    //Add the new destination to the destinations list
//                    destinations.add(certificate.getFirst());
//                }
//                if(destinations.isEmpty()){
//                    println("\nYou must choose at least 1 destination");
//                }
//
//            }while (!quit && destinations.isEmpty());
//            if(!check){
//                break;
//            }


            //Setting the products and their quantities for the certificate
//            certificate.setSecond(new HashMap<>());
//            while (!quit) {
//                if (!setNewProductWithQuantity(certificate)) {
//                    if (certificate.getSecond().isEmpty()) {
//                        println("\nYou must choose at least 1 product");
//                    } else {
//                        break;
//                    }
//                }
//            }

//            println("\nYou've finished the request for this address.");
//            if(!quit) controller.acceptCertificate(certificate.getFirst(), certificate.getSecond());
//        }

//        return destinations;
//    }

//    private void setDepartureTimeAndDate() {
//        println("Select departure time and date for the shipment");
//        String departureTime_str = null;
//        String departureDate_str;
//        do {
//            if(departureTime_str != null){
//                System.out.println("Invalid departure time or date, try again");
//            }
//            print("Departure time: ");
//            departureTime_str = takeInput();
//            print("Departure date: ");
//            departureDate_str = takeInput();
//
//        }while(!quit && !parser.setShipmentDepartureTime(departureTime_str, departureDate_str));
//    }

    //region Setting source site
//    private void setSourceForShipment() {
//        if(!quit) {
//            showData("Here are the available sources in the system:", (args)->controller.getSourceSites());
//            String sourceAddress;
//            do {
//                sourceAddress = chooseSource();
//            }
//            while (!quit && !tryInitiatingShipment(sourceAddress));
//        }
//    }

    //Try initiating a new shipment with a give source address, if wasn't successful print an error message
//    private boolean tryInitiatingShipment(String sourceAddress){
//        boolean initiated = controller.initiateShipment(sourceAddress);
//        if(!initiated){
//            println("This address doesn't exist, please choose a valid option:");
//        }
//        return initiated;
//    }

//    private String chooseSource() {
//        print("Please type the address of the source of the shipment: ");
//        return takeInput();
//    }
    //endregion

    //region Setting new destination
//    private boolean setNewDestination(Pair<String, Map<String, Integer>> certificate, List<String> chosenDestinations) {
//        //First show the available destinations with a store keeper in the shift to the user
//        showData("\nHere are the available destinations in the system:", (args) -> controller.getAvailableDestinationSites());
//
//        //Get the user input, and check if the input is valid.
//        //Set the destination input when valid
//        String destinationAddress;
//        do {
//            destinationAddress = chooseDestination();
//            if(DONE_STRINGS.contains(destinationAddress))
//                return false;
//        }
//        while (!quit && !isValidDestination(destinationAddress, chosenDestinations));
//        certificate.setFirst(destinationAddress);
//        return true;
//    }

//    private String chooseDestination() {
//        println("Please type an address or type " + DONE_STRINGS.get(0) + " if you want to stop the request:");
//        return takeInput();
//    }

    //returns true if and only if destination is valid, available, not chosen already
//    private boolean isValidDestination(String destinationAddress, List<String> chosenAddresses) {
//        boolean validAddress = containsAddress(controller.getAvailableDestinationSites(),destinationAddress);
//        boolean chosenAlready = chosenAddresses.contains(destinationAddress);
//        if (!validAddress) {
//            println("This address doesn't exist, please choose a valid option:");
//        }
//        else if(chosenAlready){
//            println("This address already been chosen, please choose a valid option:");
//        }
//        return validAddress && !chosenAlready;
//    }

//    private boolean containsAddress(List<SiteInterfaceDTO> sites, String address){
//        for (SiteInterfaceDTO site: sites) {
//            if(site.getAddress().equals(address))
//                return true;
//        }
//        return false;
//    }
    //endregion

    //region Setting a product with his quantity
//    private boolean setNewProductWithQuantity(Pair<String, Map<String, Integer>> certificate) {
//        Pair<String, Integer> productQuantity = new Pair<>();
//
//        //Select a new product. If user types finish than break(return false).
//        if(!setNewProduct(productQuantity)){
//            return false;
//        }
//
//        //Select the product quantity
//        setQuantityForProduct(productQuantity);
//
//        Map<String, Integer> productQuantityMap = certificate.getSecond();
//        String productID = productQuantity.getFirst();
//        Integer quantity = productQuantity.getSecond();
//        productQuantityMap.put(
//                productID,
//                quantity + (productQuantityMap.getOrDefault(productID, 0))
//        );
//        return true;
//    }

    //region Setting product
//    private boolean setNewProduct(Pair<String, Integer> productQuantity) {
//        showData("\nHere are the available products and their catalog number in the system:", (args)->controller.getProducts());
//
//        String productID;
//        do{
//            productID = chooseProduct();
//            if(DONE_STRINGS.contains(productID))
//                return false;
//        }while(!quit && !isValidProduct(productID));
//
//        productQuantity.setFirst(productID);
//        return true;
//    }

//    private String chooseProduct() {
//        println("Please type a catalog number of a product or 'finish' if you want to stop for this address:");
//        return takeInput();
//    }
//
//    private boolean isValidProduct(String productID) {
//        if(!controller.checkCatalogID(productID)){
//            println("This product doesn't exist, please choose a valid option");
//            return false;
//        }
//        return true;
//    }
    //endregion

    //region Setting quantity for product
//    private void setQuantityForProduct(Pair<String, Integer> productQuantity) {
//        println("Please type the quantity of this product:");
//        int quantity = chooseValidQuantity();
//        productQuantity.setSecond(quantity);
//    }

    private int isValidQuantity(String quantity_str) {
        int quantity = parser.checkValidQuantity(quantity_str);
        if(quantity == -1){
            println("The quantity must be a valid positive number. Type again:");
        }
        return quantity;
    }
    //endregion
    //endregion
    //endregion

    //region Setting the truck and the driver for the current shipment
    private void setTruckAndDriver() {
        //Setting truck and gets the available drivers
        List<DriverInterfaceDTO> driversForChosenTruck = setTruck();

        //Setting the driver
        showData(driversForChosenTruck, "\nHere are the drivers available for the truck you chose, please type the id of the driver you choose:");

        chooseDriverForTruck();
    }

    //region Setting the truck for the shipment
    //Choosing a valid truck with available drivers.
    //Returns a list of the available drivers.
    private List<DriverInterfaceDTO> setTruck() {
        //Setting truck
        List<DriverInterfaceDTO> driversForChosenTruck;
        String licenseNumber = null;
        do{
            if(licenseNumber != null){
                println("There are no drivers available for your truck currently.");
            }
            //Choosing truck
            licenseNumber = chooseValidTruck();

            driversForChosenTruck = quit? new Vector<>() : controller.getDriversForTruck();
        }while (!quit && driversForChosenTruck.isEmpty()); //If there are no drivers available for this truck, try again

        return driversForChosenTruck;
    }

    //Show the available trucks and let the user choose a valid one
    private String chooseValidTruck() {
        showData("\nNow it's time to choose from the following trucks by typing it's license number:", (args)->controller.getTrucks());

        String licenseNumber;
        licenseNumber = takeInput();
        while (!quit && !controller.chooseTruck(licenseNumber)) {
            println("This license number doesn't exist, please choose a valid option:");
            licenseNumber = takeInput();
        }
        return licenseNumber;
    }
    //endregion

    //region Setting the driver for the shipment
    private void chooseDriverForTruck() {
        String idOfDriver = takeInput();
        while (!quit && !controller.chooseDriver(idOfDriver)) {
            println("This ID doesn't exist, doesn't fit your truck or driver is not available, please choose a valid option:");
            idOfDriver = takeInput();
        }
    }
    //endregion
    //endregion

    //region Weight validating
    private void validateAndSolveWeight(List<String> destinations) {
        final int REMOVE_ADDRESS = 2, CHANGE_TRUCK = 1, REMOVE_PRODUCTS = 3;
        while (!quit && !controller.validateWeight()) {

            println("Unfortunately, your order is too heavy.\n" +
                    "Please choose how to handle it by typing the option number:\n" +
                    CHANGE_TRUCK + ". Switch Truck\n" +
                    REMOVE_ADDRESS + ". Remove Site\n" +
                    REMOVE_PRODUCTS + ". Remove products");


            int handleOption = -1;
            try {
                handleOption = Integer.parseInt(takeInput());
            } catch (Exception ignored) { }
            switch (handleOption) {
                case CHANGE_TRUCK:
                    changeTruck();
                    break;
                case REMOVE_ADDRESS:
                    removeDestination(destinations);
                    break;
                case REMOVE_PRODUCTS:
                    removeProducts();
                    break;
                default:
                    println("Please insert one of the above options");
                    break;
            }
        }
    }

    //region Case 1 - Change truck (and driver accordingly) if there are trucks to change to
    private void changeTruck() {
        if (controller.otherTrucksExists()) {
            List<DriverInterfaceDTO> otherTruckWithDrivers = chooseOtherTruckWithDrivers();

            showData(otherTruckWithDrivers, "\nHere are the drivers available for the truck you chose, please type the id of the driver you choose:");
            chooseDriverForTruck();
        }
        else {
            println("There aren't other trucks");
        }

    }

    //Returns the chosen trucks available drivers
    private List<DriverInterfaceDTO> chooseOtherTruckWithDrivers() {
        List<DriverInterfaceDTO> driversForShipment = null;
        do {
            if (driversForShipment != null)
                println("\nThere are no drivers available for your truck currently.");
            //Setting a new valid truck
            showData("\nPlease type the license number of the truck you would like to switch to:", (args)->controller.getOtherTrucks());
            chooseOtherTruck();
            driversForShipment = controller.getDriversForTruck();
        } while (!quit && driversForShipment.isEmpty());//Try again if the truck has no available drivers
        return driversForShipment;
    }

    private void chooseOtherTruck() {
        String otherLicenseNumber = takeInput();
        while (!quit && !controller.chooseOtherTruck(otherLicenseNumber)) {
            println("This license number doesn't fit, please choose a valid option:");
            otherLicenseNumber = takeInput();
        }
    }
    //endregion

    //region Case 2 - Remove destination if there is more than one
    private void removeDestination(List<String> destinations) {
        if(destinations.size() > 1) {
            showData(destinations, "\nPlease type the address you would like to remove from the shipment:");
            chooseAddressToRemove();
        }
        else{
            println("\nShipment must have at least 1 destination");
        }
    }

    private void chooseAddressToRemove() {
        String toRemoveAddress = takeInput();
        while (!quit && !controller.removeAddress(toRemoveAddress)) {
            println("This address doesn't exist, please choose a valid address to remove:");
            toRemoveAddress = takeInput();
        }
        println("The address " + toRemoveAddress + " has been removed from the shipment.");
        println("\nAll the details are complete. Now wait for the validation of your request.");
    }
    //endregion

    //region Case 3 - Remove product
    private void removeProducts() {
        //Show all the products the user can remove divided to destinations
        showData("\nHere are the products available to remove from each destination:", (args)->controller.getCurrentShipmentCertificates());
        boolean done;
        String input;
        String address;
        String catalogNum;
        int numberOfProductsRemoved;
        do {
            //Choosing address
            println("Choose a destination to remove a product from");
            address = takeInput();

            //Choosing catalog number of product
            println("\nPlease type which product catalog number would you like to remove");
            catalogNum = takeInput();

            //Choosing quantity to remove
            int quantity = chooseQuantityForProductToRemove();

            numberOfProductsRemoved = controller.removeProducts(catalogNum, quantity, address);

//            do {
//                println("Remove another product? Y|n");
//                input = takeInput();
//                done = input.equals("y");
//            }while (input.toLowerCase().equals("y") || input.toLowerCase().equals("n"));

        }while(!quit && numberOfProductsRemoved == -1);


        println(numberOfProductsRemoved + " products removed with the catalog number " + catalogNum + " from " + address + ".");
    }

    //returns a positive number chosen by the user which indicates how much to remove from a specific product
    private int chooseQuantityForProductToRemove(){
        println("Now enter how much do you want to remove:");
        return chooseValidQuantity();
    }
    //endregion
    //endregion

    //region Other used private functions
    //Returns a positive number chosen by the user
    private Integer chooseValidQuantity() {
        String  quantity_str;
        int quantity = 1;
        do {
            quantity_str = takeInput();
        } while (!quit && (quantity = isValidQuantity(quantity_str)) == -1);
        return quantity;
    }

    private  <T> void showData(List<T> dataList) {
        if(!quit) {
            for (T element : dataList) {
                println(element);
            }
            if (dataList.isEmpty()) {
                System.out.println("The list is empty :(");
            }
        }
    }
    private  <T> void showData(List<T> dataList, String msg) {
        println(msg);
        showData(dataList);
    }
    private  <T> void showData(String msg, IQuery<T> query) {
        List<T> list = query.deployQuery();
        showData(list, msg);
    }

    private String takeInput(){
        String input = "quit";
        if(!quit) {
            input = scanner.nextLine();
            if (QUIT_STRINGS.contains(input)) {
                quit = true;
            }
        }
        return input;
    }

    private <T> void println(T msg){
        print(msg.toString() + "\n");
    }
    private void print(String str) {
        if(!quit){
            System.out.print(str);
        }
    }
    //endregion
}
