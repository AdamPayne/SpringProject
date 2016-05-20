package com.pr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.property.Address;
import ezvcard.property.Birthday;
import ezvcard.property.Email;
import ezvcard.property.Gender;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;

@Controller
public class ContactsControllers
{

	@Autowired
	private ContactsDAO contactsDAO;
	
	@Autowired
	private SearchDAO searchDAO;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	private ContactFormValidator validator;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@RequestMapping("/home")
	public String home()
	{
		return "home";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
		
	@RequestMapping("/searchContacts")
	public ModelAndView searchContacts(@RequestParam(required= false, defaultValue="") String search)
	{
		ModelAndView mav = new ModelAndView("showContacts");
		List<Contact> contacts = contactsDAO.searchContacts(search.trim(),request.getSession().getAttribute("loggedInUser").toString());
		searchDAO.updateSQuery(search, request.getSession().getAttribute("loggedInUser").toString());
		mav.addObject("SEARCH_CONTACTS_RESULTS_KEY", contacts);
		return mav;
	}
	
	@RequestMapping("/viewAllContacts")
	public ModelAndView getAllContacts()
	{
		ModelAndView mav = new ModelAndView("showContacts");
		List<Contact> contacts = contactsDAO.getAllContacts(request.getSession().getAttribute("loggedInUser").toString());
		mav.addObject("SEARCH_CONTACTS_RESULTS_KEY", contacts);
		return mav;
	}
	
	@RequestMapping(value="/saveContact", method=RequestMethod.GET)
	public ModelAndView newuserForm()
	{
		ModelAndView mav = new ModelAndView("newContact");
		Contact contact = new Contact();
		mav.getModelMap().put("newContact", contact);
		return mav;
	}
	
	@RequestMapping(value="/saveContact", method=RequestMethod.POST)
	public String create(@ModelAttribute("newContact")Contact contact, BindingResult result, SessionStatus status)
	{
		validator.validate(contact, result);
		if (result.hasErrors()) 
		{				
			return "newContact";
		}
		contactsDAO.save(contact);
		status.setComplete();
		return "redirect:viewAllContacts.do";
	}
	
	@RequestMapping(value="/updateContact", method=RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id")Integer id)
	{
		ModelAndView mav = new ModelAndView("editContact");
		Contact contact = contactsDAO.getById(id);
		mav.addObject("editContact", contact);
		return mav;
	}
	
	@RequestMapping(value="/updateContact", method=RequestMethod.POST)
	public String update(@ModelAttribute("editContact") Contact contact, BindingResult result, SessionStatus status)
	{
		validator.validate(contact, result);
		if (result.hasErrors()) {
			return "editContact";
		}
		contactsDAO.update(contact);
		status.setComplete();
		return "redirect:viewAllContacts.do";
	}
	
	
	@RequestMapping("deleteContact")
	public ModelAndView delete(@RequestParam("id")Integer id)
	{
		ModelAndView mav = new ModelAndView("redirect:viewAllContacts.do");
		contactsDAO.delete(id);
		return mav;
	}
	
	@RequestMapping("downVcard")
	public void downVcard(@RequestParam("id")Integer id, HttpServletResponse response)
	{
		Contact contact = contactsDAO.getById(id);
		VCard vcard = vCardParser.parseContact(contact);	
		
		try{
			vCardParser.parseToFile(vcard, contact.getName(), request, response);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("export")
	public void downVcard(HttpServletResponse response)
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "SELECT id FROM Contacts where username=\'" + request.getSession().getAttribute("loggedInUser")+"\'";
		List contacts=session.createSQLQuery(hql).list();
		tx.commit();
		session.close();
		List<VCard> vcards= new ArrayList<VCard>();
		for (Object contactID : contacts){
			vcards.add(vCardParser.parseContact(contactsDAO.getById((Integer)contactID)));
		}
		try{
			vCardParser.parseAllToFile(vcards, "contacts", request, response);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("viewSearch")
	public ModelAndView viewSearch()
	{
		ModelAndView mav = new ModelAndView("viewSearch");
		List<Search> searches = searchDAO.searchQueries(request.getSession().getAttribute("loggedInUser").toString());
		mav.addObject("SEARCH_QUERIES_RESULTS_KEY", searches);
		return mav;
	}
	
	@RequestMapping("logOut")
	public ModelAndView logOut()
	{
		ModelAndView mav = new ModelAndView("redirect:login.do");
		request.getSession().setAttribute("loggedInUser", "");
		//System.out.println(request.getSession().getAttribute("loggedInUser").toString());
		return mav;
	}
}
