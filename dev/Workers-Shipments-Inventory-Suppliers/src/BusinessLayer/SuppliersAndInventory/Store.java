package BusinessLayer.SuppliersAndInventory;
import DataAccessLayer.Handlers.DatabaseHandler;
import DataStructures.Pair;
import InterfaceLayer.DTO.ProductDTO;
import InterfaceLayer.DTO.ReportDTO;

import java.sql.SQLException;
import java.util.*;

public class Store {

    private static Store instance;
    private Map<Integer,Map<String, Integer>> catalogIdToQuantity;
    private boolean catalogIdToQuantityBool;
    private Map<Integer,Map<String, Integer>> catalogIdToMinQuantity;
    private boolean catalogIdToMinQuantityBool;
    private Map<Integer,Map<String, List<SpecificProduct>>> catalogIdToProducts;
    private boolean catalogIdToProductsBool;
    private Map<Integer,Map<String, List<String>>> categoryToCatalogID;
    private boolean categoryToCatalogIDBool;
    private Map<Integer,Map<String, Integer>> catalogIdToCounter;
    private boolean catalogIdToCounterBool;
    private Map<Integer,String> branchIdToAddress;
    private DatabaseHandler databaseHandler = new DatabaseHandler();



    private Store() {

        branchIdToAddress = new HashMap<>();
        catalogIdToQuantityBool = false;
        catalogIdToMinQuantityBool = false;
        catalogIdToProductsBool = false;
        categoryToCatalogIDBool = false;
        catalogIdToCounterBool = false;


        initAddresses();
    }


    public static Store getInstance(){
        if (instance == null)
            instance = new Store();
        return instance;
    }

    private void initAddresses() {
        branchIdToAddress.put(1, "Hertzel St.4 , Tel Aviv");
        branchIdToAddress.put(2, "Noga St.5 , Rishon Letzion");
        branchIdToAddress.put(3, "Haazmaut St.34 , Ashdod");
        branchIdToAddress.put(4, "Bar Cochva St.19 , Petah Tikva");
        branchIdToAddress.put(5, "Mivtsa nahshon St.66 , Beer Sheva");
        branchIdToAddress.put(6, "Hashoshanim St.34 , Ganot Hadar");
        branchIdToAddress.put(7, "Kalanit St.21 , Modiin");
        branchIdToAddress.put(8, "Hamaarav St.12 , Eilat");
        branchIdToAddress.put(9, "Sivan St.14 , Ashekelon");

    }

    public String addProduct(String catalogId, ProductDTO pDTO, int quantity, int minQuantity, boolean isNewProduct, int branch) throws SQLException {
        /* 1. check if legal combination of isNew attribute
         *  2. check if pDto is half full - if it is, fill it
         *  3. run validation on expected inserted product
         *  In any case of illegality - return a error message immediately
         */

        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();

        if(catalogIdToQuantityBool == false)
            this.catalogIdToQuantity=getCatalogIdToQuantityTable();

        if(catalogIdToMinQuantityBool == false)
            this.catalogIdToMinQuantity=getCatalogIdToMinQuantityTable();

        if(catalogIdToCounterBool == false)
            this.catalogIdToCounter=getCatalogIdToCounterTable();


        String retVal = "";

        if(isNewProduct && catalogIdToProducts.get(branch).containsKey(catalogId)) {
            retVal =  "!!!!!! ERROR !!!!!!\nThe product with catalogID of " + catalogId + " is not a new one and already exists!!" ;
            return retVal;
        }

        if (!catalogIdToProducts.get(branch).containsKey(catalogId) && isNewProduct == false) {
            retVal =  "!!!!!! ERROR !!!!!!\nThe product with catalogID of " + catalogId + " does not exist!!" ;
            return retVal;
        }

        if(isNewProduct==false){
            pDTO.setName(catalogIdToProducts.get(branch).get(catalogId).get(0).getName());
        }

        if(!SharedClass.getSharedClass().getProductsMap().containsKey(catalogId))
        {
            retVal =  "!!!!!! ERROR !!!!!!\nThe product with catalogID of " + catalogId + " is not supplied by any supplier!!" ;
            return retVal;
        }
        if(!SharedClass.getSharedClass().getProductsMap().get(catalogId).getFirst().equals(pDTO.getName()))
        {
            retVal =  "!!!!!! ERROR !!!!!!\nThe product with catalogID of " + catalogId + " and name " + pDTO.getName() +" is not supplied by any supplier!!" ;
            return retVal;
        }

        /*
        if(!databaseHandler.isProductSupplied(catalogId)){
            retVal =  "!!!!!! ERROR !!!!!!\nThe product with catalogID of " + catalogId + " is not supplied by any supplier!!" ;
            return retVal;
        }

         */



        // We are dealing with a legal case

        if(minQuantity == -1) { // Incomplete product, meaning it's an existing one
            Product sample = catalogIdToProducts.get(branch).get(catalogId).get(0);
            pDTO.setSellPrice(Double.toString(sample.getSellPrice()));;
            pDTO.setCategory(sample.getCategory());
            pDTO.setSubCategory(sample.getSubCategory());
            pDTO.setSubSubCategory(sample.getSubSubCategory());
            pDTO.setName(sample.getName());
            minQuantity =1;
        }

        // We have a complete DTO object in hand & a legal case, now it's time to insert

        if (!(validateProductQuantities(quantity, minQuantity))) {
            retVal = "!!!!!! ERROR !!!!!!\nQuantity and min Quantity must be positive integers" ;
            return retVal;
        }

        if (minQuantity > quantity) {
            retVal =  "!!!!!! ERROR !!!!!!\nWe do not allow minQuantity to be greater than quantity" ;
            return retVal;
        }

        SpecificProduct p = SpecificProduct.createProduct(pDTO);

        if ( p == null) {
            retVal =  "!!!!!! ERROR !!!!!!\nOne or more of product properties are wrong"  ;
            return retVal;
        }

        List<SpecificProduct> l;

        if (isNewProduct) { // Adding new product
            catalogIdToMinQuantity.get(branch).put(catalogId, minQuantity);
            catalogIdToQuantity.get(branch).put(catalogId, 0);
            catalogIdToCounter.get(branch).put(catalogId, 1);

            addCategories(pDTO.getCategory(), catalogId,branch);
            addCategories(pDTO.getSubCategory(), catalogId,branch);
            addCategories(pDTO.getSubSubCategory(), catalogId,branch);

            List<SpecificProduct> list = new LinkedList<>();
            catalogIdToProducts.get(branch).put(catalogId, list);
            l = list;
        }
        else {
            l = catalogIdToProducts.get(branch).get(catalogId);
        }


        // We've got the list, now it's time to add
        int startingId = catalogIdToCounter.get(branch).get(catalogId);
        for (int i = 0; i < quantity; i++) {
            SpecificProduct sp1 = SpecificProduct.my_clone(p);
            sp1.setId(startingId);
            startingId++;
            l.add(sp1);
            databaseHandler.addProduct(sp1,branch);
        }
        if(isNewProduct)
            databaseHandler.addNewProductToTables(branch,pDTO,minQuantity);
        catalogIdToCounter.get(branch).replace(catalogId, startingId);
        int curQuantity = catalogIdToQuantity.get(branch).get(catalogId);
        catalogIdToQuantity.get(branch).replace(catalogId, quantity + curQuantity);
        databaseHandler.updateQuantity(quantity + curQuantity,catalogId,branch);
        databaseHandler.updateCounter(startingId,catalogId,branch);
        return retVal;
    }

    public String removeProducts(String catalogId, int quantity,int branch) throws SQLException {

        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();

        if(catalogIdToQuantityBool == false)
            this.catalogIdToQuantity=getCatalogIdToQuantityTable();

        if(catalogIdToMinQuantityBool == false)
            this.catalogIdToMinQuantity=getCatalogIdToMinQuantityTable();

        if(catalogIdToCounterBool == false)
            this.catalogIdToCounter=getCatalogIdToCounterTable();

        if(categoryToCatalogIDBool==false)
            this.categoryToCatalogID = getCategoryToCatalogIDTable();



        String retValue = "";
        List<Integer> idToDelete = new LinkedList<>();
        if(!catalogIdToProducts.get(branch).containsKey(catalogId))
            return "This Product does not exist in the Store";

        for (Map.Entry<String, List<SpecificProduct>> entry : catalogIdToProducts.get(branch).entrySet()) {
            if (entry.getKey().equals(catalogId)) {
                if (quantity > entry.getValue().size()) {
                    entry.getValue().clear();
                } else {
                    for (int i = 0; i < quantity; i++) {
                        idToDelete.add(entry.getValue().get(0).getId());
                        entry.getValue().remove(0);
                    }
                }
            }
        }
        boolean removeAll=false;
        int val = catalogIdToQuantity.get(branch).get(catalogId);
        int newVal = val - quantity;
        if (newVal <= 0) {
            removeAll=true;
            clear(catalogId,branch); // removes from all relevant lists
            retValue =  "Notice! you have removed product with catalogID - " + catalogId + " From The store" ;
        }
        else {
            catalogIdToQuantity.get(branch).put(catalogId, newVal);
            if (newVal < catalogIdToMinQuantity.get(branch).get(catalogId)) {
                retValue += "--------------------------------------------------------------------------------------------\n";
                retValue += "|                                        _ _ _        _ _         _ _ _ _                  |\n";
                retValue += "|        /\\           /\\     |          |            |     \\         |           /\\        |\n";
                retValue += "|       /  \\         /  \\    |          | _ _ _      |_ _ _/         |          /  \\       |\n";
                retValue += "|      / !! \\       /----\\   |          |            |     \\         |         / !! \\      |\n";
                retValue += "|     /_ __ _\\     /      \\  |_ _ _ _   | _ _ _      |      \\        |        /_ __ _\\     |\n";
                retValue += "--------------------------------------------------------------------------------------------\n";
                retValue +=   " ~~~~ MESSAGE ~~~~ : " + getProductName(catalogId,branch) +
                        " Product With catalogID - " + catalogId + " in branch " + branchIdToAddress.get(branch) + " IS BELLOW MINIMUM (" +
                        catalogIdToMinQuantity.get(branch).get(catalogId) + ") [ Current Quantity - " +
                        catalogIdToQuantity.get(branch).get(catalogId) + "]\n" ;
            }
        }
        databaseHandler.removeFromTables(removeAll,idToDelete,branch,catalogId);
        if(!removeAll) {
            databaseHandler.updateQuantity(newVal,catalogId, branch);
        }

        return retValue;
    }

    public String ruinItems(String catalogId,int quantity ,int branch) throws SQLException {

        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();



        if(!catalogIdToProducts.get(branch).containsKey(catalogId))
            return "There is no product with "+ catalogId + " as its catalog id";
        int counter = 0;
        List<Integer> idToRuin = new LinkedList<>();
        boolean ruinAll = false;
        for (Map.Entry<String, List<SpecificProduct>> entry : catalogIdToProducts.get(branch).entrySet()) {
            if (entry.getKey().equals(catalogId)) {
                if (quantity > entry.getValue().size()) {
                    ruinAll=true;
                    for (SpecificProduct sp : entry.getValue()) {
                        sp.setFlaw(true);
                        counter++;
                    }
                } else {
                    for (int i = 0; i < entry.getValue().size() && counter != quantity; i++) {
                        if (!entry.getValue().get(i).isFlaw()) {
                            entry.getValue().get(i).setFlaw(true);
                            idToRuin.add(entry.getValue().get(i).getId());
                            counter++;
                        }
                    }
                }
            }
        }
        if(counter==0){
            return "there was nothing to ruin";
        }
        databaseHandler.ruinProducts(ruinAll,idToRuin,branch,catalogId);
        return "All Done! "+ "we ruined "+ counter + " Products. \n(if this number is lower than you requested, it means there are no more products to ruin)";
    }

    public void printListOfProducts(int branch) {
        for (Map.Entry<String, List<SpecificProduct>> entry : catalogIdToProducts.get(branch).entrySet()) {
            System.out.println(entry.getKey());
            for (SpecificProduct sp : entry.getValue()) {
                System.out.print(sp.toString() + "\t");
                System.out.print("\n");
            }
        }
    }

    public Map<String, List<String>> getCatalogIDByCategory(List<String> categories,int branch) throws SQLException {


        if(categoryToCatalogIDBool==false)
            this.categoryToCatalogID = getCategoryToCatalogIDTable();

        Map<String, List<String>> CategoryToCatalogID = new HashMap<>();
        for (String category : categories) {

            if (this.categoryToCatalogID.get(branch).containsKey(category)) {
                CategoryToCatalogID.put(category, cloneList(this.categoryToCatalogID.get(branch).get(category)));
            }
        }

        return CategoryToCatalogID;
    }
//we assume this product exist because this function is called from report class which iterates over a list of products
    public String getProductName(String CatalogID,int branch) throws SQLException {
        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();

        return this.catalogIdToProducts.get(branch).get(CatalogID).get(0).getName();

    }

    public int getProductQuantity(String CatalogID,int branch) throws SQLException {

        if(catalogIdToQuantityBool == false)
            this.catalogIdToQuantity=getCatalogIdToQuantityTable();


        return this.catalogIdToQuantity.get(branch).get(CatalogID);
    }

    //a map between catalogID to the amount of flaws
    public Map<String, Integer> getFlaws(int branch) throws SQLException {
        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();


        Map<String, Integer> flaws = new HashMap<>();
        Date date = new Date();

        for (Map.Entry<String, List<SpecificProduct>> entry : catalogIdToProducts.get(branch).entrySet()) {
            int counter = 0;
            for (SpecificProduct sp : entry.getValue()) {
                if (sp.isFlaw() || sp.getExpirationDate().before(date))
                    counter++;

            }
            if (counter != 0)
                flaws.put(entry.getKey(), counter);
        }
        return flaws;
    }

    public Map<String, Integer> getMissings(int branch) throws SQLException {

        if(catalogIdToQuantityBool == false)
            this.catalogIdToQuantity=getCatalogIdToQuantityTable();

        if(catalogIdToMinQuantityBool == false)
            this.catalogIdToMinQuantity=getCatalogIdToMinQuantityTable();


        Map<String, Integer> missings = new HashMap<>();
        for (Map.Entry<String, Integer> entry : catalogIdToQuantity.get(branch).entrySet()) {
            if (entry.getValue() < catalogIdToMinQuantity.get(branch).get(entry.getKey()))
                missings.put(entry.getKey(), catalogIdToMinQuantity.get(branch).get(entry.getKey()) - entry.getValue());
        }
        return missings;
    }


    //this function checks if a category exists in the map
    // if it does,the categoryID will be added to the lost, if doesnt
    //  a new line will be added to the map

    private void addCategories(String Category, String CatalogId,int branch) throws SQLException {
        if(categoryToCatalogIDBool == false)
            this.categoryToCatalogID = getCategoryToCatalogIDTable();

        if (!categoryToCatalogID.get(branch).containsKey(Category)) {
            List<String> cat = new LinkedList<>();
            cat.add(CatalogId);
            categoryToCatalogID.get(branch).put(Category, cat);
        } else {
            categoryToCatalogID.get(branch).get(Category).add(CatalogId);
        }
    }


    public String moveProductsToShelf(String catalogId, int quantity,int branch) throws SQLException {

        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();

        if(!catalogIdToProducts.get(branch).containsKey(catalogId))
            return "No such item with this catalog id in the Storage";
        List<Integer> idToMove = new LinkedList<>();
        int counter = 0;
        boolean moveAll = false;
        for (Map.Entry<String, List<SpecificProduct>> entry : catalogIdToProducts.get(branch).entrySet()) {
            if (entry.getKey().equals(catalogId)) {
                if (quantity > entry.getValue().size()) {
                    moveAll=true;
                    for (SpecificProduct sp : entry.getValue()) {
                        sp.setLocation(LOCATION.STORESHELF);
                        counter++;
                    }
                } else {
                    for (int i = 0; i < entry.getValue().size() && counter != quantity; i++) {
                        if (!entry.getValue().get(i).getLocation().equals(LOCATION.STORESHELF)) {
                            entry.getValue().get(i).setLocation(LOCATION.STORESHELF);
                            idToMove.add(entry.getValue().get(i).getId());
                            counter++;
                        }
                    }
                }
            }
        }
        if(counter==0){
            return "there was nothing to move";
        }
        databaseHandler.moveProductsToShelf(moveAll,idToMove,branch,catalogId);
        return "All Done! "+ "we moved "+ counter + " Products. \n(if this number is lower than you requested, it means there are no more products to move)";
    }

    public String addDiscountProduct(String catalogId, int discount,int branch) throws SQLException {

        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();


        if(!catalogIdToProducts.get(branch).containsKey(catalogId))
            return "There is no product with "+ catalogId + " as its catalog id";
        if(discount >= 100 || discount <0)
            return "You can't give a discount of 100 percent or above , discount can't be lower than 0";
        for (Map.Entry<String, List<SpecificProduct>> entry : catalogIdToProducts.get(branch).entrySet()) {
            if (entry.getKey().equals(catalogId)) {
                for (SpecificProduct sp : entry.getValue()) {
                    sp.setDiscount(discount);
                }
            }
        }
        databaseHandler.diacountProducts(branch,catalogId,discount);
        return "added discount to "+ catalogId;
    }

    public String addDiscountCategory(String category, int discount,int branch) throws SQLException {


        if(categoryToCatalogIDBool==false)
            this.categoryToCatalogID = getCategoryToCatalogIDTable();

        if(!categoryToCatalogID.get(branch).containsKey(category))
            return "There are no products with "+ category + " as its category";
        if(discount >= 100 || discount <0)
            return "You can't give a discount of 100 percent or above , discount can't be lower than 0";
        for (Map.Entry<String, List<String>> entry : categoryToCatalogID.get(branch).entrySet()) {
            if (entry.getKey().equals(category)) {
                for (String catalogId : entry.getValue()) {
                    addDiscountProduct(catalogId,discount,branch);

                }
            }
        }
        return "added discount to products with " + category + " as their category";
    }

    private static boolean validateProductQuantities(int quantity, int minQuantity) {
        if(!(SpecificProduct.validatePositive(quantity) && SpecificProduct.validatePositive(minQuantity))) {
            return false;
        }
        return true;
    }

    private void clear(String catalogId,int branch) throws SQLException {


        catalogIdToProducts.get(branch).remove(catalogId);
        catalogIdToQuantity.get(branch).remove(catalogId);
        catalogIdToMinQuantity.get(branch).remove(catalogId);
        catalogIdToCounter.get(branch).remove(catalogId);

        for (Map.Entry<String, List<String>> entry : categoryToCatalogID.get(branch).entrySet()) {

            for(String element : cloneList(entry.getValue()))
            {
                if (element.equals(catalogId))
                {
                    entry.getValue().remove(catalogId);
                }
            }
            if(entry.getValue().isEmpty())
                categoryToCatalogID.get(branch).remove(entry);
        }
    }
    private List<String> cloneList (List<String> l){
        List ret = new LinkedList();
        for(String s : l){
            String s1 = new String(s);
            ret.add(s1);
        }
        return ret;

    }

    public String getBranchAddress(int branch) {
        return branchIdToAddress.get(branch);
    }


    public  Map<Integer,Map<String, Integer>> getCatalogIdToQuantityTable() throws SQLException {
        Map<Integer,Map<String, Integer>> ret = databaseHandler.getQuanTable();
        catalogIdToQuantityBool = true;
        return ret;
    }
    public  Map<Integer,Map<String, Integer>> getCatalogIdToMinQuantityTable() throws SQLException {
        Map<Integer,Map<String, Integer>> ret = databaseHandler.getMinQuanTable();
        catalogIdToMinQuantityBool = true;
        return ret;
    }

    public  Map<Integer,Map<String, Integer>> getCatalogIdToCounterTable() throws SQLException {
        Map<Integer,Map<String, Integer>> ret = databaseHandler.getCatalogIdToCounterTable();
        catalogIdToCounterBool = true;
        return ret;
    }


    public  Map<Integer,Map<String, List<String>>> getCategoryToCatalogIDTable() throws SQLException {
        Map<Integer,Map<String, List<String>>>  ret = databaseHandler.getCategoryToCatalogIDTable();
        categoryToCatalogIDBool = true;
        return ret;
    }

    public  Map<Integer,Map<String, List<SpecificProduct>>> getCatalogIdToProductsTable() throws SQLException {
        Map<Integer,Map<String, List<SpecificProduct>>>  ret = databaseHandler.getCatalogIdToProductsTable();
        catalogIdToProductsBool = true;
        return ret;
    }


    public void updateInventoryFromShipment(Map<String,Map<String,Integer>> productsToStore) throws SQLException {
        for(Map.Entry<String,Map<String,Integer>> entry : productsToStore.entrySet()) {
            addListOfProducts(entry.getKey(),entry.getValue());
        }

    }


    private void addListOfProducts (String branch,Map<String,Integer> products) throws SQLException {
        if(catalogIdToProductsBool == false)
            this.catalogIdToProducts=getCatalogIdToProductsTable();

        if(catalogIdToQuantityBool == false)
            this.catalogIdToQuantity=getCatalogIdToQuantityTable();

        if(catalogIdToMinQuantityBool == false)
            this.catalogIdToMinQuantity=getCatalogIdToMinQuantityTable();

        if(catalogIdToCounterBool == false)
            this.catalogIdToCounter=getCatalogIdToCounterTable();


        int branchId = -1;
        for(Map.Entry<Integer, String> entry : branchIdToAddress.entrySet()){
            if(entry.getValue().equals(branch)){
                branchId = entry.getKey();
            }
        }
        if(branchId == -1){
            return;
        }
        for(Map.Entry<String, Integer> entry : products.entrySet()){
            if(catalogIdToProducts.get(branchId).containsKey(entry.getKey())){
                addExistingProduct(branchId,entry.getKey(),entry.getValue());
            }
            else{
                addNewProduct(branchId,entry.getKey(),entry.getValue());
            }
        }
    }

    private void addExistingProduct(int branch,String catalogId , int quantity) throws SQLException {

        Product sample = catalogIdToProducts.get(branch).get(catalogId).get(0);

        SpecificProduct p = new SpecificProduct(catalogId,sample.getName(),sample.getSellPrice(),sample.getBuyPrice(),
        sample.getManufacturer(),sample.getCategory(),sample.getSubCategory(),sample.getSubSubCategory(),
                sample.getWeight(),((SpecificProduct) sample).getId(),((SpecificProduct) sample).getLocation()
        ,((SpecificProduct) sample).isFlaw(),((SpecificProduct) sample).getExpirationDate(),sample.getDiscount());


        // We are dealing with a legal case
        List<SpecificProduct> l = catalogIdToProducts.get(branch).get(catalogId);


        // We've got the list, now it's time to add
        int startingId = catalogIdToCounter.get(branch).get(catalogId);
        for (int i = 0; i < quantity; i++) {
            SpecificProduct sp1 = SpecificProduct.my_clone(p);
            sp1.setId(startingId);
            startingId++;
            l.add(sp1);
            databaseHandler.addProduct(sp1,branch);
        }

        catalogIdToCounter.get(branch).replace(catalogId, startingId);
        int curQuantity = catalogIdToQuantity.get(branch).get(catalogId);
        catalogIdToQuantity.get(branch).replace(catalogId, quantity + curQuantity);
        databaseHandler.updateQuantity(quantity + curQuantity,catalogId,branch);
        databaseHandler.updateCounter(startingId,catalogId,branch);


    }

    private void addNewProduct(int branch,String catalogId , int quantity) throws SQLException{
        String name =SharedClass.getSharedClass().getProductsMap().get(catalogId).getFirst();
        Date expDate = new Date();
        expDate.setYear(2023);

        int minQuantity = quantity -3;
        if (minQuantity<=0){
            minQuantity=1;
        }

        double weight = SharedClass.getSharedClass().getProductsMap().get(catalogId).getSecond();

        SpecificProduct p = new SpecificProduct(catalogId,name,-1,-1,
                "generalManufacturer","general","generalS","generalSS",
                weight,-1,LOCATION.STORAGE
                ,false,expDate,0);


        List<SpecificProduct> l = new LinkedList<>();

            catalogIdToMinQuantity.get(branch).put(catalogId,minQuantity);
            catalogIdToQuantity.get(branch).put(catalogId, 0);
            catalogIdToCounter.get(branch).put(catalogId, 1);

            addCategories(p.getCategory(), catalogId,branch);
            addCategories(p.getSubCategory(), catalogId,branch);
            addCategories(p.getSubSubCategory(), catalogId,branch);

            List<SpecificProduct> list = new LinkedList<>();
            catalogIdToProducts.get(branch).put(catalogId, list);
           // l = list;


        // We've got the list, now it's time to add
        int startingId = catalogIdToCounter.get(branch).get(catalogId);
        for (int i = 0; i < quantity; i++) {
            SpecificProduct sp1 = SpecificProduct.my_clone(p);
            sp1.setId(startingId);
            startingId++;
            l.add(sp1);
            databaseHandler.addProduct(sp1,branch);
        }
        databaseHandler.addNewProductToTables(branch,p,minQuantity);
        catalogIdToCounter.get(branch).replace(catalogId, startingId);
        int curQuantity = catalogIdToQuantity.get(branch).get(catalogId);
        catalogIdToQuantity.get(branch).replace(catalogId, quantity + curQuantity);
        databaseHandler.updateQuantity(quantity + curQuantity,catalogId,branch);

        databaseHandler.updateCounter(startingId,catalogId,branch);
    }





    public void saveReport(ReportDTO rDTO){
        databaseHandler.saveReport(rDTO);
    }


    public String updateShippmentProductInfo(int branch, String catalogIdToUpdate, String newCategory, String newSubCategory, String newSubSubCategory, String newManufacturer, int newSellPriceI, int newBuyPriceI) throws SQLException {
        String retVal = "";

        if (catalogIdToProductsBool == false)
            this.catalogIdToProducts = getCatalogIdToProductsTable();

        if (catalogIdToQuantityBool == false)
            this.catalogIdToQuantity = getCatalogIdToQuantityTable();

        if (catalogIdToMinQuantityBool == false)
            this.catalogIdToMinQuantity = getCatalogIdToMinQuantityTable();

        if (catalogIdToCounterBool == false)
            this.catalogIdToCounter = getCatalogIdToCounterTable();

        if (categoryToCatalogIDBool == false)
            this.categoryToCatalogID = getCategoryToCatalogIDTable();


        if (!catalogIdToProducts.get(branch).containsKey(catalogIdToUpdate)) {
            retVal = "this branch does not contains that desired product to update!";
            return retVal;
        }

        if (!catalogIdToProducts.get(branch).get(catalogIdToUpdate).get(0).getCategory().equals("general")){
            retVal = "this product is already updated!";
            return retVal;
        }



        for (SpecificProduct sp : catalogIdToProducts.get(branch).get(catalogIdToUpdate)) {
            sp.setManufacturer(newManufacturer);
            sp.setSellPrice(newSellPriceI);
            sp.setBuyPrice(newBuyPriceI);
            sp.setCategory(newCategory);
            sp.setSubCategory(newSubCategory);
            sp.setSubSubCategory(newSubSubCategory);
        }

        for (Map.Entry<String, List<String>> entry : categoryToCatalogID.get(branch).entrySet()) {

            for (String element : cloneList(entry.getValue())) {
                if (element.equals(catalogIdToUpdate)) {
                    entry.getValue().remove(catalogIdToUpdate);
                }
            }
            if (entry.getValue().isEmpty())
                categoryToCatalogID.get(branch).remove(entry);
        }

        addCategories(newCategory, catalogIdToUpdate, branch);
        addCategories(newSubCategory, catalogIdToUpdate, branch);
        addCategories(newSubSubCategory, catalogIdToUpdate, branch);

        databaseHandler.updateShippmentProductInfo(branch,catalogIdToUpdate,newCategory,newSubCategory,newSubSubCategory,newManufacturer,newSellPriceI,newBuyPriceI);




        retVal = "Product with catalogId "+ catalogIdToUpdate + " has been updated succesfully!";
        return retVal;
    }

    public String getAllReports() throws SQLException {
        return databaseHandler.getAllReports();
    }
}
