package com.pr;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SearchDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void updateSQuery(String sQuery, String user){
		if (sQuery.compareTo("")==0){
			return;
		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String hql = "SELECT count(*) FROM Search WHERE query=\'"+sQuery+"\' AND username=\'" + user + "\'";
		Query query = session.createSQLQuery(hql);
		BigInteger exists = (BigInteger) query.uniqueResult();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		if (exists.equals(new BigInteger("1"))){
			hql = "UPDATE Search SET doa=\'"+dateFormat.format(date)+"\' WHERE query=\'"+sQuery+"\'";
		} else {
			hql = "INSERT INTO tel.search (query, doa, username) VALUES (\'"+sQuery+"\',\'"+dateFormat.format(date)+"\',\'"+user+"\')";
		}
		query = session.createSQLQuery(hql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Search> searchQueries(String user)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Search.class);
		criteria.add(Restrictions.ilike("username", user));
		criteria .addOrder(Order.desc("doa"));
		return criteria.list();
	}
}
