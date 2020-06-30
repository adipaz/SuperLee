package InterfaceLayer.DTO;

import java.util.Map;

public class CertificateInterfaceDTO {

    private final Map<ProductForShipmentDTO, Integer> productQuantity;
    private final String address;

    public CertificateInterfaceDTO(String address, Map<ProductForShipmentDTO, Integer> productQuantity) {
        this.productQuantity = productQuantity;
        this.address = address;
    }

    public String toString(){
        StringBuilder str = new StringBuilder(address + ":\n");
        for (Map.Entry<ProductForShipmentDTO, Integer> productAndQuantity : productQuantity.entrySet()) {
            ProductForShipmentDTO product = productAndQuantity.getKey();
            Integer quantity = productAndQuantity.getValue();
            str.append("<" + product.getCatalogID() + ":" + product.getName() + ", " + "Quantity" + ": " + quantity + "> ");
        }
        return str.toString();
    }
}
