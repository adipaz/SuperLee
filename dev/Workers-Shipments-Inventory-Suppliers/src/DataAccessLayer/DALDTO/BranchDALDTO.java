package DataAccessLayer.DALDTO;

public class BranchDALDTO implements DALDTO {
    private String address;
    private char area;

    public BranchDALDTO(String address, char area) {
        this.address = address;
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public char getArea() {
        return area;
    }
}
