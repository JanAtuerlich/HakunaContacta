package de.hakunacontacta.fileModule;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import net.sourceforge.cardme.io.VCardWriter;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.arch.ParameterTypeStyle;
import net.sourceforge.cardme.vcard.arch.VCardVersion;
import net.sourceforge.cardme.vcard.exceptions.VCardBuildException;
import net.sourceforge.cardme.vcard.types.AdrType;
import net.sourceforge.cardme.vcard.types.EmailType;
import net.sourceforge.cardme.vcard.types.ExtendedType;
import net.sourceforge.cardme.vcard.types.NType;
import net.sourceforge.cardme.vcard.types.NameType;
import net.sourceforge.cardme.vcard.types.NoteType;
import net.sourceforge.cardme.vcard.types.ProfileType;
import net.sourceforge.cardme.vcard.types.SourceType;
import net.sourceforge.cardme.vcard.types.TelType;
import net.sourceforge.cardme.vcard.types.UrlType;
import net.sourceforge.cardme.vcard.types.VersionType;
import net.sourceforge.cardme.vcard.types.params.AdrParamType;
import net.sourceforge.cardme.vcard.types.params.EmailParamType;
import net.sourceforge.cardme.vcard.types.params.TelParamType;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.shared.ContactSourceField;
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportOption;
import de.hakunacontacta.shared.ExportTypeEnum;
import ezvcard.Ezvcard;
import ezvcard.VCard;

/**
 * @author AlHafi
 * 
 */
@SuppressWarnings("serial")
public class FileCreator implements IFileCreator, Serializable {

	private ArrayList<Contact> selectedContacts;
	private ArrayList<ExportField> exportFields;
	private ArrayList<Contact> cleansedContacts;
	private ExportTypeEnum exportFormat;

	public FileCreator() {
	}

	/**
	 * Die Methode cleanseContacts() bereitet die Kontaktdaten für den Export
	 * auf, dabei werden die selektierten Exportfields in den Kontakten nach
	 * Prioritäten gesucht, gibts es also Export-Feld Prio 1 nicht wird gesucht
	 * ob es Prio2 gibt und das Ganze wird dann als ArrayList aus Contacts and
	 * die einzelnen fielCreator weitergegeben.
	 * 
	 * @param nothing
	 * @return String Exportdatei
	 */
	@Override
	public String cleanseContacts() {

		cleansedContacts = new ArrayList<Contact>();

		for (Contact incomingContact : selectedContacts) {
			ArrayList<ContactSourceType> initSourceTypeList = new ArrayList<ContactSourceType>();
			Collections.sort(exportFields);
			for (ExportField exportField : exportFields) {
				ContactSourceType initSourceType = new ContactSourceType();
				initSourceType.setType(exportField.getName());
				ContactSourceField initSourceField = new ContactSourceField();
				initSourceField.setName(exportField.getName());
				initSourceField.setValue("");
				initSourceType.addSourceField(initSourceField);
				initSourceTypeList.add(initSourceType);
				Collections.sort(exportField.getExportOptions());
			}
			Contact cleansedContact = new Contact();
			cleansedContact.seteTag(incomingContact.geteTag());
			cleansedContact.setName(incomingContact.getName());
			cleansedContact.setSourceTypes(initSourceTypeList);
			for (ExportField exportField : exportFields) {
				for (ExportOption exportOption : exportField.getExportOptions()) {
					boolean foundbest = false;
					for (ContactSourceType incomingSourceType : incomingContact.getSourceTypes()) {
						for (ContactSourceField incomingSourceField : incomingSourceType.getSourceFields()) {
							if (exportOption.getSourceType().equals(incomingSourceType.getType()) && exportOption.getSourceField().equals(incomingSourceField.getName())) {
								for (ContactSourceType cleansedSourceType : cleansedContact.getSourceTypes()) {
									if (cleansedSourceType.getType() == exportField.getName()) {
										for (ContactSourceField cleansedSourceField : cleansedSourceType.getSourceFields()) {
											cleansedSourceField.setName(exportField.getName());
											cleansedSourceField.setValue(incomingSourceField.getValue());
										}
									}
								}
								foundbest = true;
							}
						}
					}
					if (foundbest)
						break;
				}
			}
			cleansedContacts.add(cleansedContact);
		}

		String output = "";
		if (exportFormat == ExportTypeEnum.CSV) {
			output = createCSV();
		} else if (exportFormat == ExportTypeEnum.CSVWord) {
			output = createCSVWord();
		} else if (exportFormat == ExportTypeEnum.XML) {
			output = createXML();
		} else if (exportFormat == ExportTypeEnum.vCard) {
			output = createVCard();
		} else {
			output = null;
		}

		return new String(output);
	}

	/**
	 * Die Methode createCSV() bereit die Collection exportField als String in
	 * Form einer CSV auf
	 * 
	 * @return String Exportdatei als CSV
	 */
	private String createCSV() {

		String csv = "";
		String seperator = ",";

		for (ExportField exportField : exportFields) {
			csv += exportField.getName() + seperator;
		}

		csv = csv.substring(0, csv.length() - 1);
		csv += "\n";

		for (Contact contact : cleansedContacts) {

			Collections.sort(contact.getSourceTypes());
			for (ContactSourceType sourceType : contact.getSourceTypes()) {
				for (ContactSourceField sourceField : sourceType.getSourceFields()) {
					csv += sourceField.getValue().replace(seperator, "") + seperator;
				}
			}

			csv = csv.substring(0, csv.length() - 1);
			csv += "\n";
		}
		return csv;
	}

	/**
	 * Die Methode createCSVWord bereitet die Collection exportField als String
	 * in Form einer CSV auf. Das besondere sind hier der Seperator ; und das
	 * Datensatztrennzeichen *
	 * 
	 * @param nothing
	 * @return String Exportdatei in Form einer CSV
	 */
	private String createCSVWord() {

		String csv = "";
		String seperator = ";";

		for (ExportField exportField : exportFields) {
			csv += exportField.getName() + seperator;
		}
		csv = csv.substring(0, csv.length() - 1);
		csv += "*\n";

		for (Contact contact : cleansedContacts) {

			Collections.sort(contact.getSourceTypes());

			for (ContactSourceType sourceType : contact.getSourceTypes()) {
				for (ContactSourceField sourceField : sourceType.getSourceFields()) {
					csv += sourceField.getValue().replace(", ", "\n") + seperator;
				}
			}
			csv = csv.substring(0, csv.length() - 1);
			csv += "*\n";
		}

		return csv;
	}

	/**
	 * Die Methode createXML bereitet die Collection als xCard, einer vCard in
	 * XML-Form auf Dabei wird zuerst eine vCard erzeugt und diese dann in das
	 * xCard-Format gecastet.
	 * 
	 * @param nothing
	 * @return String Exportdatei als xCard (XML)
	 */
	private String createXML() {
		String vCard = createVCard();
		VCard vcard = Ezvcard.parse(vCard).first();
		String xml = Ezvcard.writeXml(vcard).go();

		return xml;
	}

	/**
	 * Die Methode createVCard bereitet die Collection als vCard, einer vCard
	 * auf Felder die es im vCar Format eigentlich nicht gibt werden als x-Field
	 * angelegt, einer art Custom-Feld
	 * 
	 * @param nothing
	 * @return String Exportdatei als vCard (XML)
	 */
	private String createVCard() {
		String vCard = "";

		for (Contact contact : cleansedContacts) {
			Collections.sort(contact.getSourceTypes());

			VCardImpl vcard = new VCardImpl();
			vcard.setVersion(new VersionType(VCardVersion.V3_0));
			NameType name = new NameType();
			name.setName(contact.getName());
			vcard.setName(name);
			ProfileType profile = new ProfileType();
			profile.setProfile("VCard");
			vcard.setProfile(profile);
			SourceType source = new SourceType();
			source.setSource("google contacts");
			vcard.setSource(source);

			NType x = new NType();
			for (ContactSourceType sourceType : contact.getSourceTypes()) {
				if (sourceType.getType().equals("Vorname")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							x.setGivenName(sourceField.getName());
						}

					}
				} else if (sourceType.getType().equals("Nachname")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							x.setFamilyName(sourceField.getValue());
						}
					}
				} else if (sourceType.getType().equals("Zweitname")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							x.addAdditionalName(sourceField.getValue());
						}
					}
				} else if (sourceType.getType().equals("Suffix")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							x.addHonorificSuffix(sourceField.getValue());
						}
					}
				} else if (sourceType.getType().equals("Preffix")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							x.addHonorificPrefix(sourceField.getValue());
						}
					}
				} else if (sourceType.getType().equals("Homepage")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {

							try {
								vcard.addUrl(new UrlType(new URL(sourceField.getValue())));
							} catch (NullPointerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} else if (sourceType.getType().equals("Adresse")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {

							AdrType address1 = new AdrType();
							address1.setCharset("UTF-8");
							address1.setExtendedAddress(sourceField.getValue().replace("\n", " "));
							address1.addParam(AdrParamType.PREF);
							vcard.addAdr(address1);
						}
					}
				} else if (sourceType.getType().equals("Telefon")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {

							TelType telephone = new TelType();
							telephone.setCharset("UTF-8");
							telephone.setTelephone(sourceField.getValue());
							telephone.addParam(TelParamType.HOME).setParameterTypeStyle(ParameterTypeStyle.PARAMETER_VALUE_LIST);
							vcard.addTel(telephone);
						}
					}
				} else if (sourceType.getType().equals("Handy")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {

							TelType telephone = new TelType();
							telephone.setCharset("UTF-8");
							telephone.setTelephone(sourceField.getValue());
							telephone.addParam(TelParamType.CELL).setParameterTypeStyle(ParameterTypeStyle.PARAMETER_VALUE_LIST);
							vcard.addTel(telephone);
						}
					}
				} else if (sourceType.getType().equals("E-Mail privat")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {

							EmailType email = new EmailType();
							email.setEmail(sourceField.getValue());
							email.addParam(EmailParamType.HOME).setCharset("UTF-8");
							vcard.addEmail(email);
						}

					}
				} else if (sourceType.getType().equals("E-Mail geschäftlich")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {

							EmailType email = new EmailType();
							email.setEmail(sourceField.getValue());
							email.addParam(EmailParamType.WORK).setCharset("UTF-8");
							vcard.addEmail(email);
						}

					}
				} else if (sourceType.getType().equals("Kommentar")) {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							NoteType note = new NoteType();
							note.setNote(sourceField.getValue());
							vcard.addNote(note);
						}

					}
				} else {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						if (sourceField.getValue() != "") {
							vcard.addExtendedType(new ExtendedType("X-" + sourceField.getName(), sourceField.getValue()));
						}
					}
				}

			}
			vcard.setN(x);
			VCardWriter writer = new VCardWriter();
			writer.setVCard(vcard);
			try {
				vCard += writer.buildVCardString();
			} catch (VCardBuildException e) {
				e.printStackTrace();
			}
		}
		return vCard;
	}

	public void setFields(ArrayList<Contact> selectedContactsX, ArrayList<ExportField> exportFieldsX, ExportTypeEnum exportFormatX) {
		selectedContacts = selectedContactsX;
		exportFields = exportFieldsX;
		exportFormat = exportFormatX;
	}

}
