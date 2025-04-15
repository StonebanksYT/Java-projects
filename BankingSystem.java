import java.io.*;
import java.util.*;

class BankAccount implements Serializable {
    private String accountNumber;
    private String accountHolder;
    private String pin;
    private double balance;
    private List<String> transactions;

    public BankAccount(String accountNumber, String accountHolder, String pin, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.pin = pin;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
        transactions.add("Account created with deposit: ₹" + initialDeposit);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
    public boolean verifyPIN(String inputPin) { return this.pin.equals(inputPin); }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: ₹" + amount);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add("Withdrew: ₹" + amount);
            return true;
        } else {
            transactions.add("Failed withdrawal attempt of ₹" + amount + " (Insufficient balance)");
            return false;
        }
    }

    public void showDetails() {
        System.out.printf("Account Number: %s\nHolder: %s\nBalance: ₹%.2f\n", accountNumber, accountHolder, balance);
    }

    public void showTransactions() {
        System.out.println("Transaction History:");
        for (String t : transactions) {
            System.out.println("- " + t);
        }
    }
}

public class BankingSystem {
    static final String FILE_NAME = "bank_data.dat";
    static Map<String, BankAccount> accounts = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (HashMap<String, BankAccount>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Starting with fresh data...");
        }
    }

    public static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Failed to save data.");
        }
    }

    public static String generateAccountNumber() {
        Random rand = new Random();
        String acc;
        do {
            acc = String.valueOf(100000 + rand.nextInt(900000)); // 6-digit number
        } while (accounts.containsKey(acc));
        return acc;
    }

    public static void createAccount() {
        System.out.print("Enter account holder name: ");
        String name = sc.next();

        String pin;
        while (true) {
            System.out.print("Set a 4-digit PIN: ");
            pin = sc.next();
            if (pin.matches("\\d{4}")) break;
            System.out.println("PIN must be 4 digits.");
        }

        double deposit;
        while (true) {
            System.out.print("Enter initial deposit: ");
            try {
                deposit = Double.parseDouble(sc.next());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount.");
            }
        }

        String accNo = generateAccountNumber();
        BankAccount newAcc = new BankAccount(accNo, name, pin, deposit);
        accounts.put(accNo, newAcc);
        saveAccounts();
        System.out.println("Account created successfully! Your Account Number is: " + accNo);
    }

    public static BankAccount authenticate() {
        System.out.print("Enter account number: ");
        String accNo = sc.next();
        BankAccount acc = accounts.get(accNo);

        if (acc == null) {
            System.out.println("Account not found.");
            return null;
        }

        System.out.print("Enter PIN: ");
        String pin = sc.next();
        if (!acc.verifyPIN(pin)) {
            System.out.println("Incorrect PIN.");
            return null;
        }

        return acc;
    }

    public static void depositAmount() {
        BankAccount acc = authenticate();
        if (acc == null) return;

        double amount;
        while (true) {
            System.out.print("Enter amount to deposit: ");
            try {
                amount = Double.parseDouble(sc.next());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount.");
            }
        }

        acc.deposit(amount);
        saveAccounts();
        System.out.println("Deposit successful.");
    }

    public static void withdrawAmount() {
        BankAccount acc = authenticate();
        if (acc == null) return;

        double amount;
        while (true) {
            System.out.print("Enter amount to withdraw: ");
            try {
                amount = Double.parseDouble(sc.next());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount.");
            }
        }

        if (acc.withdraw(amount)) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Insufficient balance.");
        }
        saveAccounts();
    }

    public static void checkBalance() {
        BankAccount acc = authenticate();
        if (acc != null) {
            System.out.printf("Available Balance: ₹%.2f\n", acc.getBalance());
        }
    }

    public static void viewAccountDetails() {
        BankAccount acc = authenticate();
        if (acc != null) {
            acc.showDetails();
        }
    }

    public static void viewTransactions() {
        BankAccount acc = authenticate();
        if (acc != null) {
            acc.showTransactions();
        }
    }

    public static void main(String[] args) {
        loadAccounts();
        while (true) {
            System.out.println("\n=== Banking System Menu ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. View Account Details");
            System.out.println("6. View Transaction History");
            System.out.println("7. Exit");

            int choice;
            while (true) {
                System.out.print("Select option (1-7): ");
                try {
                    choice = Integer.parseInt(sc.next());
                    if (choice >= 1 && choice <= 7) break;
                    System.out.println("Enter number between 1 and 7.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositAmount();
                case 3 -> withdrawAmount();
                case 4 -> checkBalance();
                case 5 -> viewAccountDetails();
                case 6 -> viewTransactions();
                case 7 -> {
                    System.out.println("Thank you for using the Banking System.");
                    System.exit(0);
                }
            }
        }
    }
}