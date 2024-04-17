package com.poc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.dto.LoginDto;
import com.poc.dto.QuoteDto;
import com.poc.dto.RegisterDto;
import com.poc.dto.ResetPwdDto;
import com.poc.dto.UserDto;
import com.poc.entity.CityEntity;
import com.poc.entity.CountryEntity;
import com.poc.entity.StateEntity;
import com.poc.entity.UserDtlsEntity;
import com.poc.repo.CityRepo;
import com.poc.repo.CountryRepo;
import com.poc.repo.StateRepo;
import com.poc.repo.UserDtlsRepo;
import com.poc.utils.EmailUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private CountryRepo countryRepo;

	@Autowired
	private StateRepo stateRepo;

	@Autowired
	private CityRepo cityRepo;

	@Autowired
	private EmailUtils emailUtils;

	private QuoteDto[] quotations = null;

	@Override
	public Map<Integer, String> getCountries() {
		// TODO Auto-generated method stub
		Map<Integer, String> countryMap = new HashMap<>();

		List<CountryEntity> countiesList = countryRepo.findAll();
		countiesList.forEach(c -> {
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer cid) {
		// TODO Auto-generated method stub
		Map<Integer, String> stateMap = new HashMap<>();
		// if you don't want write code you can follows SQL queries

		/*
		 * Map<Integer, String>stateMap=new HashMap<>();
		 * 
		 * CountryEntity country=new CountryEntity(); country.setCountryId(cid);
		 * 
		 * StateEntity entity=new StateEntity(); entity.setCountryEntity(country);
		 * 
		 * Example<StateEntity>of= Example.of(entity);
		 * List<StateEntity>statesList=stateRepo.findAll(of);
		 */

		List<StateEntity> statesList = stateRepo.getStates(cid);

		statesList.forEach(s -> {
			stateMap.put(s.getStateId(), s.getStateName());
		});

		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer sid) {
		// TODO Auto-generated method stub
		Map<Integer, String> citiesMap = new HashMap<>();
		List<CityEntity> citiesList = cityRepo.getCities(sid);

		citiesList.forEach(c -> {
			citiesMap.put(c.getCityId(), c.getCityName());
		});
		return citiesMap;
	}

	@Override
	public UserDto getUser(String email) {
		// TODO Auto-generated method stub
		UserDtlsEntity userDtlsEntity = userDtlsRepo.findByEmail(email);

		// UserDto dto=new UserDto(); // BeanUtils.copyProperties(userDtlsEntity, dto);
		// // return dto;

		if (userDtlsEntity == null) {
			return null;
		}

		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(userDtlsEntity, UserDto.class);

		return userDto;

	}

	@Override
	public boolean registerUser(RegisterDto regDto) {
		// TODOAuto-generated method stub
		ModelMapper mapper = new ModelMapper();
		UserDtlsEntity entity = mapper.map(regDto, UserDtlsEntity.class);
		CountryEntity country = countryRepo.findById(regDto.getCountryId()).orElseThrow();
		StateEntity state = stateRepo.findById(regDto.getStateId()).orElseThrow();
		CityEntity city = cityRepo.findById(regDto.getCityId()).orElseThrow();

		entity.setCountry(country);
		entity.setState(state);
		entity.setCity(city);

		entity.setPwd(generateRandom());
		entity.setPwdUpdated("NO");

		UserDtlsEntity savedEntity = userDtlsRepo.save(entity);

		String subject = "User Registration";
		String body = "your temporary pwd is" + entity.getPwd();
		emailUtils.sendEmail(regDto.getEmail(), subject, body);

		return savedEntity.getUserId() != null;

	}

	@Override
	public UserDto getUser(LoginDto loginDto) {
		// TODO Auto-generated method stub
		System.out.println("inside login dto method");
		UserDtlsEntity userDtlsEntity = userDtlsRepo.findByEmailAndPwd(loginDto.getEmail(), loginDto.getPwd());
		if (userDtlsEntity == null) {
			return null;
		}
		ModelMapper mapper = new ModelMapper();
		System.out.println("inside login dto Ended");
		return mapper.map(userDtlsEntity, UserDto.class);
	}

	@Override
	public boolean resetPwd(ResetPwdDto pwdDto) {
		// TODO Auto-generated method stub

		// UserDtlsEntity userDtlsEntity=userDtlsRepo.findByEmail(pwdDto.getEmail());
		UserDtlsEntity userDtlsEntity = userDtlsRepo.findByEmailAndPwd(pwdDto.getEmail(), pwdDto.getOldPwd());

		if (userDtlsEntity != null) {
			userDtlsEntity.setPwd(pwdDto.getNewPwd());
			userDtlsEntity.setPwdUpdated("YES");

			userDtlsRepo.save(userDtlsEntity);
			return true;
		}
		return false;
	}

	@Override
	public String getQuote() {
		// TODO Auto-generated method stub

		if (quotations == null) {
			String url = "https://type.fit/api/quotes";

			// web Service call
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> forEntity = rt.getForEntity(url, String.class);
			String responseBody = forEntity.getBody();
			ObjectMapper mapper = new ObjectMapper();

			try {
				quotations = mapper.readValue(responseBody, QuoteDto[].class);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		Random r = new Random();
		int index = r.nextInt(quotations.length - 1);
		return quotations[index].getText();
	}

	private static String generateRandom() {
		System.out.println("calling random password");
		String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			res.append(aToZ.charAt(randIndex));
		}
		return res.toString();
	}

}
