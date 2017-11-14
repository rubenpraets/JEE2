package session;

import java.util.Set;
import javax.ejb.Remote;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Reservation;

@Remote
public interface ManagerSessionRemote {
    
    public Set<CarType> getCarTypes(String company);
    
    public Set<Integer> getCarIds(String company,String type);
    
    public int getNumberOfReservations(String company, String type, int carId);
    
    public int getNumberOfReservations(String company, String type);
    
    public void addCarRentalCompany(CarRentalCompany crc);
    
    public void addCar(Car car, CarRentalCompany crc);
    
    public void addCarType(CarType carType);
    
    public Set<String> getBestClients();
    
    public CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year);
    
    public int getNumberOfReservationsForCarType(String carRentalName, String carType);
      
}