package fr.iconvoit;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import lombok.Data;

@Data
/**
 * 
 * @author emilien An ICS Parser. I don't use the ParserDOM because the client
 *         are kind and to prepared me for Numeric Doc Module in M1 DSC. !!
 *         ISN't an generic ICS parser !! Just for ICS University planning
 */
public class IcsParser {

	private URL url;
	
	IcsParser(String url) throws Exception {
		this.url = new URL(url);
		BufferedInputStream buff = new BufferedInputStream(this.url.openStream());
		Scanner s = new Scanner(buff);
		parsing(s);

	}

	private void parsing(Scanner s) {
		Pattern begin = Pattern.compile("BEGIN:VEVENT");
		Pattern end = Pattern.compile("END:VEVENT");
		Matcher matcher;
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
				uid = s.nextLine().split(":", 2)[1];
				created = s.nextLine().split(":", 2)[1];
				lastModified = s.nextLine().split(":", 2)[1];
				sequence = s.nextLine().split(":", 2)[1];
			}
		}
	}
}
