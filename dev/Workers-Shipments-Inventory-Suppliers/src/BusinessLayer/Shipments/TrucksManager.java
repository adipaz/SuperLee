package BusinessLayer.Shipments;

import BusinessLayer.Enums.LicenseTypes;
import DataAccessLayer.DALDTO.TruckDALDTO;
import DataAccessLayer.Handlers.TruckHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class TrucksManager {

    private static TrucksManager instance=null;
    private Map<String, Truck> trucks = new HashMap<>();
    private TruckHandler truckHandler;

    private TrucksManager(){
        truckHandler = TruckHandler.getInstance();
        //for testing purposes
        truckHandler.loadAll().forEach((dalTruck) -> trucks.put(dalTruck.getLicenseNumber(), new Truck(dalTruck)));
    }
    public static TrucksManager getInstance(){
        if(instance==null)
            instance=new TrucksManager();
        return instance;
    }

    public List<Truck> getAllTrucks(){
        List<Truck> allTrucks=new Vector<>();
        allTrucks.addAll(trucks.values());
        return allTrucks;
    }

    public boolean isTruckExists(String truckLicenseNumber) {
        if (trucks.containsKey(truckLicenseNumber))
            return true;
        TruckDALDTO dalTruck = truckHandler.loadByString(truckLicenseNumber);
        if (dalTruck != null) {
            trucks.put(truckLicenseNumber, new Truck(dalTruck));
            return true;
        }
        return false;
    }

    public List<Truck> getOtherTrucks(String currentTruckNum) {
        return trucks.values().stream().filter((x) -> !x.getLicenseNumber().equals(currentTruckNum)).collect(Collectors.toList());
    }

    public int getMaxWeightByLicenseNum(String licenseNumber) {

        double truckWeight=trucks.get(licenseNumber).getTruckWeight();
        if (truckWeight <= LicenseTypes.A.getNumVal())
            return LicenseTypes.A.getNumVal();
        else if (truckWeight <= LicenseTypes.B.getNumVal())
            return LicenseTypes.B.getNumVal();
        else if (truckWeight <= LicenseTypes.C.getNumVal())
            return LicenseTypes.C.getNumVal();
        else
            return LicenseTypes.D.getNumVal();
    }

    public double getWeightOfTruck(String currentTruckNum) {
        return trucks.get(currentTruckNum).getTruckWeight();
    }

    public boolean containsOtherTrucks() {
        return trucks.size() > 1;
    }

    public double getMaxCarryWeight(String currentLicenseNumber) {
        return trucks.get(currentLicenseNumber).getMaxCarryWeight();
    }

    public String getTruckModelByLicenseNum(String currentTruckNum) {
        return trucks.get(currentTruckNum).getModel();
    }
}
