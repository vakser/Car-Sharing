package carsharing;

import carsharing.daoimplementation.CarDAOImpl;
import carsharing.daoimplementation.CompanyDAOImpl;
import carsharing.daoimplementation.CustomerDAOImpl;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CarSharingSystem {
    private final CompanyDAOImpl companyDAO = new CompanyDAOImpl();
    private final CarDAOImpl carDAO = new CarDAOImpl();
    private final CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    private final Scanner scanner = new Scanner(System.in);

    void mainMenu() {
        System.out.println("\n1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    void companyMenu() {
        System.out.println("\n1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    void carMenu() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    void rentMenu() {
        System.out.println("\n1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }
    void prepareDB(File file) {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/" + file.getName());
             Statement st = conn.createStatement()) {
            Class.forName("org.h2.Driver");
            conn.setAutoCommit(true);
            st.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(40) UNIQUE NOT NULL);");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS CAR (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(40) UNIQUE NOT NULL, COMPANY_ID INT NOT NULL, FOREIGN KEY(COMPANY_ID) REFERENCES COMPANY(ID));");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS CUSTOMER (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(40) UNIQUE NOT NULL, RENTED_CAR_ID INT, FOREIGN KEY(RENTED_CAR_ID) REFERENCES CAR(ID));");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void addCustomer() {
        System.out.println("\nEnter the customer name:");
        String name = scanner.nextLine();
        customerDAO.addCustomer(new Customer(name, 0));
        System.out.println("The customer was added!");
    }

    void operateAsCustomer() {
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers.size() == 0) {
            System.out.println("\nThe customer list is empty!");
        } else {
            System.out.println("\nCustomer list:");
            customers.stream().sorted(Comparator.comparing(Customer::getID)).forEach(customer1 -> System.out.println(customer1.getID() + ". " + customer1.getName()));
            System.out.println("0. Back");
            String chosenCustomer = scanner.nextLine();
            runRentMenu(chosenCustomer, customers);
        }
    }

    void operateAsManager() {
        companyMenu();
        String companyMenuInput = scanner.nextLine();
        runCompanyMenu(companyMenuInput);
    }

    void runCarSharingSystem(String[] args) {
        File file = createDBFile(args);
        prepareDB(file);
        mainMenu();
        String mainMenuInput = scanner.nextLine();
        runMainMenu(mainMenuInput);
    }

    File createDBFile(String[] args) {
        if (args.length == 2 && args[1] != null) {
            return new File(args[1]);
        } else {
            return new File("carsharing");
        }
    }

    void runMainMenu(String mainMenuInput) {
        while (!"0".equals(mainMenuInput)) {
            if ("1".equals(mainMenuInput)) {
                operateAsManager();
            }
            if ("2".equals(mainMenuInput)) {
                operateAsCustomer();
            }
            if ("3".equals(mainMenuInput)) {
                addCustomer();
            }
            mainMenu();
            mainMenuInput = scanner.nextLine();
            if ("0".equals(mainMenuInput)) {
                System.exit(0);
            }
        }
    }

    void printCompanyCarList(List<Car> cars) {
        if (cars.size() == 0) {
            System.out.println("\nThe car list is empty!");
        } else {
            System.out.println("\nCar list:");
            cars = cars.stream().sorted(Comparator.comparing(Car::getID)).collect(Collectors.toList());
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
            }
        }
    }

    void addCompanyCar(Company company) {
        System.out.println("\nEnter the car name:");
        String name = scanner.nextLine();
        carDAO.addCar(new Car(name, company.getID()));
        System.out.println("The car was added!");
    }

    void rentCar(List<Company> companies, Customer customer, List<Customer> customers) {
        if (companies.size() == 0) {
            System.out.println("\nThe company list is empty!");
        } else if (customer.getRentedCarID() != null ) {
            System.out.println("\nYou've already rented a car!");
        } else {
            System.out.println("\nChoose the company:");
            companies = companies.stream().sorted(Comparator.comparing(Company::getID)).collect(Collectors.toList());
            for (int i = 0; i < companies.size(); i++) {
                System.out.println(i + 1 + ". " + companies.get(i).getName());
            }
            System.out.println("0. Back");
            String chosenCompany = scanner.nextLine();
            chooseCar(chosenCompany, companies, customers, customer);
        }
    }

    void chooseCar(String chosenCompany, List<Company> companies, List<Customer> customers, Customer customer) {
        while (!"0".equals(chosenCompany)) {
            Company company = companyDAO.findCompanyByID(companies.get(Integer.parseInt(chosenCompany) - 1).getID());
            List<Customer> customersWithCar = customers.stream().filter(customer1 -> customer1.getRentedCarID() != null).collect(Collectors.toList());
            List<String> rentedCarsNames = new ArrayList<>();
            List<Car> companyFreeCars;
            for (Customer value : customersWithCar) {
                rentedCarsNames.add(carDAO.findCarByID(value.getRentedCarID()).getName());
            }
            companyFreeCars = carDAO.getAllCars().stream().filter(car -> (car.getCompanyID() == company.getID() && !rentedCarsNames.contains(car.getName()))).sorted(Comparator.comparing(Car::getID)).collect(Collectors.toList());
            if (companyFreeCars.size() == 0) {
                System.out.println("\nNo available cars in the " + company.getName() + " company");
            } else {
                System.out.println("\nChoose the car:");
                for (int i = 0; i < companyFreeCars.size(); i++) {
                    System.out.println(i + 1 + ". " + companyFreeCars.get(i).getName());
                }
                System.out.println("0. Back");
                String carChoice = scanner.nextLine();
                if (!"0".equals(carChoice)) {
                    customer.setRentedCarID(companyFreeCars.get(Integer.parseInt(carChoice) - 1).getID());
                    customerDAO.updateCustomer(customer);
                    System.out.println("\nYou rented '" + carDAO.findCarByID(customer.getRentedCarID()).getName() + "'");
                }
                chosenCompany = "0";
            }
        }
    }

    void returnCar(Customer customer) {
        if (customer.getRentedCarID() == null) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            customer.setRentedCarID(null);
            customerDAO.updateCustomer(customer);
            System.out.println("\nYou've returned a rented car!");
        }
    }

    void printInfoAboutRentedCar(Customer customer) {
        if (customer.getRentedCarID() == null) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            System.out.println("\nYour rented car:");
            System.out.println(carDAO.findCarByID(customer.getRentedCarID()).getName());
            System.out.println("Company:");
            System.out.println(companyDAO.findCompanyByID(carDAO.findCarByID(customer.getRentedCarID()).getCompanyID()).getName());
        }
    }

    void runRentMenu(String chosenCustomer, List<Customer> customers) {
        while (!"0".equals(chosenCustomer)) {
            Customer customer = customerDAO.findCustomerByID(Integer.parseInt(chosenCustomer));
            rentMenu();
            String rentMenuInput = scanner.nextLine();
            if ("0".equals(rentMenuInput)) {
                chosenCustomer = "0";
            }
            while (!"0".equals(rentMenuInput)) {
                List<Company> companies = companyDAO.getAllCompanies();
                if ("1".equals(rentMenuInput)) {
                    rentCar(companies, customer, customers);
                }
                if ("2".equals(rentMenuInput)) {
                    returnCar(customer);
                }
                if ("3".equals(rentMenuInput)) {
                    printInfoAboutRentedCar(customer);
                }
                rentMenu();
                rentMenuInput = scanner.nextLine();
                if ("0".equals(rentMenuInput)) {
                    chosenCustomer = "0";
                }
            }
        }
    }

    void runCarMenu(String chosenCompany, List<Company> companies) {
        while (!"0".equals(chosenCompany)) {
            System.out.println("\n'" + companyDAO.findCompanyByID(companies.get(Integer.parseInt(chosenCompany) - 1).getID()).getName() + "' company");
            carMenu();
            String carMenuInput = scanner.nextLine();
            while (!"0".equals(carMenuInput)) {
                Company company = companyDAO.findCompanyByID(companies.get(Integer.parseInt(chosenCompany) - 1).getID());
                List<Car> cars = carDAO.getAllCars().stream().filter(car -> car.getCompanyID() == company.getID()).collect(Collectors.toList());
                if ("1".equals(carMenuInput)) {
                    printCompanyCarList(cars);
                }
                if ("2".equals(carMenuInput)) {
                    addCompanyCar(company);
                }
                System.out.println();
                carMenu();
                carMenuInput = scanner.nextLine();
                if ("0".equals(carMenuInput)) {
                    chosenCompany = "0";
                }
            }
        }
    }

    void runCompanyMenu(String companyMenuInput) {
        while (!"0".equals(companyMenuInput)) {
            List<Company> companies = companyDAO.getAllCompanies();
            if ("1".equals(companyMenuInput)) {
                printCompanyList(companies);
            }
            if ("2".equals(companyMenuInput)) {
                createCompany();
            }
            companyMenu();
            companyMenuInput = scanner.nextLine();
        }
    }

    void printCompanyList(List<Company> companies) {
        if (companies.size() == 0) {
            System.out.println("\nThe company list is empty!");
        }
        else {
            System.out.println("\nChoose the company:");
            companies = companies.stream().sorted(Comparator.comparing(Company::getID)).collect(Collectors.toList());
            for (int i = 0; i < companies.size(); i++) {
                System.out.println(i + 1 + ". " + companies.get(i).getName());
            }
            System.out.println("0. Back");
            String chosenCompany = scanner.nextLine();
            runCarMenu(chosenCompany, companies);
        }
    }

    void createCompany() {
        System.out.println("\nEnter the company name:");
        String name = scanner.nextLine();
        companyDAO.addCompany(new Company(name));
        System.out.println("The company was created!");
    }
}
