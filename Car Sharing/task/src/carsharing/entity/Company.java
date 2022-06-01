package carsharing.entity;

public class Company {
    private int ID;
    private String name;

    public Company(String name) {
        this.name = name;
    }

    public Company() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
