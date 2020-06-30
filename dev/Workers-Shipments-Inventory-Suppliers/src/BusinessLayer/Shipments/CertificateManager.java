package BusinessLayer.Shipments;
import BusinessLayer.SuppliersAndInventory.ProductForShipment;
import DataAccessLayer.DALDTO.CertificateDALDTO;
import DataAccessLayer.Handlers.CertificateHandler;
import DataStructures.Pair;

import java.util.*;

public class CertificateManager {
    private static CertificateManager instance = null;
    private Map<Integer, Certificate> certifications = new HashMap<>();
    private CertificateHandler certificateHandler;
    private int autoIncrementID;

    private CertificateManager() {
        certificateHandler = CertificateHandler.getInstance();
        autoIncrementID = certificateHandler.loadAutoID();
    }

    public static CertificateManager getInstance() {
        if (instance == null)
            instance = new CertificateManager();
        return instance;
    }

    public void createCertificate(String address, Map<String, Integer> productIDQuantity) {
        autoIncrementID++;
        Certificate certificate = new Certificate(autoIncrementID, address, productIDQuantity);
        certifications.put(autoIncrementID, certificate);
    }

    public int getCurrentCertificateID() {
        return autoIncrementID;
    }


    public Certificate getCertificate(int id) {
        if (certifications.containsKey(id))
            return certifications.get(id);
        Certificate certificate = new Certificate(certificateHandler.loadByInt(id));
        certifications.put(id, certificate);
        return certificate;
    }

    public double calculateWeight(int certificateID, List<Pair<Integer, Double>> productQuantityAndWeight) {
        return this.certifications.get(certificateID).calculateWeight(productQuantityAndWeight);
    }

    public int removeCertificate(String address) {
        for (Integer certificateID : certifications.keySet()) {
            if (certifications.get(certificateID).getAddress().equals(address)) {
                certificateHandler.delete(toDTO(certifications.get(certificateID), 0));
                certifications.remove(certificateID);
                return certificateID;
            }
        }
        return -1;
    }


    public int removeProducts(String address, String catalogID, int quantity) {
        for(Certificate certificate: certifications.values()){
            if(certificate.getAddress().equals(address)) {
                return certificate.removeProducts(catalogID,quantity);
            }
        }
        return -1;
    }

    private Map<String, Integer> getProductsQuantities(int certificateID) {
        return certifications.get(certificateID).getProductsQuantities();
    }

    public boolean checkSizeEqualsZero(int certificateID) {
        return getProductsQuantities(certificateID).size() == 0;
    }

    public void removeCertificate(int certificateID) {
        certifications.remove(certificateID);
    }

    public List<String> getAddressesByCertificateIDs(List<Integer> certificateIDs) {
        List<String> addresses = new Vector<>();
        for (Integer certificateID : certificateIDs) {
            addresses.add(certifications.get(certificateID).getAddress());
        }
        return addresses;
    }

    public boolean checkDuplicateAddress(String addressDestination,List<Integer> certificateIDs) {
        for (int i = 0; i < certificateIDs.size(); i++) {
            if (certifications.get(certificateIDs.get(i)).getAddress().equals(addressDestination)) {
                return true;
            }
        }
        return false;
    }
    

    public int getCertificateIDByAddress(String address) {
        for(Map.Entry<Integer,Certificate> certificateEntry:certifications.entrySet()){
            if(certificateEntry.getValue().getAddress().equals(address))
                return certificateEntry.getKey();
        }
        return -1;
    }

    public String getAddressByCertificateID(Integer certificateID) {
            return certifications.get(certificateID).getAddress();
    }


    public boolean checkCatalogIDInCertificate(Integer certificateID,String catalogNum) {
        for(Map.Entry<String,Integer> productQuantity : certifications.get(certificateID).getProductsQuantities().entrySet()){
            if(productQuantity.getKey().equals(catalogNum))
                return true;
        }
        return false;
    }

    public List<Certificate> getCertificatesByIDS(List<Integer> certificateIDs) {
        List<Certificate> certificates=new Vector<>();
        for(Integer certificateID: certificateIDs){
            certificates.add(certifications.get(certificateID));
        }
        return certificates;
    }

    public CertificateDALDTO toDTO(Certificate certificate, int shipmentID) {
        return new CertificateDALDTO(certificate.getID(), certificate.getProductsQuantities(), certificate.getAddress(), shipmentID);
    }

    public void delete(int certificateID) {
        certificateHandler.delete(toDTO(getCertificate(certificateID), 0));
    }

    public void save(int certificateID, int shipmentID) {
        certificateHandler.save(toDTO(getCertificate(certificateID), shipmentID));
    }

    public void update(int certificateID) {
        certificateHandler.update(toDTO(getCertificate(certificateID), 0));
    }

    public Map<String, Integer> getProductsIDsQuantitites(Integer certificateID) {
        return certifications.get(certificateID).getProductsQuantities();
    }

    public void loadCertificate(Integer id) {
        CertificateDALDTO dalCertificate = certificateHandler.loadByInt(id);
        if(dalCertificate !=null)
            certifications.put(dalCertificate.getID(),new Certificate(dalCertificate));
    }
}


