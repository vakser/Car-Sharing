package carsharing.daoimplementation;

import carsharing.dao.CustomerDAO;
import carsharing.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private static final String FIND_ALL_CUSTOMERS = "SELECT * FROM CUSTOMER";
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER VALUES(DEFAULT, ?, NULL)";
    private static final String FIND_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER WHERE ID=?";
    private static final String UPDATE_CUSTOMER = "UPDATE CUSTOMER SET RENTED_CAR_ID=? WHERE ID=?;";

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_CUSTOMERS);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                customers.add(extractCustomer(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return customers;
    }

    @Override
    public void addCustomer(Customer customer) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(INSERT_CUSTOMER)) {
            statement.setString(1, customer.getName());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                customer.setName(rs.getString(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Customer findCustomerByID(int ID) {
        Customer customer = null;
        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
            statement = connection.prepareStatement(FIND_CUSTOMER_BY_ID);
            statement.setInt(1, ID);
            rs = statement.executeQuery();
            if (rs.next()) {
                customer = extractCustomer(rs);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException();
        }
        return customer;
    }

    @Override
    public void updateCustomer(Customer customer) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMER)) {
            if (customer.getRentedCarID() != null) {
                statement.setInt(1, customer.getRentedCarID());
            } else {
                statement.setNull(1, Types.NULL);
            }
            statement.setInt(2, customer.getID());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

    private Customer extractCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setID(rs.getInt("ID"));
        customer.setName(rs.getString("NAME"));
        if (rs.getInt("RENTED_CAR_ID") != 0) {
            customer.setRentedCarID(rs.getInt("RENTED_CAR_ID"));
        } else {
            customer.setRentedCarID(null);
        }
        return customer;
    }
}
