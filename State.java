
package bookstoreapp;


public abstract class State {
    
    public abstract void setGold(Customer c);
    public abstract void setSilver(Customer c);
    public abstract String toString();

}
