package com.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.poc.DTO.Dashboard;
import com.poc.entity.Counsellor;
import com.poc.service.CounsellorService;
import com.poc.service.EnquiryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CounsellorController {
	
	@Autowired
	private CounsellorService counsellorService;
	
	@Autowired
	private EnquiryService enqService;
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req,Model model) {
		HttpSession session=req.getSession(false);//get session
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		System.out.println("started register");
		model.addAttribute("counsellor", new Counsellor());
		System.out.println("going to postmapping register");
		return "registerView";
	}
	@PostMapping("/register")
	public String handleRegister(Counsellor c,Model model) {
		System.out.println("insdie handle register started VIMP");
		boolean status=counsellorService.saveCounsellor(c);
		if(status) {
			model.addAttribute("smsg", "counsellor saved");
		}else {
				model.addAttribute("emsg", "falied to save");
			}
		System.out.println("insdie handle register ednded");
			return "registerView";
			
		}
	
	@GetMapping("/")
	public String login(Model model) {
		System.out.println("inside login now");
		model.addAttribute("counsellor",new Counsellor());
		return "index";
	}
	
	@PostMapping("/login")
	public String handleLogin(Counsellor counsellor,HttpServletRequest req,  Model model) {
		System.out.println("inside login in post mapping");
		Counsellor c=counsellorService.getCounsellor(counsellor.getEmail(),counsellor.getPwd());
		
		if(c==null) {
			model.addAttribute("emsg", "Invalid credentials");
			return "index";
		}else {
			//set counsellor-Id in session
			System.out.println("valid login credientials Entered now");
			HttpSession session =req.getSession(true);//always new session
			session.setAttribute("cid", c.getCounsellorId());
			
			Dashboard dbinfo=enqService.getDashboardInfo(c.getCounsellorId());
			model.addAttribute("dashboard",	dbinfo);
			System.out.println("valid login credientials ENDED");
			return "dashboard";
		}	
	}
	
	@GetMapping("/dashboard")
	public String buildBashboard(HttpServletRequest req,  Model model) {
		System.out.println("display  started dashboard");
	HttpSession session =req.getSession(false);
	Integer cid=(Integer)session.getAttribute("cid");
	
	Dashboard dbinfo=enqService.getDashboardInfo(cid);
	model.addAttribute("dashboard",	dbinfo);
	System.out.println("valid login credientials ENDED");
	System.out.println("display  Stopped dashboard");
	return "dashboard";
}
}