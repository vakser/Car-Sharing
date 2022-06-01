package carsharing.daoimplementation;

import carsharing.dao.CarDAO;
import carsharing.entity.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImpl implements CarDAO {
    private static final String FIND_ALL_CARS = "SELECT * FROM CAR";
    private static final String INSERT_CAR = "INSERT INTO CAR VALUES(DEFAULT, ?, ?)";
    private static final String FIND_CAR_BY_ID = "SELECT * FROM CAR WHERE ID=?";

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_CARS);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return cars;
    }

    @Override
    public void addCar(Car car) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(INSERT_CAR)) {
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyID());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                car.setName(rs.getString(1));
                car.setCompanyID(rs.getInt(2));
            }
            rs.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Car findCarByID(int ID) {
        Car car = null;
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(FIND_CAR_BY_ID)) {
            statement.setInt(1, ID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                car = extractCar(rs);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
        return car;
    }

    private Car extractCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setID(rs.getInt("ID"));
        car.setName(rs.getString("NAME"));
        car.setCompanyID(rs.getInt("COMPANY_ID"));
        return car;
    }
}
