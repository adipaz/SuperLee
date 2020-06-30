package InterfaceLayer.DTO;



import java.util.HashMap;
import java.util.List;

public class Order_DTO {
    private String supplierName;
    private String bnNumber;
    private String adress;
    private String date;
    private String phone;
    private List<String[]> products;

     public Order_DTO(String supplierName,String bnNumber, String adress,String date,String phone,List<String[]> products)
     {
         this.supplierName=supplierName;
         this.bnNumber=bnNumber;
         this.adress=adress;
         this.date=date;
         this.phone = phone;
         this.products=products;
     }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public String getAdress()
    {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public List<String[]> getProducts() {
        return products;
    }

    public String toString(){
         return "\n\t\t\t\t\t\t\t\t\taddress- "+adress+"\n\t\t\t\t\t\t\t\t\tphone- "+phone+
                 "\n\t\t\t\t\t\t\t\t\tproducts- "+productsAsString();
    }

    public String productsAsString(){
         String output="";
        for (String[] product:products) {
            output+="\n\t\t\t\t\t\t\t\t\t\tID: "+product[0]+"_"+bnNumber+" name: "+product[1]+" amount: "+product[2]+" price: "+product[3]+" discount: "+product[4]+" final price: "+product[5];
        }
        return output;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setProducts(List<String[]> products) {
        this.products = products;
    }
}
