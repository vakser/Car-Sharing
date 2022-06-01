package carsharing.daoimplementation;

import carsharing.dao.CompanyDAO;
import carsharing.entity.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {
    private static final String FIND_ALL_COMPANIES = "SELECT * FROM COMPANY";
    private static final String INSERT_COMPANY = "INSERT INTO COMPANY VALUES(DEFAULT, ?)";
    private static final String FIND_COMPANY_BY_ID = "SELECT * FROM COMPANY WHERE ID=?";

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_COMPANIES);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                companies.add(extractCompany(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return companies;
    }

    @Override
    public void addCompany(Company company) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(INSERT_COMPANY)) {
            statement.setString(1, company.getName());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                company.setName(rs.getString(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Company findCompanyByID(int ID) {
        Company company = null;
        try (Connection connection = DriverManager.getConnection("jdbc:h2:C:/Users/vakal/Desktop/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing");
             PreparedStatement statement = connection.prepareStatement(FIND_COMPANY_BY_ID)) {
            statement.setInt(1, ID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                company = extractCompany(rs);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
        return company;
    }

    private Company extractCompany(ResultSet rs) throws SQLException {
        Company company = new Company();
        company.setID(rs.getInt("ID"));
        company.setName(rs.getString("NAME"));
        return company;
    }
}
