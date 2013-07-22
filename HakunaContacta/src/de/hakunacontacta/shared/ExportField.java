package de.hakunacontacta.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ExportField bestimmt das Zielfeld in der Ausgabedatei
 * 
 * @author AlHafi
 * @category exportauswahlModul
 * @version 0.1beta
 * 
 */
@SuppressWarnings("serial")
public class ExportField implements Comparable<ExportField>, Serializable {

	private String name;
	private ArrayList<ExportOption> exportOptions = new ArrayList<ExportOption>();

	/**
	 * Diese Methode liefert aller Exportoptions des aktuell bearbeiteten
	 * ExportSets(CSV,XML, usw) als Collection zurück
	 * 
	 * @return Collection aus ExportOption
	 * @author AlHafi @
	 */
	public ArrayList<ExportOption> getExportOptions() {
		return exportOptions;
	}

	/**
	 * Diese Methode erlaubt es eine Exportoption zum aktuellen
	 * ExportSet(CSV,XML, usw) hinzuzufügen
	 * 
	 * @param sourceType
	 *            Quelltyp des hinzuzufügenden Feldes
	 * @param sourceFields
	 *            Feldbezeichnung des hinzuzufügenden Feldes
	 * @param priority
	 *            Priorität, also Stelle an die das Feld in der GUI gezogen
	 *            wurde
	 * @return die hinzugefügte ExportOption
	 * @author AlHafi
	 */

	public void addExportOption(String sourceType, String sourceField, String displayName, int priority) {
		ExportOption exportOption = new ExportOption();
		exportOption.setDisplayName(displayName);
		exportOption.setSourceField(sourceField);
		exportOption.setSourceType(sourceType);
		exportOption.setPriority(priority);
		exportOptions.add(exportOption);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(ExportField o) {
		return this.getName().compareTo(o.getName());
	}

	public String toString() {
		String string = "";
		for (ExportOption exportOption : exportOptions) {
			string += exportOption.getSourceType() + " - " + exportOption.getSourceField() + " ( " + exportOption.getPriority() + ") \n";
		}

		return string;

	}

}
