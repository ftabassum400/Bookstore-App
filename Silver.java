
package bookstoreapp;


public class Silver extends State {

    @Override
    public void setGold(Customer c) {
        c.setState(new Gold());
    }

    @Override
    public void setSilver(Customer c) { // do nothing
        
    }
    
    @Override
    public String toString() {
        return "Silver";
    }
}
