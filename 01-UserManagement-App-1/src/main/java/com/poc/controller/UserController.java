package com.poc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poc.dto.LoginDto;
import com.poc.dto.RegisterDto;
import com.poc.dto.ResetPwdDto;
import com.poc.dto.UserDto;
import com.poc.service.UserService;
import com.poc.utils.AppConstants;
import com.poc.utils.AppProperties;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AppProperties props;

	@GetMapping("/register")
	public String registerPage(Model model) {

		model.addAttribute("registerDto", new RegisterDto());
		model.addAttribute("countries", userService.getCountries());

		return "registerView";
	}

	@GetMapping("/states/{cid}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("cid") Integer cid) {

		return userService.getStates(cid);
	}

	@GetMapping("/cities/{sid}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("sid") Integer sid) {
		return userService.getCities(sid);
	}

	@PostMapping("/register")
	public String register(RegisterDto regDto, Model model) {

		model.addAttribute("countries", userService.getCountries());

		Map<String, String> messages = props.getMessages();

		UserDto user = userService.getUser(regDto.getEmail());
		if (user != null) {
			// Duplicate Email
			model.addAttribute(AppConstants.ERROR_MSG, messages.get("dupe"));
			return "registerView";
		}
		boolean registerUser = userService.registerUser(regDto);
		if (registerUser) {
			// User Registered
			model.addAttribute(AppConstants.SUCC_MSG, messages.get("regs"));
		} else {
			// Registration Failed
			model.addAttribute(AppConstants.ERROR_MSG, messages.get("regf"));
		}
		return "registerView";
	}

	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute("loginDto", new LoginDto());
		return "index";
	}

	@PostMapping("/login")
	public String login(LoginDto loginDto, Model model) {

		// int i=100/0;

		Map<String, String> messages = props.getMessages();

		UserDto user = userService.getUser(loginDto);
		if (user == null) {
			// Invalid Credentials
			model.addAttribute(AppConstants.ERROR_MSG, messages.get("invc"));
			return "index";
		}
		if ("YES".equals(user.getPwdUpdated())) {
			// pwd already updated - go to dashboard
			return "redirect:dashboard";
		} else {
			// pwd not updated -go to reset pwd page
			ResetPwdDto resetPwdDto = new ResetPwdDto();
			resetPwdDto.setEmail(user.getEmail());
			model.addAttribute("resetPwdDto", resetPwdDto);
			return AppConstants.RESET_PWD_VIEW;
		}
	}

	@PostMapping("/resetPwd")
	public String resetPwd(ResetPwdDto pwdDto, Model model) {

		Map<String, String> messages = props.getMessages();

		if (!(pwdDto.getNewPwd().equals(pwdDto.getConfirmPwd()))) {
			// New Pwd and old Pwd should be same
			model.addAttribute(AppConstants.ERROR_MSG, messages.get("pwdme"));
			return AppConstants.RESET_PWD_VIEW;
		}

		UserDto user = userService.getUser(pwdDto.getEmail());

		if (user.getPwd().equals(pwdDto.getOldPwd())) {
			boolean resetPwd = userService.resetPwd(pwdDto);
			if (resetPwd) {
				return "redirect:dashboard";
			} else {
				// Password update Failed
				model.addAttribute(AppConstants.ERROR_MSG, messages.get("pwdue"));
				return AppConstants.RESET_PWD_VIEW;
			}
		} else {
			// Given old Pwd is wrong
			model.addAttribute(AppConstants.ERROR_MSG, messages.get("oldpwde"));
			return AppConstants.RESET_PWD_VIEW;
		}
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		String quote = userService.getQuote();
		model.addAttribute("quote", quote);

		return "dashboardView";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}

}
