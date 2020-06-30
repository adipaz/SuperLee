package DataAccessLayer.DALDTO;

public class AuthorityDALDTO implements DALDTO {
    private int ID;
    private String name;

    public AuthorityDALDTO(int id, String name) {
        ID = id;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
