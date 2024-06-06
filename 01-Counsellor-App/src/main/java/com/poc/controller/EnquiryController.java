package com.poc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poc.entity.Enquiry;
import com.poc.service.EnquiryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {
	
	@Autowired
	private EnquiryService enqService;
	
	//add enq-page display
	@GetMapping("/enquiry")
	public String addEnquiry(Enquiry enq,Model model) {
		System.out.println("inside enquiry stared");
		model.addAttribute("enq", new Enquiry());
		return "addEnq";
	}
	//save enq
	@PostMapping("/enquiry")
	public String saveEnquiry(Enquiry enq,HttpServletRequest req,Model model) {
		System.out.println("inside enquiry Save stared");
		HttpSession session=req.getSession(true);
		Integer cid=(Integer)session.getAttribute("cid");
		
		boolean status=enqService.addEnquiry(enq, cid);
		if(status) {
			model.addAttribute("smsg", "enquiry saved");
		}else {
				model.addAttribute("emsg", "enquiry not saved");
			}
		System.out.println("inside enquiry Save Ended");
		model.addAttribute("enq", new Enquiry());
			return "addEnq";
		}
	//view enqs
	@GetMapping("/enquires")
	public String getEnquires(HttpServletRequest req,Model model) {
		System.out.println("enquiry inside started");
		HttpSession session =req.getSession(false);
		Integer cid=(Integer)session.getAttribute("cid");
		
		List<Enquiry>  list=enqService.getEnquires(new Enquiry(),cid);
		model.addAttribute("enqs",list);
		
		model.addAttribute("enq", new Enquiry());
		System.out.println("enquiry inside Ended");
		return "viewEnquires";
	}
	//filter enqs
	@PostMapping("/filter-enqs")
	public String filterEnqs(@ModelAttribute("enq") Enquiry enq,HttpServletRequest req,Model model) {
		System.out.println("inside filter method started");
		HttpSession session =req.getSession(false);
		Integer cid=(Integer)session.getAttribute("cid");
		
		List<Enquiry>list=enqService.getEnquires(enq, cid);
		model.addAttribute("enqs", list);
		System.out.println("inside filter method Ended");
		return "viewEnquires";
	}
	
	//edit & update enq 
	@GetMapping("/edit")
	public String editEnquiry(@RequestParam("enqId") Integer enqid,Model model ) {
		
	Enquiry enquiry=enqService.getEnquiry(enqid);
	model.addAttribute("enq", enquiry);
	return "addEnq";
	}
}