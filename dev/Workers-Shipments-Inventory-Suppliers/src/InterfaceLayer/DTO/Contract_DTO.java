package InterfaceLayer.DTO;
import InterfaceLayer.OrderService;

import java.util.List;

public class Contract_DTO {
    //private String fixedDayDelivery;
    public String[] fixedDayDelivery;
    private List<ProductS_DTO> productsIncluded;
    private String xDiscout;

    public Contract_DTO(String[] fixedDayDelivery, List<ProductS_DTO> productsIncluded, String xDiscout) {
        this.fixedDayDelivery = fixedDayDelivery;
        this.productsIncluded = productsIncluded;
        this.xDiscout = xDiscout;
    }

    public String[] getFixedDayDelivery() {
        return fixedDayDelivery;
    }


    public void setFixedDayDelivery(String[] fixedDayDelivery) {
        this.fixedDayDelivery = fixedDayDelivery;
    }

    public List<ProductS_DTO> getProductsIncluded() {
        return productsIncluded;
    }

    public void setProductsIncluded(List<ProductS_DTO> productsIncluded) {
        this.productsIncluded = productsIncluded;
    }

    public String getxDiscout() {
        return xDiscout;
    }

    public void setxDiscout(String xDiscout) {
        this.xDiscout = xDiscout;
    }


    public String toString(String bn) {
        return "   Permanent orders: " + permanentOrdersAsString() + "\n\t\t   " + "Minimum Discount Per Order: " + xDiscout
        + "\n\t\t   Products Included: " + getProductsForView(bn);

    }

    private String getProductsForView(String bn){
        String out="";
        for (ProductS_DTO p:productsIncluded) {
            out+=p.toString(bn);
        }
        return out;
    }

    public String permanentOrdersAsString()
    {
        String output="";
        for (int i=1;i<this.fixedDayDelivery.length;i++) {
            if(!fixedDayDelivery[i].equals("0")) {
                int day = i;
                int orderID=Integer.parseInt(fixedDayDelivery[i]);
                switch (day) {
                    case 1: {
                        output += "\t\t\t\t\t\t\t\tSunday : "+ OrderService.getOrderDTOByID(orderID)+"\n";
                        break;
                    }
                    case 2: {
                        output += "\t\t\t\t\t\t\t\tMonday : "+OrderService.getOrderDTOByID(orderID)+"\n";
                        break;
                    }
                    case 3: {
                        output += "\t\t\t\t\t\t\t\tTuesday : "+OrderService.getOrderDTOByID(orderID)+"\n";
                        break;
                    }
                    case 4: {
                        output += "\t\t\t\t\t\t\t\tWensday : "+OrderService.getOrderDTOByID(orderID)+"\n";
                        break;
                    }
                    case 5: {
                        output += "\t\t\t\t\t\t\t\tThursday : "+OrderService.getOrderDTOByID(orderID)+"\n";
                        break;
                    }
                    case 6: {
                        output += "\t\t\t\t\t\t\t\tFriday : "+OrderService.getOrderDTOByID(orderID)+"\n";
                        break;
                    }
                    case 7: {
                        output += "\t\t\t\t\t\t\t\tSaturday : "+OrderService.getOrderDTOByID(orderID);
                        break;
                    }
                }
            }

        }
        if(output.equals(""))
            output="None";
        else
            output="\n"+output;
        return  output;
    }
}
