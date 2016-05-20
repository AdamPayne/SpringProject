package com.pr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="Search")
public class Search
{
	@Id
	private String query;
	@Column private Date doa;
	@Column private String username;
	
	public Search()
	{
	}
	
	public Search(String query, Date doa, String username)
	{
		super();
		this.query = query;
		this.doa = doa;
		this.username = username;
	}
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
