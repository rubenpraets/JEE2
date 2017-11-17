package rental;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;

@Entity
public class Car implements Serializable{

    private int id;
    private CarType type;
    private Set<Reservation> reservations;
    
    private EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarRental-ejbPU");
        EntityManager ecm = emf.createEntityManager(); 
        return ecm;
    }  

    /***************
     * CONSTRUCTOR *
     ***************/
    
    public Car(int uid, CarType type) {
    	this.id = uid;
        this.type = type;
        this.reservations = new HashSet<>();
    }
    
    public Car(){
        
    }

    /******
     * ID *
     ******/
    
    @Id
    public int getId() {
    	return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    /************
     * CAR TYPE *
     ************/
    
    @ManyToOne
    public CarType getType() {
        return type;
    }
	
	public void setType(CarType type) {
		this.type = type;
	}
    /****************
     * RESERVATIONS *
     ****************/

    public boolean isAvailable(Date start, Date end) {
        if(!start.before(end))
            throw new IllegalArgumentException("Illegal given period");
        System.out.println("Car of type " + this.getType().getName() + " checking own availability");

        //for(Reservation reservation : reservations) {
            //System.out.println("present reservation: " + reservation);
            //if(reservation.getEndDate().before(start) || reservation.getStartDate().after(end))
                //continue;
            //return false;
        //}
        List<Reservation> overlapList = getEntityManager().createQuery("SELECT r "
                +      "FROM Reservation r "
                +      "WHERE r.car = :car "
                +      "AND NOT ( r.startDate > :end OR r.endDate < :start)")
                       .setParameter("start",start)
                       .setParameter("end",end)
                       .setParameter("car",this).getResultList();
        return overlapList.isEmpty();
    }
    
    public void addReservation(Reservation res) {
        Set<Reservation> newReservations = getReservations();
        newReservations.add(res);
        setReservations(newReservations);
    }
    
    public void removeReservation(Reservation reservation) {
        // equals-method for Reservation is required!
        Set<Reservation> newReservations = getReservations();
        newReservations.remove(reservation);
        setReservations(newReservations);
    }

    @OneToMany(mappedBy="car", cascade=ALL, fetch=EAGER)
    public Set<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(Set<Reservation> res){
        reservations = res;
    }
}