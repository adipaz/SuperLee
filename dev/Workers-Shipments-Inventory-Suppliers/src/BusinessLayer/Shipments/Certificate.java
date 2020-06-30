package BusinessLayer.Shipments;



import DataAccessLayer.DALDTO.CertificateDALDTO;
import DataStructures.Pair;

import java.util.*;

public class Certificate {

    private int ID;
    private Map<String, Integer> productIDQuantity;
    private String address;

    public String getAddress() {
        return address;
    }

    public Certificate(int ID, String address, Map<String,Integer> productIDQuantities){
        this.ID = ID;
        productIDQuantity = new HashMap<>();
        this.address = address;
        for (Map.Entry<String, Integer> entry : productIDQuantities.entrySet()) {
            this.productIDQuantity.put(entry.getKey(),entry.getValue());
        }
    }

    public Certificate(CertificateDALDTO dalCertificate) {
        ID = dalCertificate.getID();
        productIDQuantity = dalCertificate.getProductsQuantities();
        address = dalCertificate.getAddress();
    }

    public double calculateWeight( List<Pair<Integer,Double>> productQuantityAndWeight){
        double weight=0;
        for(Pair<Integer,Double> productQuantityWeight:productQuantityAndWeight){
            weight+=productQuantityWeight.getFirst()*productQuantityWeight.getSecond();
        }
        return weight;
    }


    public Map<String, Integer> getProductsQuantities() {
        return this.productIDQuantity;
    }

    public int removeProducts(String productCatalogID,int quantity){

        if (productIDQuantity.containsKey(productCatalogID)) {
            /**
             * notify that we removed (quantity - howMuchRemoved) products.
             */
            if (productIDQuantity.get(productCatalogID) > quantity) {
                int newQuantity = productIDQuantity.get(productCatalogID) - quantity;
                productIDQuantity.put(productCatalogID, newQuantity);
                return quantity;

            }
            /**
             * notify that we removed productsQuantities.get(j).getSecond() products.
             */
            else {
                int howMuchRemoved = productIDQuantity.get(productCatalogID);
                productIDQuantity.remove(productCatalogID);
                return howMuchRemoved;
            }

        }
        //failure
        return -1;
    }

    /**
     *this function is only for test purposes
     */
    public Pair<String,Integer> getFirstPairByCatalogNum(String catalogNum){
        for(Map.Entry<String,Integer> productQuantity: productIDQuantity.entrySet()){
            if(productQuantity.getKey().equals(catalogNum))
                return new Pair(productQuantity.getKey(),productQuantity.getValue());
        }
        return null;
    }

    public int getID() {
        return ID;
    }
}
