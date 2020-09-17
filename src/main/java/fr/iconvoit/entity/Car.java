package fr.iconvoit.entity;

// import lombok.Data;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
         * Class car
         * @author Mélanie
         * 
         */

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
            System.out.println("<constructor> : registration refus");
            
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
            System.out.println("<setRegistration> : registration error");
            
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
            /*
            // search if ss or ww are in the left; ss on the right
            // if yes incorrect
            REGEX2="[^ss-]|[^SS-]|[^ww-]|[^WW-]..[-ss\b]|[-SS\b]"; //comment faire pour debut de ligne?
            Pattern pattern2 = Pattern.compile(REGEX2,Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(regi);
            boolean matchFound2 = matcher2.find();
            if (matchFound2) {
                System.out.println("<verifReg> : error begins with ss or ww | ends with ss");
                System.out.println(regi);
                return false;
            }
            else{
                System.out.println("<verifReg> : registration works !2");
                return true;
            }
            */
            // /*
            System.out.println("<verifReg> : registration works !2");
            return true;
            // */
        } 
        else {
          System.out.println("<verifReg> : contain i, o , u");
          System.out.println(regi);
          return false;
        }
    }

    public static void main (String[] args){
        /*
        Car c = new Car("red","toyota","AA-123-BB",4);
        System.out.println("--------#1--------");
        c.setRegistration("Ap-005-AU");
        System.out.println(c.getRegistration());
        */
        
    }
}
