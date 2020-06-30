package BusinessLayer.SuppliersAndInventory;

import BusinessLayer.Workers.Branch;
import BusinessLayer.Workers.BranchManager;
import DataAccessLayer.DALDTO.DALContact;
import DataAccessLayer.DALDTO.DALContract;
import DataAccessLayer.DALDTO.DALRepository;
import DataAccessLayer.DALDTO.DALSupplier;
import InterfaceLayer.DTO.Contact_DTO;
import InterfaceLayer.DTO.Contract_DTO;
import InterfaceLayer.DTO.Supplier_DTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class SupplierManager {

    public static int NumberOfSupp() {
        return Repository.getRepository().getSupplierList().size();
    }

    public static Supplier findSupplierByNameAndBN(String name, String bnNumber) {
        for (Supplier p :
                Repository.getRepository().getSupplierList()) {
            if (p.getName().equals(name) && p.getBnNumber().equals(bnNumber))
                return p;
        }

        return null;
    }

    public static Supplier findSupplierByBN( String bnNumber) {
        for (Supplier p :
                Repository.getRepository().getSupplierList()) {
            if (p.getBnNumber().equals(bnNumber))
                return p;
        }

        return null;
    }

    public static boolean changeSupplierName(Supplier_DTO oldSupplier, Supplier_DTO newSupplier) {
        String oldname = oldSupplier.getName();
        String bnNumber = oldSupplier.getBnNumber();
        String newName = newSupplier.getName();
        if (!isStringOnlyAlphabet(newName))
            return false;
        boolean succeed = false;
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            if (supplier.getName().equals(oldname) && supplier.getBnNumber().equals(bnNumber) && !succeed) {
                supplier.setName(newName);
                Repository.getRepository().getDatabaseHandler().updateSupplierName(bnNumber,newName);
                succeed = true;
            }
        }

        return succeed;
    }

    public static boolean addSupplier(Supplier_DTO s) {
        //todo
        if (!validFieldsLegality(s))
            return false;
        String[] daysArray = s.getContract().getFixedDayDelivery();
        for(int i=0;i<daysArray.length;i++)
            daysArray[i]="0";
        if ( ProductSManager.validProducts(s.getContract().getProductsIncluded()) && validBnNumber(s.getBnNumber()) && ContactManager.validContacts(s.getContacts())&&IsUniqeAdderss(s.getAddress())) {
            List<ProductS> productList = new LinkedList<>();
            Contract contract = new Contract(daysArray, productList, Double.parseDouble(s.getContract().getxDiscout()));
            ContractManager.init_Products(contract, s.getContract());

            Supplier supplier = new Supplier(s.getName(), s.getBnNumber(), s.getBankAccountNumber()
                    , Supplier.paymentMethod.valueOf(s.getPaymentMethod()), new LinkedList<>(), contract,s.getAddress());

            ContactManager.init_Contacts(s.getContacts(), supplier);

            Repository.getRepository().getSupplierList().add(supplier);
            Repository.getRepository().getDatabaseHandler().addNewSupplier(converSupplierToDal(supplier));
            return true;
        }

        return false;
    }

    private static boolean IsUniqeAdderss(String address) {
        for (Supplier sup:Repository.getRepository().getSupplierList()) {
            if(sup.getAddress().equals(address))
                return false;
        }
        for (Branch branch:BranchManager.getInstance().getAllBranches()) {
            if(branch.getAddress().equals(address))
                return false;
        }
        return true;
    }


    public static boolean changeSupplierBnNumber(String name, String oldBnNumber, String newBnNumber) {
        if (!isStringcontainsOnlyNumbers(newBnNumber))
            return false;
        boolean succeed = false;
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            if (supplier.getName().equals(name) && supplier.getBnNumber().equals(oldBnNumber) && !succeed) {
                if (validBnNumber(newBnNumber)) {
                    supplier.setBnNumber(newBnNumber);
                    Repository.getRepository().getDatabaseHandler().updateSupplierBnNumer(oldBnNumber,newBnNumber);
                    succeed = true;
                }
            }
        }

        return succeed;
    }

    public static boolean DeleteSupplier(Supplier_DTO supplier_dto) {
        boolean succeed = false;
        for (Supplier p : Repository.getRepository().getSupplierList()) {
            if (p.getName().equals(supplier_dto.getName()) && p.getBnNumber().equals(supplier_dto.getBnNumber())) {
                succeed = Repository.getRepository().getSupplierList().remove(p);
                if(succeed) {
                    Repository.getRepository().getDatabaseHandler().deleteSupplier(supplier_dto.getBnNumber());
                    for (ProductS product:p.getContract().getProductsIncluded()) {
                        if(!ProductSManager.stillProvided(product.getCatalogID())) ProductSManager.deleteProductFromSystem(product.getCatalogID());
                    }
                }
                break;
            }

        }

        return succeed;
    }

    public static boolean changeSupplierPaymentMethod(String name, String bnNumber, String newPaymentMethod) {
        if (!validatePaymentMethod(newPaymentMethod))
            return false;
        boolean succeed = false;
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            if (supplier.getName().equals(name) && supplier.getBnNumber().equals(bnNumber) && !succeed) {
                supplier.setPaymentmethod(Supplier.paymentMethod.valueOf(newPaymentMethod));
                Repository.getRepository().getDatabaseHandler().updatePaymentMethod(bnNumber,newPaymentMethod);
                succeed = true;
            }
        }

        return succeed;
    }

    public static boolean changeSupplierBankAccountNumber(String name, String bnNumber, String newBankAccountNumber) {
        boolean succeed = false;
        if (!isStringcontainsOnlyNumbers(newBankAccountNumber))
            return false;
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            if (supplier.getName().equals(name) && supplier.getBnNumber().equals(bnNumber) && !succeed) {
                supplier.setBankAccountNumber(newBankAccountNumber);
                Repository.getRepository().getDatabaseHandler().updateSupplierBankAccountNumber(bnNumber,newBankAccountNumber);
                succeed = true;
            }
        }

        return succeed;
    }


    //**********DTO************
    public static List<Supplier_DTO> getSupplierDTOList() {
        List<Supplier_DTO> supplier_dtos = new LinkedList<>();
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            supplier_dtos.add(convertSupplierToDTO(supplier));
        }
        return supplier_dtos;
    }

    public static Supplier_DTO convertSupplierToDTO(Supplier supplier) {
        String name = supplier.getName();
        String bnNumber = supplier.getBnNumber();
        String bankAccountNumber = supplier.getBankAccountNumber();
        String paymentMethod = supplier.getPaymentmethod().toString();
        List<Contact_DTO> contacts =ContactManager.getContactDTO(supplier.getContacts());
        Contract_DTO contract = ContractManager.convertContractToDTO(supplier.getContract());

        return new Supplier_DTO(name, bnNumber, bankAccountNumber, paymentMethod, contacts, contract,supplier.getAddress());
    }

    //*********Validation******

    public static boolean validBnNumber(String bnNumber) {
        if (!isStringcontainsOnlyNumbers(bnNumber))
            return false;
        for (Supplier supplier : Repository.getRepository().getSupplierList()) {
            if (supplier.getBnNumber().equals(bnNumber))
                return false;
        }
        return true;
    }

    public static boolean validFieldsLegality(Supplier_DTO supplier) {
        if (supplier.getName() == null | supplier.getName() == "" || !isStringOnlyAlphabet(supplier.getName()))
            return false;
        if (supplier.getBnNumber() == null | supplier.getBnNumber() == "")
            return false;
        if (supplier.getBankAccountNumber() == null | supplier.getBankAccountNumber() == "" || !supplier.getBankAccountNumber().matches("^\\d+$"))
            return false;
        if (supplier.getPaymentMethod() == null | supplier.getPaymentMethod() == "")
            return false;
        if (supplier.getContract() == null)
            return false;
        return true;
    }

    public static boolean validatePaymentMethod(String method) {
        try {
            Supplier.paymentMethod.valueOf(method);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isStringOnlyAlphabet(String str) {
        return ((str != null))
                && (!str.equals("")
                && (str.matches("^[a-zA-Z]*$")));
    }


    public static boolean isStringcontainsOnlyNumbers(String str) {
        return str.matches("[0-9]+");
    }

    //**************DAL**************
    private static DALSupplier converSupplierToDal(Supplier supplier) {
        List<DALContact> dalContacts=new LinkedList<>();
        for (Contact c:supplier.getContacts()) {
            dalContacts.add(new DALContact(c.getName(),c.getEmail(),c.getPhone()));
        }
        DALContract dalContract=new DALContract(supplier.getContract().fixedDayDelivery,null,String.valueOf(supplier.getContract().xDiscount));
        DALSupplier dalSupplier=new DALSupplier(supplier.getName(),supplier.getBnNumber(),supplier.getBankAccountNumber(),supplier.getPaymentmethod().toString(),dalContacts,dalContract,supplier.getAddress());
        return  dalSupplier;
    }


    public static void loadRepository() throws SQLException {
        if(!Repository.getRepository().wasLoaded()){
            if(Repository.getRepository().getDatabaseHandler().loadOrderCounter()==0)
                Repository.getRepository().getDatabaseHandler().insertCounter();
            else
                Repository.getRepository().setORDERSCOUNTER(Repository.getRepository().getDatabaseHandler().loadOrderCounter());
            DALRepository dalRepository=Repository.getRepository().getDatabaseHandler().loadRepository();
            convertDalSupplierList(dalRepository.getDalSupplierList());
            OrderManager.convertDalOrderMap(dalRepository.getDalOrderMap());
            Repository.getRepository().setLoaded(true);
        }
    }

    private static void convertDalSupplierList(List<DALSupplier> dalSupplierList) {
        for (DALSupplier dalSupp:dalSupplierList) {
            Contract contract=ContractManager.convertDalContract(dalSupp.getContract());
            List<Contact> contacts=ContactManager.convertDalContacts(dalSupp.getContacts());
            Supplier supplier=new Supplier(dalSupp.getName(),dalSupp.getBnNumber(),
                    dalSupp.getBankAccountNumber(),Supplier.paymentMethod.valueOf(dalSupp.getPaymentMethod())
                    ,contacts,contract,dalSupp.getAddress());
            Repository.getRepository().getSupplierList().add(supplier);
        }
    }

    //TODO: need to update this function
    public static boolean checkIfSupplierWorksInDate(int day, String bnNumber) {
        Supplier supplier = findSupplierByBN(bnNumber);
        if (supplier != null && day<8 && day>0) {
            String[] fixedDaysArr = supplier.getContract().getFixedDayDelivery();
            String isAvailable = fixedDaysArr[day];
            if(isAvailable.equals("0"))
                return false;
            else return true;

        }
        return false;
    }
}
