package InterfaceLayer;

import BusinessLayer.*;
import BusinessLayer.SuppliersAndInventory.ProductSManager;
import BusinessLayer.SuppliersAndInventory.SharedClass;
import BusinessLayer.SuppliersAndInventory.SupplierManager;
import InterfaceLayer.DTO.ProductS_DTO;
import InterfaceLayer.DTO.Supplier_DTO;
import DataStructures.Pair;


import java.util.HashMap;

public class ProductSService {


    public static HashMap<String,Pair<String,Double>> getGeneralProducts()
    {
        return ProductSManager.getGeneralProducts();
    }

    public static String getProductName(String name,String bnNumber,String catalogID)
    {
        return String.valueOf(ProductSManager.getProductName(name, bnNumber, catalogID));
    }

    public static boolean changeProductName(String name,String bnNumber,String catalogID,String newname)
    {
        return ProductSManager.changeProductName(name,bnNumber,catalogID,newname);
    }

    public static boolean changeProductCatalogID(String name,String bnNumber,String catalogID,String newCatalogID)
    {
        return ProductSManager.changeProductCatalogID(name, bnNumber, catalogID, newCatalogID);
    }
    public static boolean changeProductPrice(String name,String bnNumber,String catalogID,String newPrice)
    {
        return ProductSManager.changeProductPrice(name, bnNumber, catalogID, newPrice);
    }



    public static String viewProductsForedit(String name,String bnNumber) //creating string for printing method
    {
        Supplier_DTO sup=null;
        for (Supplier_DTO supplier: SupplierManager.getSupplierDTOList()) {
            if(supplier.getName().equals(name)&&supplier.getBnNumber().equals(bnNumber))
                sup=supplier;
        }
        String str="";
        if(sup!=null) {
            int i=1;
            for (ProductS_DTO p:sup.getContract().getProductsIncluded()) {
                str+=""+i+")" +" Name: " + p.getName() + " Catalog ID: " + p.getCatalogID()+"_"+bnNumber+ "\n";
                i++;
            }

        }
        return str;
    }

    public static String viewProducts(String name, String bnNumber) //creating string for printing method
    {
        Supplier_DTO sup=null;
        for (Supplier_DTO supplier: SupplierManager.getSupplierDTOList()) {
            if(supplier.getName().equals(name)&&supplier.getBnNumber().equals(bnNumber))
                sup=supplier;
        }
        String str="";
        if(sup!=null) {
            int i=1;
            for (ProductS_DTO p:sup.getContract().getProductsIncluded()) {
                //str+=p.toString();
                str+=""+i+")"  +" Name: " + p.getName() + " Catalog ID: " + p.getCatalogID()+"_"+bnNumber+ " Price: "  +p.getPrice()  + "\n";
                i++;
            }

        }

        if(str.equals(""))
            str+= "THERE IS NO PRODUCTS IN THE CONTRACT TO BE SHOWN" +"\n";
        return str;
    }

    public static boolean validCatalogID(String catalogID, String name, String bnNumber) {
        return ProductSManager.validCatalogID(catalogID,name,bnNumber);
    }


    public static String getAllProducts() {
        String output="";
        for (String key : SharedClass.getSharedClass().getProductsMap().keySet()) {
            output+="catalog id: "+key+" name: "+SharedClass.getSharedClass().getProductsMap().get(key)+"\n";
        }
        return output;
    }

    public static boolean existCatalogID(String catalogID) {
        return  SharedClass.getSharedClass().getProductsMap().containsKey(catalogID);
    }

    public static  String getProductNameByCataogID(String catalogID)
    {
        return SharedClass.getSharedClass().getProductsMap().get(catalogID).getFirst();
    }

    public static Double getWeightByCatalogId(String catalogID)
    {
        return SharedClass.getSharedClass().getProductsMap().get(catalogID).getSecond();
    }

    public static void addNewProductToTheSystem(String catalogID, String pname,String weight) {
        Pair<String,Double> p = new Pair<>(pname,Double.parseDouble(weight));
        SharedClass.getSharedClass().getProductsMap().put(catalogID, p) ;
    }

    public static void deleteProductFromSystem(String catalogID) {
        ProductSManager.deleteProductFromSystem(catalogID);

    }

}
