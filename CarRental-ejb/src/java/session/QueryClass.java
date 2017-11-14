/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.CarRentalCompany;
import rental.CarType;

/**
 *
 * @author hp
 */
public class QueryClass {
    
    @PersistenceContext
    private EntityManager em;
    
    public Map<String,CarRentalCompany> getRentals(){
        Map<String,CarRentalCompany> rentals = new HashMap<>();
        for(CarRentalCompany c: (List<CarRentalCompany>)em.createQuery("SELECT c FROM CarRentalCompany c").getResultList()){
            rentals.put(c.getName(),c);
        }
        return rentals;
    }
    
    /*public CarRentalCompany getRental(String name){
        return  (CarRentalCompany) em.createQuery("SELECT c FROM CarRentalCompany c WHERE c.name LIKE :name")
                .setParameter("name", name).getSingleResult();
    }*/
    
    public CarRentalCompany getRental(String name){
        return (CarRentalCompany) em.find(CarRentalCompany.class, name);
    }
    
    public ArrayList<CarType> getCarTypes(String companyName) {
	return (ArrayList<CarType>)em.createQuery("SELECT DISTINCT t " +
                                                  "FROM CarRentalCompany c, " +
                                                  "IN (c.carTypes) t " +
                                                  "WHERE c.name LIKE :name")
                                                  .setParameter("name", companyName).getResultList();
    }
    
    /*public int getNbResOfCTInCRC(String companyName, String carType) {
        return (int) em.createQuery("SELECT COUNT (r) " +
                                    "FROM Reservation r JOIN CarRentalCompany crc " +
                                    "ON  r.rentalCompany = crc " +
                                    "WHERE r.carType.name LIKE :type " +
                                    "AND crc.name LIKE :name").setParameter("type", carType)
                                    .setParameter("name", companyName).getSingleResult();
    }*/
    
    public int getNbResOfCTInCRC(String companyName, String carType) {
        return (int) em.createQuery("SELECT COUNT (r) " +
                                    "FROM Reservation r " +
                                    "WHERE r.carType.name LIKE :type " +
                                    "AND r.rentalCompany.name LIKE :name").setParameter("type", carType)
                                    .setParameter("name", companyName).getSingleResult();
    }
    
    public Set<String> bestClients(){
        return new HashSet<>(em.createQuery("SELECT y.carRenter " +
                                            "FROM (SELECT COUNT(*) AS num" +
                                            "      FROM Reservation r " +
                                            "      GROUP BY r.carRenter) y " +
                                            "HAVING y.num = MAX(y.num)").getResultList());
    }
    
    public CarType mostPopularCarType(String company, int year){
        return (CarType) em.createQuery("SELECT y.carType " +
                                        "FROM (SELECT COUNT(*) AS num" +
                                        "      FROM Reservation r " +
                                        "      WHERE r.rentalCompany.name LIKE :company," +
                                        "      r.startDate.year = :year" +
                                        "      GROUP BY r.carType) y " +
                                        "HAVING MAX(y.num)")
                                        .setParameter("company", company).setParameter("year", year).getSingleResult();
    }
    
    //TODO niet af
    public String cheapestCarType(Date start, Date end, String region){
        /*return (String) em.createQuery("SELECT c.name " + 
                                       "FROM CarType c " +
                                       "WHERE c.region").getSingleResult();*/
        return null;
    }
    
}
    
