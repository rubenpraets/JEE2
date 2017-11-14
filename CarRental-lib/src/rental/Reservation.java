package rental;

import java.util.Date;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;

@Entity
public class Reservation extends Quote {
    
    @Id
    @GeneratedValue(strategy=IDENTITY)
    private int id;
    
    @ManyToOne(cascade=PERSIST)
    private Car car;
    
    /***************
     * CONSTRUCTOR *
     ***************/

    public Reservation(Quote quote, Car car) {
    	super(quote.getCarRenter(), quote.getStartDate(), quote.getEndDate(), 
    		quote.getRentalCompany(), quote.getCarType(), quote.getRentalPrice());
        this.car = car;
    }
    
    public Reservation(){
        
    }
    
    /******
     * ID *
     ******/
    
    public int getCarId() {
    	return car.getId();
    }
    
    public int getId() {
    	return id;
    }
    
    /*************
     * TO STRING *
     *************/
    
    @Override
    public String toString() {
        return String.format("Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f", 
                getCarRenter(), getStartDate(), getEndDate(), getRentalCompany(), getCarType(), getCarId(), getRentalPrice());
    }	
}