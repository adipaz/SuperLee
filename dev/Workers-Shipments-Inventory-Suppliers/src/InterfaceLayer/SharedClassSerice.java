package InterfaceLayer;

import BusinessLayer.Manager;
import BusinessLayer.SuppliersAndInventory.SharedClass;

import java.sql.SQLException;

public class SharedClassSerice {
    public static boolean nextDay() throws SQLException {
        SupplierService.loadRepository();
        Manager.getInstance().checkForShipmentsExecutions();
        return SharedClass.getSharedClass().nextDate();
    }

    public static String getDayString()
    {
        return SharedClass.getSharedClass().getDAYString();
    }

    public static void createNewDataBase()
    {
        SharedClass.getSharedClass().createNewDataBase();
    }

   public static void initProducts() throws SQLException {
        SharedClass.getSharedClass().initProducts();
    }
}
