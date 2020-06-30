package DataAccessLayer.DALDTO;
import java.util.List;

public class DALSupplier {
    private String name;
    private String bnNumber;
    private String bankAccountNumber;
    private String paymentMethod;
    private List<DALContact> contacts;
    private DALContract contract;
    private String address;

    public DALSupplier(String name, String bnNumber, String bankAccountNumber, String paymentMethod, List<DALContact> contacts, DALContract contract,String address) {
        this.name = name;
        this.bnNumber = bnNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.paymentMethod = paymentMethod;
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

    public List<DALContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<DALContact> contacts) {
        this.contacts = contacts;
    }

    public DALContract getContract() {
        return contract;
    }

    public void setContract(DALContract contract) {
        this.contract = contract;
    }
}
