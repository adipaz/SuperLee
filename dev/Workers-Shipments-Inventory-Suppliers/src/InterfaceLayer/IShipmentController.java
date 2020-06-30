package InterfaceLayer;

import DataStructures.Pair;
import InterfaceLayer.DTO.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public interface IShipmentController {


    /* Shipment module commands----------------------------------------------------------------------------------------- */
    //Initiates a new shipment from the request queue
    ShipmentRequestDTO popNextShipmentRequest();
    //Choose a truck by its license number @Param for the current built shift
    boolean chooseTruck(String truckLicenseNumber);
    //Check if the given quantity is valid
    boolean checkValidQuantity(int quantity);
    //Check if the given catalog id is valid
    boolean checkCatalogID(String catalogID);
    //Choose a driver by his id for the current shipment
    boolean chooseDriver(String id);
    //Validate the current shipment weight
    boolean validateWeight();
    //Choose a new truck for the current shipment
    boolean chooseOtherTruck(String licenseNum);
    //Remove branch from destinations by its address from the current shipment
    boolean removeAddress(String address);
    //Remove products from current shipment by its catalogID from the current shipment
    //Returns the number of products removed
    int removeProducts(String catalogID, int quantity,String address);
    //Checks if there are other trucks in the system not includes the current shipment's truck
    boolean otherTrucksExists();
    void createShipment();
    Pair<LocalDate,LocalTime> setShipmentDepartureTime();

    /* Shipment module queries------------------------------------------------------------------------------------------ */
    List<TruckInterfaceDTO> getOtherTrucks();
    List<ShipmentInterfaceDTO> getShipments(String[] args);
//    List<ProductS_DTO> getProducts();
    List<TruckInterfaceDTO> getTrucks();
    //List<SiteInterfaceDTO> getSourceSites();
    //Available means the current shipment's shift has a storekeeper
    //List<SiteInterfaceDTO> getAvailableDestinationSites();
    List<DriverInterfaceDTO> getDriversForTruck();
    List<CertificateInterfaceDTO> getCurrentShipmentCertificates();


    void abortShipment();
}
