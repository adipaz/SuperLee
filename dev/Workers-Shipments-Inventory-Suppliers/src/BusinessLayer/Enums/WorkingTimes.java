package BusinessLayer.Enums;

public enum WorkingTimes {
    day(0),night(1),both(2),none(3);


    private int numVal;

    WorkingTimes(int numVal) {
        this.numVal = numVal;
    }
    public int getNumVal() {
        return numVal;
    }
}
