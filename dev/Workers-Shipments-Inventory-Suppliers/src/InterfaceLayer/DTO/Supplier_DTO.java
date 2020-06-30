package InterfaceLayer.DTO;
import java.util.List;


public class Supplier_DTO {
    private String name;
    private String bnNumber;
    private String bankAccountNumber;
    private String paymentMethod;
    private List<Contact_DTO> contacts;
    private Contract_DTO contract;
    private String address;

    public Supplier_DTO(String name, String bnNumber, String bankAccountNumber, String paymentMethod, List<Contact_DTO> contacts, Contract_DTO contract,String address) {
        this.name = name;
        this.bnNumber = bnNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.paymentMethod = paymentMethod;
        this.contacts = contacts;
        this.contract = contract;
        this.address=address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Contact_DTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact_DTO> contacts) {
        this.contacts = contacts;
    }

    public Contract_DTO getContract() {
        return contract;
    }

    public void setContract(Contract_DTO contract) {
        this.contract = contract;
    }

    @Override
    public String toString() {


        return "Supplier name: "+name +"\n  "+ "BN Number: " + bnNumber+ "\n  Bank Account Number: "+ bankAccountNumber
                + "\n  Payment Method: " + paymentMethod +
                "\n  Contract Details: \n\t\t" + contract.toString((this.bnNumber))+  "\n Address: \n\t\t" + address + "\n  Contacts Details: \n\t\t" + contacts;
      /*  return "Supplier : {\n" +
                "\tname='" + name + '\'' +
                ",\n\tbnNumber='" + bnNumber + '\'' +
                ",\n\tbankAccountNumber='" + bankAccountNumber + '\'' +
                "'\n\tpaymentMethod='" + paymentMethod + '\'' +
                ",\n\tcontacts=" + contacts +
                ",\n\tcontract=" + contract +
                "}\n";

       */
    }
}
