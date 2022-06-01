package carsharing.dao;

import carsharing.entity.Company;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDAO {
    List<Company> getAllCompanies() throws ClassNotFoundException, SQLException;

    void addCompany(Company company);

    Company findCompanyByID(int ID);
}
