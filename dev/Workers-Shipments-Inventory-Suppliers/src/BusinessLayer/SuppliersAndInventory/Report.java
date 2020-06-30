package BusinessLayer.SuppliersAndInventory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Report {
    final static int extra = 10;

    private Date creationTime;
    private TYPE type;
    private String address;
    private String description;

    public Report(Date creationTime, TYPE type, String description,String address) {
        this.creationTime = creationTime;
        this.type = type;
        this.address = address;
        this.description = description;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public TYPE getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static Report createSupplyReport(List<String> categories,int branch) throws SQLException {
        String addr = Store.getInstance().getBranchAddress(branch);

        Map<String , List<String>> categoryToCatalogID = Store.getInstance().getCatalogIDByCategory(categories,branch);
        String Description="Categories:                    CatalogID:                    Name:                    Quantity:\n";
        for (Map.Entry<String, List<String>> entry : categoryToCatalogID.entrySet()) {
            for(String CID:entry.getValue()){
                String category = entry.getKey();
                String id=CID;
                for(String c : categoryToCatalogID.keySet()) {
                    if(categoryToCatalogID.get(c).contains(CID) && !(c.equals(category))) {
                        category += ", " + c;
                        categoryToCatalogID.get(c).remove(CID);
                    }
                }
                String Name=Store.getInstance().getProductName(CID,branch);
                String quantity = Integer.toString(Store.getInstance().getProductQuantity(CID,branch));

                int categoryLength = category.length();
                int idLength = id.length();
                int nameLength = Name.length();

                category = addSpaces("Categories:".length(),category,categoryLength);
                id = addSpaces("CatalogID:".length(),id,idLength);
                Name = addSpaces("Name:".length(),Name,nameLength );


                Description += (category+id+ Name+quantity+"\n" );
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        LocalDate date2 =  SharedClass.getSharedClass().getDATE();
        date.setYear(date2.getYear());
        date.setMonth(date2.getMonth().getValue());
        date.setDate(date2.getDayOfMonth()%7+1);



        Report report = new Report(date, TYPE.SUPPLY_REPORT,Description,addr);

        return report;

    }
    public static Report createFlawReport(int branch) throws SQLException {
        String addr = Store.getInstance().getBranchAddress(branch);

        Map<String,Integer> flaws = Store.getInstance().getFlaws(branch);
        String Description="CatalogID:                    Name:                    Quantity:\n";
        for (Map.Entry<String,Integer> entry : flaws.entrySet()) {

            String id=entry.getKey();
            String Name=Store.getInstance().getProductName(entry.getKey(),branch);
            String flaw = flaws.get(entry.getKey()).toString();
            int idLength = id.length();
            int nameLength = Name.length();

            id = addSpaces("CatalogID:".length(),id,idLength);
            Name = addSpaces("Name:".length(),Name,nameLength );

            Description+=(id+Name+flaw+"\n" );

        }
        Date date = new Date();

        LocalDate date2 =  SharedClass.getSharedClass().getDATE();
        date.setYear(date2.getYear());
        date.setMonth(date2.getMonth().getValue());
        date.setDate(date2.getDayOfMonth()%7+1);

        Report report = new Report(date, TYPE.FLAW_REPORT,Description,addr);

        return report;
    }

    public static Report createOrderReport(int branch) throws SQLException {
        String addr = Store.getInstance().getBranchAddress(branch);

        Map<String,Integer> Missings = Store.getInstance().getMissings(branch);

        String Description="CatalogID:                    Name:                    Quantity To Order:\n";
        for (Map.Entry<String,Integer> entry : Missings.entrySet()) {

            String id=entry.getKey();
            String Name=Store.getInstance().getProductName(entry.getKey(),branch);
            String quantity = Integer.toString(entry.getValue() + extra );
            int idLength = id.length();
            int nameLength = Name.length();

            id = addSpaces("CatalogID:".length(),id,idLength);
            Name = addSpaces("Name:".length(),Name,nameLength );

            Description+=(id+Name+quantity+"\n" );

        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        LocalDate date2 =  SharedClass.getSharedClass().getDATE();
        date.setYear(date2.getYear());
        date.setMonth(date2.getMonth().getValue());
        date.setDate(date2.getDayOfMonth()%7+1);


        Report report = new Report(date, TYPE.ORDER_REPORT,Description,addr);

        return report;
    }





    private static String addSpaces(int total, String str, int strLength) {
        int spaces=total+20-strLength;
        String ans = str;
        for(int i=0;i<spaces ; i++){
            ans+=" ";
        }
       /* for(int i=0;i<spaces/4 ; i++){
            ans+="\t";
        }*/

        return ans;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
