package BusinessLayer.SuppliersAndInventory;
import DataAccessLayer.DALDTO.DALContract;
import DataAccessLayer.DALDTO.DALProductS;
import InterfaceLayer.DTO.Contract_DTO;
import InterfaceLayer.DTO.ProductS_DTO;
import InterfaceLayer.DTO.Supplier_DTO;

import java.util.LinkedList;
import java.util.List;

public class ContractManager {
    public static double getMinDiscount(String name,String bnNumber)
    {
        Supplier s = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        return s.getContract().xDiscount;
    }

    public static boolean deleteProductFromContract(String name, String bnNumber, String catalogid) //validate
    {
        Supplier supplier = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        if (supplier != null) {
            Contract contract = supplier.getContract();
            if(!ProductSManager.validateBeforeDeleteProduct(contract,catalogid))
                return false;
            if (contract != null) {
                boolean output= contract.deleteProductFromContract(catalogid);
                if(output) Repository.getRepository().getDatabaseHandler().deleteProductFromContract(bnNumber,catalogid);
                return  output;
            }

        }
        return false;
    }



    public static boolean addExistProductToContract(ProductS_DTO productDto, String name, String bnNumber) {
        Supplier supplier = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
        if (supplier != null) {
            if(supplier.getContract().hasProductWithThisID(productDto.getCatalogID()))
                return false;
            supplier.getContract().addNewProductToContract(productDto);
            Repository.getRepository().getDatabaseHandler().addExistProduct(convertProductToDAL(productDto),bnNumber);
            return true;
        }
        return false;
    }

    public static boolean changeMinDiscount(Supplier_DTO supplier, String newDiscount) {
        if (!vaildMinDiscount(newDiscount))
            return false;
        Supplier sup = SupplierManager.findSupplierByNameAndBN(supplier.getName(), supplier.getBnNumber());
        if (!sup.equals(null)) {
            Contract contract = sup.getContract();
            if (contract == null)
                return false;
            else {
                String[] fixedDaysOrders = contract.fixedDayDelivery;
                for(int i=0;i<fixedDaysOrders.length;i++)
                {
                    if(!fixedDaysOrders[i].equals("0"))
                        return false;
                }
                contract.setxDiscount(Double.parseDouble(newDiscount));
                Repository.getRepository().getDatabaseHandler().updateMinDiscount(sup.getBnNumber(),newDiscount);
                return true;
            }
        }
        return false;
    }

    public static boolean addNewProductToContract(ProductS_DTO product_dto, String name, String bnNumber) {
        if(ProductSManager.validProductID(product_dto.getCatalogID())&&ProductSManager.validProductName(product_dto.getName())) {
            Supplier supplier = SupplierManager.findSupplierByNameAndBN(name, bnNumber);
            if (supplier != null) {
                supplier.getContract().addNewProductToContract(product_dto);
                Repository.getRepository().getDatabaseHandler().addNewProduct(convertProductToDAL(product_dto),bnNumber);
                return true;
            }
        }
        return false;
    }



    public static void init_Products(Contract contract, Contract_DTO contract_dto) {
        for (ProductS_DTO p : contract_dto.getProductsIncluded()) {
            contract.addNewProductToContract(p);
        }
    }

    //********DTO*********

    public static Contract_DTO convertContractToDTO(Contract contract) {
        List<ProductS_DTO> products = ProductSManager.getProductDTO(contract.getProductsIncluded());
        String xDiscount = "" + contract.getxDiscount();

        return new Contract_DTO(contract.getFixedDayDelivery(), products, xDiscount);
    }

    public static DALProductS convertProductToDAL(ProductS_DTO product) {
        return new DALProductS(product.getName(), product.getCatalogID(), "" + product.getPrice(), product.getWeight());
    }





    //*********Validation*********

    public static boolean vaildMinDiscount(String discount_string) {
        if (discount_string == null || discount_string.equals("") || !discount_string.matches("-?\\d+(?:\\.\\d+)?"))
            return false;
        double discount = Double.parseDouble(discount_string);
        if (discount >= 0 && discount <= 5)
            return true;
        else return false;
    }



    //************************DAL*********************
    public static Contract convertDalContract(DALContract dalContract) {
        List<ProductS> productS=new LinkedList<>();
        for (DALProductS dalProduct:dalContract.getProductsIncluded()) {
            productS.add(ProductSManager.convertDalProduct(dalProduct));
        }

        return new Contract(dalContract.fixedDaysOrders,productS,Double.parseDouble(dalContract.getxDiscout()));
    }
}
