package BusinessLayer.SuppliersAndInventory;

import BusinessLayer.Shipments.Shipment;
import DataAccessLayer.DALDTO.DALProductS;
import DataStructures.Pair;
import InterfaceLayer.DTO.ProductS_DTO;
import InterfaceLayer.DTO.Supplier_DTO;

import java.sql.SQLException;
import java.util.*;

public class ProductSManager {

    public static boolean changeProductName(String name, String bnNumber, String catalogID, String newName) //validate
    {

        Supplier p = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        if (p != null) {
            Contract contract = p.getContract();
            if (contract != null) {
                return contract.changeProductName(catalogID, newName);
            }
        }
        return false;
    }

    public static boolean changeProductPrice(String name, String bnNumber, String catalogID, String newPrice) //validate
    {
        Supplier p = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        if (p != null) {
            Contract contract = p.getContract();
            if (contract != null) {
                if(!validateBeforeDeleteProduct(contract,catalogID))
                    return false;
                boolean succeed= contract.changeProductPrice(catalogID, newPrice);
                if(succeed) Repository.getRepository().getDatabaseHandler().updateProductPrice(bnNumber,catalogID,newPrice);
                return succeed;
            }
        }
        return false;
    }

    public static boolean changeProductCatalogID(String name, String bnNumber, String catalogID, String newCatalogID) {
        if (validProductID(newCatalogID)) {
            Supplier p = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
            if (p != null) {
                Contract contract = p.getContract();
                if (contract != null) {
                    return p.getContract().changeProductCatalogID(catalogID, newCatalogID);
                }
            }
        }
        return false;
    }



    public static double getProductPrice(String name,String bnNumber,String catalogID)
    {
        Supplier s = SupplierManager.findSupplierByNameAndBN(name,bnNumber);
        for (ProductS p:s.getContract().productsIncluded) {
            if(p.getCatalogID().equals(catalogID))
                return p.getPrice();
        }
        return -1;
    }

    public static String getProductName(String name,String bnNumber,String catalogID)
    {
        Supplier s = SupplierManager.findSupplierByNameAndBN(name,bnNumber);
        for (ProductS p:s.getContract().productsIncluded) {
            if(p.getCatalogID().equals(catalogID))
                return p.getName();
        }
        return "";
    }

    public static HashMap<String,Pair<String,Double>> getGeneralProducts()
    {
        return SharedClass.getSharedClass().getProductsMap();
    }

    public static boolean stillProvided(String catalogID) {
        for (Supplier supplier:Repository.getRepository().getSupplierList() ) {
            for (ProductS product:supplier.getContract().getProductsIncluded()) {
                if(product.getCatalogID().equals(catalogID))
                    return true;
            }
        }
        return false;
    }

    public static void deleteProductFromSystem(String catalogID) {
        if(!stillProvided(catalogID)) {
            SharedClass.getSharedClass().getProductsMap().remove(catalogID);
            Repository.getRepository().getDatabaseHandler().deleteProductFromSystem(catalogID);
        }
    }

    public static double getWeightByCatalogID(String catalogID)
    {
        return SharedClass.getSharedClass().getProductsMap().get(catalogID).getSecond();
    }

    //*******DTO**********
    public static List<ProductS_DTO> getProductDTO(List<ProductS> products) {
        List<ProductS_DTO> product_dtos = new LinkedList<>();
        for (ProductS product : products) {
            product_dtos.add(convertProductToDTO(product));
        }
        return product_dtos;
    }

    public static ProductS_DTO convertProductToDTO(ProductS product) {
        return new ProductS_DTO(product.getName(), product.getCatalogID(), "" + product.getPrice(), product.getWeight());
    }



    //********Validation*******

    public static boolean validCatalogID(String catalogID, String name, String bnNumber) {
        if (catalogID == null)
            return false;
        for (ProductS product : SupplierManager.findSupplierByNameAndBN(name, bnNumber).getContract().getProductsIncluded()) {
            if (product.getCatalogID().equals(catalogID))
                return true;
        }
        return false;
    }

    public static boolean validProducts(List<ProductS_DTO> productsIncluded) {
        for (ProductS_DTO product_dto : productsIncluded)
            if (!validProductID(product_dto.getCatalogID()))
                return false;
        return true;
    }

    public static boolean validProductID(String id) {
        for (Supplier_DTO supplier_dto : SupplierManager.getSupplierDTOList())
            for (ProductS_DTO product : supplier_dto.getContract().getProductsIncluded())
                if (product.getCatalogID().equals(id))
                    return false;
        return true;
    }

    public static boolean validProductName(String name) {
        for (Supplier_DTO supplier_dto : SupplierManager.getSupplierDTOList())
            for (ProductS_DTO product : supplier_dto.getContract().getProductsIncluded())
                if (product.getName().equals(name))
                    return false;
        return true;
    }


    public static boolean validateBeforeDeleteProduct(Contract c, String catalogID)
    {
        String[] fixedDayDel = c.getFixedDayDelivery();
        for(int i=0 ; i < fixedDayDel.length ; i++)
        {
            Order o = Repository.getRepository().getOrdersByID().get(Integer.parseInt(fixedDayDel[i]));
            if(o!=null) {
                for (String[] productCatalog : o.getProducts()) {
                    if (productCatalog[0].equals(catalogID))
                        return false;
                }
            }
        }
        return true;
    }


    //******************DAL***********************
    public static ProductS convertDalProduct(DALProductS dalProduct) {
        return new ProductS(dalProduct.getName(),dalProduct.getCatalogID(),Double.parseDouble(dalProduct.getPrice()),dalProduct.getWeight());
    }


    /**
     * added functions needed for shipments
     */

    public static boolean checkValidQuantity(int quantity) {
        return quantity>0;
    }

    public static boolean checkCatalogID(String catalogID) {
        return SharedClass.getSharedClass().getProductsMap().containsKey(catalogID);
    }


    //TODO: implement this method
    public static double getProductWeightByID(String catalogID) {
        Map<String, Pair<String,Double>> products= SharedClass.getSharedClass().getProductsMap();
        return products.get(catalogID).getSecond();
    }

    public static String getProductNameByCatalogID(String catalogID) {
        Map<String, Pair<String,Double>> products= SharedClass.getSharedClass().getProductsMap();
        return products.get(catalogID).getFirst();
    }
}
