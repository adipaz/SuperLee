package InterfaceLayer.DTO;

import BusinessLayer.Shipments.Truck;

public class TruckInterfaceDTO {
    private final String licenseNumber;
    private final String model;
    private final double truckWeight;
    private final double maxCarryWeight;


    public TruckInterfaceDTO(Truck truck){
        this.licenseNumber = truck.getLicenseNumber();
        this.model = truck.getModel();
        this.truckWeight = truck.getTruckWeight();
        this.maxCarryWeight = truck.getMaxCarryWeight();
    }

    @Override
    public String toString() {
        return  "LicenseNumber: " + licenseNumber +
                "\tModel: " + model +
                "\tTruckWeight: " + truckWeight +
                "\tMaxCarryWeight: " + maxCarryWeight;
    }
}
