package carsharing.entity;

public class Car {
    private int ID;
    private String name;
    private int companyID;

    public Car(String name, int companyID) {
        this.name = name;
        this.companyID = companyID;
    }

    public Car() {
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

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
}
