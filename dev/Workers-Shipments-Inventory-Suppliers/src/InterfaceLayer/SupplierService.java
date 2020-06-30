package InterfaceLayer;

import BusinessLayer.SuppliersAndInventory.SupplierManager;
import InterfaceLayer.DTO.Contact_DTO;
import InterfaceLayer.DTO.Contract_DTO;
import InterfaceLayer.DTO.ProductS_DTO;
import InterfaceLayer.DTO.Supplier_DTO;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SupplierService {

    public static int getNumOfSupp()
    {
        return SupplierManager.NumberOfSupp();
    }

    public static boolean changeSupplierName(String oldname,String bnNumber, String newname)
    {
        Supplier_DTO newSupplier = new Supplier_DTO(newname,"","","",new LinkedList<>(),new Contract_DTO(new String[8],new LinkedList<>(),""),"");
        Supplier_DTO oldSupplier =  new Supplier_DTO(oldname,bnNumber,"","",new LinkedList<>(),new Contract_DTO(new String[8],new LinkedList<>(),""),"");
        return SupplierManager.changeSupplierName(oldSupplier,newSupplier);
    }


    public static boolean AddSupplier(String name, String bnNumber, String bankAccountNumber, String paymentMethod, List<List<String>> contacts, List<List<String>> products, String[] fixeddays, String xDiscount,String address)
    {
        List<ProductS_DTO> list_products = new LinkedList<>();
        for (List<String> semiProduct: products)
        {
            String pname = semiProduct.get(0);
            String catalogID = semiProduct.get(1);
            String price = semiProduct.get(2);
            String weight= semiProduct.get(3);      //This need to be added

            ProductS_DTO product_dto = new ProductS_DTO(pname,catalogID,price,Double.parseDouble(weight));
            list_products.add(product_dto);
        }

        Contract_DTO contract_dto = new Contract_DTO(fixeddays,list_products,xDiscount);

        List<Contact_DTO> contact_dtos = new LinkedList<>();
        for (List<String> contant : contacts)
        {
            String cname = contant.get(0);
            String email = contant.get(1);
            String phone= contant.get(2);
            Contact_DTO contant_dto = new Contact_DTO(cname,email,phone);
            contact_dtos.add(contant_dto);
        }

        Supplier_DTO supplier_dto = new Supplier_DTO(name,bnNumber,bankAccountNumber,paymentMethod,contact_dtos,contract_dto,address);
        if(SupplierManager.addSupplier(supplier_dto))
            return true;
        else return false;
    }

    public static String viewForEdit()
    {
        String output="";
        int i=1;
        for (Supplier_DTO supplier:SupplierManager.getSupplierDTOList()) {
            output+=""+i+")" + " Name: "+ supplier.getName() + " BN Number: " + supplier.getBnNumber()+"\n";
            i++;
        }
        return output;
    }

    public static String viewSuppliers() //creating string for printing method
    {
        String output="";
        int i=1;
        for (Supplier_DTO supplier:SupplierManager.getSupplierDTOList()) {
            output+=""+i+"."+supplier.toString()+"\n";
            i++;
        }
        if(output=="")
            output += "NO SUPPLIERS EXIST" +"\n";
        return output;
    }


    public static boolean changeSupplierBnNumber(String name,String oldBnNumber, String newBnNumber) {
        return SupplierManager.changeSupplierBnNumber(name,oldBnNumber,newBnNumber);
    }

    public static boolean changeSupplierBankAccountNumber(String name, String bnNumber, String newBankAccountNumber) {
        return SupplierManager.changeSupplierBankAccountNumber(name,bnNumber,newBankAccountNumber);
    }

    public static boolean changeSupplierPaymentMethod(String name, String bnNumber, String newPaymentMethod) {
        return SupplierManager.changeSupplierPaymentMethod(name,bnNumber,newPaymentMethod);
    }

    public static boolean DeleteSupplier(String name , String bnNumber)
    {
        Supplier_DTO supplier_dto = new Supplier_DTO(name,bnNumber,"","",new LinkedList<>(),new Contract_DTO(new String[8],new LinkedList<>(),""),"");
        return SupplierManager.DeleteSupplier(supplier_dto);
    }

    public static void loadRepository() throws SQLException { SupplierManager.loadRepository();
    }
}
