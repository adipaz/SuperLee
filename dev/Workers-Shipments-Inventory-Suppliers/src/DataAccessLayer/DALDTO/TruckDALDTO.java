package DataAccessLayer.DALDTO;

public class TruckDALDTO implements DALDTO {
    private String licenseNumber;
    private String model;
    private double truckWeight;
    private double maxCarryWeight;

    public TruckDALDTO(String licenseNumber, String model, double truckWeight, double maxCarryWeight) {
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