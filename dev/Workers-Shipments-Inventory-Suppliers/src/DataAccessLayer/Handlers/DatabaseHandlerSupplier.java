package DataAccessLayer.Handlers;



import DataAccessLayer.DALDTO.*;
import DataAccessLayer.SQLiteJDBC;
import DataStructures.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseHandlerSupplier {
    private SQLiteJDBC s;

    public DatabaseHandlerSupplier() {
        s = new SQLiteJDBC();
    }


    public Map<String,Pair<String,Double>> getProductsTable() throws SQLException {
        ResultSet rs = s.selectAll("product");
        Map<String,Pair<String,Double>> ret = new HashMap<>();

        while (rs.next()) {
            String catalogID = (String)rs.getObject(1);
            String name = (String) rs.getObject(2);
            Double weight= rs.getDouble(3);
            ret.put(catalogID,new Pair<>(name,weight));
        }
        return ret;

    }

    public void updatePerOrderAddress(DALOrder dalOrder, String newAddress) {s.updatePerOrderAddress(dalOrder, newAddress);}

    public void updatePerOrderPhoneNumber(DALOrder dalOrder, String phone) {s.updatePerOrderPhoneNumber(dalOrder, phone);}

    public void updatePerOrderDate(DALOrder dalOrder,String newday,String oldday) {s.updatePerOrderDate(dalOrder, newday, oldday);}

    public void deleteProductFromPerOrder(String orderID , String catalogID) {s.deleteProductFromPerOrder(orderID, catalogID);}

    public void addNewSupplier(DALSupplier dalSupplier) {
        s.addNewSupplier(dalSupplier);
    }

    public void addNewProduct(DALProductS dalProductS, String bnNumber)
    {
        s.addNewProduct(dalProductS, bnNumber);
    }

    public void addExistProduct(DALProductS dalProductS, String bnNumber) { s.addExistProduct(dalProductS,bnNumber);}

    public void updateSupplierName(String bnNumber, String newName) { s.updateSupplierName(bnNumber,newName);}

    public void updateSupplierBnNumer(String oldBnNumber, String newBnNumber) { s.updateSupplierBnNumber(oldBnNumber,newBnNumber);}

    public void updateSupplierBankAccountNumber(String bnNumber, String newBankAccountNumber) { s.updateSupplierBankAccountNumber(bnNumber,newBankAccountNumber);}

    public void updatePaymentMethod(String bnNumber, String newPaymentMethod) { s.updatePaymentMethod(bnNumber,newPaymentMethod);}

    public void addNewContact(DALContact dalContact, String bnNumber) { s.addNewContact(dalContact,bnNumber);}

    public void updateSupplierContact(DALContact oldContact, DALContact newContact, String bnNumber) { s.updateSupplierContact(oldContact,newContact,bnNumber);}

    public void updateMinDiscount(String bnNumber, String newDiscount) { s.updateMinDiscount(bnNumber,newDiscount);}

    public void updateProductPrice(String bnNumber, String catalogID, String newPrice) { s.updateProductPrice(bnNumber,catalogID,newPrice);}

    public void addPerOrder(DALOrder dalOrder) { s.addPerOrder(dalOrder); }

    public void deleteSupplier(String bnNumber) { s.deleteSupplier(bnNumber);}

    public void deleteProductFromSystem(String catalogID) { s.deleteProductFromSystem(catalogID);}

    public void deleteSupplierContact(String bnNumber, DALContact dalContact) { s.deleteSupplierContact(bnNumber,dalContact);}

    public void deleteProductFromContract(String bnNumber, String catalogID) { s.deleteProductFromContract(bnNumber,catalogID);}

    public void deletePermanentOrder(DALOrder order) { s.deletePermanentOrder(order);}

    public void addProductToPerOrder(String orderID, String catalogID,String quantity,String bnNumber){s.addProductToPerOrder(orderID, catalogID, quantity, bnNumber);}

    public DALRepository loadRepository() throws SQLException {
        //supplier list
        ResultSet rs = s.selectAll("Supplier");
        List<DALSupplier> dalSupplierList=new LinkedList<>();
        while (rs.next()) {
            //primitive supplier fields
            String bnNumber=(String) rs.getObject("bnNumber");
            String name=(String) rs.getObject("name");
            String bankAccountNumber=(String) rs.getObject("bankAccountNumber");
            String paymentMethod=(String) rs.getObject("paymentMethod");
            String address = (String) rs.getObject("address");
            //supplier's contacts
            List<DALContact> dalContactList=getSupplierContacts(bnNumber);
            //supplier's contract
            DALContract dalContract=getSupplierContract(bnNumber);

            dalSupplierList.add(new DALSupplier(name,bnNumber,bankAccountNumber,paymentMethod,dalContactList,dalContract,address));
        }

        //orders map
        rs = s.selectAll("Orders");
        Map<String,DALOrder> dalOrderMap=new HashMap<>();
        while (rs.next()) {
            //primitive order fields
            String orderID=(String) rs.getObject("orderID");
            String supplierName=(String) rs.getObject("name");
            String bnNumber=(String) rs.getObject("bnNumber");
            String address=(String) rs.getObject("address");
            String date=(String) rs.getObject("orderDate");
            String phone=(String) rs.getObject("phone");
            //product in order
            HashMap<String, Pair<String,String>> dalProducts=getProductInOrder(orderID);

            dalOrderMap.put(orderID,new DALOrder(supplierName,bnNumber,address,date,phone,dalProducts,orderID));
         }

        DALRepository dalRepository=new DALRepository(dalSupplierList,dalOrderMap);
        return dalRepository;
    }

    private HashMap<String,Pair<String,String>> getProductInOrder(String orderID) throws SQLException {
        ResultSet rs = s.selectAll("productInOrder");
        HashMap<String,Pair<String,String>> dalProducts=new HashMap<>();
        while (rs.next()) {
            if(((String) rs.getObject("orderID")).equals(orderID)) {
                dalProducts.put((String) rs.getObject("catalogID"),new Pair<>((String) rs.getObject("catalogID"),(String) rs.getObject("quantity")));
            }
        }
        return dalProducts;
    }

    private List<DALContact> getSupplierContacts(String bnNumber) throws SQLException {
        ResultSet rs = s.selectAll("Contact");
        List<DALContact> dalContactList=new LinkedList<>();
        while (rs.next()) {
            if(((String) rs.getObject("bnNumber")).equals(bnNumber)) {
                String name=(String) rs.getObject("name");
                String email=(String) rs.getObject("email");
                String phone=(String) rs.getObject("phone");
                dalContactList.add(new DALContact(name,email,phone));
            }
        }
        return dalContactList;
    }

    private DALContract getSupplierContract(String bnNumber) throws SQLException {
        ResultSet rs = s.selectAll("Contract");
        DALContract dalContract=null;
        while (rs.next()) {
            if(((String) rs.getObject("bnNumber")).equals(bnNumber)) {
                String xDiscount=(String) rs.getObject("xDiscount");
                String[] fixedDaysOrders={"0",(String) rs.getObject("sunday")
                        ,(String) rs.getObject("monday")
                        ,(String) rs.getObject("tuesday")
                        ,(String) rs.getObject("wensday")
                        ,(String) rs.getObject("thursday")
                        ,(String) rs.getObject("friday")
                        ,(String) rs.getObject("saturday")};
                List<DALProductS> dalProductSList= getProductInContract(bnNumber);
                dalContract=new DALContract(fixedDaysOrders,dalProductSList,xDiscount);
            }
        }
        return dalContract;
    }

    private List<DALProductS> getProductInContract(String bnNumber) throws SQLException {
        ResultSet rs = s.selectAll("productInContract");
        List<DALProductS> dalProductSList=new LinkedList<>();
        while (rs.next()) {
            if(((String) rs.getObject("bnNumber")).equals(bnNumber)) {
                String catalogID=(String) rs.getObject("catalogID");
                String name=(String) rs.getObject("name");
                String price=(String) rs.getObject("price");
                double weight= rs.getDouble("weight");      //need to check it
                dalProductSList.add(new DALProductS(name,catalogID,price,weight));
            }
        }
        return dalProductSList;
    }


    public int loadOrderCounter() throws SQLException {
        ResultSet rs = s.selectAll("orderCounter");
        int counter=0;
        while (rs.next()) {
            counter=(Integer) rs.getObject(1);
        }
        return counter;
    }

    public void insertCounter() { s.insertCounter();}

    public void updateORDERCOUNTER(int orderscounter) {
        s.updateOrderCounter(orderscounter);
    }
}
