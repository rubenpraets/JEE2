package session;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
//import rentalstore.RentalStore;
import rental.Reservation;

@Stateless
public class ManagerSession implements ManagerSessionRemote {
    
    private QueryClass queryClass = new QueryClass();
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Set<CarType> getCarTypes(String company) {
        try {
            return new HashSet<CarType>(queryClass.getRental(company).getAllTypes());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Set<Integer> getCarIds(String company, String type) {
        Set<Integer> out = new HashSet<>();
        try {
            for(Car c: queryClass.getRental(company).getCars(type)){
                out.add(c.getId());
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return out;
    }

    @Override
    public int getNumberOfReservations(String company, String type, int id) {
        try {
            return queryClass.getRental(company).getCar(id).getReservations().size();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public int getNumberOfReservations(String company, String type) {
        Set<Reservation> out = new HashSet<>();
        try {
            for(Car c: queryClass.getRental(company).getCars(type)){
                out.addAll(c.getReservations());
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return out.size();
    }

    @Override
    public void addCarRentalCompany(CarRentalCompany crc) {
        em.persist(crc);
    }

    @Override
    public void addCar(Car car, CarRentalCompany crc) {
        crc.addCar(car);
        em.persist(car);
    }

    @Override
    public void addCarType(CarType carType) {
        em.persist(carType);
    }

    @Override
    public Set<String> getBestClients() {
        return queryClass.bestClients();
    }

    @Override
    public CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) {
        return queryClass.mostPopularCarType(carRentalCompanyName, year);
    }

    @Override
    public int getNumberOfReservationsForCarType(String carRentalName, String carType) {
        return queryClass.getNbResOfCTInCRC(carRentalName, carType);
    }
    
    

}