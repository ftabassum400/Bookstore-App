
package bookstoreapp;





// customer class contains all the information for a single customer object
public class Customer {
    private String username;
    private String password;
    private int points; // starting points of the customer is 0, will be controlled by purchase

    
    public State state;
    
    
    // initialize customer object
    public Customer() {
        this.username = "";
        this.password = "";
        this.points = 0;
        this.state = new Silver(); // customer starting state is silver
        
    }
    
    // customer class constructor
    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;

        
        if (this.points > 1000) { // set state if points is added
            state = new Gold();
        } else {
            state = new Silver();
        }
    }
    
    //customer setters
    public void setUsername(String addUsername) {
        username = addUsername;
    }
    public void setPassword(String addPassword) {
        password = addPassword;
    }
    public void setPoints(int addPoints) {
        points = addPoints;
    }
    public void setState(State s) { // manually set state of object
        state = s;
    }
    
    // methods to make the customer instance silver or gold
    public void setSilver() {
        state.setSilver(this);
    }
    public void setGold() {
        state.setGold(this);
    }
    
    
      // customer getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getPoints() {
        return points;
    }
    public State getState() { // return state to user
        if (points > 1000) {
            setSilver();
        } else {
            setGold();            
        }
        return state;
    }
    
    // used to save customer instance into customers.txt
    @Override
    public String toString() {
        return "" + username + "|" + password + "|" +points;
    }
    
    public String welcomeMessage() {
        return "Welcome " + username +". You have " + points + " points. Your status is " + state.toString();
    }
}

