package de.hakunacontacta.exportModule;

import java.util.ArrayList;

/**
 * ExportField bestimmt das Zielfeld in der Ausgabedatei
 * @author AlHafi
 * @category exportauswahlModul
 * @version 0.1beta
 *
 */
public class ExportField implements Comparable<ExportField> {

	private String name;
	private ArrayList<ExportOption> exportOptions = new ArrayList<ExportOption>();
	
	/**
	 * Diese Methode liefert aller Exportoptions des aktuell bearbeiteten ExportSets(CSV,XML, usw) als Collection  zurück
	 * @return Collection aus ExportOption
	 * @author AlHafi
	 * @
	 */
	public ArrayList<ExportOption> getExportOptions() {
		return exportOptions;
	}
	
	/**
	 * Diese Methode erlaubt es eine Exportoption zum aktuellen ExportSet(CSV,XML, usw) hinzuzufügen
	 * @param sourceType Quelltyp des hinzuzufügenden Feldes
	 * @param sourceFields Feldbezeichnung des hinzuzufügenden Feldes
	 * @param priority Priorität, also Stelle an die das Feld in der GUI gezogen wurde
	 * @return die hinzugefügte ExportOption
	 * @author AlHafi
	 */
	public ExportOption addExportOption(String sourceType, String sourceFields, int priority) {
		ExportOption exportOption = new ExportOption(sourceType, sourceFields, priority);
		exportOptions.add(exportOption);
		return exportOption;
	}

	/**
	 * Die Methode entfernt eine entsprechende ExportOption aus dem aktuellen ExportSet(CSV,XML, usw) anhand der Attribute
	 * @param sourceType Quelltyp des zu entfernden Feldes
	 * @param sourceField Feldbezeichnung des zu entfernden Feldes
	 * @author AlHafi
	 */
	public void removeExportOption(String sourceType, String sourceField) {
		for (ExportOption exportOptionx : this.exportOptions) {
			if (exportOptionx.getSourceField() == sourceField && exportOptionx.getSourceType() == sourceType) {
				this.exportOptions.remove(exportOptionx);
			}

		}
	}	

	public ExportField(String name) {
		this.name = name;
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



	
	// public void addExportOption(String sourceType, String name, int priority)
	// {
	// ExportOption exportOption = new ExportOption(sourceType, name, priority);
	// exportOptions.add(exportOption);
	// }

}
