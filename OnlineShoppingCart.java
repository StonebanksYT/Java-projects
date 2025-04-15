import java.util.*;

class Product {
    int id;
    String name;
    double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.price * quantity;
    }
}

class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class OnlineShoppingCart {
    static Scanner sc = new Scanner(System.in);
    static List<Product> products = new ArrayList<>();
    static List<CartItem> cart = new ArrayList<>();
    static Map<String, User> users = new HashMap<>();
    static Map<String, Integer> coupons = new HashMap<>();
    static User currentUser = null;
    static double discountApplied = 0;

    public static void initializeProducts() {
        products.add(new Product(101, "Laptop", 55000));
        products.add(new Product(102, "Smartphone", 18000));
        products.add(new Product(103, "Headphones", 1500));
        products.add(new Product(104, "Keyboard", 700));
        products.add(new Product(105, "Monitor", 9000));
    }

    public static void initializeCoupons() {
        coupons.put("DISCOUNT10", 10);
        coupons.put("SALE20", 20);
    }

    public static void initializeUsers() {
        users.put("admin", new User("admin", "admin123"));
        users.put("kaustubh", new User("kaustubh", "pass123"));
    }

    public static void login() {
        System.out.print("Enter username: ");
        String uname = sc.next();
        System.out.print("Enter password: ");
        String pwd = sc.next();

        if (users.containsKey(uname) && users.get(uname).password.equals(pwd)) {
            currentUser = users.get(uname);
            System.out.println("Login successful! Welcome, " + currentUser.username);
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    public static void showProductCatalog() {
        System.out.println("\n--- Product Catalog ---");
        for (Product p : products) {
            System.out.printf("ID: %d | Name: %s | Price: ₹%.2f\n", p.id, p.name, p.price);
        }
    }

    public static void addToCart() {
        showProductCatalog();
        System.out.print("Enter Product ID to add: ");
        int id = sc.nextInt();
        Product selected = null;
        for (Product p : products) {
            if (p.id == id) {
                selected = p;
                break;
            }
        }
        if (selected == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();
        for (CartItem item : cart) {
            if (item.product.id == id) {
                item.quantity += qty;
                System.out.println("Updated quantity in cart.");
                return;
            }
        }
        cart.add(new CartItem(selected, qty));
        System.out.println("Item added to cart.");
    }

    public static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\n--- Your Cart ---");
        double total = 0;
        for (CartItem item : cart) {
            System.out.printf("%s x%d - ₹%.2f\n", item.product.name, item.quantity, item.getTotalPrice());
            total += item.getTotalPrice();
        }
        if (discountApplied > 0) {
            System.out.printf("Discount: -₹%.2f\n", total * (discountApplied / 100));
            total -= total * (discountApplied / 100);
        }
        System.out.printf("Total Amount: ₹%.2f\n", total);
    }

    public static void updateQuantity() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        viewCart();
        System.out.print("Enter Product ID to update quantity: ");
        int id = sc.nextInt();
        for (CartItem item : cart) {
            if (item.product.id == id) {
                System.out.print("Enter new quantity: ");
                int qty = sc.nextInt();
                if (qty <= 0) {
                    cart.remove(item);
                    System.out.println("Item removed.");
                } else {
                    item.quantity = qty;
                    System.out.println("Quantity updated.");
                }
                return;
            }
        }
        System.out.println("Product not found in cart.");
    }

    public static void removeFromCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        viewCart();
        System.out.print("Enter Product ID to remove: ");
        int id = sc.nextInt();
        CartItem toRemove = null;
        for (CartItem item : cart) {
            if (item.product.id == id) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            cart.remove(toRemove);
            System.out.println("Item removed from cart.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    public static void applyCoupon() {
        System.out.print("Enter coupon code: ");
        String code = sc.next();
        if (coupons.containsKey(code)) {
            discountApplied = coupons.get(code);
            System.out.println("Coupon applied! " + (int)discountApplied + "% off.");
        } else {
            System.out.println("Invalid coupon code.");
        }
    }

    public static void checkout() {
        viewCart();
        if (cart.isEmpty()) return;
        System.out.println("Proceeding to checkout...");
        System.out.println("Thank you for your purchase!");
        cart.clear();
        discountApplied = 0;
    }

    public static void main(String[] args) {
        initializeProducts();
        initializeCoupons();
        initializeUsers();

        System.out.println("Welcome to Online Shopping Cart");
        while (currentUser == null) {
            login();
        }

        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Show Product Catalog");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Update Quantity");
            System.out.println("5. Remove from Cart");
            System.out.println("6. Apply Coupon");
            System.out.println("7. Checkout");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> showProductCatalog();
                case 2 -> addToCart();
                case 3 -> viewCart();
                case 4 -> updateQuantity();
                case 5 -> removeFromCart();
                case 6 -> applyCoupon();
                case 7 -> checkout();
                case 8 -> {
                    System.out.println("Thanks for shopping, " + currentUser.username + "!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}