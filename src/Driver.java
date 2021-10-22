package src;

import java.util.Scanner;
import java.util.Vector;

public class Driver {
    static User user;
    static Order order;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        Vector<String> options = new Vector<>();
        options.add("Login");
        options.add("Add Product");
        options.add("Show Products");
        options.add("Add To Cart");
        options.add("View Your Cart");
        options.add("Order");
        options.add("See options");
        options.add("Exit");

        Products products = new Products();
        Utils.showOptions(options);
        boolean flag = true;
        while (flag) {
            int caseChosen = Utils.getOption(options, scanner);

            switch (caseChosen) {
            case 0:
                authenticateUser(scanner);
                break;

            case 1:
                if (user.isStaff) {
                    products.addProduct(scanner);
                } else {
                    Print.print("You do not have permission to add products.", Print.YELLOW);
                }
                break;

            case 2:
                Print.print(products.getProducts());
                break;

            case 3:
                if (order != null) {
                    Product selectedProduct = null;
                    while (selectedProduct == null) {
                        int productId = Utils.getIntInRange("Add to cart, Product ID: ", 1, 10000, scanner);
                        selectedProduct = products.productIdToProduct(productId);
                        if (selectedProduct != null) {
                            Print.print(selectedProduct.name + " ✅ Added to cart.", Print.GREEN);
                        } else {
                            Print.print("Invalid Product ID, try again...", Print.RED);
                        }
                    }
                    order.addToCart(selectedProduct);
                } else {
                    Print.print("Login to explore this feature...", Print.YELLOW);
                }
                break;

            case 4:
                if (order != null) {
                    order.getCartItems();
                } else {
                    Print.print("Login to explore this feature...", Print.YELLOW);
                }
                break;
            case 5:
                if (order != null) {
                    order.completeOrder();
                } else {
                    Print.print("Login to explore this feature...", Print.YELLOW);
                }
                break;

            case 6:
                Utils.showOptions(options);

            default:
                flag = false;
                break;
            }

        }
        scanner.close();
    }

    public static void authenticateUser(Scanner scanner) {
        // get phone number from user
        String userPhone = "" + Utils.getBigIntInRange("📞 Phone: ", 10, 10, scanner);

        // initiate the PhoneOtp class
        PhoneOtp phoneOtp = new PhoneOtp(userPhone);

        // generate otp
        phoneOtp.generateOtp();

        while (true) {
            // get otp from user
            String userOtp = Integer.toString(Utils.getIntInRange("🔑 OTP: ", 6, 6, scanner));

            // validate otp
            if (phoneOtp.validateOtp(userPhone, userOtp)) {
                user = new User(userPhone, scanner);
                order = new Order(user);

                Print.print("✅ Logged In", Print.GREEN);
                return;
            }
            Print.print("❗️ Incorrect OTP. Try again...", Print.RED);
        }
    }

}
