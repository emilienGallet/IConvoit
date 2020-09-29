package fr.iconvoit.entity;

// import lombok.Data;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
         * Class car
         * @author Mélanie
         * 
         */
@Entity
@Data
public class Car {

    
    @Id
    String registration;
    String Format;
    Integer nbOfSeats;
    String color;
    String brand;

    /**
         * Constructor empty
         */
    public Car(){}

    /**
         * Constructor
         * @param String color,String brand,String registration,Integer nbOfSeats
         */
    public Car(String color,String brand,String registration,String Format,Integer nbOfSeats){
        this.color=color;
        this.brand=brand;
        this.Format=Format;
        if(verifRegistration(registration)==true){
            this.registration=registration;
        }else{
            System.out.println("registration declined");
            
        }
        this.nbOfSeats=nbOfSeats;
    }

    public void setRegistration(String regi){
        if(verifRegistration(regi)==true){
            this.registration=regi;
        }
        else
            System.out.println("registration error");
            
    }
    // TODO check if number of seat is available for the registration car 
    // (i.e DA-503-BG is an Zoe car and got 5 maximum seats  
    /**
         * set the nb of seats verifying if it's not enormous
         * @param Integer nbOfSeat
         * @return void
         */
    public void setNbOfSeats(Integer nbOfSeat){
        if(verifNbOfSeats(nbOfSeat)==true){
            this.nbOfSeats=nbOfSeat;
        }
        else
            System.out.println("number of seats impossible");
    }
    /**
         * verification of the Registration
         * @param String regi
         * @return boolean
         */
    public boolean verifRegistration(String regi){
        // search if i o or u are in the registration
        // if yes incorrect
        String REGEX,REGEX2;
        REGEX="[A-HJ-NP-Za-hj-np-z]{2,4}-[0-9]{3}-[A-HJ-NP-Za-hj-np-z]{2}";
        Pattern pattern = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(regi);
        boolean matchFound = matcher.find();
        if(matchFound) {
            
            // search if ss or ww are in the left; ss on the right
            // if yes incorrect
            REGEX2="[/^ss]{2}-......|[/^SS]{2}-......|[/^ww]{2}-......|[/^WW]{2}-......|......-[ss]{2}|......-[SS]{2}"; 
            Pattern pattern2 = Pattern.compile(REGEX2,Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(regi);
            boolean matchFound2 = matcher2.find();
            if (matchFound2) {
                System.out.println("error begins with ss or ww | ends with ss");
                System.out.println(regi);
                return false;
            }
            else{
                return true;
            }
        } 
        else {
          System.out.println("error : contains i, o , u");
          System.out.println(regi);
          return false;
        }
    }

/**
         * verification of the nb of seats
         * @param int nbOfSeats
         * @return boolean
         */
    public boolean verifNbOfSeats(int NbOfSeats){
        if(this.Format == "citadine") {
            if(NbOfSeats <= 5){
                return true;
            }
        }
        
        return false;
    } 
}
