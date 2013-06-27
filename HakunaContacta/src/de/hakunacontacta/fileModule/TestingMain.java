package de.hakunacontacta.fileModule;

import java.io.IOException;

import com.google.appengine.api.files.AppEngineFile;

import de.hakunacontacta.dummyContactManager.ContactDummy;
import de.hakunacontacta.dummyContactManager.ContactManagerDummy;
import de.hakunacontacta.dummyContactManager.SourceTypeDummy;
import de.hakunacontacta.exportModule.ExportField;
import de.hakunacontacta.exportModule.ExportManager;
import de.hakunacontacta.exportModule.ExportOption;
import de.hakunacontacta.exportModule.ExportManager.exportTypeEnum;
import de.hakunacontacta.fileModule.FileCreator;
import de.hakunacontacta.fileModule.IFileCreator;

public class TestingMain {
	
	private static ContactManagerDummy contactManager = new ContactManagerDummy();
	private static ExportManager exportManager = new ExportManager();
	
	public static void main(String[] args) {
		
		exportManager.setExportFormat(exportTypeEnum.vCard);
				
//		testKontakteAnlegen();
//		testAusgabeKontakte();
//		System.out.println("");
		testExportFelderAnlegen();
		testAusgabeExportFelder();
		
		
		
		contactManager.randomContacts(10, contactManager);
		
		System.out.println("Alle erstellten Kontakte ausgeben:");

		for (ContactDummy contact : contactManager.getContacts()) {
			System.out.println(contact.toString());
		}
		
		
		System.out.println("ENDE von: Alle erstellten Kontakte ausgeben:");
		
		IFileCreator fileCreator = new FileCreator(contactManager.getSelectedContacts(), exportManager.getChosenExportFields(), exportManager.getExportFormat());
		

		String file = fileCreator.cleanseContacts();

		
		System.out.println("\nDas File sieht so aus: \n\n" + file + "\nund ist im Format " + exportManager.getExportFormat() + ".");
		
	}
	
	public static void testKontakteAnlegen(){
		contactManager.addContact("Kontakt1", "Max Mustermann");
		contactManager.addContact("Kontakt2", "Igor Huska");
		contactManager.addContact("Kontakt3", "Elise Dorm");
		
		for (ContactDummy contact : contactManager.getContacts()) {
			contact.setSelected(true);
			if(contact.geteTag()=="Kontakt1"){
				contact.addSourceType("Telefon");
				contact.addSourceType("E-Mail");
			}else {
				contact.addSourceType("E-Mail");
				contact.addSourceType("Telefon");
				contact.addSourceType("Fax");
			}
		}
		for (ContactDummy contact : contactManager.getContacts()) {
			for (SourceTypeDummy sourceType : contact.getSourceTypes()) {
				if(contact.geteTag() =="Kontakt1" && sourceType.getType() == "E-Mail"){
					sourceType.addSourceField("Arbeit", "MM@gmx.de");
					sourceType.addSourceField("Home", "MM@gmx.ch");
				}
				if(contact.geteTag() =="Kontakt2" && sourceType.getType() == "E-Mail"){
					sourceType.addSourceField("Arbeit", "Igor@Huska.com");	
				}
				if(contact.geteTag() =="Kontakt1" && sourceType.getType() == "Telefon"){
					sourceType.addSourceField("Privat", "+49 361 219210");
				}
				if(contact.geteTag() =="Kontakt2" && sourceType.getType() == "Telefon"){
					sourceType.addSourceField("Arbeit", "011");
					sourceType.addSourceField("Arbeit2", "012");
				}
				if(contact.geteTag() =="Kontakt2" && sourceType.getType() == "Fax"){
					sourceType.addSourceField("Arbeit", "011-582");
				}
			}
		}
	}
	
	public static void testAusgabeKontakte(){
		for (ContactDummy contact : contactManager.getContacts()) {
			System.out.println(contact.toString());
		}
	}
	
	public static void testExportFelderAnlegen(){
//		exportManager.addExportField("Export-Email-Adresse");
//		exportManager.addExportField("Export-Telefon");
//		exportManager.addExportField("Export-Adresse");
		
//		contact.addSourceType("E-Mail");
//		contact.addSourceType("Telefon");
//		contact.addSourceType("Adresse");
		
//Testfelder die in vCard automatisch erstellt werden:
//		exportManager.addExportField("Vorname");
//		exportManager.addExportField("Nachname");
//		exportManager.addExportField("Adresse");
//		exportManager.addExportField("Telefon");
//		exportManager.addExportField("Handy");
//		exportManager.addExportField("E-Mail privat");
//		exportManager.addExportField("E-Mail geschäftlich");
//		exportManager.addExportField("Kommentar");
		
		exportManager.addExportOption("Vorname", "Vorname", "Name", 1);
		exportManager.addExportOption("Nachname", "Nachname", "Name", 1);
		exportManager.addExportOption("Adresse", "x", "Adresse", 1);
		exportManager.addExportOption("Telefon", "xtel", "Telefon", 1);
		exportManager.addExportOption("Handy", "xhdy", "Telefon", 1);
		exportManager.addExportOption("E-Mail privat", "x", "E-Mail", 1);
		exportManager.addExportOption("E-Mail geschäftlich", "x", "E-Mail", 1);//gleiche Nummer in beide Felder
		exportManager.addExportOption("E-Mail privat", "x", "E-Mail", 1);
		exportManager.addExportField("BeispielXFeld"); //nicht vorhandenes vCard Feld, sollte via X-Feldnamen angelegt werden
		exportManager.addExportOption("BeispielXFeld", "x", "Adresse", 1);
				
//		this.addExportField("Kommentar", exportFieldsvCard);
//		
//		Markus' Testdaten
//		exportManger.addExportField("Export-Fax");
//		
		
//E-Mail , Telefon , Adresse
//5 EmailField	"Mail", "eMail", "e-mail", "mail-geschäftlich", "mailprivat" };
//5Tel			"Home", "Daheim", "Geschäft", "mobil", "privat" };
//3 Adress		"privat", "geschäftliche", "post" };
//		
//		for (ExportField exportField : exportManager.getChosenExportFields()) {
//			if(exportField.getName()=="Export-Email-Adresse"){				
//				Markus' Testdaten
//				exportField.addExportOption("E-Mail", "Home", 3);
//				exportField.addExportOption("E-Mail", "Arbeit", 4);
//				
//				
//				exportField.addExportOption("E-Mail", "Mail", 1);
//				exportField.addExportOption("E-Mail", "mailprivat", 2);	
//			}
//			else if(exportField.getName()=="Export-Telefon"){
//							//Markus' Testdaten
//				exportField.addExportOption("Telefon", "Privat", 3);
//				exportField.addExportOption("Telefon", "Arbeit2", 4);
				//
//				
//				exportField.addExportOption("Telefon", "mobil", 1);
//				exportField.addExportOption("Telefon", "privat", 2);	
//			}
//			else if(exportField.getName()=="Export-Adresse"){
//				exportField.addExportOption("Adresse", "privat", 1);	
//			}
			
			//Markus' Testdaten
//			else if(exportField.getName()=="Export-Fax"){
//				exportField.addExportOption("Fax", "Arbeit", 1);	
//			}
			//
			
	}
	
	public static void testAusgabeExportFelder(){
		for (ExportField exportField : exportManager.getChosenExportFields()) {
			System.out.println("ExportFeldName: " + exportField.getName());
			System.out.println("Hierzu wurden folgende Exportoptionen angelegt: SourceType, ExportOptionName, ExportOptionPriorität");
			for (ExportOption exportOption : exportField.getExportOptions()) {
				System.out.println(exportOption.getSourceType() + ", " + exportOption.getSourceField() + ", " + exportOption.getPriority());
			}
			System.out.println("");
		}
	}
}
