package com.medinet;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

class Universal{
    public int id;
    public String name;
}

class Manufacturer extends Universal{
    public ArrayList<Integer> product_List_manufactured = new ArrayList<>();
    // This List contains the list of products manufactured by the manufacturer
}

class Product extends Universal{
    public String name_of_manufacturer;
    public int id_of_manufacturer;
}

class Customer extends Universal{
    public int zipcode;
    public ArrayList<Integer> list_of_product_purchased = new ArrayList<>();
}

class Tuple{
    int product_id;
    int frequency;
}

class Shop extends Universal{
    public int zipcode;
    // Now we have to think of a way to have an inventory within the class
    public ArrayList<Tuple> inventory_list = new ArrayList<>();

}

class Delivery_agent extends Universal{
    public int zipcode;
    public int no_of_products_delivered;
}

class id_Handling{

    public int id_generator(int num, ArrayList<Integer> id_used){

        // Generating a 5 digit random number
        // Each id is going to be a 6 digit number with 5 randomly generated digits and the
        // first digit is going to be the digit as per the entity of the entry

        while(true) {
            double random_number = Math.floor(Math.random() * 100000);
            int id = (int) random_number;
            String data = String.valueOf(id);     // Converting integer to string

            // Making the string length equal to 5
            while (data.length() < 5) {
                data = "0" + data;
            }
            data = String.valueOf(num) + data;
            id = parseInt(data);

            for (int i = 0; i < id_used.size(); i++){
                if(id == id_used.get(i)){
                    continue;
                }
            }
            return id;
        }
    }

    public int general_id_checker(int id){
        // This function tells whether the id entered is valid on not
        double division = id/100000;

        if(division >= 1 && division < 5){
            return (int)division;
            // The entered id is correct
        }
        else{
            return -1;
        }
    }

    public int product_id_checker(int id){

        // THIS METHOD ENSURES IF THE PRODUCT ID IS A 5 DIGIT NUMBER OR NOT
        int num = id/10000;
        if(num >= 1 && num < 10){
            // Product id is a 5 digits number
            return 1;
        }
        else{
            return 0;
        }
    }
}

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> assigned_ids = new ArrayList<Integer>();
        // List for assigned id to avoid repetition of the ids

        ArrayList<Customer> customer_data = new ArrayList<Customer>();
        ArrayList<Manufacturer> manufacturer_data = new ArrayList<Manufacturer>();
        ArrayList<Shop> shop_data = new ArrayList<Shop>();
        ArrayList<Delivery_agent> delivery_agent_data = new ArrayList<Delivery_agent>();
        ArrayList<Product> product_data = new ArrayList<Product>();

        // THESE ARE ALL THE LISTS FOR STORING THE VARIOUS CLASSES FOR THE VARIOUS ENTITIES
        // SMALL DATABASE FOR TESTING CREATED, USER CAN ALSO CREATE ADD NEW ENTITIES AFTER RUNNING THE PROGRAM

        System.out.println("Welcome to the Medinet.com website\n");
        System.out.println("<------USER MANUAL------>");
        System.out.println("When you create an account, a 6 digit user id will be generated, this id will be used for logging in and to access other functionalities");
        System.out.println("Customer will be able to order after they have created their account, by logging in their account");
        System.out.println("Manufacturers and Shops can add products to their list during account creation in and also when they are logged in");
        System.out.println("Product database can only be altered by manufacturers, they can add/remove products in the database");
        System.out.println("Shops can only enter those products in their inventory which are in the product data base, that is they have a manufacturer");
        System.out.println("Here is the already existing database for testing purpose :- ");
        System.out.println("Customers id : 128462 143546 132535");
        System.out.println("Manufacturer ids : 234566 234555");
        System.out.println("Product ids : 12344 12345 12346 12347 12348 12349");
        System.out.println("Shop ids : 345677 345678");
        System.out.println("Delivery agent ids : 456787 456788 456789\n");

        int type_of_login, num;

        // THE MAIN CODE BEGINS FROM HERE

        while (true) {
            System.out.println("Press 1 to make an Account / Register");
            System.out.println("Press 2 to Visit / Login your account");
            System.out.println("Press -1 to exit the website");
            Scanner scan1 = new Scanner(System.in);
            num = scan1.nextInt();

            // Code for login, that is making new account
            if (num == 1) {
                // In this section we are going to deal with making of the accounts of various entities
                // This will also include entering products by manufacturers, shops entering their inventory
                // Also all the entities will get a unique 6 digit id produced by the system

                System.out.println("Who are you ?");
                System.out.println("Enter 1 for customer        Enter 2 for manufacturer\nEnter 3 for Shop           Enter 4 for Delivery Agent");

                Scanner scan2 = new Scanner(System.in);
                type_of_login = scan2.nextInt();

                // Generating the unique login id
                id_Handling id__generator = new id_Handling();
                int user_id = id__generator.id_generator(type_of_login, assigned_ids);
                assigned_ids.add(user_id);

                // Interface code goes here
                // Try to avoid redundancy in the code

                // Code for customer login
                if (type_of_login == 1) {

                    System.out.println("Please Enter Your Name : ");
                    Scanner scan_name = new Scanner(System.in);
                    String name = scan_name.nextLine();

                    System.out.println("Please Enter your zip code(single digit code) : ");
                    Scanner scan_zip = new Scanner(System.in);
                    int zip = scan_zip.nextInt();

                    Customer customer = new Customer();
                    customer.name = name;
                    customer.id = user_id;
                    customer.zipcode = zip;
                    // ADDING THE NEW CUSTOMER CLASS TO THE customer_data LIST
                    customer_data.add(customer);

                    System.out.println("Congratulations Your account has been created !");
                    System.out.println("Here are your credentials :");
                    System.out.println("Name : " + customer.name);
                    System.out.println("User id : " + customer.id);
                    System.out.println("Remember the user id, it will be used for login and ordering goods.");
                }

                // Code for manufacturer login
                if (type_of_login == 2) {
                    System.out.println("Please Enter the Name of the manufacturer : ");
                    Scanner scan_name = new Scanner(System.in);
                    String name = scan_name.nextLine();

                    Manufacturer manu1 = new Manufacturer();
                    manu1.name = name;
                    manu1.id = user_id;

                    System.out.println("Please enter number of products manufactured by you : ");
                    Scanner num_pro = new Scanner(System.in);
                    int products = num_pro.nextInt();

                    for (int i = 1; i <= products; i++) {

                        int proid;
                        while (true) {
                            System.out.println("Enter the 5 digit id of the product " + i + " : ");
                            Scanner pro = new Scanner(System.in);
                            proid = pro.nextInt();

                            id_Handling checkid = new id_Handling();
                            int product_id_valid = checkid.product_id_checker(proid);

                            if (product_id_valid == 0) {
                                System.out.println("Please enter a 5 digit numeric product id");
                                continue;
                            } else {
                                break;
                            }
                        }

                        System.out.println("Enter the name of product " + i + " : ");
                        Scanner pro1 = new Scanner(System.in);
                        String proname = pro1.nextLine();

                        // Does not tackle already existing product
                        // Assumes that the entered product does not exist in the list
                        Product product1 = new Product();
                        product1.name = proname;
                        product1.id = proid;
                        product1.id_of_manufacturer = manu1.id;
                        product1.name_of_manufacturer = manu1.name;

                        // ADDING THE NEWLY ADDED PRODUCT CLASS TO THE product_data LIST
                        product_data.add(product1);
                        manu1.product_List_manufactured.add(product1.id);
                    }

                    // ADDING THE NEWLY ADDED MANUFACTURER TO THE manufacturer_data list
                    manufacturer_data.add(manu1);

                    System.out.println("Congratulations Your account has been created !");
                    System.out.println("Here are your credentials :");
                    System.out.println("Name : " + manu1.name);
                    System.out.println("User id : " + manu1.id);

                    // Now we will print the details of the products manufactured by the manufacturer

                    System.out.println("Here are the details of products manufactured by you : ");

                    for (int i = 0; i < manu1.product_List_manufactured.size(); i++) {
                        int id_product = manu1.product_List_manufactured.get(i);

                        for (int j = 0; j < product_data.size(); j++) {

                            if (id_product == product_data.get(j).id) {
                                System.out.println("Product Name : " + product_data.get(j).name + " Id : " + product_data.get(j).id);
                            }
                        }
                    }
                    System.out.println("Remember the user id, it will be used for viewing your account.");
                }

                // Code for shop login
                if (type_of_login == 3) {
                    System.out.println("Please Enter the Name of the shop : ");
                    Scanner scan_name = new Scanner(System.in);
                    String name = scan_name.nextLine();

                    System.out.println("Please Enter your zip code(single digit code) : ");
                    Scanner scan_zip = new Scanner(System.in);
                    int zip = scan_zip.nextInt();

                    Shop shop1 = new Shop();
                    shop1.name = name;
                    shop1.id = user_id;
                    shop1.zipcode = zip;

                    // Now we have to take the inventory input of the shop
                    System.out.println("Please enter the number of products that you have : ");
                    Scanner shop_pro = new Scanner(System.in);
                    int shop_pron = shop_pro.nextInt();

                    // Taking the inputs from the shop
                    for (int i = 1; i <= shop_pron; i++) {

                        Tuple tuple1 = new Tuple();

                        int shop_pron1 = 0;     // Contains product id of newly added product

                        // Checking the validity of the product id
                        int product_id_valid = 0;

                        while (product_id_valid != 1) {
                            System.out.println("Enter the 5 digit id of the product " + i + " : ");
                            Scanner shop_pro1 = new Scanner(System.in);
                            shop_pron1 = shop_pro1.nextInt();

                            id_Handling checkid = new id_Handling();
                            product_id_valid = checkid.product_id_checker(shop_pron1);

                            if (product_id_valid == 0) {
                                System.out.println("Please enter a 5 digit numeric product id");
                                continue;
                            }
                            else {
                                // Now as the id is of 5 digits, we now have to ensure that the id is the products_data list
                                // Shops can only include those products in the inventory which are manufactured by a manufactured in our data base
                                // Only those products are in the products_data list which have registered manufacturer

                                for (int j=0; j <product_data.size(); j++){
                                    if(shop_pron1 == product_data.get(j).id){
                                        product_id_valid = 1;
                                        break;
                                    }
                                    else{
                                        product_id_valid = -1;
                                    }
                                }
                                if(product_id_valid == -1){
                                    System.out.println("Please Enter a product id which is available in the data base");
                                }
                            }
                        }


                        System.out.println("Enter the frequency of product " + i + " : ");
                        Scanner shop_pro2 = new Scanner(System.in);
                        int shop_pron2 = shop_pro2.nextInt();

                        tuple1.frequency = shop_pron2;
                        tuple1.product_id = shop_pron1;

                        // THE SHOP INVENTORY LIST CONSISTS OF THE OBJECT tuple, WHICH CONTAINS THE ID AND FREQUENCY OF THE PRODUCT IN THE SHOP
                        shop1.inventory_list.add(tuple1);

                    }

                    // ADDING THE NEWLY ADDED SHOP OBJECT TO THE shop_data list
                    shop_data.add(shop1);

                    System.out.println("Congratulations Your account has been created !");
                    System.out.println("Here are your credentials :");
                    System.out.println("Shop Name : " + shop1.name);
                    System.out.println("User id : " + shop1.id);
                    System.out.println("Zipcode : " + shop1.zipcode);

                    // Displaying the products available in the shop
                    // Ideally, only those products should be there which are mentioned by the manufacturer
                    // If the id entered by the shop is not in the product list, then it should
                    // throw error, and only ids in the product list are acceptable.

                    System.out.println("Here is the list and availability of the products in the shop : ");

                    for (int i = 0; i < shop1.inventory_list.size(); i++) {
                        // Searching for the inventory id in the product_data to display product details
                        int idobtained =  shop1.inventory_list.get(i).product_id;

                        for (int j = 0; j < product_data.size(); j++){
                            if(idobtained == product_data.get(j).id){
                                System.out.println("Product Name : " + product_data.get(j).name + "  Product id : " + product_data.get(j).id + "  Availability : " + shop1.inventory_list.get(i).frequency);
                            }
                        }
                    }

                    System.out.println("Remember the user id, it will be used for login and changing the inventory products.");
                }

                // Code for delivery agent login
                if (type_of_login == 4) {

                    System.out.println("Please Enter Your Name : ");
                    Scanner scan_name = new Scanner(System.in);
                    String name = scan_name.nextLine();

                    System.out.println("Please Enter your zip code(single digit code) : ");
                    Scanner scan_zip = new Scanner(System.in);
                    int zip = scan_zip.nextInt();

                    Delivery_agent agent = new Delivery_agent();
                    agent.name = name;
                    agent.id = user_id;
                    agent.zipcode = zip;
                    agent.no_of_products_delivered = 0;
                    delivery_agent_data.add(agent);

                    System.out.println("Congratulations Your account has been created !");
                    System.out.println("Here are your credentials :");
                    System.out.println("Name : " + agent.name);
                    System.out.println("User id : " + agent.id);
                    System.out.println("No. of products delivered : " + agent.no_of_products_delivered + "  Zipcode : " + agent.zipcode);
                    System.out.println("Remember the user id, it will be used for login and ordering goods.");
                }
            }


            // THIS PART CONTAINS ALL THE CODE FOR DISPLAYING THE ACCOUNT DETAILS OF THE ENTITY
            // THIS ALSO INCLUDES ORDERING GOODS AND DISPLAYING BROUGHT PRODUCTS BY CUSTOMERS, ADDING PRODUCTS BY MANUFACTURERS, CHANGING THE INVENTORY BY THE SHOPS

            // If num is 2, means the user wants to view the account, the account can be viewed if the user enters the correct user id
            else if (num == 2) {
                System.out.println("WELCOME TO THE LOGIN PORTAL : ");

                int out, entered_id;
                while (true) {
                    System.out.println("Please enter your Login Id : ");
                    Scanner scan11 = new Scanner(System.in);
                    entered_id = scan11.nextInt();

                    //Now we will check if the id entered is a valid id
                    // It has to be a six digits id starting from 1,2,3,4 depending on the type of entity entering the portal
                    id_Handling check = new id_Handling();
                    out = check.general_id_checker(entered_id);
                    if (out == -1) {
                        System.out.println("Please enter the valid id");
                    } else {
                        break;
                    }
                }
                // Now the id entered is correct
                // Now we will check if the id exists in the data base
                // If yes, then we will show the account, if no we will direct them to login portal

                // Account viewing for the customer, as the id entered is valid
                if (out == 1) {

                    // Checking if the account exists
                    int customer_index = -1;
                    for (int i = 0; i < customer_data.size(); i++) {
                        if (entered_id == customer_data.get(i).id) {
                            // The account exists
                            customer_index = i;
                            break;
                        }
                    }

                    // If account does not exist
                    if (customer_index == -1) {
                        System.out.println("Account does not exist, Please create your account first");
                        num = 1;
                    } else {
                        System.out.println("Hello, Welcome " + customer_data.get(customer_index).name);
                        System.out.println("Your id is " + customer_data.get(customer_index).id);
                        System.out.println("Your zip code is " + customer_data.get(customer_index).zipcode);
                        System.out.println("You have purchased the following products :");

                        if (customer_data.get(customer_index).list_of_product_purchased.size() == 0) {
                            System.out.println("None");
                        } else {
                            for (int i = 0; i < customer_data.get(customer_index).list_of_product_purchased.size(); i++) {

                                // Searching for the inventory id in the product_data to display product details
                                int idobtained_cust =  customer_data.get(customer_index).list_of_product_purchased.get(i);

                                for (int j = 0; j < product_data.size(); j++){
                                    if(idobtained_cust == product_data.get(j).id){
                                        System.out.println("Product Name : " + product_data.get(j).name + "  Product id : " + product_data.get(j).id);
                                    }
                                }
                            }
                        }

                        while (num != 1) {
                            // This section contains the order processing and account deletion part
                            System.out.println("Press 1 to process an order\nPress 2 to view the product List\nPress 3 to delete your account\nPress -1 to Logout");
                            Scanner scan12 = new Scanner(System.in);
                            int process = scan12.nextInt();

                            if (process == -1) {
                                num = 1;
                            }

                            else if(process == 2){
                                System.out.println("Product Name         Product Id");
                                for (int i = 0; i < product_data.size(); i++) {
                                    System.out.println(product_data.get(i).name + "      " + product_data.get(i).id);
                                }
                            }

                            else if (process == 2) {
                                // Account deletion code
                                int temp = customer_data.get(customer_index).id;

                                // Deleting this id from the alloted ids
                                for (int i = 0; i < assigned_ids.size(); i++) {
                                    if (temp == assigned_ids.get(i)) {
                                        assigned_ids.remove(i);
                                        break;
                                    }
                                }
                                customer_data.remove(customer_index);
                                System.out.println("Your account has been deleted");
                                num = 1;
                            } else if (process == 1) {

                                int order = 0;
                                int shop_index = 0;
                                // Here we will process the order
                                System.out.println("Enter the 5 digit product id that you want to order : ");
                                Scanner scan13 = new Scanner(System.in);
                                int product_id_enter = scan13.nextInt();

                                // Now we will search this product id in the shop inventory of the same zip code as
                                // that of the customer

                                int temp = customer_data.get(customer_index).zipcode;

                                for (int i = 0; i < shop_data.size(); i++) {

                                    if (temp == shop_data.get(i).zipcode) {
                                        for (int j = 0; j < shop_data.get(i).inventory_list.size(); j++) {
                                            if (product_id_enter == shop_data.get(i).inventory_list.get(j).product_id) {
                                                if (shop_data.get(i).inventory_list.get(j).frequency > 0) {
                                                    // Now the order can be processed
                                                    shop_data.get(i).inventory_list.get(j).frequency--;
                                                    order = 1;
                                                    shop_index = i;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (order == 0) {
                                    System.out.println("SORRY !! The product is not available with the shop");
                                } else if (delivery_agent_data.size() == 0) {
                                    System.out.println("Sorry, the order could not be processed as no delivery agents are available");
                                } else {
                                    System.out.println("Congratulations !! Your order has been processed");

                                    // Dsiplaying the product details
                                    for (int i = 0; i < product_data.size(); i++) {
                                        if (product_id_enter == product_data.get(i).id) {
                                            System.out.println("Product Name : " + product_data.get(i).name + "  Product Manufacturer : " + product_data.get(i).name_of_manufacturer);
                                            break;
                                        }
                                    }
                                    // Now we will display the shop name with zip code and assign the delivery boy
                                    System.out.println("Shop Name : " + shop_data.get(shop_index).name);
                                    System.out.println("Shop Zipcode : " + shop_data.get(shop_index).zipcode);

                                    // Assigning the delivery boy with the least number of deliveries

                                    int min = delivery_agent_data.get(0).no_of_products_delivered;
                                    int cnt = 0;
                                    for (int i = 0; i < delivery_agent_data.size(); i++) {
                                        if (delivery_agent_data.get(i).no_of_products_delivered < min) {
                                            min = delivery_agent_data.get(i).no_of_products_delivered;
                                            cnt = i;
                                        }
                                    }

                                    customer_data.get(customer_index).list_of_product_purchased.add(product_id_enter);
                                    // Should also add the name of the product along with the manufacturer

                                    delivery_agent_data.get(cnt).no_of_products_delivered++;
                                    System.out.println("Delivery agent Name : " + delivery_agent_data.get(cnt).name);
                                    System.out.println("Delivery agent Zipcode : " + delivery_agent_data.get(cnt).zipcode);
                                    System.out.println("Thanks for trusting us with the order :)");
                                }
                            }
                        }
                    }
                }

                // Account viewing for manufacturer
                else if (out == 2) {
                    int manufacturer_index = -1;
                    for (int i = 0; i < manufacturer_data.size(); i++) {
                        if (entered_id == manufacturer_data.get(i).id) {
                            // The account exists
                            manufacturer_index = i;
                            break;
                        }
                    }

                    // If account does not exist
                    if (manufacturer_index == -1) {
                        System.out.println("Account does not exist, Please create your account first");
                        num = 1;
                    }

                    // Account exists
                    else {
                        // Displaying Information of the manufacturer
                        System.out.println("Hello, Welcome " + manufacturer_data.get(manufacturer_index).name);
                        System.out.println("Your id is " + manufacturer_data.get(manufacturer_index).id);
                        System.out.println("You manufacture the following products :");

                        if (manufacturer_data.get(manufacturer_index).product_List_manufactured.size() == 0) {
                            System.out.println("None");
                        } else {
                            System.out.println(manufacturer_data.get(manufacturer_index).product_List_manufactured);

                            for (int i = 0; i < manufacturer_data.get(manufacturer_index).product_List_manufactured.size() ; i++) {

                                // Searching for the inventory id in the product_data to display product details
                                int idobtained_manu =  manufacturer_data.get(manufacturer_index).product_List_manufactured.get(i);

                                for (int j = 0; j < product_data.size(); j++){
                                    if(idobtained_manu == product_data.get(j).id){
                                        System.out.println("Product Name : " + product_data.get(j).name + "  Product id : " + product_data.get(j).id);
                                    }
                                }
                            }
                        }

                        // THIS SECTION CONTAINS DELETING THE ACCOUNT AND ADDING THE PRODUCTS MANUFACTURED

                        while (num != 1) {
                            System.out.println("Press 1 to add product manufactured\nPress 2 to delete your account\nPress -1 to Logout");
                            Scanner scan12 = new Scanner(System.in);
                            int process = scan12.nextInt();

                            if (process == -1) {
                                num = 1;
                            } else if (process == 2) {
                                // Account deletion code
                                int temp = manufacturer_data.get(manufacturer_index).id;

                                // Deleting this id from the alloted ids
                                for (int i = 0; i < assigned_ids.size(); i++) {
                                    if (temp == assigned_ids.get(i)) {
                                        assigned_ids.remove(i);
                                        break;
                                    }
                                }

                                // NOW WE HAVE TO DELETE THE MANUFACTURED IDS FROM THE products_data LIST AS THOSE PRODUCTS ALSO HAVE TO BE REMOVED
                                for (int i = 0; i < manufacturer_data.get(manufacturer_index).product_List_manufactured.size();i++){
                                    int product_id_delete = manufacturer_data.get(manufacturer_index).product_List_manufactured.get(i);
                                    // Now delete this id from the products_data list
                                    for (int j = 0; j < product_data.size();j++){
                                        if(product_id_delete == product_data.get(j).id){
                                            product_data.remove(j);
                                        }
                                    }
                                }

                                manufacturer_data.remove(manufacturer_index);
                                System.out.println("Your account has been deleted");
                                num = 1;
                            } else if (process == 1) {
                                // Here we are going to add new products made by the manufacturer
                                System.out.println("WELCOME TO THE PRODUCT ADDITION PORTAL : ");

                                System.out.println("Enter the name of the new product :");
                                Scanner scan14 = new Scanner(System.in);
                                String newname = scan14.nextLine();

                                System.out.println("Enter the id of the new product :");
                                Scanner scan15 = new Scanner(System.in);
                                int newid = scan15.nextInt();

                                // Making and adding new product
                                Product newproduct = new Product();
                                newproduct.name = newname;
                                newproduct.id = newid;
                                newproduct.name_of_manufacturer = manufacturer_data.get(manufacturer_index).name;
                                newproduct.id_of_manufacturer = manufacturer_data.get(manufacturer_index).id;

                                product_data.add(newproduct);
                                manufacturer_data.get(manufacturer_index).product_List_manufactured.add(newid);
                                System.out.println("The new product has been added in the list");
                            }
                        }
                    }
                }

                // ACCOUNT VIEWING FOR SHOP
                else if (out == 3) {
                    // Checking if the account exists
                    int shop_index = -1;
                    for (int i = 0; i < shop_data.size(); i++) {
                        if (entered_id == shop_data.get(i).id) {
                            // The account exists
                            shop_index = i;
                            break;
                        }
                    }

                    // If account does not exist
                    if (shop_index == -1) {
                        System.out.println("Account does not exist, Please create your account first");
                        num = 1;
                    }

                    // Account exists
                    else{
                        System.out.println("Hello, Welcome " + shop_data.get(shop_index).name);
                        System.out.println("Your id is " + shop_data.get(shop_index).id);
                        System.out.println("Your zip code is " + shop_data.get(shop_index).zipcode);

                        // Viewing the current inventory list
                        System.out.println("\nThe current inventory list in the shop is : ");
                        System.out.println("Product Name        Product Id         Manufacturer            Quantity in Shop");
                        for (int i = 0; i < shop_data.get(shop_index).inventory_list.size(); i++){
                            int locate_id = shop_data.get(shop_index).inventory_list.get(i).product_id;

                            // As per our program, the products in inventory are surely going to be in the product_data ArrayList
                            for (int j = 0; j < product_data.size(); j++){
                                if(locate_id == product_data.get(j).id){
                                    System.out.println(product_data.get(j).name + "       " + product_data.get(j).id + "          " + product_data.get(j).name_of_manufacturer+ "                       " + shop_data.get(shop_index).inventory_list.get(i).frequency);
                                    break;
                                }
                            }
                        }

                        while(num != 1) {

                            System.out.println("\nEnter 1 for changing the inventory list\nEnter 2 for deleting the account\nEnter -1 for logout");

                            Scanner scan15 = new Scanner(System.in);
                            int shop_input = scan15.nextInt();

                            if (shop_input == 2) {
                                // Account deletion code
                                int temp = shop_data.get(shop_index).id;

                                // Deleting this id from the alloted ids
                                for (int i = 0; i < shop_data.size(); i++) {
                                    if (temp == assigned_ids.get(i)) {
                                        assigned_ids.remove(i);
                                        break;
                                    }
                                }
                                shop_data.remove(shop_index);
                                System.out.println("Your account has been deleted");
                                num = 1;
                            }
                            else if (shop_input == -1) {
                                num = 1;
                            }

                            else if(shop_input == 1){
                                // This section gives the shop an option to edit its inventory list
                                System.out.println("WELCOME TO INVENTORY EDITING SECTION");

                                System.out.println("Enter the product id that you want to edit : ");
                                Scanner scan16 = new Scanner(System.in);
                                int editid = scan16.nextInt();

                                System.out.println("Enter number of copies that has been added : ");
                                Scanner scan17 = new Scanner(System.in);
                                int editfreq = scan17.nextInt();

                                // The product should already be available in the product_data
                                int alpha = 0;
                                for (int i = 0; i < product_data.size(); i++){
                                    if(editid == product_data.get(i).id){
                                        alpha = 1;
                                        break;
                                    }
                                }
                                if(alpha == 0){
                                    System.out.println("Sorry this product cannot be added as it is not in the products data base. No manufacturer manufactures it.");
                                }
                                else{
                                    alpha = 0;
                                    for (int i = 0; i < shop_data.get(shop_index).inventory_list.size(); i++){
                                        if(editid == shop_data.get(shop_index).inventory_list.get(i).product_id){
                                            alpha = 1; // This means that product is already a part of the inventory list of the shop
                                            shop_data.get(shop_index).inventory_list.get(i).frequency += editfreq;
                                            break;
                                        }
                                    }
                                    if(alpha == 0){
                                        // This means that the product is newely added to the shop
                                        Tuple newtuple = new Tuple();
                                        newtuple.frequency = editfreq;
                                        newtuple.product_id = editid;

                                        // Now pushing this tuple to the inventory List
                                        shop_data.get(shop_index).inventory_list.add(newtuple);
                                    }
                                    System.out.println("CONGRATULATIONS, the product has been added to the inventory");

                                }

                            }
                        }
                    }
                }


                // Account viewing of the delivery agent
                else if (out == 4) {

                    // Checking if the account exists
                    int delivery_index = -1;
                    for (int i = 0; i < delivery_agent_data.size(); i++) {
                        if (entered_id == delivery_agent_data.get(i).id) {
                            // The account exists
                            delivery_index = i;
                            break;
                        }
                    }

                    // If account does not exist
                    if (delivery_index == -1) {
                        System.out.println("Account does not exist, Please create your account first");
                        num = 1;
                    }

                    // Account exists
                    else {
                        System.out.println("Hello, Welcome " + delivery_agent_data.get(delivery_index).name);
                        System.out.println("Your id is " + delivery_agent_data.get(delivery_index).id);
                        System.out.println("Your zip code is " + delivery_agent_data.get(delivery_index).zipcode);
                        System.out.println("No. of products delivered : "+ delivery_agent_data.get(delivery_index).no_of_products_delivered);
                        System.out.println("Enter 1 for deleting the account\nEnter -1 for logout");

                        Scanner scan15 = new Scanner(System.in);
                        int process = scan15.nextInt();


                        if (process == -1) {
                            num = 1;
                        } else if (process == 1) {
                            // Account deletion code
                            int temp = delivery_agent_data.get(delivery_index).id;

                            // Deleting this id from the alloted ids
                            for (int i = 0; i < delivery_agent_data.size(); i++) {
                                if (temp == assigned_ids.get(i)) {
                                    assigned_ids.remove(i);
                                    break;
                                }
                            }
                            delivery_agent_data.remove(delivery_index);
                            System.out.println("Your account has been deleted");
                            num = 1;
                        }
                    }
                }
            }
            // Program Terminating Step
            else if(num == -1){
                System.out.println("THANKS FOR VISITING");
                break;
            }

            else{
                System.out.println("Please enter the correct number");
            }
        }
    }
}

