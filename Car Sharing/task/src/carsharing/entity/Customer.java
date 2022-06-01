package carsharing.entity;

public class Customer {
    private int ID;
    private String name;
    private Integer rentedCarID;

    public Customer(String name, Integer rentedCarID) {
        this.name = name;
        this.rentedCarID = rentedCarID;
    }

    public Customer() {
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

    public Integer getRentedCarID() {
        if (this.rentedCarID != null) {
            return rentedCarID;
        }
        return null;
    }

    public void setRentedCarID(Integer rentedCarID) {
        this.rentedCarID = rentedCarID;
    }
}
