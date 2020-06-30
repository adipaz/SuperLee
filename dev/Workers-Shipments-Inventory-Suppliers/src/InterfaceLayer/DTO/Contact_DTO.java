package InterfaceLayer.DTO;

public class Contact_DTO {
    private String name;
    private String email;
    private String phone;


    public Contact_DTO(String name,String email,String phone){
        this.name=name;
        this.email=email;
        this.phone=phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Name: "  +name+ "\tEmail: "  +email +"\tPhone: "  +phone + "\n\t   ";
       /* return "\n\t\t{\n" +
                "\t\t name='" + name + '\'' +
                ",\n\t\t email='" + email + '\'' +
                ",\n\t\t phone='" + phone + '\'' +
                "\n\t\t}";*/
    }
}
