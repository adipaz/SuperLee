package BusinessLayer.SuppliersAndInventory;
import DataAccessLayer.DALDTO.DALContact;
import InterfaceLayer.DTO.Contact_DTO;

import java.util.LinkedList;
import java.util.List;

public  class ContactManager {
    public static void init_Contacts(List<Contact_DTO> contact_dtoList, Supplier supplier) {
        for (Contact_DTO contact : contact_dtoList) {
            supplier.addNewContact(contact);
        }
    }

    public static boolean AddNewContact(String name, String bnNumber, Contact_DTO newContact) {
        boolean succeed = false;
        Supplier supplier = SupplierManager.findSupplierByNameAndBN(name, bnNumber);

        if (supplier != null) {
            if (validContact(newContact)) {
                supplier.addNewContact(newContact);
                Repository.getRepository().getDatabaseHandler().addNewContact(new DALContact(newContact.getName(),newContact.getEmail(),newContact.getPhone()),bnNumber);
                succeed = true;
            }

        }

        return succeed;
    }


    public static boolean DeleteContact(String name, String bnNumber, Contact_DTO oldContact) {
        boolean succeed = false;
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            if (supplier.getName().equals(name) && supplier.getBnNumber().equals(bnNumber) && !succeed) {
                succeed = supplier.deleteContact(oldContact);
                if(succeed) Repository.getRepository().getDatabaseHandler().deleteSupplierContact(bnNumber,new DALContact(oldContact.getName(),oldContact.getEmail(),oldContact.getPhone()));
            }
        }

        return succeed;
    }

    public static boolean editSupplierContact(Contact_DTO contactDto, String suppliername, String supplierbnnumber, Contact_DTO newconcat) {
        boolean succeed = false;
        if (validContact(newconcat)) {
            for (Supplier supplier : Repository.getRepository().getSupplierList()) {
                if (supplier.getName().equals(suppliername) && supplier.getBnNumber().equals(supplierbnnumber) && !succeed) {
                    for (Contact contact : supplier.getContacts()) {
                        if (contact.getName().equals(contactDto.getName()) && contact.getEmail().equals(contactDto.getEmail()) && contact.getPhone().equals(contactDto.getPhone()) && !succeed) {
                            if (!newconcat.getName().equals("")) contact.setName(newconcat.getName());
                            if (!newconcat.getEmail().equals("")) contact.setEmail(newconcat.getEmail());
                            if (!newconcat.getPhone().equals("")) contact.setPhone(newconcat.getPhone());
                            Repository.getRepository().getDatabaseHandler().updateSupplierContact(new DALContact(contactDto.getName(),contactDto.getEmail(),contactDto.getPhone()),new DALContact(contact.getName(),contact.getEmail(),contact.getPhone()),supplierbnnumber);
                            succeed = true;
                        }
                    }
                }
            }
        }
        return succeed;
    }

    //********DTO*********

    public static List<Contact_DTO> getContactDTO(List<Contact> contacts) {
        List<Contact_DTO> contactDtos = new LinkedList<>();
        for (Contact contact : contacts) {
            contactDtos.add(convertContactToDTO(contact));
        }
        return contactDtos;
    }

    public static Contact_DTO convertContactToDTO(Contact contact) {
        return new Contact_DTO(contact.getName(), contact.getEmail(), contact.getPhone());
    }

    //********Validation**********
    public static boolean validContacts(List<Contact_DTO> contacts) {
        for (Contact_DTO contact : contacts) {
            if (!validContact(contact))
                return false;
        }
        return true;
    }

    public static boolean validContact(Contact_DTO contact) {
        if (!contact.getEmail().equals("") && !validEmail(contact.getEmail()))
            return false;
        if (!contact.getName().equals("") && !SupplierManager.isStringOnlyAlphabet(contact.getName()))
            return false;
        return true;
        //return validEmail(contact.getEmail())&& isStringOnlyAlphabet(contact.getName());
    }


    public static boolean validEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    //***************DAL***************************

    public static List<Contact> convertDalContacts(List<DALContact> dalContacts) {
        List<Contact> contacts=new LinkedList<>();
        for (DALContact dalContact:dalContacts) {
            contacts.add(new Contact(dalContact.getName(),dalContact.getEmail(),dalContact.getPhone()));
        }
        return contacts;
    }
}
