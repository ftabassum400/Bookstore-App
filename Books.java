
package bookstoreapp;

import javafx.scene.control.CheckBox;


// class contains all the information for a single book object
public class Books {
    
    private String bookName;
    private double bookPrice;
    private CheckBox select;
    
    public Books() {
        this.bookName = "";
        this.bookPrice = 0.0;
//        this.select = new CheckBox();
    }
   
    public Books(String bookName, double bookPrice) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.select = new CheckBox();
    }
    
    // book setters
    public void setBookName(String bookN) {
        bookName = bookN;
    }
    public void setBookPrice(double bookP) {
        bookPrice = bookP;
    }
    public void setSelect(CheckBox select) {
        this.select = select;
    }
    
    // book getters
    public String getBookName() {
        return bookName;
    }
    public double getBookPrice() {
        return bookPrice;
    }
    public CheckBox getSelect() {
        return select;
    }

    
    
    @Override
    public String toString() {
        return "" + bookName + "|" + bookPrice;
    }
    
    
}
