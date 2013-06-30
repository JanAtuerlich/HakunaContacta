package de.hakunacontacta.fileModule;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import net.sourceforge.cardme.io.VCardWriter;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.arch.EncodingType;
import net.sourceforge.cardme.vcard.arch.LanguageType;
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
import de.hakunacontacta.exportModule.ExportField;
import de.hakunacontacta.exportModule.ExportOption;
import de.hakunacontacta.shared.ContactSourceField;
import de.hakunacontacta.shared.ContactSourceType;

import ezvcard.Ezvcard;
import ezvcard.VCard;

public class FileCreator implements IFileCreator {

	private ArrayList<Contact> selectedContacts;
	private ArrayList<ExportField> exportFields;
	private ArrayList<Contact> cleansedContacts = new ArrayList<Contact>();
	private String exportFormat;
	private static VCardImpl vcardFull = null;

	public FileCreator(ArrayList<Contact> selectedContacts, ArrayList<ExportField> exportFields, String exportFormat) {
		this.selectedContacts = selectedContacts;
		this.exportFields = exportFields;
		this.exportFormat = exportFormat;
	}

	@Override
	public String cleanseContacts(){

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
							if (exportOption.getSourceType() == incomingSourceType.getType()
									&& exportOption.getSourceField() == incomingSourceField.getName()) {
								for (ContactSourceType cleansedSourceType : cleansedContact.getSourceTypes()) {
									if (cleansedSourceType.getType() == exportField.getName()) {
										System.out.println(cleansedContact.getName() + ", " + cleansedSourceType.getType());
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
		
		String output;
		if (exportFormat == "CSV") {
			output = createCSV();

		} else if (exportFormat == "CSVWord") {
			output = createCSVWord();

		} else if (exportFormat == "XML") {
			output = createXML();

		} else if (exportFormat == "vCard") {
			output = createVCard();

		} else {
			output = null;
		}
		
		
		byte[] encoded = Base64.encodeBase64(output.getBytes()); 
		
		  
		  return new String(encoded);

		
	}

	private String createCSV() {

		// TODO einfach den Namen aus dem Kontaktobjekt übernehmen ist schlecht,
		// da wird Felder Vorname Nachname bekommen werden,
		String csv = "Contact Name";
		String seperator = ",";

		for (ExportField exportField : exportFields) {
			csv += seperator + exportField.getName();
		}

		csv += seperator + "\n";

		for (Contact contact : cleansedContacts) {

			Collections.sort(contact.getSourceTypes());

			csv += contact.getName();
			for (ContactSourceType sourceType : contact.getSourceTypes()) {
				for (ContactSourceField sourceField : sourceType.getSourceFields()) {
					// System.out.println(sourceType.getType() + " of contact "
					// + contact.getName() + " " + sourceField.getName() + ", "
					// + sourceField.getValue());
					csv += seperator + sourceField.getValue();
				}
			}
			csv += seperator + "\n";
		}
		return csv;
	}

	private String createCSVWord() {

		// TODO einfach den Namen aus dem Kontaktobjekt übernehmen ist schlecht,
		// da wird Felder Vorname Nachname bekommen werden,

		String csv = "Contact Name";
		String seperator = ";";

		for (ExportField exportField : exportFields) {
			csv += seperator + exportField.getName();
		}

		csv += seperator + "\n";

		for (Contact contact : cleansedContacts) {

			Collections.sort(contact.getSourceTypes());

			csv += contact.getName();
			for (ContactSourceType sourceType : contact.getSourceTypes()) {
				for (ContactSourceField sourceField : sourceType.getSourceFields()) {
					csv += seperator + sourceField.getValue();
				}
			}
			csv += seperator + "\n";
		}

		return csv;

	}

	private String createXML() {
		String vCard = createVCard();
		VCard vcard = Ezvcard.parse(vCard).first();
		String xml = Ezvcard.writeXml(vcard).go();
		
		return xml;
	}

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
			NType n = new NType();
			n.setEncodingType(EncodingType.QUOTED_PRINTABLE);
			n.setCharset(Charset.forName("UTF-8"));
			n.setLanguage(LanguageType.DE);

			for (ContactSourceType sourceType : contact.getSourceTypes()) {

				if (sourceType.getType() == "Vorname") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						n.setGivenName(sourceField.getValue());
					}
				} else if (sourceType.getType() == "Nachname") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						n.setFamilyName(sourceField.getValue());
					}
				} else if (sourceType.getType() == "Homepage") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						
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
				} else if (sourceType.getType() == "Adresse") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {

						AdrType address1 = new AdrType();
						address1.setCharset("UTF-8");
						address1.setExtendedAddress(sourceField.getValue());
						// address1.setCountryName("U.S.A.");
						// address1.setLocality("New York");
						// address1.setRegion("New York");
						// address1.setPostalCode("NYC887");
						// address1.setPostOfficeBox("25334");
						// address1.setStreetAddress("South cresent drive, Building 5, 3rd floor");
						// address1.addParam(AdrParamType.HOME)
						// .addParam(AdrParamType.PARCEL)
						address1.addParam(AdrParamType.PREF);
						// .addExtendedParam(new
						// ExtendedParamType("CUSTOM-PARAM-TYPE",
						// VCardTypeName.ADR))
						// .addExtendedParam(new
						// ExtendedParamType("CUSTOM-PARAM-TYPE",
						// "WITH-CUSTOM-VALUE", VCardTypeName.ADR));
						

						vcard.addAdr(address1);
						// TODO sourceField.getValue())));
					}
				} else if (sourceType.getType() == "Telefon") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {

						TelType telephone = new TelType();
						telephone.setCharset("UTF-8");
						telephone.setTelephone(sourceField.getValue());
						telephone.addParam(TelParamType.HOME).setParameterTypeStyle(ParameterTypeStyle.PARAMETER_VALUE_LIST);
						vcard.addTel(telephone);

					}
				} else if (sourceType.getType() == "Handy") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {

						TelType telephone = new TelType();
						telephone.setCharset("UTF-8");
						telephone.setTelephone(sourceField.getValue());
						telephone.addParam(TelParamType.CELL).setParameterTypeStyle(ParameterTypeStyle.PARAMETER_VALUE_LIST);
						vcard.addTel(telephone);

					}
				} else if (sourceType.getType() == "E-Mail Privat") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {

						EmailType email = new EmailType();
						email.setEmail(sourceField.getValue());
						email.addParam(EmailParamType.HOME).setCharset("UTF-8");
						vcard.addEmail(email);

					}
				} else if (sourceType.getType() == "E-Mail geschäftlich") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {

						EmailType email = new EmailType();
						email.setEmail(sourceField.getValue());
						email.addParam(EmailParamType.WORK).setCharset("UTF-8");
						vcard.addEmail(email);

					}
				} else if (sourceType.getType() == "Kommentar") {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {

						NoteType note = new NoteType();
						note.setNote(sourceField.getValue());
						vcard.addNote(note);

					}
				} else {
					for (ContactSourceField sourceField : sourceType.getSourceFields()) {
						vcard.addExtendedType(new ExtendedType("X-" + sourceField.getName(), sourceField.getValue()));
					}
				}

				vcard.setN(n);

			}
			
			VCardWriter writer = new VCardWriter();
			writer.setVCard(vcard);
			try {
				vCard  += writer.buildVCardString();
			} catch (VCardBuildException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return vCard;
	}

}
