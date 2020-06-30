package BusinessLayer.SuppliersAndInventory;

public class Contact {
    private String name;
    private String email;
    private String phone;

    public Contact(String name,String email,String phone){
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

    public boolean equals(Object contact)
    {
        if(contact!=null&&contact instanceof Contact)
            if(this.name.equals(((Contact)contact).getName())&&this.email.equals(((Contact)contact).getEmail())&&this.phone.equals(((Contact)contact).getPhone()))
                return true;
        return false;
    }


}
