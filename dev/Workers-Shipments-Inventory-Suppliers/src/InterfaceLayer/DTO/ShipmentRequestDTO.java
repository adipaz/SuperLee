package InterfaceLayer.DTO;

import BusinessLayer.SuppliersAndInventory.ProductForShipment;
import BusinessLayer.SuppliersAndInventory.ShipmentRequest;

import java.util.HashMap;
import java.util.Map;

public class ShipmentRequestDTO {
    private final String supplierAddress;
    private final Map<String, Map<ProductForShipmentDTO, Integer>> productsPerDestination;

    public ShipmentRequestDTO(ShipmentRequest initiateShipment) {
        this.supplierAddress = initiateShipment.getSupplierAddress();
        Map<String, Map<ProductForShipment, Integer>> businessProducts = initiateShipment.getProductsForShipment();
        this.productsPerDestination = new HashMap<>();
        for (Map.Entry<String, Map<ProductForShipment, Integer>> destination: businessProducts.entrySet()) {
            Map<ProductForShipmentDTO, Integer> newMap = new HashMap<>();
            for (Map.Entry<ProductForShipment, Integer> product:destination.getValue().entrySet()) {
                newMap.put(new ProductForShipmentDTO(product.getKey()), product.getValue());
            }
            this.productsPerDestination.put(destination.getKey(), newMap);
        }
    }

    public Map<String, Map<ProductForShipmentDTO, Integer>> getProductsPerDestination(){
        return this.productsPerDestination;
    }

    @Override
    public String toString() {
        String str = "Shipment request\n";
        str += "Supplier address: " + supplierAddress ;
        for (Map.Entry<String, Map<ProductForShipmentDTO, Integer>> destination: productsPerDestination.entrySet()) {
            str += ("\nDestination: " + destination.getKey() + " -\n");
            for (Map.Entry<ProductForShipmentDTO, Integer> product:destination.getValue().entrySet()) {
                str += ("\t" + product.getKey().toString());
                str += ("\tQuantity: " + product.getValue() + "\n");
            }

        }
        return str;
    }
}
