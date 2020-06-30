package InterfaceLayer.DTO;

import BusinessLayer.SuppliersAndInventory.Product;
import BusinessLayer.SuppliersAndInventory.ProductForShipment;
import BusinessLayer.SuppliersAndInventory.ProductS;

public class ProductS_DTO {
    private String name;
    private String catalogID;
    private String price;
    private double weight;

    public ProductS_DTO(String name, String catalogID, String price, double weight) {
        this.name = name;
        this.catalogID = catalogID;
        this.price = price;
        this.weight= weight;
    }

    public ProductS_DTO(ProductS product){
        this.name = product.getName();
        this.catalogID = product.getCatalogID();
        this.price = ""+ product.getPrice();
        this.weight= product.getWeight();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String toString(String bn) {
        return "Name: " +name+ "\tCatalog ID: " +catalogID +"_"+bn+"\tWeight: "+weight +"\tPrice: " +price + "\n\t\t\t\t\t\t";
       /* return "\n\t\t\t{\n" +
                "\t\t\t name='" + name + '\'' +
                ",\n\t\t\t catalogID='" + catalogID + '\'' +
                ",\n\t\t\t price='" + price + '\'' +
                ",\n\t\t\t weight='" + weight + '\'' +
                "}";
        */
    }
}
