package de.hakunacontacta.exportModule;

import java.util.ArrayList;
import java.util.Collection;

import de.hakunacontacta.exportModule.ExportManager.exportTypeEnum;
import de.hakunacontacta.shared.ExportField;

/**
 * Interface für ExportManager
 * @author AlHafi
 * @category exportauswahlModul
 * @version 0.1beta
 *
 */
public interface IExportManager {
	
	
	/**
	 * Setzt den Bearbeitungszeiger für das Exportset anhand der übergeben Enum
	 * @param type Enum die den zu bearbeitenden Typ angibt
	 */
	public void setExportFormat(exportTypeEnum type);

	/**
	 * Aendert das ExportSet anhand des uebergebenen Enums und liefert das neues Set als Collection zurueck
	 * @param type Enum die den zu bearbeitenden Typ angibt
	 * @return die sich aktuell in bearbeitung befindliche Collection aus ExportFields
	 */
	public Collection<ExportField> changeExportFormat(exportTypeEnum type);

	/**
	 * Fuegt eine ExportOption dem entsprechenden ExportField hinzu
	 * @param exportField zudem die ExportOption hinzugefuegt werden soll
	 * @param sourceType Quelltyp des hinzuzufügenden Feldes
	 * @param sourceFields Feldbezeichnung des hinzuzufügenden Feldes
	 * @param priority Priorität, also Stelle an die das Feld in der GUI gezogen wurde
	 */
	public void addExportOption(String exportField, String sourceFieldName, String sourceType, String displayName, int priority);

	/**
	 * Erstellt eine neues ExportField im aktuell in der Bearbeitung befindlichen ExportSet
	 * @param name Name des zu erstellenden ExportFields
	 */
	public void addExportField(String name);

	/**
	 * Entfernt das ExportField mit dem entsprechenden Namen aus dem aktuell in der Bearbeitung befindlichen ExportSet
	 * @param name
	 */
	public void removeExportField(String name);


	/**
	 * Diese Methode liefert das sich aktuell in der Bearbeitung befindliche ExportSet zurueck
	 * @return ExportSet / Collection aus ExportFields
	 */
	public Collection<ExportField> getChosenExportFields();

	/**
	 * Diese Methode gibt das Zielformat als String zurueck
	 * @return String mit dem Zielformat
	 */
	public String getExportFormat();
	
	public void setExportField(ArrayList<ExportField> exportFields);

}
