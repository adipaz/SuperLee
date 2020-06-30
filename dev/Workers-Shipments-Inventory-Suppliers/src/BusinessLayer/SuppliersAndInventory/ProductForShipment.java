package BusinessLayer.SuppliersAndInventory;

public class ProductForShipment {

    private String name;
    private String catalogID;
    private double weight;

    public void setName(String name) {
        this.name = name;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public ProductForShipment(String name, String catalogID, double weight) {
        this.name = name;
        this.catalogID = catalogID;
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public double getWeight() {
        return weight;
    }
}
