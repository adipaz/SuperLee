package BusinessLayer.SuppliersAndInventory;
import InterfaceLayer.DTO.ProductS_DTO;

import java.util.List;


public class Contract {
    //public boolean[] fixedDayDelivery;
    public String[] fixedDayDelivery= new String[8];
    List<ProductS> productsIncluded;
    double xDiscount;

    public Contract(String[] fixedDayDelivery, List<ProductS> productsIncluded , double xDiscount)
    {
        this.fixedDayDelivery = fixedDayDelivery;
        this.productsIncluded = productsIncluded;
        this.xDiscount = xDiscount;
    }


    public boolean addNewProductToContract(ProductS_DTO p)
    {
       ProductS product = new ProductS(p.getName(),p.getCatalogID(),Double.parseDouble(p.getPrice()),p.getWeight());
       productsIncluded.add(product);
       return true;
    }

    public boolean deleteProductFromContract(String catalogID)
    {
        ProductS p = findProductByCatalogID(catalogID);
        if(p==null)
            return false;
        productsIncluded.remove(p);
        return true;
    }

    public ProductS findProductByCatalogID(String catalogID)
    {
        for (ProductS p: productsIncluded) {
            if(p.getCatalogID().equals(catalogID)){
                return p;
            }
        }

        return null;
    }

    public String[] getFixedDayDelivery() {
        return fixedDayDelivery;
    }

    public void setFixedDayDelivery(String[] fixedDayDelivery) {
        this.fixedDayDelivery = fixedDayDelivery;
    }

    public double getxDiscount() {
        return xDiscount;
    }

    public boolean changeProductName(String CatalogID,String newName)
    {
       ProductS p = findProductByCatalogID(CatalogID);
       if(p!=null) {
           p.setName(newName);
           return true;
       }
       return false;
    }

    public boolean changeProductPrice(String CatalogID,String newPrice)
    {
        ProductS p = findProductByCatalogID(CatalogID);
        if(p!=null) {
            p.setPrice(Double.parseDouble(newPrice));
            return true;
        }
        return false;
    }

    public boolean changeProductCatalogID(String CatalogID,String newCatalogID)
    {
        ProductS p = findProductByCatalogID(CatalogID);
        if(p!=null) {
            p.setCatalogID(newCatalogID);
            return true;
        }
        return false;
    }


    public boolean hasProductWithThisID(String catalogID){
        for (ProductS p:productsIncluded ) {
            if(p.getCatalogID().equals(catalogID))
                return true;
        }
        return false;
    }



    public void setProductsIncluded(List<ProductS> productsIncluded) {
        this.productsIncluded = productsIncluded;
    }

    public List<ProductS> getProductsIncluded() {
        return productsIncluded;
    }

    public void setxDiscount(double xDiscount) {
        this.xDiscount = xDiscount;
    }
}


