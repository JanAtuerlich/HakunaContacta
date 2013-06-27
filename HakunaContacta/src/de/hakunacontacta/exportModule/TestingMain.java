package de.hakunacontacta.exportModule;

import java.util.Random;

import de.hakunacontacta.dummyContactManager.ContactDummy;
import de.hakunacontacta.dummyContactManager.ContactManagerDummy;
import de.hakunacontacta.dummyContactManager.SourceTypeDummy;

public class TestingMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
//		ExportManager exportManager = new ExportManager();		
//		exportManager.addExportField("FieldforCostum");	
//		System.out.println(exportManager.toString());	
//		exportManager.changeExportFormat(exportTypeEnum.CSV);		
//		System.out.println(exportManager.toString());
//		exportManager.addExportField("FieldforCSV");
//		System.out.println(exportManager.toString());
//		
//		exportManager.changeExportFormat(exportTypeEnum.Costum);	
//		
//		System.out.println(exportManager.toString());		

		ContactManagerDummy conti = new ContactManagerDummy();

		randomContacts(100, conti);

		for (ContactDummy contact : conti.getContacts()) {
			System.out.println(contact.toString());
		}

	}

	static Random random = new Random();
	static String prenames[] = { // 50 Vornamen
	"Mia", "Emma", "Hannah", "Lea", "Sofia", "Anna", "Lena", "Leonie", "Lina", "Marie", "Emily", "Emilia", "Lilly", "Luisa", "Amelie", "Sophie",
			"Laura", "Nele", "Johanna", "Lara", "Maja", "Sarah", "Clara", "Leni", "Charlottev", "Ben", "Luka", "Paul", "Lukas", "Fynn", "Jonas",
			"Leon", "Luisa", "Maximilian", "Felix", "Noah", "Elias", "Tim", "Max", "Julian", "Moritz", "Philipp", "Niklas", "Jakob", "Alexander",
			"David", "Jan", "Henry", "Tom", "Erik" };
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
	static String sourceFieldsMail[] = { // 5 EmailField
	"Mail", "eMail", "e-mail", "mail-geschäftlich", "mailprivat" };
	static String sourceFieldsTel[] = { // 5Tel
	"Home", "Daheim", "Geschäft", "mobil", "privat" };
	static String sourceFieldsAdr[] = { "privat", "geschäftliche", "post" };

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

	public static void randomContacts(int anzahl, ContactManagerDummy contactManager) {

		for (int i = 0; i < anzahl; i++) {

			String name = prenames[(Math.abs(random.nextInt(50)))];
			name = name + " " + surnames[(Math.abs(random.nextInt(150)))];

			ContactDummy contact = contactManager.addContact("Kontakt " + i, name);
			contact.setSelected(true);
			contact.addSourceType("E-Mail");
			contact.addSourceType("Telefon");
			contact.addSourceType("Adresse");

			for (SourceTypeDummy sourceType : contact.getSourceTypes()) {
				if (sourceType.getType() == "E-Mail") {
					for (int j = 0; j < (Math.abs(random.nextInt(3))); j++) {
						sourceType.addSourceField(sourceFieldsMail[(Math.abs(random.nextInt(5)))], randomMail(contact.getName()));
					}
				}
				if (sourceType.getType() == "Telefon") {
					for (int j = 0; j < (Math.abs(random.nextInt(3))); j++) {
						sourceType.addSourceField(sourceFieldsTel[(Math.abs(random.nextInt(5)))], randomTel());
					}
				}

				if (sourceType.getType() == "Adresse") {
					if (Math.abs(random.nextInt(2)) == 1) {

						for (int j = 0; j < (Math.abs(random.nextInt(2))); j++) {
							sourceType.addSourceField(sourceFieldsAdr[(Math.abs(random.nextInt(2)))], "AdressDummyFor" + contact.getName());
						}

					}

				}

			}

		}

	}

}
