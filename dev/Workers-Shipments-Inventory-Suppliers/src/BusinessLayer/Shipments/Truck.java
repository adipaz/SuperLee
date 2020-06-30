package BusinessLayer.Shipments;


import DataAccessLayer.DALDTO.TruckDALDTO;
import java.util.Objects;

public class Truck {
    private String licenseNumber;
    private String model;
    private double truckWeight;
    private double maxCarryWeight;

    public Truck(TruckDALDTO DALTruck) {
        this.licenseNumber = DALTruck.getLicenseNumber();
        this.model = DALTruck.getModel();
        this.truckWeight = DALTruck.getTruckWeight();
        this.maxCarryWeight = DALTruck.getMaxCarryWeight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return truckWeight == truck.truckWeight &&
                maxCarryWeight == truck.maxCarryWeight &&
                Objects.equals(licenseNumber, truck.licenseNumber) &&
                Objects.equals(model, truck.model);
    }

    public Truck(String licenseNumber, String model, double truckWeight, double maxCarryWeight) {
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.truckWeight = truckWeight;
        this.maxCarryWeight = maxCarryWeight;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public double getTruckWeight() {
        return truckWeight;
    }

    public double getMaxCarryWeight() {
        return maxCarryWeight;
    }
}
