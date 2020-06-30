package InterfaceLayer.DTO;


import BusinessLayer.Workers.Branch;

public class BranchInterfaceDTO {

    private final String address;
    private final char area;


    public BranchInterfaceDTO(Branch s) {
        address = s.getAddress();
        area = s.getArea();
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Address: " + address + "\t|\tArea: " + area;
    }
}