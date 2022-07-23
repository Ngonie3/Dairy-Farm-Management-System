package model;

public class EmployeeSearchModel {
    int employeeID;
    String employeeName;
    String employeeSalary;

    public EmployeeSearchModel(int employeeID, String employeeName, String employeeSalary) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    public int getEmployeeID() {
        return employeeID;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public String getEmployeeSalary() {
        return employeeSalary;
    }
}
