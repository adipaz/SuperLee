package PresentationLayer;
import DataStructures.Pair;
import InterfaceLayer.*;
import InterfaceLayer.DTO.ShipmentInterfaceDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class SupplierMenu {

    public static void SupplierOptionsForStoreManager() throws IOException, SQLException {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Select Operation: ");
            System.out.println("(1) View Suppliers");
            System.out.println("(2) Edit Supplier Details");
            System.out.println("(3) Add New Supplier");
            System.out.println("(4) Delete Supplier");
            System.out.println("(5) Show Shipments Reports");
            System.out.println("(6) Show Inventory Reports");
            System.out.println("(7) Advance Day");
            System.out.println("(8) Logout");
            String option1 = input.nextLine();
            int option;
            try{
                option=Integer.parseInt(option1);
            }
            catch (Exception ex)
            {
                option = 0;
            }
            switch (option) {
                case 1: {
                    viewSuppliers();
                    break;
                }

                case 2: {
                    EditSupplier();
                    break;
                }

                case 3: {
                    AddSupplier();
                    break;
                }
                case 4: {
                    deleteSupplier();
                    break;
                }
                case 5: {
                    System.out.println("~~~~~~~~Shipments report~~~~~~~~");
                    List<ShipmentInterfaceDTO> ships = Controller.getInstance().getShipmentsReports();
                    for (ShipmentInterfaceDTO ship: ships) {

                        System.out.println(ship.toString());
                    }
                    break;
                }
                case 6: {
                    System.out.println(InventoryController.getAllReports());
                    break;
                }
                case 7: {
                    SharedClassSerice.nextDay();
                    System.out.println("\n~~~~~~~Today is " +SharedClassSerice.getDayString()+"~~~~~~~");
                    break;
                }
                case 8:{
                    CLImenu.getInstance().logout();
                    exit = true;
                    CLImenu.getInstance().mainMenu();
                    break;
                }
            }
        }
    }

    public static void SupplierOptionsForStoreKeeper() throws IOException, SQLException {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Select Operation: ");
            System.out.println("(1) View Suppliers");
            System.out.println("(2) Edit Supplier Details");
            System.out.println("(3) Add New Supplier");
            System.out.println("(4) Delete Supplier");
            System.out.println("(5) Advance Day");
            System.out.println("(6) Logout");
            String option1 = input.nextLine();
            int option;
            try{
                option=Integer.parseInt(option1);
            }
            catch (Exception ex)
            {
                option = 0;
            }
            switch (option) {
                case 1: {
                    viewSuppliers();
                    break;
                }

                case 2: {
                    EditSupplier();
                    break;
                }

                case 3: {
                    AddSupplier();
                    break;
                }
                case 4: {
                    deleteSupplier();
                    break;
                }
                case 5: {
                    SharedClassSerice.nextDay();
                    System.out.println("\n~~~~~~~Today is " +SharedClassSerice.getDayString()+"~~~~~~~");
                    break;
                }
                case 6:{
                    CLImenu.getInstance().logout();
                    exit = true;
                    CLImenu.getInstance().mainMenu();
                    break;
                }
            }
        }
    }

    private static void deleteSupplier() throws IOException, SQLException {
        loadRepository();
        Scanner input = new Scanner(System.in);
        String name = "";
        String bnNumber = "";
        boolean flag=false;
        boolean noSuppliers=false;
        while (!flag){
            System.out.println("choose supplier to delete");
            String supList = SupplierService.viewForEdit();
            if(supList.equals("")){
                System.out.println("NO SUPPLIERS EXIST");
                noSuppliers=true;
                break;
            }
            System.out.print(supList);
            int numOfSup = SupplierService.getNumOfSupp()+1;
            System.out.println(numOfSup+") back to menu");
            String supplier_index = input.nextLine();
            if(supplier_index.equals(Integer.toString(numOfSup)))
                SupplierOptionsForStoreManager();
            String[] supInfo = supList.split("( )|(\n)");
            for (int i = 0; i < supInfo.length; i++) {
                if (supInfo[i].equals(supplier_index+")")) {
                    name = supInfo[i + 2];
                    bnNumber = supInfo[i + 5];
                    flag=true;
                    break;
                }
                else{
                    if(i==supInfo.length-1){
                        System.out.println("input is out of bounds");
                    }
                }
            }
        }
        if(noSuppliers)
            return;

        if(SupplierService.DeleteSupplier(name,bnNumber))
            System.out.println("Supplier Deleted Successfully!");
    }

    private static void viewSuppliers() throws SQLException {
        loadRepository();
        System.out.println(SupplierService.viewSuppliers());
    }

    private static void viewSupplierContacts(String name,String bnNumber) {
        System.out.println(ContactService.viewSupplierContact(name,bnNumber));
    }

    private static void AddSupplier() throws SQLException {
        loadRepository();
        Scanner input = new Scanner(System.in);
        System.out.println("ADDING SUPPLIER");
        System.out.println("insert supplier's name");
        String name = input.nextLine();
        System.out.println("insert BN number");
        String bnNumber = input.nextLine();
        System.out.println("insert bank account number");
        String bankAccountNumber = input.nextLine();
        System.out.println("insert address");
        String address = input.nextLine();
        System.out.println("select payment method: \n (1) cheque \n (2) cash \n (3) transfer");
        String paymentMethod="";
        String str_option = input.nextLine();
        int option=0;
        boolean parseSuccess = false;
        while(!parseSuccess) {
            try {
                option=Integer.parseInt(str_option);
                parseSuccess=true;
            } catch (Exception e) {
                System.out.println("INVALID INPUT");
                str_option = input.nextLine();
            }
        }

        switch (option){
            case 1: {
                paymentMethod = "cheque";
                break;
            }
            case 2: {
                paymentMethod = "cash";
                break;
            }
            case 3: {
                paymentMethod = "transfer";
                break;
            }
        }

        List<List<String>> contacts = new LinkedList<>();
        System.out.println("insert contact");
        System.out.println("insert new contact: y|n");
        String yesORno = input.nextLine();
        while(!yesORno.equals("y")&&!yesORno.equals("n"))
        {
            System.out.println("INVALID INPUT");
            yesORno = input.nextLine();
        }
        while (yesORno.equals("y")) {
            System.out.println("insert name");
            String cname = input.nextLine();
            System.out.println("insert email");
            String email = input.nextLine();
            System.out.println("insert phone number");
            String phone = input.nextLine();

            List<String> contact = new LinkedList<>();
            contact.add(cname);
            contact.add(email);
            contact.add(phone);
            contacts.add(contact);
            System.out.println("insert new contact: y|n");
            yesORno = input.nextLine();
        }


        System.out.println("ADDING CONTRACT");
        System.out.println("insert minimum discount");
        String xDiscount = input.nextLine();


        String[] fixeddays = {"0","0","0","0","0","0","0","0"};

        List<List<String>> products = new LinkedList<>();

        if (SupplierService.AddSupplier(name, bnNumber, bankAccountNumber, paymentMethod, contacts, products, fixeddays, xDiscount,address))
            System.out.println("Supplier Was Added Successfully!");
        else
            System.out.println("OPERATION FAILED");
    } //done


    private static void EditSupplier() throws IOException, SQLException {
        loadRepository();
        Scanner input = new Scanner(System.in);
        String name = "";
        String bnNumber = "";
        boolean flag = false;
        boolean noSuppliers=false;
        while(!flag) {
            System.out.println("CHOOSE A SUPPLIER TO EDIT");
            String supList = SupplierService.viewForEdit();
            if(supList.equals("")){
                System.out.println("NO SUPPLIERS EXIST");
                noSuppliers=true;
                break;
            }
            System.out.print(supList);
            int numOfSupp = SupplierService.getNumOfSupp()+1;
            System.out.println(numOfSupp+") back to menu");
            String supplier_index = input.nextLine();
            if(supplier_index.equals(String.valueOf(numOfSupp))) {
                SupplierOptionsForStoreManager();
            }
            String[] supInfo = supList.split("( )|(\n)");
            for (int i = 0; i < supInfo.length; i++) {
                if (supInfo[i].equals(supplier_index + ")")) {
                    name = supInfo[i + 2];
                    bnNumber = supInfo[i + 5];
                    flag=true;
                    break;
                } else {
                    if (i == supInfo.length - 1) {
                        System.out.println("INVALID INPUT");
                    }
                }
            }
        }

        if(noSuppliers)
            return;

        flag = false;
        while(!flag) {
            System.out.println("CHOOSE A FIELD TO EDIT");
            System.out.println("(1) supplier's name");
            System.out.println("(2) supplier's Bn number");
            System.out.println("(3) supplier's bank account number");
            System.out.println("(4) supplier's payment method");
            System.out.println("(5) supplier's contract");
            System.out.println("(6) supplier's contacts");
            System.out.println("(7) return");

            flag = true;

            String option1 = input.nextLine();
            int option;
            try {
                option = Integer.parseInt(option1);
            } catch (Exception ex) {
                option = 8;
            }

            switch (option) {
                case 1: {
                    System.out.println("insert new name");
                    //input.nextLine();
                    String newname = input.nextLine();
                    if (SupplierService.changeSupplierName(name, bnNumber, newname))
                        System.out.println("Name Changed Successfully!");
                    else
                        System.out.println("OPERATION FAILED");
                    break;
                }

                case 2: {
                    System.out.println("insert new Bn Number");
                    //input.nextLine();
                    String newbnNumber = input.nextLine();
                    if (SupplierService.changeSupplierBnNumber(name, bnNumber, newbnNumber))
                        System.out.println("Bn Number changed successfully!");
                    else
                        System.out.println("OPERATION FAILED");
                    break;
                }

                case 3: {
                    System.out.println("insert new bank account number");
                    //input.nextLine();
                    String newBankAccountNumber = input.nextLine();
                    if (SupplierService.changeSupplierBankAccountNumber(name, bnNumber, newBankAccountNumber))
                        System.out.println("Bank Account Number changed successfully!");
                    else
                        System.out.println("OPERATION FAILED");
                    break;
                }
                case 4: {
                    System.out.println("insert new payment method: \n (1) cheque \n (2) cash \n (3) transfer");
                    int option2 = 0;
                    try {
                        option2 = input.nextInt();
                    } catch (Exception ex) {
                        System.out.println("OPERATION FAILED");
                        break;
                    }

                    String slashn = input.nextLine();
                    String newpaymentMethod = "";
                    switch (option2) {
                        case 1: {
                            newpaymentMethod = "cheque";
                            break;
                        }
                        case 2: {
                            newpaymentMethod = "cash";
                            break;
                        }
                        case 3: {
                            newpaymentMethod = "transfer";
                            break;
                        }
                    }

                    if (SupplierService.changeSupplierPaymentMethod(name, bnNumber, newpaymentMethod))
                        System.out.println("payment method changed successfully!");
                    else
                        System.out.println("OPERATION FAILED");
                    break;
                }
                case 5: {
                    ContractOperations(name, bnNumber);
                    break;
                }
                case 6: {
                    ContactsOperations(name, bnNumber);
                    break;
                }
                case 7: {
                    EditSupplier();
                }
                case 8:{
                    System.out.println("INVALID INPUT");
                    flag = false;
                    break;
                }

            }
        }

    }



    private static void ContractOperations(String supplierName,String supplierBnNumber) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);
        while ((true)) {
            System.out.println("Select Operation:");
            System.out.println("(1) view supplier's contract");
            System.out.println("(2) change supplier's minimum discount");
            System.out.println("(3) edit products");
            System.out.println("(4) change fixed-days Orders");
            System.out.println("(5) return");
            String op = input.nextLine();
            int option;
            try
            {
                option=Integer.parseInt(op);
            }
            catch (Exception ex)
            {
                System.out.println("INVALID INPUT");
                option=0;
                break;
            }
            //int option = input.nextInt();
            //input.nextLine();
            switch (option) {
                case 1:{
                    viewSupplierContract(supplierName,supplierBnNumber);
                    break;
                }
                case 2:{
                    changeMinDiscount(supplierName,supplierBnNumber);
                    break;
                }
                case 3:{
                    editProducts(supplierName,supplierBnNumber);
                    break;
                }

                case 4:{
                    fixedDayOperations(supplierName, supplierBnNumber);
                    break;
                }
                case 5:{
                    EditSupplier();
                    return;
                }
            }

        }
    }

    private static void fixedDayOperations(String name,String bnNumber) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);
        while ((true)) {
            System.out.println("Select Operation:");
            System.out.println("(1) create new permanent order");
            System.out.println("(2) edit existing permanent order");
            System.out.println("(3) delete existing permanent order");
            System.out.println("(4) return");

            String op = input.nextLine();
            int option;
            try
            {
                option=Integer.parseInt(op);
            }
            catch (Exception ex)
            {
                System.out.println("INVALID INPUT");
                option=0;
                break;
            }

            switch (option) {
                case 1:
                {
                    addPerOrder(name,bnNumber);
                    break;
                }
                case 2:
                {
                    editPerOrder(name, bnNumber);
                    break;
                }
                case 3:
                {
                    deletePerOrder(bnNumber,name);
                }
                case 4:
                {
                    ContractOperations(name, bnNumber);
                    return;
                }
            }
        }

    }

    private static void deletePerOrder(String bnNumber,String name)
    {
        Scanner input = new Scanner(System.in);

        List<Pair<String,String>> orders = OrderService.printPerOrders(name, bnNumber);
        if(orders.isEmpty())
        {
            System.out.println("The Supplier doesnt have any weekly orders to be shown");
            return;
        }
        System.out.println("Select order to edit:");
        for(int i=0;i<orders.size();i++)
        {
            System.out.println(i+1+")"+" "+orders.get(i).getSecond());
        }
        String op = input.nextLine();
        int option = Integer.parseInt(op);
        String orderID = orders.get(option-1).getFirst();

        if (OrderService.deletePerOrder(name,bnNumber,orderID))
            System.out.println("order deleted successfully!");
        else
            System.out.println("Operation failed");
    }

    private static boolean createPerOrder(String bnNumber,String name,String day)
    {
        Scanner input = new Scanner(System.in);
        String index="";
        String amount ="";
        String adress="";
        String phone="";


        System.out.println("Select address");
        Map<Integer,String> address = OrderService.printAddressesToMenu();
        for(Integer i : address.keySet()) {
            System.out.println(i + ") " + address.get(i));
        }
        String iaddress = input.nextLine();
        adress = address.get(Integer.parseInt(iaddress));


        System.out.println("Insert contact's phone number");
        phone = input.nextLine();

        List<String[]> orderProducts = new LinkedList<>();
        System.out.println("Select Product:");
        boolean yn = true;

        while(yn) {
            HashMap<String, Pair<String, String>> supProducts = OrderService.viewProductsForPerOrders(bnNumber, name);
            if (supProducts.isEmpty()) {
                System.out.println("THE SUPPLIER DOESNT HAVE ANY PRODUCTS TO BE SHOWN");
                return false;
            }
            for (String p_index :
                    supProducts.keySet()) {
                System.out.println(p_index + ") " + supProducts.get(p_index).getSecond());
            }
            index = input.nextLine();
            if (!supProducts.get(index).equals(null)) {
                System.out.println("Select amount:");
                amount = input.nextLine();
                String[] details = new String[6];
                details[0] = supProducts.get(index).getFirst(); //product's catalog id
                details[1] = ProductSService.getProductName(name,bnNumber,details[0]);
                details[2] = amount;
                List<String> priceDetails = OrderService.getOrderPrices(name, bnNumber, details[0],amount);
                details[3] = priceDetails.get(0); //original price
                details[4] = priceDetails.get(1);//discount
                details[5] = priceDetails.get(2);//final price

                orderProducts.add(details);
            }

            System.out.println("Add another product? y|n");
            if(input.nextLine().equals("n"))
                yn = false;
        }

        return OrderService.addPerOrder(name,bnNumber,orderProducts,day,adress,phone);

    }

    private static void editPerOrder(String name,String bnNumber) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);

        List<Pair<String,String>> orders = OrderService.printPerOrders(name, bnNumber);
        if(orders.isEmpty())
        {
            System.out.println("The Supplier doesnt have any weekly orders to be shown");
            return;
        }
        System.out.println("Select order to edit:");
        for(int i=0;i<orders.size();i++)
        {
            System.out.println(i+1+")"+" "+orders.get(i).getSecond());
        }
        String op = input.nextLine();
        int option = Integer.parseInt(op);
        String orderID = orders.get(option-1).getFirst();

        System.out.println("Choose a field to edit:");
        System.out.println("(1) address");
        System.out.println("(2) day");
        System.out.println("(3) contact's phone number");
        System.out.println("(4) products");
        System.out.println("(5) return");
        option = input.nextInt();
        input.nextLine();

        switch (option)
        {
            case 1:
            {
               System.out.println("Select new address:");
                String newadress="";
                System.out.println("Select address");
                Map<Integer,String> address = OrderService.printAddressesToMenu();
                for(Integer i : address.keySet()) {
                    System.out.println(i + ") " + address.get(i));
                }
                String iaddress = input.nextLine();
                newadress = address.get(Integer.parseInt(iaddress));
                if(OrderService.changePerOrderAddress(newadress,orderID))
                    System.out.println("Address changed successfully");
                else System.out.println("Operation failed");
                break;
            }
            case 2:
            {
                boolean succeed = false;
                String day="";
                while(!succeed) {
                    System.out.println("Choose a new day:");
                    System.out.println("1) Sunday");
                    System.out.println("2) Monday");
                    System.out.println("3) Tuesday");
                    System.out.println("4) Wednesday");
                    System.out.println("5) Thursday");
                    System.out.println("5) Friday");
                    System.out.println("6) Saturday");

                    String option1 = input.nextLine();
                    int option_;
                    try{
                        option_=Integer.parseInt(option1);
                    }
                    catch (Exception ex)
                    {
                        option_ = 0;
                        System.out.println("Invalid input");
                    }
                    if(OrderService.getToday().equals(option1))
                    {
                        option_=0;
                        System.out.println("A weekly order must be notified at least a day beforehand");
                    }
                    if(option_!=0) {
                        day = String.valueOf(option_);
                        succeed=true;
                    }
                }

                if(OrderService.moveWeeklyOrderToDiffDay(bnNumber,name,day,orderID))
                {
                    System.out.println("Day changed successfully");
                }
                else
                {
                    System.out.println("Operation failed");
                }
                break;
            }
            case 3:
            {
                System.out.println("insert new phone-number:");
                String newphone = input.nextLine();
                if(OrderService.changePerOrderPhone(newphone,orderID))
                    System.out.println("Phone-number changed successfully");
                else System.out.println("Operation failed");
                break;
            }
            case 4:
            {
                editProductsInPerOrder(name,bnNumber,orderID);
                break;
            }
            case 5:
            {
                fixedDayOperations(name, bnNumber);
                return;
            }
        }
    }

    private static void editProductsInPerOrder(String name,String bnNumber,String orderID) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);

        System.out.println("Select operation:");
        System.out.println("1) view products");
        System.out.println("2) delete product");
        System.out.println("3) add new product");
        System.out.println("4) return");

        int option = input.nextInt();
        input.nextLine();

        switch (option) {
            case 1: {
                String productsFromOrder = OrderService.viewProductsFromPerOrder(orderID);
                if(productsFromOrder.equals(null))
                    System.out.println("There are no products in the order to be shown");
                else System.out.println(productsFromOrder);
                break;
            }
            case 2:{
                System.out.println("select product to delete by the catalogID before the '_' (example: to delete product 11_2, select only 11)");
                String catalogID = input.nextLine();
                if(OrderService.deleteProductFromOrder(orderID,catalogID))
                    System.out.println("product deleted successfully");
                else System.out.println("operation failed");
                break;

            }
            case 3:{
                System.out.println("Select Product:");
                HashMap<String, Pair<String, String>> supProducts = OrderService.viewProductsForPerOrders(bnNumber, name);
                if (supProducts.isEmpty()) {
                    System.out.println("THE SUPPLIER DOESNT HAVE ANY PRODUCTS TO BE SHOWN");
                    break;
                }
                for (String p_index :
                        supProducts.keySet()) {
                    System.out.println(p_index + ") " + supProducts.get(p_index).getSecond());
                }
                String index = input.nextLine();
                if (!supProducts.get(index).equals(null)) {
                    System.out.println("Select amount:");
                    String amount = input.nextLine();
                    String[] details = new String[6];
                    details[0] = supProducts.get(index).getFirst(); //product's catalog id
                    details[1] = ProductSService.getProductName(name,bnNumber,details[0]);
                    details[2] = amount;
                    List<String> priceDetails = OrderService.getOrderPrices(name, bnNumber, details[0],amount);
                    details[3] = priceDetails.get(0); //original price
                    details[4] = priceDetails.get(1);//discount
                    details[5] = priceDetails.get(2);//final price

                    if(OrderService.addProductToOrder(orderID,details))
                        System.out.println("Product added successfully");
                    else System.out.println("Operation failed");
                }
                break;

            }
            case 4:{
                editPerOrder(name, bnNumber);
                return;
            }
        }

    }


    private static void addPerOrder(String name,String bnNumber)
    {
        Scanner input = new Scanner(System.in);
        boolean succeed = false;
        String day="";

        System.out.println("Create New Permanent Order");
        while(!succeed) {
            System.out.println("Choose a day:");
            System.out.println("1) Sunday");
            System.out.println("2) Monday");
            System.out.println("3) Tuesday");
            System.out.println("4) Wednesday");
            System.out.println("5) Thursday");
            System.out.println("6) Friday");
            System.out.println("7) Saturday");

            String option1 = input.nextLine();
            int option;
            try{
                option=Integer.parseInt(option1);
            }
            catch (Exception ex)
            {
                option = 0;
                System.out.println("Invalid input");
            }
            if(OrderService.getToday().equals(option1))
            {
                option=0;
                System.out.println("A weekly order must be notified at least a day beforehand");
            }
            if(option!=0) {
                day = String.valueOf(option);
                succeed=true;
            }
        }




        if(createPerOrder(bnNumber,name,day))
            System.out.println("Permanent-Order added successfully!");
        else
            System.out.println("Operation failed");
    }


    //TODO: need to add getWeight from the user
    private static void editProducts(String name,String bnNumber) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);
        while (true){
            System.out.println();
            System.out.println("Select Operation:");
            System.out.println("(1) view supplier's products");
            System.out.println("(2) add product to the cotract");
            System.out.println("(3) delete product from contract");
            System.out.println("(4) edit exist product's price");
            System.out.println("(5) return");
            int option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1: {
                    viewProducts(name,bnNumber);
                    break;
                }
                case 2:{
                    System.out.println("To add an existing product insert 1, to add a new product insert 2");
                    String onrORtwo =input.nextLine();
                    while(!onrORtwo.equals("1")&!onrORtwo.equals("2"))
                    {
                        System.out.println("INVALID INPUT");
                        System.out.println("insert 1|2");
                        onrORtwo=input.nextLine();
                    }
                    if(onrORtwo.equals("1"))
                    {
                        String products = ProductSService.getAllProducts();
                        if(products.equals(""))
                        {
                            System.out.println("There are no products in the system");
                            break;
                        }
                        else{
                            System.out.println("existing products:\n");
                            System.out.println(products);
                        }
                        System.out.println("insert the product's catalogID before the '_' that you want to add (example: to add product 11_2, select only 11)");
                        String catalogID = input.nextLine();
                        while (!ProductSService.existCatalogID(catalogID)){
                            System.out.println("Invalid Catalog-ID");
                            System.out.println("insert the product's catalog-ID you want to add");
                            catalogID = input.nextLine();
                        }
                        System.out.println("insert price per unit");
                        String price = input.nextLine();
                        if(ContractService.addExistProductToContract(name,bnNumber,ProductSService.getProductNameByCataogID(catalogID),catalogID,price,ProductSService.getWeightByCatalogId(catalogID)))
                            System.out.println("Product added successfully");
                        else
                            System.out.println("OPERATION FAILED");
                    }
                    else{

                        String products = ProductSService.getAllProducts();
                        if(products.equals("")) {
                            System.out.println("insert new product catalog id (without '_')");
                        }
                        else {
                            System.out.println("existing products:\n");
                            System.out.println(products);
                            System.out.println("insert catalog id that does not exist in the list (without '_')");
                        }
                        String catalogID = input.nextLine();
                        System.out.println("insert product name");
                        String pname = input.nextLine();
                        System.out.println("insert price per unit");
                        String price = input.nextLine();
                        System.out.println("insert weight");
                        String weight = input.nextLine();
                        if(ContractService.addNewProductToContract(name,bnNumber,pname,catalogID,price,Double.parseDouble(weight))) {
                            System.out.println("Product added successfully");
                            addNewProductToTheSystem(catalogID,pname,weight);
                        }
                        else
                            System.out.println("OPERATION FAILED");
                    }
                    break;
                }

                case 3:{
                    String products=ProductSService.viewProductsForedit(name, bnNumber);
                    if(products.equals("")){
                        System.out.println("THERE ARE NO PRODUCTS IN THE CONTRACT TO BE SHOWN");
                        break;
                    }
                    System.out.println(products);
                    System.out.println("insert the product's catalog-ID you want to delete before the '_' (example: to delete product 11_2, select only 11)");
                    String catalogID = input.nextLine();
                    while (!ProductSService.validCatalogID(catalogID,name,bnNumber)){
                        System.out.println("Invalid Catalog-ID");
                        System.out.println("insert the product's catalog-ID you want to delete before the '_' (example: to delete product 11_2, select only 11)");
                        catalogID = input.nextLine();
                    }
                    if(ContractService.deleteProductFromContract(name,bnNumber,catalogID)) {
                        System.out.println("Product Deleted Successfully!");
                        deleteProductFromSystem(catalogID);
                    }
                    else System.out.println("OPERATION FAILED");
                    break;

                }
                case 4: {
                    String products=ProductSService.viewProductsForedit(name, bnNumber);
                    if(products.equals("")){
                        System.out.println("THERE ARE NO PRODUCTS IN THE CONTRACT TO BE SHOWN");
                        break;
                    }
                    System.out.println(products);
                    System.out.println("insert the product's catalog-ID you want to edit before the '_' (example: to edit product 11_2, select only 11)");
                    String catalogID = input.nextLine();
                    while(!ProductSService.validCatalogID(catalogID,name,bnNumber)){
                        System.out.println("Invalid Catalog-ID");
                        System.out.println("insert the product's catalog-ID you want to edit before the '_' (example: to edit product 11_2, select only 11)");
                        catalogID=input.nextLine();
                    }
                    System.out.println("insert new price");
                    String newprice = input.nextLine();
                    if(ProductSService.changeProductPrice(name,bnNumber,catalogID,newprice))
                        System.out.println("Price Changed Successfully!");
                    else System.out.println("OPERATION FAILED");
                    break;
                }

                case 5:{
                    ContractOperations(name, bnNumber);
                    return;
                }
            }
        }

    }

    private static void deleteProductFromSystem(String catalogID) {
        ProductSService.deleteProductFromSystem(catalogID);
    }

    private static void addNewProductToTheSystem(String catalogID, String pname,String weight) {
        ProductSService.addNewProductToTheSystem(catalogID,pname,weight);
    }


    private static void changeMinDiscount(String name, String bnNumber)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("insert new discount. if there is no discount insert 0");
        String discount = input.nextLine();
        if(ContractService.changeMinDiscount(name,bnNumber,discount))
            System.out.println("Minimum Discount Changed Successfully");
        else
            System.out.println("OPERATION FAILED");
    }


    private static void viewSupplierContract(String name,String bnNumber)
    {
        System.out.println(ContractService.viewContract(name,bnNumber));
    }

    private static void viewProducts(String name,String bnNumber)
    {
        String products=ProductSService.viewProducts(name,bnNumber);
        if(products.equals(""))
            System.out.println("THERE ARE NO PRODUCTS IN THE CONTRACT TO BE SHOWN");
        else
            System.out.println(products);
    }

    private static void ContactsOperations(String supplierName,String supplierBnNumber) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);

        while ((true)) {
            System.out.println("Select Operation:");
            System.out.println("(1) view supplier's contacts");
            System.out.println("(2) add new contact");
            System.out.println("(3) delete contact");
            System.out.println("(4) edit exist contact");
            System.out.println("(5) return");
            String op = input.nextLine();
            int option;
            try{
                option=Integer.parseInt(op);
            }
            catch (Exception ex)
            {
                System.out.println("Invalid input");
                option=0;
            }
            // int option = input.nextInt();
            //input.nextLine();
            switch (option) {
                case 1: {
                    viewSupplierContacts(supplierName, supplierBnNumber);
                    break;
                }

                case 2: {
                    System.out.println("Insert new contact's name");
                    String name = input.nextLine();
                    System.out.println("Insert new contact's email");
                    String email = input.nextLine();
                    System.out.println("Insert new contact's phone");
                    String phone = input.nextLine();
                    if(AddNewContact(supplierName,supplierBnNumber,name,email,phone))
                        System.out.println("Contact Added Successfully!");
                    else
                        System.out.println("OPERATION FAILED");
                    break;
                }

                case 3: {

                    String contacts_list = ContactService.viewSupplierContact(supplierName,supplierBnNumber);
                    if(contacts_list.equals(""))
                    {
                        System.out.println("THE SUPPLIER DOESNT HAVE ANY CONTACTS TO BE SHOWN");
                        break;
                    }
                    System.out.println("Select contact to delete");
                    System.out.println(contacts_list);


                    String concat_index = input.nextLine();
                    String[] supInfo = contacts_list.split("( )|(\n)");

                    String name = "";
                    String email = "";
                    String phone ="";
                    boolean outOfBounds=false;
                    for (int i = 0; i < supInfo.length; i++) {
                        if (supInfo[i].equals(concat_index+")")) {
                            name = supInfo[i + 2];
                            email = supInfo[i + 4];
                            phone = supInfo[i+6];
                            break;
                        }
                        else{
                            if(i==supInfo.length-1){
                                System.out.println("input is out of bounds");
                                outOfBounds=true;
                            }
                        }
                    }
                    if(outOfBounds)
                        break;
                    if(DeleteContact(supplierName,supplierBnNumber,name,email,phone))
                        System.out.println("Contact Deleted Successfully!");
                    break;
                }
                case 4: {
                    boolean flag = false;
                    boolean noContacts=false;
                    String name = "";
                    String email = "";
                    String phone = "";
                    while(!flag) {
                        flag=true;

                        String contacts_list = ContactService.viewSupplierContact(supplierName,supplierBnNumber);
                        if(contacts_list.equals(""))
                        {
                            System.out.println("THE SUPPLIER DOESNT HAVE ANY CONTACTS TO BE SHOWN");
                            noContacts=true;
                            break;
                        }
                        System.out.println("Select contact to edit");
                        System.out.println(contacts_list);


                        String concat_index = input.nextLine();
                        String[] supInfo = contacts_list.split("( )|(\n)");
                        for (int i = 0; i < supInfo.length; i++) {
                            if (supInfo[i].equals(concat_index + ")")) {
                                name = supInfo[i + 2];
                                email = supInfo[i + 4];
                                phone = supInfo[i + 6];
                                break;
                            } else {
                                if (i == supInfo.length - 1) {
                                    flag=false;
                                    System.out.println("input is out of bounds");
                                    break;
                                }
                            }
                        }
                    }
                    if(noContacts)
                        break;

                    String newname="",newemail="",newphone="";

                    System.out.println("change contact's name y|n");
                    String yORn =input.nextLine();
                    while(!yORn.equals("y")&!yORn.equals("n"))
                    {
                        System.out.println("INVALID INPUT");
                        System.out.println("change contact's name y|n");
                        yORn=input.nextLine();
                    }
                    if(yORn.equals("y"))
                    {
                        System.out.println("insert new name");
                        newname = input.nextLine();
                    }
                    System.out.println("change contact's email y|n");
                    yORn=input.nextLine();
                    while(!yORn.equals("y")&&!yORn.equals("n"))
                    {
                        System.out.println("INVALID INPUT");
                        System.out.println("change contact's email y|n");
                        yORn=input.nextLine();
                    }
                    if(yORn.equals("y"))
                    {
                        System.out.println("insert new email");
                        newemail = input.nextLine();
                    }
                    System.out.println("change contact's phone y|n");
                    yORn=input.nextLine();
                    while(!yORn.equals("y")&&!yORn.equals("n"))
                    {
                        System.out.println("INVALID INPUT");
                        System.out.println("change contact's phone y|n");
                        yORn=input.nextLine();
                    }
                    if(yORn.equals("y"))
                    {
                        System.out.println("insert new phone");
                        newphone = input.nextLine();
                    }

                    if(ContactService.EditContact(supplierName,supplierBnNumber,name,email,phone,newname,newemail,newphone))
                        System.out.println("Supplier Details Changed Successfully!");
                    else
                        System.out.println("OPERATION FAILED");
                    break;
                }
                case 5: {
                    EditSupplier();
                    return;
                }

            }
        }

    }

    private static boolean DeleteContact(String supplierName, String supplierBnNumber, String name, String email, String phone) {
        return ContactService.DeleteContact(supplierName,supplierBnNumber,name,email,phone);
    }

    private static boolean AddNewContact(String supplierName, String supplierBnNumber, String name, String email, String phone) {
        return ContactService.AddNewContact(supplierName,supplierBnNumber,name,email,phone);
    }

    public static void printOrders(String order_details)
    {
        System.out.println(order_details);
    }


    public static void loadRepository() throws SQLException { SupplierService.loadRepository();}
}

