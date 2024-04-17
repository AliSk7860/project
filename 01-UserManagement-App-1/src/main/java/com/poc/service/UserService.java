package com.poc.service;

import java.util.Map;

import com.poc.dto.LoginDto;
import com.poc.dto.RegisterDto;
import com.poc.dto.ResetPwdDto;
import com.poc.dto.UserDto;
import com.poc.entity.UserDtlsEntity;

public interface UserService {

	public Map<Integer, String> getCountries();

	public Map<Integer, String> getStates(Integer cid);

	public Map<Integer, String> getCities(Integer sid);

	public UserDto getUser(String email);

	public boolean registerUser(RegisterDto regDto);

	public UserDto getUser(LoginDto loginDto);

	public boolean resetPwd(ResetPwdDto pwdDto);

	public String getQuote(); // api-call

}
