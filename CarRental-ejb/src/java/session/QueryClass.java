/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.CarRentalCompany;

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
    
    public CarRentalCompany getRental(String name){
        return  (CarRentalCompany) em.createQuery("SELECT c FROM CarRentalCompany c WHERE c.name LIKE :name")
                .setParameter("name", name).getSingleResult();
    }
    
}
