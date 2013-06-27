package de.hakunacontacta.fileModule;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;

import ezvcard.Ezvcard;
import ezvcard.VCard;

import net.sourceforge.cardme.io.VCardWriter;
import net.sourceforge.cardme.util.Util;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.arch.EncodingType;
import net.sourceforge.cardme.vcard.arch.LanguageType;
import net.sourceforge.cardme.vcard.arch.ParameterTypeStyle;
import net.sourceforge.cardme.vcard.arch.VCardTypeName;
import net.sourceforge.cardme.vcard.arch.VCardVersion;
import net.sourceforge.cardme.vcard.errors.VCardErrorHandler;
import net.sourceforge.cardme.vcard.exceptions.VCardBuildException;
import net.sourceforge.cardme.vcard.types.AdrType;
import net.sourceforge.cardme.vcard.types.BDayType;
import net.sourceforge.cardme.vcard.types.CategoriesType;
import net.sourceforge.cardme.vcard.types.ClassType;
import net.sourceforge.cardme.vcard.types.EmailType;
import net.sourceforge.cardme.vcard.types.ExtendedType;
import net.sourceforge.cardme.vcard.types.FNType;
import net.sourceforge.cardme.vcard.types.GeoType;
import net.sourceforge.cardme.vcard.types.ImppType;
import net.sourceforge.cardme.vcard.types.KeyType;
import net.sourceforge.cardme.vcard.types.LabelType;
import net.sourceforge.cardme.vcard.types.LogoType;
import net.sourceforge.cardme.vcard.types.MailerType;
import net.sourceforge.cardme.vcard.types.NType;
import net.sourceforge.cardme.vcard.types.NameType;
import net.sourceforge.cardme.vcard.types.NicknameType;
import net.sourceforge.cardme.vcard.types.NoteType;
import net.sourceforge.cardme.vcard.types.OrgType;
import net.sourceforge.cardme.vcard.types.PhotoType;
import net.sourceforge.cardme.vcard.types.ProdIdType;
import net.sourceforge.cardme.vcard.types.ProfileType;
import net.sourceforge.cardme.vcard.types.RevType;
import net.sourceforge.cardme.vcard.types.RoleType;
import net.sourceforge.cardme.vcard.types.SortStringType;
import net.sourceforge.cardme.vcard.types.SoundType;
import net.sourceforge.cardme.vcard.types.SourceType;
import net.sourceforge.cardme.vcard.types.TelType;
import net.sourceforge.cardme.vcard.types.TitleType;
import net.sourceforge.cardme.vcard.types.TzType;
import net.sourceforge.cardme.vcard.types.UidType;
import net.sourceforge.cardme.vcard.types.UrlType;
import net.sourceforge.cardme.vcard.types.VersionType;
import net.sourceforge.cardme.vcard.types.media.AudioMediaType;
import net.sourceforge.cardme.vcard.types.media.ImageMediaType;
import net.sourceforge.cardme.vcard.types.media.KeyTextType;
import net.sourceforge.cardme.vcard.types.params.AdrParamType;
import net.sourceforge.cardme.vcard.types.params.EmailParamType;
import net.sourceforge.cardme.vcard.types.params.ExtendedParamType;
import net.sourceforge.cardme.vcard.types.params.ImppParamType;
import net.sourceforge.cardme.vcard.types.params.LabelParamType;
import net.sourceforge.cardme.vcard.types.params.TelParamType;


public class Main {


	private static VCardImpl vcardFull = null;
	
	public static void main(String[] args) throws IOException, URISyntaxException, VCardBuildException {

		vcardFull = getFullVCardNoErrors();
		
		
		VCardWriter writer = new VCardWriter();
		writer.setVCard(vcardFull);
		String vString = writer.buildVCardString();
		System.out.println(vString);
		

		VCard vcard = Ezvcard.parse(vString).first();
		String xml = Ezvcard.writeXml(vcard).go();
		System.out.println("ACHTUNG:");
		System.out.println(xml);


	}
	
	private static VCardImpl getFullVCardNoErrors() throws IOException, URISyntaxException
	{
		VCardImpl vcard = new VCardImpl();
		vcard.setVersion(new VersionType(VCardVersion.V3_0));
		
		NameType name = new NameType();
		name.setName("VCard for John Doe");
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
		n.setLanguage(LanguageType.EN);
		n.setFamilyName("DÖe");
//		n.setFamilyName("Doe");
		n.setGivenName("John");
		n.addHonorificPrefix("Mr.");
		n.addHonorificSuffix("I");
		n.addAdditionalName("Johny");
		vcard.setN(n);
		
		FNType fn = new FNType();
		fn.setFormattedName("John \"Johny\" Doe");
		fn.setCharset(Charset.forName("UTF-8"));
		fn.setLanguage(LanguageType.EN);
		vcard.setFN(fn);
		
		CategoriesType categories = new CategoriesType();
		categories.addCategory("Category 1");
		categories.addCategory("Category 2");
		categories.addCategory("Category 3");
		vcard.setCategories(categories);
		
		OrgType organizations = new OrgType();
		organizations.setOrgName("IBM");
		organizations.addOrgUnit("SUN");
		vcard.setOrg(organizations);
		
		vcard.setUid(new UidType("c0ff639f-9633-4e57-bcfd-55079cfd9d65"));
		vcard.addUrl(new UrlType(new URL("http://www.sun.com")));
		vcard.setGeo(new GeoType(3.4f, -2.6f));

		Calendar birthday = Calendar.getInstance();
		birthday.clear();
		birthday.set(Calendar.YEAR, 1980);
		birthday.set(Calendar.MONTH, 4);
		birthday.set(Calendar.DAY_OF_MONTH, 21);
		vcard.setBDay(new BDayType(birthday));

		Calendar revision = Calendar.getInstance();
		revision.clear();
		revision.set(Calendar.YEAR, 2012);
		revision.set(Calendar.MONTH, 6);
		revision.set(Calendar.DAY_OF_MONTH, 8);
		vcard.setRev(new RevType(revision));
		
		vcard.setTz(new TzType(Calendar.getInstance().getTimeZone()));
		
		AdrType address1 = new AdrType();
		address1.setCharset("UTF-8");
		address1.setExtendedAddress("");
		address1.setCountryName("U.S.A.");
		address1.setLocality("New York");
		address1.setRegion("New York");
		address1.setPostalCode("NYC887");
		address1.setPostOfficeBox("25334");
		address1.setStreetAddress("South cresent drive, Building 5, 3rd floor");
		address1.addParam(AdrParamType.HOME)
		.addParam(AdrParamType.PARCEL)
		.addParam(AdrParamType.PREF)
		.addExtendedParam(new ExtendedParamType("CUSTOM-PARAM-TYPE", VCardTypeName.ADR))
		.addExtendedParam(new ExtendedParamType("CUSTOM-PARAM-TYPE", "WITH-CUSTOM-VALUE", VCardTypeName.ADR));
		

		LabelType labelForAddress1 = new LabelType();
		labelForAddress1.setCharset("UTF-8");
		labelForAddress1.addParam(LabelParamType.HOME)
		.addParam(LabelParamType.PARCEL)
		.addParam(LabelParamType.PREF)
		.setLabel("John Doe\nNew York, NewYork,\nSouth Crecent Drive,\nBuilding 5, floor 3,\nUSA");
		
		address1.setLabel(labelForAddress1);
		vcard.addAdr(address1);
		
		TelType telephone = new TelType();
		telephone.setCharset("UTF-8");
		telephone.setTelephone("+1 (212) 204-34456");
		telephone.addParam(TelParamType.CELL)
		.addParam(TelParamType.HOME)
		.setParameterTypeStyle(ParameterTypeStyle.PARAMETER_VALUE_LIST);
		vcard.addTel(telephone);
		
		TelType telephone2 = new TelType();
		telephone2.setTelephone("00-1-212-555-7777");
		telephone2.addParam(TelParamType.FAX)
		.addParam(TelParamType.WORK)
		.setParameterTypeStyle(ParameterTypeStyle.PARAMETER_LIST);
		vcard.addTel(telephone2);
		
		EmailType email = new EmailType();
		email.setEmail("john.doe@ibm.com");
		email.addParam(EmailParamType.IBMMAIL)
		.addParam(EmailParamType.INTERNET)
		.addParam(EmailParamType.PREF)
		.setCharset("UTF-8");
		vcard.addEmail(email);
		vcard.addEmail(new EmailType("billy_bob@gmail.com"));
		
		NoteType note = new NoteType();
		note.setNote("THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\"\nAND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE\nIMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE\nARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE\nLIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\nCONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF\nSUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS\nINTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN\nCONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)\nARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE\nPOSSIBILITY OF SUCH DAMAGE.");
		vcard.addNote(note);
		
		ExtendedType Xitem1 = new ExtendedType("X-item", "ValueOfItem1");
		Xitem1.setCharset("UTF-8");
		
		vcard.addExtendedType(Xitem1);
		vcard.addExtendedType(new ExtendedType("X-LONG-STRING", "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
		
		vcard.addImpp(new ImppType("im:alice@example.com"));
		vcard.addImpp(new ImppType(new URI("im:alice2@example.com")));
		
		ImppType impp = new ImppType();
		impp.setUri(new URI("im:alice3@example.com"));
		impp.addParam(ImppParamType.HOME)
		.addParam(ImppParamType.PREF)
		.addExtendedParam(new ExtendedParamType("X-BLA", "BLE", VCardTypeName.IMPP));
		vcard.addImpp(impp);
		
		((VCardErrorHandler)vcard).setThrowExceptions(false);
		
		return vcard;
	}

}
