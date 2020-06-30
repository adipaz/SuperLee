package BusinessLayer.SuppliersAndInventory;

import DataAccessLayer.Handlers.DatabaseHandlerSupplier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Repository {
    private List<Supplier> supplierList = new LinkedList<>();
    private Map<Integer, Order> OrdersByID = new HashMap<>(); //<OrderID,Order>
    private static Repository singleton = new Repository();
    private int ORDERSCOUNTER =1;
    private DatabaseHandlerSupplier databaseHandler = new DatabaseHandlerSupplier();
    private boolean loaded=false;


    private Repository() {
    } //private constructor



    public static Repository getRepository()  {
        return singleton;
    }

    //*********TODAY***********
    public String getToday()
    {
        return String.valueOf(SharedClass.getSharedClass().getDATE());
    }
    public void nextDay()
    {
       OrderManager.sendWeeklyOrders();
    }

    //********getters and setters*************
    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public Map<Integer, Order> getOrdersByID() {
        return OrdersByID;
    }

    public DatabaseHandlerSupplier getDatabaseHandler()
    {
        return databaseHandler;
    }


    public int getORDERSCOUNTER() {
        return ORDERSCOUNTER;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public void setOrdersByID(Map<Integer, Order> ordersByID) {
        OrdersByID = ordersByID;
    }


    public void setORDERSCOUNTER(int ORDERSCOUNTER) {
        this.ORDERSCOUNTER = ORDERSCOUNTER;
    }


    public void setLoaded(boolean loaded) { this.loaded = loaded; }

    public boolean wasLoaded() { return loaded; }















}
