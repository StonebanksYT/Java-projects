import java.io.*;
import java.util.*;

class Employee implements Serializable {
    private String id;
    private String name;
    private String department;
    private double salary;

    public Employee(String id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getId() { return id; }

    public void setName(String name) { this.name = name; }

    public void setDepartment(String department) { this.department = department; }

    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Dept: %s | Salary: %.2f", id, name, department, salary);
    }
}

public class EmployeeManagementSystem {
    static Scanner sc = new Scanner(System.in);
    static Map<String, Employee> employeeMap = new HashMap<>();
    static final String FILE_NAME = "employees.dat";

    public static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employeeMap = (HashMap<String, Employee>) ois.readObject();
        } catch (Exception e) {
            System.out.println("No existing data found. Starting fresh.");
        }
    }

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employeeMap);
        } catch (IOException e) {
            System.out.println("Failed to save data.");
        }
    }

    public static void addEmployee() {
        System.out.print("Enter ID: ");
        String id = sc.next();

        if (employeeMap.containsKey(id)) {
            System.out.println("Employee ID already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Enter Department: ");
        String dept = sc.next();

        double salary;
        while (true) {
            System.out.print("Enter Salary: ");
            try {
                salary = Double.parseDouble(sc.next());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for salary.");
            }
        }

        employeeMap.put(id, new Employee(id, name, dept, salary));
        System.out.println("Employee added!");
        saveData();
    }

    public static void viewAllEmployees() {
        if (employeeMap.isEmpty()) {
            System.out.println("No employees available.");
            return;
        }

        for (Employee e : employeeMap.values()) {
            System.out.println(e);
        }
    }

    public static void searchEmployee() {
        System.out.print("Enter Employee ID to search: ");
        String id = sc.next();
        Employee e = employeeMap.get(id);
        if (e != null) {
            System.out.println(e);
        } else {
            System.out.println("Employee not found.");
        }
    }

    public static void updateEmployee() {
        System.out.print("Enter Employee ID to update: ");
        String id = sc.next();
        Employee e = employeeMap.get(id);

        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.print("Enter new name: ");
        e.setName(sc.next());

        System.out.print("Enter new department: ");
        e.setDepartment(sc.next());

        double salary;
        while (true) {
            System.out.print("Enter new salary: ");
            try {
                salary = Double.parseDouble(sc.next());
                e.setSalary(salary);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }

        System.out.println("Employee updated.");
        saveData();
    }

    public static void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        String id = sc.next();
        if (employeeMap.remove(id) != null) {
            System.out.println("Employee deleted.");
            saveData();
        } else {
            System.out.println("Employee not found.");
        }
    }

    public static void main(String[] args) {
        loadData();
        while (true) {
            System.out.println("\n=== Employee Management System ===");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee by ID");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Exit");

            int choice = -1;
            while (true) {
                System.out.print("Choose an option (1-6): ");
                try {
                    choice = Integer.parseInt(sc.next());
                    if (choice >= 1 && choice <= 6) break;
                    System.out.println("Please enter a number between 1 and 6.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number.");
                }
            }

            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> viewAllEmployees();
                case 3 -> searchEmployee();
                case 4 -> updateEmployee();
                case 5 -> deleteEmployee();
                case 6 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }
        }
    }
}