package com.pr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import antlr.collections.List;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Address;
import ezvcard.property.Birthday;
import ezvcard.property.Email;
import ezvcard.property.Gender;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;

public class vCardParser {
	private static final int BUFFER_SIZE = 4096;

	public static VCard parseContact(Contact contact) {
		VCard vcard = new VCard();

		String name = contact.getName();
		String firstName = "", lastName = "";
		if (name.split("\\w+").length > 1) {
			lastName = name.substring(name.lastIndexOf(" ") + 1);
			firstName = name.substring(0, name.lastIndexOf(' '));
		} else {
			firstName = name;
		}

		StructuredName sn = new StructuredName();
		sn.setFamily(lastName);
		sn.setGiven(firstName);

		Address address = new Address();
		address.setStreetAddress(contact.getAddress());

		Gender gender = (contact.getGender().equalsIgnoreCase("M") ? Gender.male() : Gender.female());

		Birthday bday = new Birthday(contact.getDob());

		Email email = new Email(contact.getEmail());

		Telephone tel = new Telephone(contact.getMobile());

		vcard.setFormattedName(name);
		vcard.addAddress(address);
		vcard.setGender(gender);
		vcard.setBirthday(bday);
		vcard.addEmail(email);
		vcard.addTelephoneNumber(tel);

		return vcard;
	}

	public static void parseToFile(VCard vcard, String name, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		String firstName = (name.split("\\w+").length > 1 ? name.substring(0, name.lastIndexOf(' ')) : name);
		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("");
		System.out.println("appPath = " + appPath);
		
		String fullPath = appPath + "vCard_" + firstName + ".vcf";
		System.out.println("appPath = " + appPath);
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
		}
		File vCardFile = new File(fullPath);
		OutputStream output;
		output = new FileOutputStream(vCardFile);
		Ezvcard.write(vcard).version(VCardVersion.V3_0).go(vCardFile);
		output.close();
		
		InputStream input;
		input = new FileInputStream(vCardFile);
		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "vCard_" + firstName + ".vcf");
		response.setContentType(mimeType);
		response.setContentLength((int) vCardFile.length());
		response.setHeader(headerKey, headerValue);

		output = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		input.close();
		output.close();
	}
	public static void parseAllToFile(Collection<VCard> vcard, String name, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		String firstName = (name.split("\\w+").length > 1 ? name.substring(0, name.lastIndexOf(' ')) : name);
		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("");
		System.out.println("appPath = " + appPath);
		
		String fullPath = appPath + "vCard_" + firstName + ".vcf";
		System.out.println("appPath = " + appPath);
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
		}
		File vCardFile = new File(fullPath);
		OutputStream output;
		output = new FileOutputStream(vCardFile);
		Ezvcard.write(vcard).version(VCardVersion.V3_0).go(vCardFile);
		output.close();
		
		InputStream input;
		input = new FileInputStream(vCardFile);
		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "vCard_" + firstName + ".vcf");
		response.setContentType(mimeType);
		response.setContentLength((int) vCardFile.length());
		response.setHeader(headerKey, headerValue);

		output = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		input.close();
		output.close();
	}
}
