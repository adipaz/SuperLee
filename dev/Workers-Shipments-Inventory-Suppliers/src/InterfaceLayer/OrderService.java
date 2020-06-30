package InterfaceLayer;
import BusinessLayer.SuppliersAndInventory.OrderManager;
import BusinessLayer.SuppliersAndInventory.Repository;
import BusinessLayer.SuppliersAndInventory.SupplierManager;
import DataStructures.Pair;
import InterfaceLayer.DTO.Order_DTO;
import InterfaceLayer.DTO.ProductS_DTO;
import InterfaceLayer.DTO.Supplier_DTO;
import PresentationLayer.SupplierMenu;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderService {

    public static boolean addPerOrder(String name, String bnNumber, List<String[]> orderdetails, String day, String adress, String phone)
    {
        Order_DTO orderDto = new Order_DTO(name,bnNumber,adress,day,phone,orderdetails);
        if(!OrderManager.addPerOrder(bnNumber,name,orderDto))
            return false;
        return true;
    }

    public static void printOrders(String orderdetails)
    {
        SupplierMenu.printOrders(orderdetails);
    }

    public static boolean moveWeeklyOrderToDiffDay(String bnNumber,String name,String newday,String orderID)
    {
        return OrderManager.moveWeeklyOrderToDiffDay(name,bnNumber,orderID,newday);
    }

    public static Map<Integer,String> printAddressesToMenu()
    {
        return OrderManager.printAddressesToMenu();
    }


    public static List<String> getOrderPrices(String name,String bnNumber, String catalogID,String amount)
    {
        List<String> details = new LinkedList<>();
        List<Double> details_int = OrderManager.getOrderPrices(bnNumber,name,catalogID,amount);
        details.add(String.valueOf(details_int.get(0)));
        details.add(String.valueOf(details_int.get(1)));
        details.add(String.valueOf(details_int.get(2)));
        return details;
    }

    public static HashMap<String, Pair<String,String>> viewProductsForPerOrders(String bnNumber, String name)
    {
        HashMap<String,Pair<String, String>> products = new HashMap<>();
        Supplier_DTO sup=null;
        for (Supplier_DTO supplier: SupplierManager.getSupplierDTOList()) {
            if(supplier.getName().equals(name)&&supplier.getBnNumber().equals(bnNumber))
                sup=supplier;
        }
        String str="";
        if(sup!=null) {
            int i=1;
            for (ProductS_DTO p:sup.getContract().getProductsIncluded()) {
                str=" Name: " + p.getName() + " Catalog ID: " + p.getCatalogID()+"_"+bnNumber;
                Pair<String,String> pair = new Pair<>(p.getCatalogID(),str);
                products.put(String.valueOf(i),pair);
                i++;
                str="";
            }

        }
        return products;
    }

    public static boolean deletePerOrder(String name,String bnNumber,String orderID)
    {
        return OrderManager.deletePerOrder(bnNumber,name,orderID);
    }

    public static String getOrderDTOByID(int orderID){
        return OrderManager.getOrderDTOByID(orderID).toString();
    }

    public static List<Pair<String,String>> printPerOrders(String name, String bnNumber)
    {
        return OrderManager.printperOrder(name, bnNumber);
    }

    public static boolean changePerOrderAddress(String adress,String orderID)
    {
        return OrderManager.changePerOrderAdress(adress,orderID);
    }
    public static boolean changePerOrderPhone(String phone,String orderID)
    {
        return OrderManager.changePerOrderPhone(phone,orderID);
    }

    public static String viewProductsFromPerOrder(String orderID)
    {
        return OrderManager.getOrderDTOByID(Integer.parseInt(orderID)).productsAsString();
    }

    public static boolean deleteProductFromOrder(String orderID,String catalogID)
    {
        return OrderManager.deleteProductFromOrder(orderID,catalogID);
    }

    public static boolean addProductToOrder(String orderID,String[] product)
    {
        return OrderManager.addProductToOrder(orderID, product) ;
    }

    public static String getToday()
    {
        return Repository.getRepository().getToday();
    }

    public static void addNewOrder(List<Pair<String, Integer>> catalogXamount, String branchAddress) throws SQLException {
        OrderManager.addNewOrder(catalogXamount, branchAddress);
    }
}
