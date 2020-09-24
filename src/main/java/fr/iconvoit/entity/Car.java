package fr.iconvoit.entity;

// import lombok.Data;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
         * Class car
         * @author Mélanie
         * 
         */
// @entity
public class Car {
    String color;
    String brand;
    String registration;
    Integer nbOfSeats;

    /**
         * Constructor empty
         */
    public Car(){}

    /**
         * Constructor
         * @param String color,String brand,String registration,Integer nbOfSeats
         */
    public Car(String color,String brand,String registration,Integer nbOfSeats){
        this.color=color;
        this.brand=brand;
        if(verifRegistration(registration)==true){
            this.registration=registration;
        }else{
            System.out.println("registration declined");
            
        }
        this.nbOfSeats=nbOfSeats;
    }

    ////////////////////////////////getters setters/////////////////////////
    public String getColor(){
        return this.color;
    }
    public String getBrand(){
        return this.brand;
    }
    public String getRegistration(){
        return this.registration;
    }
    public Integer getNbOfSeats(){
        return this.nbOfSeats;
    }

    public void setColor(String color){
        this.color=color;
    }
    public void setBrand(String brand){
        this.brand=brand;
    }
    public void setRegistration(String regi){
        if(verifRegistration(regi)==true){
            this.registration=regi;
        }
        else
            System.out.println("registration error");
            
    }
    public void setNbOfSeats(Integer nbOfSeats){
        this.nbOfSeats=nbOfSeats;
    }

    /////////////////// functions /////////////////////////////
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

    
}
