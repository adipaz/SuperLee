package BusinessLayer.SuppliersAndInventory;
import InterfaceLayer.DTO.Contact_DTO;

import java.util.List;


public class Supplier {
    
    enum paymentMethod { cheque, cash, transfer}
    private String name;
    private String bnNumber;
    private String bankAccountNumber;
    private paymentMethod paymentmethod;
    private List<Contact> contacts;
    private Contract contract;
    private String address;

    public Supplier(String name,String bnNumber,String bankAccountNumber,paymentMethod paymentMethod,List<Contact> contacts,Contract contract,String address)
    {
        this.name=name;
        this.bnNumber=bnNumber;
        this.bankAccountNumber=bankAccountNumber;
        this.paymentmethod = paymentMethod;
        this.contacts = contacts;
        this.contract = contract;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean addNewContact(Contact_DTO c)
    {
        Contact contant = new Contact(c.getName(),c.getEmail(),c.getPhone());
        contacts.add(contant);
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public paymentMethod getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(paymentMethod paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contants) {
        this.contacts = contants;
    }

    public boolean deleteContact(Contact_DTO oldContact) {
        Contact contact=new Contact(oldContact.getName(),oldContact.getEmail(),oldContact.getPhone());
        for (Contact con:this.contacts) {
            if(con.equals(contact))
                return this.contacts.remove(con);
        }
        return false;
    }

}
