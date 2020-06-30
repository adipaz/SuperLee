package PresentationLayer;

import InterfaceLayer.SharedClassSerice;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
//    public static void main(String[] args) throws IOException, SQLException {
//        SharedClassSerice.createNewDataBase();
//        SharedClassSerice.initProducts();
//
//        if (args.length > 0) {
//            InventoryController.loadProducts(args[0], 1);
//        }
//        System.out.println("~~ Welcome To Superli ~~\n");
//        System.out.println("Today is " + SharedClassSerice.getDayString());
//        System.out.println("First you insert username & password");
//        System.out.println("A menu will be displayed as soon as correct username and password will be inserted.");
//        System.out.println("[Insert 'q' as username and password in any time to exit]");
//        String username = "", password = "";
//
//        Scanner s = new Scanner(System.in);
//        do {
//            System.out.print("username : ");
//            username = s.nextLine();
//            System.out.print("password : ");
//            password = s.nextLine();
//
//            if ((username.equals("admin") && password.equals("superli")))
//                MainMenu();
//        }while(true);
//}

public static void MainMenu() throws IOException, SQLException {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Select Operation: ");
            System.out.println("(1) Supplier options");
            System.out.println("(2) Inventory options");
            System.out.println("(3) Advance day");
            System.out.println("(4) Logout");
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
                    SupplierMenu.SupplierOptionsForStoreKeeper();
                    break;
                }
                case 2:{
                    InventoryMenu.FirstMenu();
                    break;
                }
                case 3:
                {
                    SharedClassSerice.nextDay();
                    System.out.println("\n~~~~~~~Today is " +SharedClassSerice.getDayString()+"~~~~~~~");

                    break;
                }
                case 4: {
                    CLImenu.getInstance().logout();
                    exit = true;
                }
            }
        }
    }
}
