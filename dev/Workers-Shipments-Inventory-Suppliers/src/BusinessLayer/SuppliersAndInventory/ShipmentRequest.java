package BusinessLayer.SuppliersAndInventory;

import DataStructures.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentRequest {

    private String supplierAddress;
    private String bnNumber;
    private Map<String, Map<ProductForShipment,Integer>> productsForShipment; // pair.getSecond()= quantity


    public ShipmentRequest(String supplierAddress , Map<String, Map<ProductForShipment,Integer>> products,String bnNumber)
    {
        this.supplierAddress = supplierAddress;
        this.productsForShipment = products;
        this.bnNumber = bnNumber;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }


    public Map<String, Map<ProductForShipment, Integer>> getProductsForShipment() {
        return productsForShipment;
    }

    public void combine(ShipmentRequest shipmentRequest) {
        for(String branchAddress:shipmentRequest.productsForShipment.keySet()){
            if(this.productsForShipment.keySet().contains(branchAddress)){
                for(Map.Entry<ProductForShipment,Integer> productQuantityInput: shipmentRequest.getProductsForShipment().get(branchAddress).entrySet()){
                    boolean isFound = false;
                    for(Map.Entry<ProductForShipment,Integer> productQuantity: productsForShipment.get(branchAddress).entrySet()){
                        if(productQuantityInput.getKey().getCatalogID().equals(productQuantity.getKey().getCatalogID())) {
                            productsForShipment.get(branchAddress).put(productQuantity.getKey(),productQuantity.getValue() + productQuantityInput.getValue());
                            isFound = true;
                            break;
                        }
                    }
                    if(!isFound)
                        productsForShipment.get(branchAddress).put(productQuantityInput.getKey(),productQuantityInput.getValue());
                }
            }
            else
                this.productsForShipment.put(branchAddress, shipmentRequest.getProductsForShipment().get(branchAddress));
        }
    }
}
