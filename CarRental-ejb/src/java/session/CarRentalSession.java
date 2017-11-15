package session;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
//import rentalstore.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {

    private String renter;
    private List<Quote> quotes = new LinkedList<Quote>();
    private QueryClass queryClass = new QueryClass();

    //TODO move query
    @Override
    public Set<String> getAllRentalCompanies() {
        return queryClass.getRentals().keySet();
    }
    
    @Override
    public List<CarType> getAvailableCarTypes(Date start, Date end) {
        List<CarType> availableCarTypes = new LinkedList<CarType>();
        for(String crc : getAllRentalCompanies()) {
            for(CarType ct : queryClass.getRentals().get(crc).getAvailableCarTypes(start, end)) {
                if(!availableCarTypes.contains(ct))
                    availableCarTypes.add(ct);
            }
        }
        return availableCarTypes;
    }

    
    public Quote createQuote(CarRentalCompany company, ReservationConstraints constraints) throws ReservationException {
        try {
            System.out.println("null??");
            System.out.println(queryClass);
            System.out.println(company);
            
            Quote out = company.createQuote(constraints, renter);
            quotes.add(out);
            return out;
        } catch(Exception e) {
            System.out.println("createquote error");
            e.printStackTrace();
            throw new ReservationException("");
        }
    }
    
    @Override
    public Quote addQuoteToSession(String renter, ReservationConstraints constraints) throws ReservationException{
        Map<String, CarRentalCompany> crcs = queryClass.getRentals();
        Quote quote = null;
        for(CarRentalCompany crc: crcs.values()){
            try{
                quote = createQuote(crc, constraints);
                break;
            } catch(Exception e){
                
            }
        }
        
        if (quote == null) {
            throw new ReservationException("ReservationSession: impossible to create this quote.");
	}
        return quote;
    }

    @Override
    public List<Quote> getCurrentQuotes() {
        return quotes;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public List<Reservation> confirmQuotes() throws ReservationException {
        List<Reservation> done = new LinkedList<Reservation>();
        try {
            for (Quote quote : quotes) {
                done.add(queryClass.getRental(quote.getRentalCompany().getName()).confirmQuote(quote));
            }
        } catch (Exception e) {
            /*for(Reservation r:done)
                queryClass.getRental(r.getRentalCompany().getName()).cancelReservation(r);*/
            throw new ReservationException(e);
        }
        return done;
    }

    @Override
    public void setRenterName(String name) {
        if (renter != null) {
            throw new IllegalStateException("name already set");
        }
        renter = name;
    }

    @Override
    public void checkForAvailableCarTypes(Date start, Date end) {
        Set<CarType> carTypes = new HashSet<>();
        for (CarRentalCompany crc: queryClass.getRentals().values()){
            carTypes.addAll(crc.getAvailableCarTypes(start, end));
        }
        for(CarType c: carTypes){
            System.out.println(c);
        }
    }
    
}