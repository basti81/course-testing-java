package com.javaguides.springboot.employeeTestToController;


import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    void saveAll(List<Employee> listOfEmployee);
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployee(long id);

}
