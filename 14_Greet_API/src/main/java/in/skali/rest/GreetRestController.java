package in.skali.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetRestController {

	@GetMapping("/greet")
	public String WelcomeMsg() {
		
		String msg="Good morning";
		return msg;
	}
	
}
