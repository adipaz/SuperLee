package InterfaceLayer.DTO;

import BusinessLayer.Shipments.Shipment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ShipmentInterfaceDTO {
    private final LocalDate date;
    private final LocalTime departureTime;
    private final String truckNumber;
    private final String driverName;
    private final String source;
    private final List<CertificateInterfaceDTO> destinations;
    private final double overallWeight;
    private final List<String> comments;

    public ShipmentInterfaceDTO(Shipment shipment, List<CertificateInterfaceDTO> destinations) {
        this.date = shipment.getDate();
        this.departureTime = shipment.getDepartureTime();
        this.truckNumber = shipment.getTruckNumber();
        this.driverName = shipment.getDriverName();
        this.source = shipment.getSource();
        this.overallWeight = shipment.getOverallWeight();
        this.comments = shipment.getComments();
        this.destinations = destinations;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Date: " + date + " Departure time: " + departureTime + " Source address: " + source + "\n");
        str.append("Truck license number: " + truckNumber + " Driver name: " + driverName + " Overall weight: " + overallWeight + "\n");
        str.append("Destinations: \n");
        for (CertificateInterfaceDTO certificate:destinations) {
            str.append(certificate).append("\n");
        }
        str.append("Comments: \n");
        for (int i = 0; i < comments.size(); i++){
            str.append(comments.get(i));
            if(i != comments.size() - 1){
                str.append(" | ");
            }
        }
        str.append("\n");
        return str.toString();
    }
}
