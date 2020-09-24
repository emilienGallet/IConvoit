package fr.iconvoit.factory;

import java.sql.Timestamp;

import fr.iconvoit.entity.Localisation;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.exceptions.SlotException;

public class SlotFactory {
	/**
	 * 
	 * @param start
	 * @param end
	 * @param place
	 * @return an SlotOther
	 */
	public static Slot createSlot(Timestamp start, Timestamp end, Localisation place) throws SlotException{
		
		return null;	
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param placeStart
	 * @param placeEnd
	 * @return
	 */
	public static Slot createSlot(Timestamp start, Timestamp end, Localisation placeStart, Localisation placeEnd) throws SlotException{
		
		return null;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param place
	 * @return
	 */
	public static Slot createSlot(String start, String end, String place) throws SlotException{
		
		return SlotOtherFactory.createSlotOther();
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param placeStart
	 * @param placeEnd
	 * @return a SlotTravel 
	 */
	public static Slot createSlot(String start, String end, String placeStart, String placeEnd) throws SlotException{
		
		return SlotTravelFactory.createSlotTravel(start,end,placeStart,placeEnd);
	}
}
