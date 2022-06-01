package carsharing.dao;

import carsharing.entity.Car;

import java.sql.SQLException;
import java.util.List;

public interface CarDAO {
    List<Car> getAllCars() throws ClassNotFoundException, SQLException;

    void addCar(Car car);

    Car findCarByID(int ID);
}
