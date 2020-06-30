package InterfaceLayer.DTO;

import BusinessLayer.Workers.Authority;

public class AuthorityInterfaceDTO {

    private final int ID;
    private final String name;

    public AuthorityInterfaceDTO(Authority authority) {
        this.ID = authority.getID();
        this.name = authority.getName();
    }

    public String toString(){
        return "ID" + ": " + ID + " " + "Name" + ": " + name;
    }
}
