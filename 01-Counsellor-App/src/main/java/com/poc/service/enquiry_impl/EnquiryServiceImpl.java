package com.poc.service.enquiry_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.poc.DTO.Dashboard;
import com.poc.entity.Counsellor;
import com.poc.entity.Enquiry;
import com.poc.repo.CounsellorRepository;
import com.poc.repo.EnquiryRepository;
import com.poc.service.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private EnquiryRepository enqRepo;
	
	@Autowired
	private CounsellorRepository counsellorRepository;
	
	
	@Override
	public Dashboard getDashboardInfo(Integer counsellorID) {
		// TODO Auto-generated method stub
		System.out.println("inside dashbaord now");
		Long totalEnq=enqRepo.getEnquires(counsellorID);
		Long openCnt=enqRepo.getEnquires(counsellorID,"new");
		Long lostCnt=enqRepo.getEnquires(counsellorID,"lost");
		Long enrolledCnt=enqRepo.getEnquires(counsellorID,"enrolled");
		
		Dashboard d=new Dashboard();
		d.setTotalEnqs(totalEnq);
		d.setEnrolledEnqs(enrolledCnt);
		d.setLostEnqs(lostCnt);
		d.setOpenEnqs(openCnt);
		System.out.println("inside dashbaord ended");

		return d;
	}

	@Override
	public boolean addEnquiry(Enquiry enquiry,Integer counsellorId) {
		// TODO Auto-generated method stub
		Counsellor counsellor=counsellorRepository.findById(counsellorId).orElseThrow();
		enquiry.setCounsellor(counsellor);//association for fk
		Enquiry savedEnq=enqRepo.save(enquiry);
		
		return savedEnq.getEnqId()!=null;
	}

	@Override
	public List<Enquiry> getEnquires(Enquiry enquiry, Integer counsellorId) {
		// TODO Auto-generated method stub
		System.out.println("inside enquire service method started");
		Counsellor counsellor=counsellorRepository.findById(counsellorId).orElseThrow();
		enquiry.setCounsellor(counsellor);
		
		//adding filter values to entity
		Enquiry searchCriteria=new Enquiry();
		searchCriteria.setCounsellor(counsellor);
		
		/*
		 * if(!"".equals(enquiry.getCourse())) {
		 * searchCriteria.setCourse(enquiry.getCourse()); }
		 */
		
		if(null!=enquiry.getCourse() && !"".equals(enquiry.getCourse())) {
			searchCriteria.setCourse(enquiry.getCourse());
		}
		
		if(null!=enquiry.getCourse()&& !"".equals(enquiry.getMode())) {
			searchCriteria.setMode(enquiry.getMode());
		}
		
		if(null!=enquiry.getCourse()&& !"".equals(enquiry.getStatus())) {
			searchCriteria.setStatus(enquiry.getStatus());
		}
		
		//dynamic query creation
		Example <Enquiry> of =Example.of(searchCriteria);
		System.out.println("inside enquire service method Ended");
		return enqRepo.findAll(of);
	}

	@Override
	public Enquiry getEnquiry(Integer enqID) {
		// TODO Auto-generated method stub
		return enqRepo.findById(enqID).orElseThrow(); 
	}

}
