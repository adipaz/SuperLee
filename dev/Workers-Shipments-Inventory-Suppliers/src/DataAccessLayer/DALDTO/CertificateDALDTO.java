package DataAccessLayer.DALDTO;

import java.util.Map;

public class CertificateDALDTO implements DALDTO {
    private int ID;
    private Map<String, Integer> productsQuantities;
    private String address;
    private int shipmentID;

    public CertificateDALDTO(int id, Map<String, Integer> productsQuantities, String address, int shipmentID) {
        ID = id;
        this.productsQuantities = productsQuantities;
        this.address = address;
        this.shipmentID = shipmentID;
    }

    public int getID() {
        return ID;
    }

    public Map<String, Integer> getProductsQuantities() {
        return productsQuantities;
    }

    public String getAddress() {
        return address;
    }

    public int getShipmentID() {
        return shipmentID;
    }
}
