package BusinessLayer.SuppliersAndInventory;

import BusinessLayer.Shipments.ShipmentManager;
import BusinessLayer.Workers.Branch;
import BusinessLayer.Workers.BranchManager;
import DataAccessLayer.DALDTO.DALOrder;
import DataStructures.Pair;
import InterfaceLayer.DTO.Order_DTO;
import InterfaceLayer.OrderService;
//import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderManager {

    public static Map<Integer,String> printAddressesToMenu()
    {
        Map<Integer,String> addresses = new HashMap<>();
        Map<String, Branch> branches = BranchManager.getInstance().getbranchMap();
        int i=1;
        for(Branch b : branches.values())
        {
            addresses.put(i , b.getAddress());
            i++;
        }

        return addresses;
    }

    public static void sendWeeklyOrders()
    {
        int day = SharedClass.getSharedClass().getDAY();
        for (Supplier s : Repository.getRepository().getSupplierList())
        {
            Contract c = s.getContract();
            String[] fixedDays = c.fixedDayDelivery;
            if(!fixedDays[day].equals("0"))
            {
                Order order = Repository.getRepository().getOrdersByID().get(Integer.parseInt(fixedDays[day]));
              OrderService.printOrders("Order passed to Shipment\n" + order.toString());
              sendOrderToShipment(order);
            }
        }

    }

    public static void sendOrderToShipment(Order order)
    {
        String supplieraddress = SupplierManager.findSupplierByBN(order.getBnNumber()).getAddress();
        Map<ProductForShipment,Integer> productsMap = new HashMap<>();

        for(String[] productDetails : order.getProducts())
        {
            ProductForShipment productForShipment = new ProductForShipment(productDetails[1],productDetails[0], ProductSManager.getProductWeightByID(productDetails[0]));
            Integer amount = Integer.parseInt(productDetails[2]);
            productsMap.put(productForShipment,amount);
        }

        Map<String , Map<ProductForShipment,Integer>> finalOrder = new HashMap<>();
        finalOrder.put(order.getAdress(),productsMap);

        ShipmentRequest shipmentRequest = new ShipmentRequest(supplieraddress,finalOrder,order.getBnNumber());

        ShipmentManager.getInstance().addRequestForShipment(shipmentRequest);
    }

    public static boolean addNewOrder(List<Pair<String,Integer>> catalogXamount , String address) throws SQLException //orders from inventory
    {
        SupplierManager.loadRepository();
        HashMap<String,List<String[]>> ordersWithrepeats = new HashMap<>(); //List<BusinessLayer.Pair<supplierBnNumber,Orderdetails>>

        for (Pair<String,Integer> product :catalogXamount) {
            Pair<String,String[]> deal = findBestDeal(product);
            if(ordersWithrepeats.containsKey(deal.getFirst()))
                ordersWithrepeats.get(deal.getFirst()).add(deal.getSecond());
            else {
                List<String[]> products_deatils = new LinkedList<>();
                products_deatils.add(deal.getSecond());
                ordersWithrepeats.put(deal.getFirst(),products_deatils);
            }
        }

        List<Order> finalOrders = mergeOrdersFromSameSupp(ordersWithrepeats,address);
        if(finalOrders.equals(null))
            return false;
        for (Order o: finalOrders)
        {
            OrderService.printOrders("Order Sent to Shipment! \n"+o.toString());
            sendOrderToShipment(o);
        }
        return true;


    }

    public static List<Pair<String,String>> printperOrder(String name, String bnNumber)
    {
        List<Pair<String,String>> ordersTostring = new LinkedList<>();
        Supplier s = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        Contract c = s.getContract();
        String[] fixedDays = c.fixedDayDelivery;
        for(int i=0; i<fixedDays.length ; i++)
        {
            if(!fixedDays[i].equals("0"))
            {
                Order o = Repository.getRepository().getOrdersByID().get(Integer.parseInt(fixedDays[i]));
                if(!(o==null))
                {
                    Pair<String,String> orderDetails = new Pair<>(o.getOrderID(),o.toString());
                    ordersTostring.add(orderDetails);
                }
            }
        }

        return ordersTostring;
    }


    public static boolean changePerOrderAdress(String adress,String orderID)
    {
        Order order = Repository.getRepository().getOrdersByID().get(Integer.parseInt(orderID));
        order.setAdress(adress);
        Repository.getRepository().getDatabaseHandler().updatePerOrderAddress(converOrderToDAL(order),adress);
        return true;
    }

    public static boolean addProductToOrder(String orderID,String[] newProduct)
    {
        Order o = Repository.getRepository().getOrdersByID().get(Integer.parseInt(orderID));
        String catalogID=newProduct[0];
        o.getProducts().add(newProduct);
        o= mergeIfSameProducts(o);
        String amount="0";
        for (String[] p:o.getProducts()) {
            if(p[0].equals(catalogID))
                amount=p[2];
        }

        Repository.getRepository().getDatabaseHandler().addProductToPerOrder(o.getOrderID(),catalogID,amount,o.getBnNumber());
        return true;
    }

    public static boolean deleteProductFromOrder(String orderID,String catalogID)
    {
        Order order = Repository.getRepository().getOrdersByID().get(Integer.parseInt(orderID));
        List<String[]> products =order.getProducts();
        for(String[] productInfo : products)
        {
            if(productInfo[0].equals(catalogID))
            {
                products.remove(productInfo);
                Repository.getRepository().getDatabaseHandler().deleteProductFromPerOrder(orderID ,catalogID);
                if(products.isEmpty()) {
                    deletePerOrder(order.getBnNumber(), order.getSupplierName(), orderID);
                }
                return true;
            }
        }
        return false;
    }

    public static  boolean moveWeeklyOrderToDiffDay(String name,String bnNumber,String orderID,String newday)
    {
        Supplier s =SupplierManager.findSupplierByNameAndBN(name,bnNumber);
        Order o = Repository.getRepository().getOrdersByID().get(Integer.parseInt(orderID));
        Contract c = s.getContract();
        String[] days = c.getFixedDayDelivery();
        int oldday=0;
        for(int i=0;i<days.length;i++)
        {
            if(days[i].equals(orderID))
                oldday=i;
        }
        if(oldday==0)
            return false;
        int Nday = Integer.parseInt(newday);
        if(!days[Nday].equals("0"))
        {
            return false;
        }
        else
        {
            days[oldday] = "0";
            days[Nday] = orderID;

            Repository.getRepository().getDatabaseHandler().updatePerOrderDate(converOrderToDAL(o),newday,String.valueOf(oldday));

            return true;
        }

    }

    public static boolean changePerOrderPhone(String Phone,String orderID)
    {
        Order order = Repository.getRepository().getOrdersByID().get(Integer.parseInt(orderID));
        order.setPhone(Phone);
        Repository.getRepository().getDatabaseHandler().updatePerOrderPhoneNumber(converOrderToDAL(order),Phone);

        return true;
    }

    private static List<Order> mergeOrdersFromSameSupp(HashMap<String,List<String[]>> ordersWithRepeats,String address)
    {
        List<Order> orders = new LinkedList<>();
        for (String sBN:ordersWithRepeats.keySet())
        {
            Supplier sup = SupplierManager.findSupplierByBN(sBN);
            List<String[]> products = ordersWithRepeats.get(sup.getBnNumber());
            String phone = "the supplier doesnt have any contacts";
            if(!sup.getContacts().isEmpty())
            {
                phone = sup.getContacts().get(0).getPhone();
            }
            Order neworder = new Order(sup.getName(),sup.getBnNumber(),address,Repository.getRepository().getToday(),phone,products,String.valueOf(Repository.getRepository().getORDERSCOUNTER())); //TODO
            incORDERCOUNTER();
            orders.add(neworder);
        }
        return orders;
    }

    private static Pair<String,String[]> findBestDeal(Pair<String,Integer> product)
    {
        double minPrice=Integer.MAX_VALUE;
        String BnNumber = "";
        List<Double> bestDeal = null;

        for (Supplier s:Repository.getRepository().getSupplierList())
        {
            for(ProductS p : s.getContract().productsIncluded)
            {
                if(p.getCatalogID().equals(product.getFirst()))
                {
                    //original price -> discount -> final price
                    if(getOrderPrices(s.getBnNumber(),s.getName(),p.getCatalogID(),String.valueOf(product.getSecond())).get(2)<minPrice)
                    {
                        BnNumber = s.getBnNumber();
                        bestDeal = getOrderPrices(s.getBnNumber(),s.getName(),p.getCatalogID(),String.valueOf(product.getSecond()));
                    }
                }
            }
        }

        String[] bestDealArr = new String[6]; //catalogid->name->amount->original price -> discount -> final price
        bestDealArr[0] = product.getFirst();
        bestDealArr[1] = SharedClass.getSharedClass().getProductsMap().get(product.getFirst()).getFirst();
        bestDealArr[2] = String.valueOf(product.getSecond());
        bestDealArr[3] = String.valueOf(bestDeal.get(0));
        bestDealArr[4] = String.valueOf(bestDeal.get(1));
        bestDealArr[5] = String.valueOf(bestDeal.get(2));

        return new Pair<>(BnNumber,bestDealArr);
    }

    public static boolean addPerOrder(String bnNumber, String name, Order_DTO orderDto)
    {

        Supplier s = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        if(s.equals(null))
            return false;
        Contract c = s.getContract();
        if(c.equals(null))
            return false;
        if(!c.fixedDayDelivery[Integer.parseInt(orderDto.getDate())].equals("0"))
            return false;
        Order order = new Order(name,bnNumber,orderDto.getAdress(),orderDto.getDate(),orderDto.getPhone(),orderDto.getProducts(),String.valueOf(Repository.getRepository().getORDERSCOUNTER()));
        order = mergeIfSameProducts(order);
        Repository.getRepository().getOrdersByID().put(Repository.getRepository().getORDERSCOUNTER(),order);
        c.fixedDayDelivery[Integer.parseInt(orderDto.getDate())] = String.valueOf(Repository.getRepository().getORDERSCOUNTER());
        incORDERCOUNTER();
        Repository.getRepository().getDatabaseHandler().addPerOrder(converOrderToDAL(order));

        return true;
    }

    private static Order mergeIfSameProducts(Order orderDto)
    {
        List<String[]> finalList = new LinkedList<>();
        List<String[]> orderList = new LinkedList<>();
        List<String[]> currProducts = orderDto.getProducts();
        int finalamount;
      for(int i=0 ;i<currProducts.size() ; i++)
      {
         String[] currOrder = orderDto.getProducts().get(i);
         finalamount = Integer.parseInt(currOrder[2]);
         for(int j = i+1 ; j < orderDto.getProducts().size() ; j++)
         {
             String[] secOrder = orderDto.getProducts().get(j);
             if(secOrder[0].equals(currOrder[0]))
             {
                 finalamount += Integer.parseInt(secOrder[2]);
                 secOrder[0] = "";
             }
         }

         List<Double> prices = getOrderPrices(orderDto.getBnNumber(),orderDto.getSupplierName(),currOrder[0],String.valueOf(finalamount));
         String[] details = {currOrder[0],currOrder[1],String.valueOf(finalamount),String.valueOf(prices.get(0)),String.valueOf(prices.get(1)),String.valueOf(prices.get(2))};
         finalList.add(details);
      }

      for(int i=0 ; i < finalList.size() ; i++)
      {
          if(!finalList.get(i)[0].equals(""))
              orderList.add(finalList.get(i));
      }

      orderDto.setProducts(orderList);
      return orderDto;
    }


    public static List<Double> getOrderPrices(String bnNumber, String name,String catalogID,String amountS)
    {
        List<Double> prices = new LinkedList<>(); //original price -> discount -> final price
        int amount = Integer.parseInt(amountS);
        double pricePerUnit = ProductSManager.getProductPrice(name,bnNumber,catalogID);
        double minDiscount = ContractManager.getMinDiscount(name, bnNumber);
        double percent=0;
        if(percent==0) {
            if (amount < 500) {
                percent = 0;
            }
            if (amount >= 500 && amount < 1000) {
                percent = minDiscount;
            }
            if (amount >= 1000 && amount < 1500) {
                percent = 5 + minDiscount;
            }
            if (amount >= 1500 && amount < 2000) {
                percent = 10 + minDiscount;
            }
            if (amount >= 2000) {
                percent = 15 + minDiscount;
            }
        }

        prices.add(pricePerUnit*amount); //original price
        prices.add((percent*prices.get(0))/100); //discount
        prices.add(prices.get(0)-prices.get(1)); //finite price

        return prices;
    }

    private static void incORDERCOUNTER()
    {
        Repository.getRepository().setORDERSCOUNTER(Repository.getRepository().getORDERSCOUNTER()+1);
        Repository.getRepository().getDatabaseHandler().updateORDERCOUNTER(Repository.getRepository().getORDERSCOUNTER());
    }

    public static boolean deletePerOrder(String bnNumber,String name,String orderID)
    {
        Supplier s = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        String[] fixedDays = s.getContract().fixedDayDelivery;
        for(int i=0; i < fixedDays.length ; i++)
        {
            if(fixedDays[i].equals(orderID))
                fixedDays[i] = "0";
        }
        Order o = Repository.getRepository().getOrdersByID().get(Integer.parseInt(orderID));
        Repository.getRepository().getOrdersByID().remove(Integer.parseInt(orderID));
        Repository.getRepository().getDatabaseHandler().deletePermanentOrder(converOrderToDAL(o));
        return true;
    }



    //*******DTO**********
    public static Order_DTO getOrderDTOByID(int orderID){
        Order order=Repository.getRepository().getOrdersByID().get(orderID);
        Order_DTO order_dto=new Order_DTO(order.getSupplierName(),order.getBnNumber(),order.getAdress(),order.getDate(),order.getPhone(),order.getProducts());
        return order_dto;
    }

    //****************DAL*******************


    private static DALOrder converOrderToDAL(Order order) { //catalogid->name->amount->original price -> discount -> final price
        List<String[]> productsDetails = order.getProducts();
        HashMap<String,Pair<String,String>> productsForDAL = new HashMap();
        for(String[] product : productsDetails)
        {
            Pair<String,String> pair = new Pair<>(product[0],product[2]); //catalogID,amount
            productsForDAL.put(product[0],pair);
        }
        return new DALOrder(order.getSupplierName(),order.getBnNumber(),order.getAdress(),order.getDate(),order.getPhone(),productsForDAL,order.getOrderID());
    }


    public static void convertDalOrderMap(Map<String, DALOrder> dalOrderMap) {
        for (DALOrder dalOrder:dalOrderMap.values()) {
            Repository.getRepository().getOrdersByID().put(Integer.parseInt(dalOrder.getOrderID()),convertDalOrder(dalOrder));
        }
    }

    private static Order convertDalOrder(DALOrder dalOrder) {
        return new Order(dalOrder.getSupplierName(),dalOrder.getBnNumber(),dalOrder.getAdress(),dalOrder.getDate(),dalOrder.getPhone(),convertDalProductsInOrder(dalOrder.getProducts(),dalOrder.getBnNumber()),dalOrder.getOrderID());
    }

    private static List<String[]> convertDalProductsInOrder(HashMap<String, Pair<String, String>> dalProducts,String bnNumber) {
        List<String[]> productInOrder=new LinkedList<>();
        for (Pair<String,String> cat_amount:dalProducts.values()) {
            String catalogID=cat_amount.getFirst();
            String amount=cat_amount.getSecond();
            String name=SharedClass.getSharedClass().getProductsMap().get(cat_amount.getFirst()).getFirst();
            String origprice=Double.toString(ProductSManager.getProductPrice(SupplierManager.findSupplierByBN(bnNumber).getName(),bnNumber,catalogID)* Double.valueOf(amount));
            String discount=Double.toString(SupplierManager.findSupplierByBN(bnNumber).getContract().getxDiscount()*Double.valueOf(origprice)/100.0);
            String finalprice=Double.toString(Double.parseDouble(origprice)-Double.parseDouble(discount));
            String[] product={catalogID,name,amount,origprice,discount,finalprice};
            productInOrder.add(product);
        }

        return productInOrder;
    }
}


