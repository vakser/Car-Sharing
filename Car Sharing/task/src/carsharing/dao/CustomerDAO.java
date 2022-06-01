package carsharing.dao;

import carsharing.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> getAllCustomers();

    void addCustomer(Customer customer);

    Customer findCustomerByID(int ID);

    void updateCustomer(Customer customer);
}
