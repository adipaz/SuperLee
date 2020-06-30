package InterfaceLayer;


import BusinessLayer.SuppliersAndInventory.ContractManager;
import BusinessLayer.SuppliersAndInventory.SupplierManager;
import InterfaceLayer.DTO.Contract_DTO;
import InterfaceLayer.DTO.ProductS_DTO;
import InterfaceLayer.DTO.Supplier_DTO;

import java.util.LinkedList;

public class ContractService {

    public static boolean addExistProductToContract(String name,String bnNumber , String pname,String catalogid,String price, double weight)
    {
        ProductS_DTO product_dto = new ProductS_DTO(pname,catalogid,price,weight);
        return ContractManager.addExistProductToContract(product_dto,name,bnNumber);
    }

    public static boolean deleteProductFromContract(String name,String bnNumber,String catalogid)
    {
        return ContractManager.deleteProductFromContract(name,bnNumber,catalogid);
    }

    public static String viewContract(String name, String bnNumber) //creating string for printing method
    {
        Supplier_DTO sup=null;
        for (Supplier_DTO supplier: SupplierManager.getSupplierDTOList()) {
            if(supplier.getName().equals(name)&&supplier.getBnNumber().equals(bnNumber))
                sup=supplier;
        }
        String str="";
        if(sup!=null) {
            str = sup.getContract().toString(bnNumber);
        }

        return "\n  Contacts Details: \n\t\t"+str;
    }

    public static String getMinDiscount(String name,String bnNumber)
    {
        return String.valueOf(ContractManager.getMinDiscount(name, bnNumber));
    }

    public static boolean changeMinDiscount(String name , String bnNumber , String discount)
    {
        Supplier_DTO supplier_dto = new Supplier_DTO(name,bnNumber,"","",new LinkedList<>(),new Contract_DTO(new String[8],new LinkedList<>(),""),"");
        return ContractManager.changeMinDiscount(supplier_dto,discount);
    }

    public static boolean addNewProductToContract(String name, String bnNumber, String pname, String catalogID, String price,double weight) {
        ProductS_DTO product_dto = new ProductS_DTO(pname,catalogID,price,weight);
        return ContractManager.addNewProductToContract(product_dto,name,bnNumber);
    }
}
