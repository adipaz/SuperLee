package DataAccessLayer.DALDTO;


import java.util.List;

public class DALContract {
    public String[] fixedDaysOrders= new String[8];
    private List<DALProductS> productsIncluded;
    private String xDiscout;

    public DALContract(String[] fixedDaysOrders, List<DALProductS> productsIncluded, String xDiscout) {
        this.fixedDaysOrders = fixedDaysOrders;
        this.productsIncluded = productsIncluded;
        this.xDiscout = xDiscout;
    }


    public String[] getFixedDaysOrders() {
        return fixedDaysOrders;
    }

    public void setFixedDaysOrders(String[] fixedDaysOrders) {
        this.fixedDaysOrders = fixedDaysOrders;
    }

    public List<DALProductS> getProductsIncluded() {
        return productsIncluded;
    }

    public void setProductsIncluded(List<DALProductS> productsIncluded) {
        this.productsIncluded = productsIncluded;
    }

    public String getxDiscout() {
        return xDiscout;
    }

    public void setxDiscout(String xDiscout) {
        this.xDiscout = xDiscout;
    }
}
