package de.hakunacontacta.dummyContactManager;

import java.util.ArrayList;
import java.util.Random;

public class ContactManagerDummy {

	ArrayList<ContactDummy> contacts = new ArrayList<ContactDummy>();

	public ArrayList<ContactDummy> getContacts() {
		return contacts;
	}

	public ArrayList<ContactDummy> getSelectedContacts() {
		ArrayList<ContactDummy> selectedContacts = new ArrayList<>();
		for (ContactDummy c : contacts) {
			if (c.isSelected()) {
				selectedContacts.add(c);
			}
		}
		return selectedContacts;
	}

	public ContactDummy addContact(String eTag, String name) {
		ContactDummy contact = new ContactDummy(eTag, name);
		contacts.add(contact);
		return contact;
	}

	// Testmethoden
	static Random random = new Random();
	static String prenames[] = { // 50 Vornamen
	"Mia", "Emma", "Hannah", "Lea", "Sofia", "Anna", "Lena", "Leonie", "Lina", "Marie", "Emily", "Emilia", "Lilly", "Luisa", "Amelie", "Sophie",
			"Laura", "Nele", "Johanna", "Lara", "Maja", "Sarah", "Clara", "Leni", "Charlottev", "Ben", "Luka", "Paul", "Lukas", "Fynn", "Jonas",
			"Leon", "Luisa", "Maximilian", "Felix", "Noah", "Elias", "Tim", "Max", "Julian", "Moritz", "Philipp", "Niklas", "Jakob", "Alexander",
			"David", "Jan", "Henry", "Tom", "Erik" 
			
	};
	static String surnames[] = { // 150 Nachnamen
	"Mueller", "Schmidt", "Schneider", "Fischer", "Meyer", "Weber", "Becker", "Wagner", "Schulz", "Herrmann", "Schaefer", "Bauer", "Koch", "Richter",
			"Klein", "Wolf", "Schroeder", "Neumann", "Zimmermann", "Krueger", "Hoffmann", "Braun", "Schmitz", "Schmitt", "Hartmann", "Lange",
			"Krause", "Schmid", "Werner", "Schwarz", "Meier", "Lehmann", "Koehler", "Schulze", "Maier", "Walter", "Huber", "Mayer", "Kaiser",
			"Peters", "Weiss", "Moeller", "Peter", "Frank", "Koenig", "Sommer", "Stein", "Winter", "Berger", "Hansen", "Sauer", "Beyer", "Franz",
			"Mann", "Bach", "Kuhn", "Kramer", "Klaus", "Bayer", "Sander", "Kuehn", "Lindner", "Pohl", "Voigt", "Breuer", "Zimmer", "Jahn", "Ziegler",
			"Voss", "Kern", "Haas", "Schumann", "May", "Ritter", "Langer", "Bender", "Ernst", "Baum", "Seifert", "Rose", "Reuter", "Reinhardt",
			"Thiel", "Pfeiffer", "Arndt", "Steiner", "Walther", "Huebner", "Kaufmann", "Kunz", "Gruber", "Nowak", "Lutz", "Horn", "Dietrich",
			"Kruse", "Baer", "Adam", "Fritz", "Lenz", "Jung", "Schuster", "Gross", "Fuchs", "Scholz", "Keller", "Friedrich", "Lorenz", "Baumann",
			"Beck", "Schubert", "Lang", "Hahn", "Ludwig", "Engel", "Vogel", "Simon", "Roth", "Schreiber", "Bergmann", "Kraus", "Schumacher",
			"Winkler", "Berg", "Otto", "Maus", "Heinrich", "Guenther", "Miller", "Jansen", "Kraemer", "Vogt", "Jaeger", "Busch", "Hofmann", "Paul",
			"Wurst", "Petersen", "Boehm", "Albrecht", "Janssen", "Graf", "Seidel", "Heinz", "Franke", "Schulte", "Brandt", "Hermann", "Thomas",
			"Arnold"

	};
	static String sourceFieldsMail[] = { "x"
	//"Mail", "eMail", "e-mail", "mail-geschäftlich", "mailprivat"
	};
	static String sourceFieldsTel[] = { "xtel", "xhdy"
	//"Home", "Daheim", "Geschäft", "mobil", "privat" 
	};
	static String sourceFieldsAdr[] = { "x"
	//"privat", "geschäftliche", "post" 
	};

	public static String randomMail(String name) {
		String mail = name.toLowerCase().replaceAll(" ", "");

		String[] hosts = { "@web.de", "@gmail.com", "@gmx.com", "@aol.de", "@whatever.com", "@yahoo.com", "@htwg-konstanz.de", "@fh-konstanz.de",
				"@me.com", "@heise.com" };
		mail += hosts[Math.abs(random.nextInt(10))];

		return mail;

	}

	public static String randomTel() {
		String tel = "0";

		for (int i = 0; i < 12; i++) {
			tel += Math.abs(random.nextInt(10));
		}
		return tel;
	}

	public void randomContacts(int anzahl, ContactManagerDummy contactManager) {

		for (int i = 0; i < anzahl; i++) {

			String vorname = prenames[Math.abs(random.nextInt(prenames.length-1))];
			String nachname = surnames[Math.abs(random.nextInt(surnames.length-1))];
			String name = vorname + " " + nachname;

			ContactDummy contact = contactManager.addContact("Kontakt " + i, name);
			contact.setSelected(true);
			contact.addSourceType("E-Mail");
			contact.addSourceType("Telefon");
			contact.addSourceType("Adresse");
			contact.addSourceType("Name");

			

			for (SourceTypeDummy sourceType : contact.getSourceTypes()) {
				if (sourceType.getType() == "E-Mail") {
					sourceType.addSourceField(sourceFieldsMail[random.nextInt(sourceFieldsMail.length)], randomMail(contact.getName()));
				}
				if (sourceType.getType() == "Telefon") {
						sourceType.addSourceField(sourceFieldsTel[random.nextInt(sourceFieldsTel.length)], randomTel());
				}
				if (sourceType.getType() == "Adresse") {
							sourceType.addSourceField(sourceFieldsAdr[random.nextInt(sourceFieldsAdr.length)], "AdressDummyFor" + contact.getName());
				}
				if (sourceType.getType() == "Name") {
					sourceType.addSourceField("Vorname", vorname);
					sourceType.addSourceField("Nachname", nachname);
					
				}

			}

		}

	}

}
