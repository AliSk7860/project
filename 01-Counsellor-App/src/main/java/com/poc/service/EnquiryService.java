package com.poc.service;

import java.util.List;

import com.poc.DTO.Dashboard;
import com.poc.entity.Enquiry;

public interface EnquiryService {
	
	//for dashboard page
	public Dashboard getDashboardInfo(Integer counsellorID);
	
	//save enquiry
	public boolean addEnquiry(Enquiry enquiry,Integer counsellorId);
	
	//view enquires+filter
	public List<Enquiry> getEnquires(Enquiry enquiry,Integer counsellorId);
	
	//edit
	public Enquiry getEnquiry(Integer enqID);

}
