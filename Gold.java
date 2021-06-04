
package bookstoreapp;


public class Gold extends State {

    @Override
    public void setGold(Customer c) { // do nothing
    }

    @Override
    public void setSilver(Customer c) { // make gold customer into silver
        c.setState(new Silver());
    }
    
    @Override
    public String toString() {
        return "Gold";
    }
    
    
}
