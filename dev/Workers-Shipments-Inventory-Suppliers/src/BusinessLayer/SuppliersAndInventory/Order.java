package BusinessLayer.SuppliersAndInventory;

import java.util.List;

public class Order {
    private String orderID;
    private String supplierName;
    private String bnNumber;
    private String adress;
    private String date;
    private String phone;
    private List<String[]> products; //catalogid->name->amount->original price -> discount -> final price

    public Order(String supplierName,String bnNumber, String adress,String date,String phone,List<String[]> products,String orderID)
    {
        this.supplierName=supplierName;
        this.adress=adress;
        this.bnNumber=bnNumber;
        this.date=date;
        this.phone=phone;
        this.products=products;
        this.orderID=orderID;
    }



    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String[]> getProducts() {
        return products;
    }

    public void setProducts(List<String[]> products) {
        this.products = products;
    }

    public String toString()
    {
        return "Supplier's name: "+supplierName+"\nSupplier bn-Number: "+bnNumber+ "\nAddress: " +adress + "\nDate: "+findDay(date) + "\nPhone: " +phone + "\nProducts: " +productsAsString() ;
    }

    private String findDay(String day)
    {
        if(day.length()==1) {
            int day_i = Integer.parseInt(day);
            switch (day_i) {
                case 1: {
                    return "Sunday";
                }
                case 2: {
                    return "Monday";
                }
                case 3: {
                    return "Tuesday";
                }
                case 4: {
                    return "Wensday";
                }
                case 5: {
                    return "Thursday";
                }
                case 6: {
                    return "Friday";
                }
                case 7: {
                    return "Saturday";
                }
            }
        }

        return day;
    }

    private String productsAsString(){
        String output="";
        for (String[] product:products) {
            output+="ID: "+product[0]+"_"+bnNumber+" name: "+product[1]+" amount: "+product[2]+" price: "+product[3]+" discount: "+product[4]+" final price: "+product[5];
        }
        return output;
    }
}
