package com.poc.service.counsellor_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.entity.Counsellor;
import com.poc.repo.CounsellorRepository;
import com.poc.service.CounsellorService;

@Service
public class CounsellorServiceImpl implements CounsellorService {

	@Autowired
	private CounsellorRepository counsellorRepository;

	@Override
	public boolean saveCounsellor(Counsellor counsellor) {
		System.out.println("juts started saved method");
		// TODO Auto-generated method stub
		Counsellor findByEmail = counsellorRepository.findByEmail(counsellor.getEmail());
		if (findByEmail != null) {
			return false;
		} else {
			Counsellor savedCounsellor = counsellorRepository.save(counsellor);
			System.out.println("just now ended  method in CounsellorServiceImpl ");
			return savedCounsellor.getCounsellorId() != null;
			
		}
	}

	@Override
	public Counsellor getCounsellor(String email, String pwd) {
		// TODO Auto-generated method stub
		return counsellorRepository.findByEmailAndPwd(email, pwd);
	}

}
