package com.poc.service;

import com.poc.entity.Counsellor;

public interface CounsellorService {
	
	//registation
	public boolean saveCounsellor(Counsellor counsellor);
	
	//login
	public Counsellor getCounsellor(String email,String pwd);
	

}
