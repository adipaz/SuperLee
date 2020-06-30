package InterfaceLayer;

import BusinessLayer.SuppliersAndInventory.ContactManager;
import BusinessLayer.SuppliersAndInventory.SupplierManager;
import InterfaceLayer.DTO.Contact_DTO;
import InterfaceLayer.DTO.Supplier_DTO;

public class ContactService {

    public static String viewSupplierContact(String name, String bnNumber) //creating string for printing method
    {
        Supplier_DTO sup=null;
        for (Supplier_DTO supplier: SupplierManager.getSupplierDTOList()) {
            if(supplier.getName().equals(name)&&supplier.getBnNumber().equals(bnNumber))
                sup=supplier;
        }
        String str="";
        if(sup!=null) {
            int i=1;
            for (Contact_DTO contact:sup.getContacts()) {
                str+=""+i+")" + " Name: " + contact.getName() + " Email: " + contact.getEmail()+ " Phone: "+ contact.getPhone() + "\n";
                i++;
            }

        }

        //if(str.equals(""))
        //str+= "THE SUPPLIER DOESNT HAVE ANY CONTACTS TO BE SHOWN" +"\n";
        return str;
    }

    public static boolean AddNewContact(String supplierName, String supplierBnNumber, String name, String email, String phone) {
        Contact_DTO newContact=new Contact_DTO(name,email,phone);
        return ContactManager.AddNewContact(supplierName,supplierBnNumber,newContact);
    }

    public static boolean DeleteContact(String supplierName, String supplierBnNumber, String name, String email, String phone) {
        Contact_DTO oldContact=new Contact_DTO(name,email,phone);
        return ContactManager.DeleteContact(supplierName,supplierBnNumber,oldContact);
    }

    public static boolean EditContact(String supplierName, String supplierBnNumber, String name, String email, String phone, String newname , String newemail, String newphone)
    {
        Contact_DTO Contact=new Contact_DTO(name,email,phone);
        Contact_DTO newDetails = new Contact_DTO(newname,newemail,newphone);

        return ContactManager.editSupplierContact(Contact,supplierName,supplierBnNumber,newDetails);
    }
}
