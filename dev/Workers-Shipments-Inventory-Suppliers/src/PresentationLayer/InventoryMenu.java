package PresentationLayer;

import BusinessLayer.SuppliersAndInventory.Store;
import DataStructures.Pair;
import InterfaceLayer.DTO.ReportDTO;
import InterfaceLayer.InventoryController;
import InterfaceLayer.OrderService;
import InterfaceLayer.SharedClassSerice;
//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

//import com.sun.xml.internal.bind.v2.TODO;

public class InventoryMenu {

    private static Scanner s;

    private static void printDummyReport(ReportDTO rep) {
        System.out.println(rep.getType());
        System.out.println(rep.getCreationDate());
        System.out.println(rep.getAddress());
        System.out.println(rep.getDescription());
    }


    public InventoryMenu() {
        this.s = new Scanner(System.in);
    }



    public static void FirstMenu() throws IOException, SQLException {
        ReportDTO report;
        InventoryMenu m = new InventoryMenu();
        boolean exit = false, done = false;
        String retVal = "";
        int userOption;
        int branch = 1;
        do {
            System.out.println("\nYou are managing inventory on branch " + branch + "!");
            System.out.println("\n~ Inventory Module ~ Select Option:");
            System.out.println("1) Add products ");
            System.out.println("2) Remove products");
            System.out.println("3) Turn items to flaw ones");
            System.out.println("4) Create inventory report");
            System.out.println("5) Create flaw products report");
            System.out.println("6) Create absent products ( A.K.A order )");
            System.out.println("7) Move specific items to Store shelf");
            System.out.println("8) Announce sale and update prices of products");
            System.out.println("9) Finish sales");
            System.out.println("10) Load pre-made Storage ( apples, bananas, milk and bread )");
            System.out.println("11) Switch Branch");
            System.out.println("12) Go Back");
            System.out.println("13) Update new Shipped products info");


            String userOptionStr = m.getS().nextLine();
            if (!tryParseInt(userOptionStr)) {
                System.out.println("You need to choose a number");
            } else {
                userOption = Integer.parseInt(userOptionStr);
                System.out.println();
                switch (userOption) {
                    case 1:
                        System.out.println("You chose to Add a Product!!. Would you like to add a totally new product or an existing one? ");
                        System.out.println("Press 'e' to add an existing product");
                        System.out.println("Press 'n' to add a new product ( that means new catalog ID )");
                        String newOrExistingProd;
                        newOrExistingProd = s.nextLine();
                        boolean isNewProduct = false;
                        boolean isNewProductUpdated = false;
                        switch (newOrExistingProd) {
                            case "e":
                                isNewProduct = false;
                                isNewProductUpdated = true;
                                break;

                            case "n":
                                isNewProduct = true;
                                isNewProductUpdated = true;
                                break;

                            default:
                                System.out.println("Wrong choice, back to main menu");
                                break;
                        }
                        if (isNewProductUpdated == false) {
                            break;
                        } else if (isNewProduct == true) {
                            String retValueAddProduct = addProducts(true, branch);
                            if (retValueAddProduct.equals("")) // empty string - success
                                System.out.println("Added product/s successfully!");
                            else
                                System.out.println(retValueAddProduct);
                        } else {
                            String retValueAddProduct = addExistingProduct(false, branch);
                            if (retValueAddProduct.equals("")) // empty string - success
                                System.out.println("Added product/s successfully!");
                            else
                                System.out.println(retValueAddProduct);
                        }
                        break;

                    case 2:
                        System.out.println("You chose to remove products!! did we sell them and made money? Hurray!");
                        String catalogId;
                        int quantity;
                        System.out.print("Insert CatalogId of the product you would like to remove : ");
                        catalogId = s.nextLine();
                        if (!tryParseInt(catalogId)) {
                            System.out.println("catalogId must contain only digits");
                            break;
                        }
                        System.out.print("Great, How many Items would you like to Remove ? ");
                        String quantityStr = s.nextLine();
                        if (tryParseInt(quantityStr))
                            quantity = Integer.parseInt(quantityStr);
                        else {
                            System.out.println("Illegal Quantity");
                            break;
                        }
                        String retValue = InventoryController.delegateRemoveProducts(catalogId, quantity, branch);
                        if (retValue != "")
                            System.out.println(retValue);
                        break;

                    case 3:
                        System.out.println("Oh! you chose to ruin some items");
                        String catalogId1;
                        int quantity1;
                        System.out.print("Insert CatalogId of the product you would like to make flaw : ");
                        catalogId1 = s.nextLine();
                        if (!tryParseInt(catalogId1)) {
                            System.out.println("catalogId must contain only digits");
                            break;
                        }
                        System.out.print("Great, How many Items would you like to make flaw? ");
                        String quantityStr1 = s.nextLine();
                        if (tryParseInt(quantityStr1))
                            quantity1 = Integer.parseInt(quantityStr1);
                        else {
                            System.out.println("Illegal Quantity");
                            break;
                        }
                        String ret = InventoryController.delegateMakeProductsFlaw(catalogId1, quantity1, branch);
                        if (ret != "")
                            System.out.println(ret);
                        break;

                    case 4:
                        System.out.println("Interesting, you'd like to view some categories ah?");
                        String category;
                        List<String> categories = new LinkedList<>();
                        System.out.println("Insert Categories to be shown in the requested Report: ( press 'q' when done )");
                        do {
                            category = s.nextLine();
                            if (!category.equals("q"))
                                categories.add(category);
                        } while (!category.equals("q"));
                        report = InventoryController.delegateInventoryReport(categories, branch);
                        System.out.println("~~~~~~ Inventory Report ~~~~~~");
                        printDummyReport(report);
                        break;

                    case 5:
                        System.out.println("Huston, we've got some flaw products over here");
                        report = InventoryController.delegateFlawsReport(branch);
                        System.out.println("~~~~~~ Flaws Report ~~~~~~");
                        printDummyReport(report);
                        break;

                    case 6:
                        System.out.println("Order huh? I would like some burger and french fries");
                        report = InventoryController.delegateOrderReport(branch);
                        System.out.println("~~~~~~ Absent Report ~~~~~~");
                        printDummyReport(report);
                        List<Pair<String,Integer>> catalogXamount = convertReportToOrder(report);
                        OrderService.addNewOrder(catalogXamount, Store.getInstance().getBranchAddress(branch));
                        break;

                    case 7:
                        System.out.println("The Shelfs look a little bit empty d'ont they?,lets fill them up");
                        System.out.println("Which CatalogID are we missing?");
                        String catalogId2 = s.nextLine();
                        if (!tryParseInt(catalogId2)) {
                            System.out.println("catalogId must contain only digits");
                            break;
                        }
                        System.out.println("How many should we take?");
                        String quantityStr2 = s.nextLine();
                        if (!tryParseInt(quantityStr2)) {
                            System.out.println("Illegal number");
                            break;
                        }

                        int quantity2 = Integer.parseInt(quantityStr2);
                        retVal = InventoryController.MoveProductsToShelf(catalogId2, quantity2, branch);
                        System.out.println(retVal);

                        break;
                    case 8:
                        System.out.println("Discount");
                        System.out.println("Discount to:\n" +
                                "1) Specific product\n" +
                                "2) Specific category");

                        String choiceStr = s.nextLine();
                        if (!tryParseInt(choiceStr)) {
                            System.out.println("Illegal choice");
                            break;
                        }
                        int choice = Integer.parseInt(choiceStr);
                        int discount = 0;

                        switch (choice) {
                            case 1:
                                System.out.println("Catalog ID?");
                                String catalogId3 = s.nextLine();
                                if (!tryParseInt(catalogId3)) {
                                    System.out.println("catalogId must contain only digits");
                                    break;
                                }
                                System.out.println("Which discount?");
                                String disStr = s.nextLine();
                                if (!tryParseInt(disStr)) {
                                    System.out.println("Illegal Discount");
                                    break;
                                }
                                discount = Integer.parseInt(disStr);
                                retVal = InventoryController.addDiscountProduct(catalogId3, discount, branch);
                                System.out.println(retVal);
                                break;
                            case 2:
                                System.out.println("Category?");
                                String category1 = s.nextLine();
                                System.out.println("Which discount?");
                                String discountStr = s.nextLine();
                                if (tryParseInt(discountStr))
                                    discount = Integer.parseInt(discountStr);
                                else {
                                    System.out.println("Illegal Discount");
                                    break;
                                }

                                retVal = InventoryController.addDiscountCategory(category1, discount, branch);
                                System.out.println(retVal);
                                break;
                            default:
                                System.out.println("You need to choose a number, 1 or 2 ");
                                break;
                        }
                        break;

                    case 9:
                        System.out.println("Good things c'ant lasts forever");
                        System.out.println("End Discount to:\n" +
                                "1) Specific product\n" +
                                "2) Specific category");

                        String choiceStr1 = s.nextLine();
                        if (!tryParseInt(choiceStr1)) {
                            System.out.println("Illegal choice");
                            break;
                        }
                        int choice1 = Integer.parseInt(choiceStr1);

                        switch (choice1) {
                            case 1:
                                System.out.println("Catalog ID?");
                                String catalogId4 = s.nextLine();
                                if (!tryParseInt(catalogId4)) {
                                    System.out.println("catalogId must contain only digits");
                                    break;
                                }
                                retVal = InventoryController.addDiscountProduct(catalogId4, 0, branch);
                                System.out.println(retVal);
                                break;
                            case 2:
                                System.out.println("Category?");
                                String category2 = s.nextLine();
                                retVal = InventoryController.addDiscountCategory(category2, 0, branch);
                                System.out.println(retVal);
                                break;
                            default:
                                System.out.println("You need to choose a number, 1 or 2 ");
                                break;
                        }

                    case 10:
                        System.out.println(InventoryController.delegateAddProduct("1232", "banana", "3.5", "2.0", "Havat Yaffa", "Food", "Fruits", "0.5kg", "0.5", "0", "STORAGE", "false", "2023", "01", "10", "10", "5", true, branch));
                        System.out.println(InventoryController.delegateAddProduct("1234", "apple", "2.0", "1.0", "Havat Yaffa", "Food", "Fruits", "0.5kg", "0.5", "0", "STORAGE", "false", "2021", "05", "20", "6", "3", true, branch));
                        System.out.println(InventoryController.delegateAddProduct("1235", "milk", "5", "3.25", "Refet Moshe", "Drink", "Diary", "1 liter", "1", "0", "STORAGE", "false", "2021", "05", "18", "4", "3", true, branch));
                        System.out.println(InventoryController.delegateAddProduct("1236", "white bread", "4.7", "2.99", "Angel Bakery", "Food", "Bread", "0.75kg", "0.75", "0", "STORAGE", "false", "2025", "02", "10", "13", "7", true, branch));
                        System.out.println("Storage Updated!");
                        break;

                    case 11:
                        branch = 0;
                        while (branch < 1 || branch > 9) {
                            System.out.println("\n~ Inventory Module ~ Select Branch:");
                            System.out.println("1) Hertzel St.4 , Tel Aviv ");
                            System.out.println("2) Noga St.5 , Rishon Letzion");
                            System.out.println("3) Haazmaut St.34 , Ashdod");
                            System.out.println("4) Bar Cochva St.19 , Petah Tikva");
                            System.out.println("5) Mivtsa nahshon St.66 , Beer Sheva");
                            System.out.println("6) Hashoshanim St.34 , Ganot Hadar");
                            System.out.println("7) Kalanit St.21 , Modiin");
                            System.out.println("8) Hamaarav St.12 , Eilat");
                            System.out.println("9) Sivan St.14 , Ashekelon ");

                            String branchStr = m.getS().nextLine();
                            if (!tryParseInt(branchStr)) {
                                System.out.println("You need to choose a number");
                            } else {

                                branch = Integer.parseInt(branchStr);

                                if (branch < 1 || branch > 10) {
                                    System.out.println("There are only 9 branches");

                                }
                            }
                        }

                        break;
                    case 12:
                        exit = true;
                        break;

                    case 13:
                        System.out.println("oopsy , we need to order some new shipping info");
                        String catalogIdToUpdate;
                        String newCategory;
                        String newSubCategory;
                        String newSubSubCategory;
                        String newManufacturer;
                        String newSellPrice;
                        String newBuyPrice;

                        int newSellPriceI;
                        int newBuyPriceI;

                        System.out.println("enter catalogId of the new Product:");
                        catalogIdToUpdate = s.nextLine();

                        System.out.println("enter new Category:");
                        newCategory = s.nextLine();

                        System.out.println("enter new SubCategory:");
                        newSubCategory = s.nextLine();

                        System.out.println("enter new SubSubCategory:");
                        newSubSubCategory = s.nextLine();

                        System.out.println("enter Manufacturer:");
                        newManufacturer = s.nextLine();

                        System.out.println("enter SellPrice:");
                        newSellPrice = s.nextLine();
                        if (!tryParseInt(newSellPrice)) {
                            System.out.println("You need to choose a number");
                            break;
                        } else {

                            newSellPriceI = Integer.parseInt(newSellPrice);

                        }
                        System.out.println("enter BuyPrice:");
                        newBuyPrice = s.nextLine();
                        if (!tryParseInt(newBuyPrice)) {
                            System.out.println("You need to choose a number");
                            break;
                        } else {

                            newBuyPriceI = Integer.parseInt(newBuyPrice);
                        }

                        if(newSellPriceI<0 || newBuyPriceI<0){
                            System.out.println("sell price and buy price needs to be positive");
                        }
                        else {
                            retVal = InventoryController.updateShippmentProductInfo(branch,catalogIdToUpdate,newCategory,newSubCategory,newSubSubCategory,newManufacturer,newSellPriceI,newBuyPriceI);
                            System.out.println(retVal);
                        }
                            break;

                    default:
                        System.out.println("You need to choose a number between 1 to 14");
                        break;


                }
            }


        } while (!exit);
    }

    private static List<Pair<String, Integer>> convertReportToOrder(ReportDTO report) {
        List<Pair<String, Integer>> retVal = new LinkedList<>();
        String strings[] = report.getDescription().split("\n| ");
        List<String> strs = new LinkedList<>();
        for (String s: strings) {
            if(!s.equals(""))
                strs.add(s);
        }
        for(int i = 5; i < strs.size(); i+=3) {
            Pair<String, Integer> p = new Pair<String, Integer>(strs.get(i), Integer.parseInt(strs.get(i+2)));
            retVal.add(p);
        }
        return retVal;
    }


//    public static void main(String[] args) throws IOException, SQLException {
//        FirstMenu();
//    }

    private static String addExistingProduct(boolean isNewProduct, int branch) throws SQLException {
        String quantity, day, month, year;
        String weight, buyPrice;
        String catalogId, manufacturer;
        Scanner s = new Scanner(System.in);
        System.out.print("Insert CatalogId of the product you would like to add : ");
        catalogId = s.nextLine();
        if (!tryParseInt(catalogId)) {
            return "catalogId must contain only digits";
        }
        System.out.print("Great, How many Items would you like to add? ");
        quantity = s.nextLine();
        if (!tryParseInt(quantity)) {
            return "Illegal quantity";
        }

        System.out.println("Ok, we are all set!! we just need few details about the product. Please Insert:");
        System.out.print("Buying price? ");
        buyPrice = s.nextLine();
        if (!tryParseInt(buyPrice)) {
            return "Illegal buying price";
        }
        System.out.println("manufacturer : ");
        manufacturer = s.nextLine();
        System.out.println("weight : ");
        weight = s.nextLine();
        if (!tryParseDouble(weight)) {
            return "Illegal Weight";
        }
        System.out.println("expiration Day : ");
        day = s.nextLine();
        if (!tryParseInt(day)) {
            return "Illegal day of date";
        }
        System.out.println("expiration Month : ");
        month = s.nextLine();
        if (!tryParseInt(month)) {
            return "Illegal month of date";
        }
        System.out.println("expiration Year : ");
        year = s.nextLine();
        if (!tryParseInt(year)) {
            return "Illegal year of date";
        }

        return InventoryController.delegateAddProduct(catalogId, "-1", "-1", buyPrice, manufacturer, "-1",
                "-1", "-1", weight, "0", "STORAGE", "false", year, month, day, quantity, "-1", isNewProduct, branch);
    }


    public Scanner getS() {
        return s;
    }

    private static String addProducts(boolean isNewProduct, int branch) throws SQLException {
        String quantity, minQuantity, day, month, year;
        String sellPrice, buyPrice, weight;
        String catalogId, name, manufacturer, category, subCategory, subSubCategory;
        Scanner s = new Scanner(System.in);
        System.out.print("Insert CatalogId of the product you would like to add : ");
        catalogId = s.nextLine();
        if (!tryParseInt(catalogId)) {
            return "catalogId must contain only digits";
        }
        System.out.print("Great, How many Items would you like to add? ");
        quantity = s.nextLine();
        if (!tryParseInt(quantity)) {
            return "Illegal quantity";
        }
        System.out.print("What is the minimum quantity of this product?  ");
        minQuantity = s.nextLine();
        if (!tryParseInt(minQuantity)) {
            return "Illegal minimum quantity";
        }
        System.out.println("Ok, we are all set!! we just need few details about the product. Please Insert:");
        System.out.println("name: ");
        name = s.nextLine();
        System.out.println("manufacturer : ");
        manufacturer = s.nextLine();
        System.out.println("category : ");
        category = s.nextLine();
        System.out.println("sub - category : ");
        subCategory = s.nextLine();
        System.out.println("sub - sub - category : ");
        subSubCategory = s.nextLine();
        if(category.equals(subCategory) || category.equals(subSubCategory) || subCategory.equals(subSubCategory))
            return "Illegal categories: each category should have a different name";


        System.out.println("selling price : ");
        sellPrice = s.nextLine();
        if (!tryParseDouble(sellPrice)) {
            return "Illegal Sell price";
        }
        System.out.println("buying price : ");
        buyPrice = s.nextLine();
        if (!tryParseDouble(buyPrice)) {
            return "Illegal Sell price";
        }
        System.out.println("weight : ");
        weight = s.nextLine();
        if (!tryParseDouble(weight)) {
            return "Illegal Weight";
        }
        System.out.println("expiration Day : ");
        day = s.nextLine();
        if (!tryParseInt(day)) {
            return "Illegal day of date";
        }
        System.out.println("expiration Month : ");
        month = s.nextLine();
        if (!tryParseInt(month)) {
            return "Illegal month of date";
        }
        System.out.println("expiration Year : ");
        year = s.nextLine();
        if (!tryParseInt(year)) {
            return "Illegal year of date";
        }


        return InventoryController.delegateAddProduct(catalogId, name, sellPrice, buyPrice, manufacturer, category,
                subCategory, subSubCategory, weight, "0", "STORAGE", "false", year, month, day, quantity, minQuantity, isNewProduct, branch);
    }

    private static boolean tryParseInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean tryParseDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}