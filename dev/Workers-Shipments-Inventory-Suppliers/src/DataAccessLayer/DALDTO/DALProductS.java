package DataAccessLayer.DALDTO;

public class DALProductS implements DALDTO
{
    private String name;
    private String catalogID;
    private String price;
    private double weight;


    public DALProductS(String name, String catalogID, String price, double weight) {
        this.name = name;
        this.catalogID = catalogID;
        this.price = price;
        this.weight = weight;
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

    public void setPrice(String price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }
}
