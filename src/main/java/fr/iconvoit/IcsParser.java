package fr.iconvoit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.exceptions.SlotException;
import lombok.Data;

@Data
/**
 * 
 * @author emilien An ICS Parser. I don't use the ParserDOM because the client
 *         are kind and to prepared me for Numeric Doc Module in M1 DSC. !!
 *         ISN't an generic ICS parser !! Just for ICS University planning
 */
public class IcsParser {

	@SuppressWarnings("unused")
	public static ArrayList<Slot> parsing(String url) throws MalformedURLException, IOException {
		ArrayList<Slot> sl = new ArrayList<Slot>();
		BufferedInputStream buff = new BufferedInputStream(new URL(url).openStream());
		try (Scanner s = new Scanner(buff)) {
			String description = new String();
			String str, dtStamp, dtStart, dtEnd, Summary, Location, uid, created, lastModified, sequence;
			while (s.hasNext()) {
				str = s.nextLine();
				if (str.contentEquals("BEGIN:VEVENT")) {
					// str = s.nextLine().split(":", 2)[1];
					// System.out.println(str);
					dtStamp = s.nextLine().split(":", 2)[1];
					dtStart = s.nextLine().split(":", 2)[1];
					dtEnd = s.nextLine().split(":", 2)[1];
					Summary = s.nextLine().split(":", 2)[1];
					Location = s.nextLine().split(":", 2)[1];
					while (!(str = s.nextLine()).startsWith("UID:")) {
						description += str;
					}
					uid = str.split(":", 2)[1];
					created = s.nextLine().split(":", 2)[1];
					lastModified = s.nextLine().split(":", 2)[1];
					sequence = s.nextLine().split(":", 2)[1];
					try {
						sl.add(new SlotOther(Summary, dtStart, dtEnd, Location, uid));
					} catch (SlotException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return sl;
	}
	/**
	 * 
	 * @param s String contain in fileds DTSTART, DTEND,DTSTAMP
	 * @return
	 */
	public static LocalDateTime dtTimeToLocalDateTime(String s) {
		//Easier to split by 'T'
		String[] sl = s.split("T");
		LocalDate ld = LocalDate.parse(sl[0],DateTimeFormatter.BASIC_ISO_DATE);
		LocalTime lt = LocalTime.parse(sl[1], DateTimeFormatter.ofPattern("HHmmssX"));
		
		return LocalDateTime.of(ld, lt);
	}
	
	public static Localization AdeParsing(String location) {
		if (location.isBlank()) {
			return null;
		}
		return null;
	}
	
	
	/**
	 * Test function of parsing LocalDateTime form Ics file
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Test du parser");
		LocalDateTime l =IcsParser.dtTimeToLocalDateTime("19970714T170000Z");
		System.out.println(l.toString());
	}


}
