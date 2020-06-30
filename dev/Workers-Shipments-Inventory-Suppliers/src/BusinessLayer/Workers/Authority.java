package BusinessLayer.Workers;

import DataAccessLayer.DALDTO.AuthorityDALDTO;

public class Authority  {
    private int ID;
    private String name;

    public Authority(int ID, String name){
        this.ID=ID;
        this.name=name;
    }

    public Authority(AuthorityDALDTO dalAuthority) {
        ID = dalAuthority.getID();
        name = dalAuthority.getName();
    }


    public int getID() {
        return ID;
    }


    public String getName() {
        return name;
    }



    public String toString() {
        String description = "Authority: ";
        description += "ID: " + getID() + ", ";
        description += "Name: " + getName();
        return description;
    }
}
