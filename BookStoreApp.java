
package bookstoreapp;

//import java.awt.event.ActionEvent;
import javafx.application.Application;
//import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
//import javafx.scene.layout.StackPane; // unused because are using grid layout instead of stackpane layout for login screen
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
//import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
//import java.util.Iterator;
//import java.io.FileOutputStream;
//import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.scene.control.CheckBox;


public class BookStoreApp extends Application {
    // login screen buttons
    Button loginBtn = new Button("Login"); 
    Button booksBtn = new Button("Books");
    Button customersBtn = new Button("Customers");
    
    // admin screen buttons
    Button logoutBtn = new Button("Logout");
    Button addCustomerBtn = new Button("Add"); // admin screen add customer button
    Button deleteCustomerBtn = new Button("Delete");
    Button adminCustomerBack = new Button("Back");
    
    // admin book table buttons
    Button addBookBtn = new Button("Add");
    Button deleteBookBtn = new Button("Delete");
    
    //customer bookstore screen buttons
    Button cBuyBtn = new Button("Buy");
    Button cBuyAndRedeemBtn = new Button("Redeem points & Buy");
    
    //Username and password textfields for login screen
    TextField txtUser = new TextField(); // username textfield 
    PasswordField pwBox = new PasswordField(); // password textfield
    
    // username and password textfields for admin to register new customers
    TextField addUsername, addPassword;
    // book name and book price textfields for admin to register new customers
    TextField addBookName, addBookPrice;
    
    // customer arraylist 
    private ArrayList<Customer> currentCustomersList = new ArrayList<>();
    
    // customer list seen by admin in table
    final ObservableList<Customer> customersList = FXCollections.observableArrayList();
    // books list seen by admin in table
    final ObservableList<Books> booksList = FXCollections.observableArrayList();
    
    // NEW!! selectedBooksObservable used books for customer book screen
    ObservableList<Books> booksData = FXCollections.observableArrayList();
    
    //new arraylist for loading in customers from file
    ArrayList<Customer> loadCustomersFromFile = new ArrayList<>();
    // new arrylist for loading books from file
    ArrayList<Books> loadBooksFromFile = new ArrayList<>();
    
    // customer selected books arraylist
    ArrayList<Books> cSelectedBooks = new ArrayList<>();
    
    // arraylist of showable customer (declared at the top so all methods can see it) - RELATEDto deleteCustomersButton() method
    ArrayList<Customer> showableCustomer = new ArrayList<>();
    // arraylist of showable book - realted to deleteBookBtn
    ArrayList<Books> showableBook = new ArrayList<>();
    
    //new table of type customer in customer admin screen
    TableView<Customer> customerTable = new TableView<>();
    
    //NEW!!! table of type books (monday)
    TableView<Books> bookTable = new TableView<>();
    TableView<Books> adminBookTable = new TableView<>();
   
    // single customer instance used to store customer info when they log in
    Customer customerInstance;
    
    // file path to the customer.txt file
    private static final String customerListFilePath = "customers.txt";
    // customer points
    private int cNPoints;
    
    
 
    // this reads the customers from the file and returns it as an arraylist that another arraylist will take
    public static ArrayList<Customer> readCustomersFromFile(String fileName) throws FileNotFoundException{
        File customerFile = new File(fileName);
        Scanner s = new Scanner(customerFile);
        
        ArrayList<Customer> customerListV2 = new ArrayList<>();
        
        while (s.hasNextLine()) {
            String line = s.nextLine();
            
            String[] items = line.split("\\|");
            
            String username = items[0];
            String password = items[1];
            int points = Integer.parseInt(items[2]);
            
            Customer newCustomer = new Customer(username, password, points);
            customerListV2.add(newCustomer);
            
        }
        return customerListV2;
    }
    // this reads the books from booksList.txt and returns it as an arraylist
    public static ArrayList<Books> readBooksFromFile(String fileName) throws FileNotFoundException {
        File bookFile = new File(fileName);
        Scanner bookScanner = new Scanner(bookFile);
        
        ArrayList<Books> bookListV2 = new ArrayList<>();
        
        // could use hasNext() so it can accept spaces
        while (bookScanner.hasNextLine()) {
            String line = bookScanner.nextLine();
            
            String[] items = line.split("\\|");
            
            String bookName = items[0];
            double bookPrice = Double.parseDouble(items[1]);
            
            Books newBook = new Books(bookName, bookPrice);
            bookListV2.add(newBook);
            
        }
        return bookListV2;
        
    }
    

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(showLoginScreen(), 500, 200); // this modifes the size of the window (width, length)
        
        primaryStage.setTitle("BookStore App");
        primaryStage.setScene(scene); // sets the scene in the stage
        primaryStage.show(); // shows the primary stage
        
        // read the customers from the customers.txt file and insert them into an arraylist called loadCustomersFromFile
        try {
            loadCustomersFromFile = readCustomersFromFile("customers.txt"); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // read books from bookList.txt file and insert them into an arraylist called loadBooksFromFile
        try {
            loadBooksFromFile = readBooksFromFile("booksList.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
        }
 
                 
        // use this code here to control the logic of the login function 
        loginBtn.setOnAction((javafx.event.ActionEvent e) -> {
            System.out.println("Login Button Clicked!");
            
            if (txtUser.getText().equals("admin") && pwBox.getText().equals("admin") ) { // verifies if admin is logging in
                System.out.println("Welcome admin!");
                primaryStage.setScene(new Scene(showAdminScreen(), 500, 200));
            }
            else { // verifies if customer is logging in
                for(int i = 0; i<loadCustomersFromFile.size(); i++) {
                    if (loadCustomersFromFile.get(i).getUsername().equals(txtUser.getText()) && loadCustomersFromFile.get(i).getPassword().equals(pwBox.getText())) {
                        System.out.println("correct password!");
                        customerInstance = new Customer(loadCustomersFromFile.get(i).getUsername(), loadCustomersFromFile.get(i).getPassword(), loadCustomersFromFile.get(i).getPoints() );
                        
                        primaryStage.setScene(new Scene(showCustomerScreen(), 700, 200));
                    }
                    else { System.out.println("wrong password, try again"); } // can show this multiple times until correct password is shown
                }
            }
        });
        
        
        
        

        //BUTTON ACTION EVENTS
        // show customer table screen when customers button is clicked in admin menu
        customersBtn.setOnAction((javafx.event.ActionEvent e) -> {
            primaryStage.setScene(new Scene(customerAdminScreenV2(), 750, 400));
        });
        
        // show admin book table screen when books button is clicked in admin menu
        booksBtn.setOnAction((javafx.event.ActionEvent e) -> {
            primaryStage.setScene(new Scene(showBookScreen(), 500, 200));
        });
        
        // add new customer when addCustomerBtn is clicked
        addCustomerBtn.setOnAction(e -> addCustomerBtnClicked());
        
        // delete customer when deleteCustomerBtn is clicked
        deleteCustomerBtn.setOnAction(e -> deleteCustomerBtnClicked());
        
        //logic of back button from admin customer table (can also be used for 
        adminCustomerBack.setOnAction((javafx.event.ActionEvent e) -> {
            primaryStage.setScene(new Scene(showAdminScreen(), 500, 200) ); // goes back to default admin screen
            // prevents duplicate columns if log back in
            customerTable.getColumns().clear();
            adminBookTable.getColumns().clear();
        });
        
        //code which controls the logic of the logout button
        logoutBtn.setOnAction((javafx.event.ActionEvent e) -> {
            primaryStage.setScene(new Scene(showLoginScreen(),500, 200));
            // prevents duplicate columns if log back in 
            bookTable.getColumns().clear();
        });
        //code that controls the buy button 
        cBuyBtn.setOnAction((javafx.event.ActionEvent e) -> {
            cNPoints = ((int) buyBooks())*10;
            primaryStage.setScene(new Scene(showPurchaseScreen(),500, 200));

        });
        // code that controls the buy and redeem button
        cBuyAndRedeemBtn.setOnAction((javafx.event.ActionEvent e) -> {
            cNPoints = (int) buyBooksWPoints(); 
            primaryStage.setScene(new Scene(showPurchaseScreen(),500, 200));

        });        
        
        // add book when addBookBtn is clicked
        addBookBtn.setOnAction(e -> addBookBtnClicked());
        // remove book when removeBookBtn is clicked
        deleteBookBtn.setOnAction(e -> deleteBookBtnClicked());
        
        // BUTTON ACTION EVENTS END
        
        
        
    }// PRIMARY STAGE END
    
    //BEGINNING OF BUTTON METHODS AND SCREEN TEMPLATES AND SAVING METHODS
    // addCustomerBtn logic
    public void addCustomerBtnClicked() {
        Customer customer = new Customer();
        customer.setUsername(addUsername.getText());
        customer.setPassword(addPassword.getText());
        customer.setPoints(0);
        customerTable.getItems().add(customer);
        currentCustomersList.add(customer);
        
        // save newly added customer to customers.txt file
        try {
            saveCustomerToFile(customer);
        } catch (IOException ex) {
        }
        //
        
        addUsername.clear();
        addPassword.clear();
        
    }
    
    // code used to save the new customer to the customers.txt file
    public static void saveCustomerToFile(Customer customer) throws IOException {
        FileWriter cw = new FileWriter("customers.txt", true); // cw stands for customer writer
        PrintWriter pc = new PrintWriter(cw); // cw stands for print customer to file
        
        String text = customer.toString();
  
        pc.println(text); // print text to file
        pc.close();
    }
    
    // method to calculate total price
    public double buyBooks() {
        double totalCost =0;
        ObservableList<Books> bSelected = FXCollections.observableArrayList();
       
        // step 1 move selected books into an observable arraylist
        for(Books bean: booksData) {
            if (bean.getSelect().isSelected()) {
                bSelected.add(bean);
            }   
        }
        // step2 convert observable arraylist of books into an arraylist 
        cSelectedBooks = new ArrayList<Books>(bSelected);
        //step3 calculate total cost
        for(Books item: cSelectedBooks) {
            totalCost += item.getBookPrice();
        }
        return totalCost;
    }
    // returns the amnt of points being deducted from customer
    public double buyBooksWPoints() {
        double cPoint = customerInstance.getPoints();
        double totalCost = 0;
        ObservableList<Books> bSelected = FXCollections.observableArrayList();
       
        // step 1 move selected books into an observable arraylist
        for(Books bean: booksData) {
            if (bean.getSelect().isSelected()) {
                bSelected.add(bean);
            }   
        }
        // step2 convert observable arraylist of books into an arraylist 
        cSelectedBooks = new ArrayList<Books>(bSelected);
        //step3 calculate total cost
        for(Books item: cSelectedBooks) {
            totalCost += item.getBookPrice();
        }
 
        // should return the amount of points being deducted
        cPoint =  -(totalCost*10);
        return cPoint; 
    }
    
  
    // SCREEN THAT CUSTOMER SEES WHEN THEY LOG IN
    public VBox showCustomerScreen() {

        // Book name column
        TableColumn<Books, String> booksColumn = new TableColumn<>("Books");
        booksColumn.setMinWidth(250);
        booksColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        // Book Price column
        TableColumn<Books, Double> bookPriceColumn = new TableColumn<>("Book Price");
        bookPriceColumn.setMinWidth(250);
        bookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
        
        // Select Books column
        TableColumn selectCol = new TableColumn("Select");
        selectCol.setCellValueFactory(
                            new PropertyValueFactory<Books, String>("select"));
        
        booksData = getBooks();
        bookTable.setItems(booksData);
        bookTable.getColumns().addAll(booksColumn, bookPriceColumn, selectCol);
        
       // customer welcome message
        final HBox cWelcome = new HBox();
        Text customerLWelcome = new Text("Welcome " +customerInstance.getUsername()+ 
                ". Your points is " +customerInstance.getPoints()+ ". Your status is " +customerInstance.state.toString()+ ".");
        customerLWelcome.setFont(Font.font("Roboto", FontWeight.LIGHT, 15));
        cWelcome.getChildren().addAll(customerLWelcome);
        
        // cBuy, cReedemAndBuy, logout
        final HBox customerPOptions = new HBox();
        customerPOptions.getChildren().addAll(cBuyBtn, cBuyAndRedeemBtn, logoutBtn);
        customerPOptions.setSpacing(5);
       
        // inserting table and components into scene
        final VBox showBookTable = new VBox();
        showBookTable.setSpacing(5);
        showBookTable.setPadding(new Insets(10, 10, 10, 10));
        showBookTable.getChildren().addAll(cWelcome, bookTable, customerPOptions);
        
        

        return showBookTable;
        
    }
    
    //BOOKS IMPLEMENTATION START--------------------------------------------------------------------------------------------------/
    
    //ADMIN BOOK SCREEN
    public VBox showBookScreen() {
        
        // Book name column
        TableColumn<Books, String> booksColumn = new TableColumn<>("Books");
        booksColumn.setMinWidth(250);
        booksColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        // Book Price column
        TableColumn<Books, Double> bookPriceColumn = new TableColumn<>("Book Price");
        bookPriceColumn.setMinWidth(250);
        bookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
        
        // fill columns with data from bookList.txt
        adminBookTable.setItems(getBooks());
        adminBookTable.getColumns().addAll(booksColumn, bookPriceColumn);      
        
        // add new book textfields 
        addBookName = new TextField();
        addBookName.setPromptText("Book Name");
        addBookName.setMinWidth(150);
        addBookPrice = new TextField();
        addBookPrice.setPromptText("Book Price");
        addBookPrice.setMinWidth(150);
        
        // add textfields and buttons in horizontal box
        final HBox aBookScrOptions = new HBox();
        aBookScrOptions.getChildren().addAll(addBookName, addBookPrice, addBookBtn, deleteBookBtn, adminCustomerBack);
        aBookScrOptions.setSpacing(5);
       
        // inserting table and components into scene
        final VBox showABookTable = new VBox();
        showABookTable.setSpacing(5);
        showABookTable.setPadding(new Insets(10, 10, 10, 10));
        showABookTable.getChildren().addAll(adminBookTable, aBookScrOptions);        
        
        return showABookTable;
    }
   
    // returns an observable list called booksList 
    public ObservableList<Books> getBooks() {
        int a;
        booksList.removeAll(booksList); 

        for (a = 0; a<loadBooksFromFile.size(); a++) { // iterates through loadBooksFromFile arraylist
            booksList.add(new Books( // loads the books in the loadBookFromFile arraylist into the booksList arraylist
                    loadBooksFromFile.get(a).getBookName(), 
                    loadBooksFromFile.get(a).getBookPrice()));
        }
        return booksList;
        
    }
    
    // code used to save the new book to the booksList.txt file
    public static void saveBookToFile(Books book) throws IOException {
        FileWriter bw = new FileWriter("booksList.txt", true); // bw stands for book writer
        PrintWriter pb = new PrintWriter(bw); // pb stands for print book to file
        
        String text = book.toString(); // book toString to put text into textfile
  
        pb.println(text); // print text to file
        pb.close();
    }
    
    //add bookBtnClicked logic
    public void addBookBtnClicked() {
        Books book = new Books();
        book.setBookName(addBookName.getText());
        book.setBookPrice(Double.parseDouble(addBookPrice.getText()));

        adminBookTable.getItems().add(book);

        // save newly added customer to customers.txt file
        try {
            saveBookToFile(book);
        } catch (IOException ex) {
        }
      
        addBookName.clear();
        addBookPrice.clear();
        
    }    
    
    
    // delte book btn logic
    public void deleteBookBtnClicked() {
        ObservableList<Books> bookSelected, allBooks;
        allBooks = adminBookTable.getItems();
        bookSelected = adminBookTable.getSelectionModel().getSelectedItems();
        bookSelected.forEach(allBooks::remove);
        
        
        //THIS WHOLE CODE BLOCK IS USED TO DELETE books FROM THE CUSTOMERS LIST
        //STEP 1: convert observable list into an arraylist (this is with the customer deleted)
        showableBook = new ArrayList<Books>(adminBookTable.getItems());
        System.out.println("the book has been deleted from the arraylist");
        
        //STEP 2: clear the customers.txt file 
        PrintWriter bwriter = null;
        try {
            bwriter = new PrintWriter("booksList.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        bwriter.close();
        
        //STEP 3: loop converted osbervable list to add the updated book arraylist to the empty customers.txt file 
        for (int i = 0; i<showableBook.size(); i++ ) {
            try {
                saveBookToFile(showableBook.get(i));
            } catch (IOException ex) {
                Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        System.out.println("the booksList.txt file has been updated with the new list of customers");
        
        //STEP 4: refresh the observable arraylist
        
        //clear the loadBooksFromFile arraylist and add the items from the converted arraylist (the updated) to the loadBooksFromFile arraylist
        loadBooksFromFile.clear();
        for(int i = 0; i<showableBook.size(); i++) {
            loadBooksFromFile.add(
                    new Books(showableBook.get(i).getBookName(), 
                                 showableBook.get(i).getBookPrice()));
                                 
        }
        System.out.println("the observable arraylist has been updated, so if you press the back button, the up to date arraylist is displayed");
        // END OF CODE BLOCK FOR DELETING BOOKS FROM BOOKS LIST
        
         
    }
 
    //BOOKS IMPLEMENTATION END--------------------------------------------------------------------------------------------------/
    
    
    
    // CUSTOMER PURCHASE SCREEN
    public VBox showPurchaseScreen() {
        

        
        // save customer points to file        
        //STEP 2: clear the customers.txt file 
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(customerListFilePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        writer.close();
        
        //STEP 3.2: update customer  points
        customerInstance.setPoints(customerInstance.getPoints()+cNPoints);
        //STEP 3.3 update customer points in loadCustomersFromFile arraylist
        for (int i = 0; i < loadCustomersFromFile.size(); i++) {
            if (loadCustomersFromFile.get(i).getUsername().equals(customerInstance.getUsername())) {
                loadCustomersFromFile.get(i).setPoints(customerInstance.getPoints());
            }
        }

        //STEP 4: loop through loadCustomersFromFIle to add the updated customer points to the empty customers.txt file 
        for (int i = 0; i<loadCustomersFromFile.size(); i++ ) {
            try {
                saveCustomerToFile(loadCustomersFromFile.get(i));
            } catch (IOException ex) {
                Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        System.out.println("the customers.txt file has been updated with the new points of customers"); 
        

        final VBox purchaseText = new VBox();
        purchaseText.setAlignment(Pos.CENTER);
        purchaseText.setSpacing(5);
        
        Text cTotalCost = new Text("Total Cost: " + buyBooks());
        cTotalCost.setFont(Font.font("Roboto", FontWeight.LIGHT, 15));
        
        Text cPointsAndStatus = new Text("Your New Points: " + (customerInstance.getPoints()) + " Status: " +customerInstance.state.toString());
        cPointsAndStatus.setFont(Font.font("Roboto", FontWeight.LIGHT, 15));
     
        purchaseText.getChildren().addAll(cTotalCost, cPointsAndStatus, logoutBtn);
        // reset points incase customer logsout so most updated points is always shown
        cNPoints = 0;
        return purchaseText;
        
    }
    
    
    
    
    // delete customer still a WIP
    public void deleteCustomerBtnClicked() {
        
        ObservableList<Customer> customerSelected, allCustomers;
        allCustomers = customerTable.getItems();
        customerSelected = customerTable.getSelectionModel().getSelectedItems();
        customerSelected.forEach(allCustomers::remove);
        
        
        //THIS WHOLE CODE BLOCK IS USED TO DELETE CUSTOMERS FROM THE CUSTOMERS LIST
        //STEP 1: convert observable list into an arraylist (this is with the customer deleted)
        showableCustomer = new ArrayList<Customer>(customerTable.getItems());
        System.out.println("the customer has been deleted from the arraylist");
        
        //STEP 2: clear the customers.txt file 
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(customerListFilePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        writer.close();
        
        //STEP 3: loop converted osbervable list to add the updated customer arraylist to the empty customers.txt file 
        for (int i = 0; i<showableCustomer.size(); i++ ) {
            try {
                saveCustomerToFile(showableCustomer.get(i));
            } catch (IOException ex) {
                Logger.getLogger(BookStoreApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        System.out.println("the customers.txt file has been updated with the new list of customers");
        
        //STEP 4: refresh the observable arraylist
        
        //clear the loadCustomersFromFile arraylist and add the items from the converted arraylist (the updated) to the loadCustomersFromFile arraylist
        loadCustomersFromFile.clear();
        for(int i = 0; i<showableCustomer.size(); i++) {
            loadCustomersFromFile.add(
                    new Customer(showableCustomer.get(i).getUsername(), 
                                 showableCustomer.get(i).getPassword(),
                                 showableCustomer.get(i).getPoints() ));
        }
        System.out.println("the observable arraylist has been updated, so if you press the back button, the up to date arraylist is displayed");
        // END OF CODE BLOCK FOR DELETING CUSTOMERS FROM CUSTOMERS LIST
        
        
    }
        
    
    public VBox customerAdminScreenV2() {
        // Username column
        TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(250);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Password column
        TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setMinWidth(250);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Points column
        TableColumn<Customer, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setMinWidth(250);
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        
        // setting up customer table by adding all the data
        customerTable.setItems(getCustomers());
        customerTable.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);
        
        //adding new customer textfields and button declaration
        addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setMinWidth(150);
        addPassword = new TextField();
        addPassword.setPromptText("Password");
        addPassword.setMinWidth(150);
        
        final HBox aCustomerScrOptions = new HBox();
        aCustomerScrOptions.getChildren().addAll(addUsername, addPassword, addCustomerBtn, deleteCustomerBtn, adminCustomerBack);
        aCustomerScrOptions.setSpacing(5);
       
        // inserting table and components into scene
        final VBox showCustomerTable = new VBox();
        showCustomerTable.setSpacing(5);
        showCustomerTable.setPadding(new Insets(10, 10, 10, 10));
        showCustomerTable.getChildren().addAll(customerTable, aCustomerScrOptions);
        

        return showCustomerTable;
        
    }
    
    
    // updating the table (to be used for later)
    public ObservableList<Customer> getCustomers() {
        int a;
        
        // this refreshes the table so the updated data (no duplicates) is shown all the time        
        customersList.removeAll(customersList); 

        for (a = 0; a<loadCustomersFromFile.size(); a++) {
            customersList.add(new Customer(
                    loadCustomersFromFile.get(a).getUsername(), 
                    loadCustomersFromFile.get(a).getPassword(),
                    loadCustomersFromFile.get(a).getPoints()) );
        }
        return customersList;
        
    }
    
    // admin screen formatting
    public GridPane showAdminScreen() {
        GridPane gp2 = new GridPane();
        gp2.setAlignment(Pos.CENTER);
        // buttons
        gp2.add(booksBtn, 0, 1); gp2.add(customersBtn, 0, 2); gp2.add(logoutBtn, 0, 3); // adds books, customres, and logout button
        gp2.setVgap(10);
        booksBtn.setMinHeight(35); customersBtn.setMinHeight(35); logoutBtn.setMinHeight(35);
        booksBtn.setMinWidth(100); customersBtn.setMinWidth(100); logoutBtn.setMinWidth(100);

        return gp2;
    }

    // login screen formatting
    public GridPane showLoginScreen() { // seperated main screen into it's own class so when the login button is clicked, it goes away. 
        GridPane mainScreen = new GridPane();
        mainScreen.setAlignment(Pos.CENTER);
        mainScreen.setVgap(10);
        mainScreen.setHgap(10);
        mainScreen.setPadding(new Insets(10));
        
        Text welcomeTxt = new Text("Welcome to the BookStore app");
        welcomeTxt.setFont(Font.font("Roboto", FontWeight.LIGHT, 15));
        mainScreen.add(welcomeTxt, 0, 0); // this makes sure the welcome text is shown ontop of the username and password
        
        // username text
        Label lblUser = new Label("Username:"); 
        mainScreen.add(lblUser, 0 , 1);
        
        // username textbox
        txtUser.setPromptText("username");
        
        // password text
        Label lblPassword = new Label("Password:"); 
        mainScreen.add(lblPassword, 0 , 2);
        pwBox.setPromptText("password"); // password textbox
        
        // add password textbox and username textboxes to screen
        mainScreen.add(txtUser, 1, 1); 
        mainScreen.add(pwBox, 1, 2); // password textbox
        mainScreen.add(loginBtn, 1, 3); // add login button to mainScreen
         
        return mainScreen; // returns all the formatting of the main screen, defined above, when createMainScreen() is called so it can be shown to the user
    }
    
    // THE MAIN 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
}
