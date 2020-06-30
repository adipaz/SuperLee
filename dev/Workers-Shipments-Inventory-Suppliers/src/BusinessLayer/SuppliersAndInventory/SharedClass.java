package BusinessLayer.SuppliersAndInventory;
import DataAccessLayer.SQLiteJDBC;
import DataStructures.Pair;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class SharedClass {


    private HashMap<String, Pair<String,Double>> products = new HashMap<>();// <Catlogid,Name>
    private LocalDate DATE= LocalDate.of(2020,9,1);
    private static SharedClass sharedClass = new SharedClass();



    private SharedClass() {
    }

    public static SharedClass getSharedClass() {
        return sharedClass;
    }

    public LocalDate getDATE()
    {
        return DATE;
    }

    public int getDAY(){

        return (DATE.getDayOfWeek().getValue() % 7)+1;
    }

    public String getDAYString()
    {
        switch ((DATE.getDayOfWeek().getValue()% 7)+1){

            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
        }
        return "";
    }

    public boolean nextDate()
    {
        DATE= DATE.plusDays(1);
        Repository.getRepository().nextDay();
        return true;
    }

    public HashMap<String,Pair<String,Double>> getProductsMap()
    {
        return products;
    }


    public void createNewDataBase()
    {
        SQLiteJDBC.createNewDatabase("SuperLee8.db");
    }

    public void initProducts() throws SQLException {
        products = (HashMap<String, Pair<String,Double>>) Repository.getRepository().getDatabaseHandler().getProductsTable();
    }

    //    public void addProduct(String catalogID,String name) {
//        this.products.put(catalogID,name);
//    }
}
