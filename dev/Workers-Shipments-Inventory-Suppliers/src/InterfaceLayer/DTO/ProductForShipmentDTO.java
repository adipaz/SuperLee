package InterfaceLayer.DTO;

import BusinessLayer.SuppliersAndInventory.ProductForShipment;

public class ProductForShipmentDTO {
    private String name;
    private String catalogID;
    private double weight;


    public ProductForShipmentDTO(ProductForShipment product){
        this.name = product.getName();
        this.catalogID = product.getCatalogID();
        this.weight= product.getWeight();
    }

    public ProductForShipmentDTO(String name, String catalogID, double weight){
        this.name = name;
        this.catalogID = catalogID;
        this.weight= weight;
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

    public double getWeight() {
        return weight;
    }


    @Override
    public String toString() {
        return "Name: " +name+ "\tCatalog ID: " +catalogID ;
    }
}
