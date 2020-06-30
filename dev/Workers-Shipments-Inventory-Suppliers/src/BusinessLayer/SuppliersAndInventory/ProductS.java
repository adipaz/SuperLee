package BusinessLayer.SuppliersAndInventory;

import DataAccessLayer.DALDTO.DALProductS;

public class ProductS {
    private String name;
    private String catalogID;
    private double price;
    private double weight;

    public ProductS(String name, String catalogID, double price, double weight) {
        this.name = name;
        this.catalogID = catalogID;
        this.price = price;
        this.weight=weight;
    }

    public ProductS(DALProductS DALProduct) {
        this.catalogID = DALProduct.getCatalogID();
        this.name = DALProduct.getName();
        this.price = Double.parseDouble(DALProduct.getPrice());
        this.weight = DALProduct.getWeight();
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }
}
