package uni.project.cardealership.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import uni.project.cardealership.WebSecurityConfig;
import uni.project.cardealership.bean.UserBean;
import uni.project.cardealership.repo.UserRepo;

@RestController
public class UserController {

	private UserRepo userRepo;
	private WebSecurityConfig webSecurityConfig;
	
	public UserController(UserRepo userRepo, WebSecurityConfig webSecurityConfig) {
		this.userRepo = userRepo;
		this.webSecurityConfig = webSecurityConfig;
	}
	
	
	@PostMapping(path = "/register")
	public ResponseEntity<UserBean> register(@RequestParam(value ="email")String email, 
			@RequestParam(value ="username")String username, @RequestParam(value ="password")String password,
			@RequestParam(value ="repeatPassword")String repeatPassword) {
		
		if(password.equals(repeatPassword)) {
			
			UserBean user = new UserBean(username, hashPassword(password), email);
			
			return new ResponseEntity<>(userRepo.saveAndFlush(user),HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(path = "/login")
	public String login(
			@RequestParam(value = "username")
			String username, 
			@RequestParam(value = "password")
			String password, HttpSession session) {
		
		UserBean user = userRepo.findUserByUsernameAndPassword(username, hashPassword(password));
		
		if(user != null) {
			session.setAttribute("user", user);
			
			try {
				UserDetails userDetails = 
						webSecurityConfig.userDetailsServiceBean().
						loadUserByUsername(user.getUsername());
				
				if(userDetails != null) {
					Authentication auth = new UsernamePasswordAuthenticationToken(
							userDetails.getUsername(),
							userDetails.getPassword(),
							userDetails.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(auth);
					
					ServletRequestAttributes attr = (ServletRequestAttributes)
							RequestContextHolder.currentRequestAttributes();
					
					HttpSession http = attr.getRequest().getSession(true);
					http.setAttribute("SPRING_SECURITY_CONTEXT", 
							SecurityContextHolder.getContext());
				}
				
				return "home.html";
				
			} catch (UsernameNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		return "error.html";	
				
	}

	
	@GetMapping(path="/profile")
	public ResponseEntity<UserBean> getLoggedProfile(HttpSession session){
		
		UserBean user = (UserBean) session.getAttribute("user");
		
		if(user != null)
		{
			
			Optional<UserBean> userFromDb = userRepo.findById(user.getId());
			
			if(userFromDb.isPresent())
			{
				return new ResponseEntity<>(
					userFromDb.get(),HttpStatus.OK);
			}
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
				
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);		
	}

	
	@PutMapping(path="/profile/updateMyProfile")
	public ResponseEntity<Boolean> updateMyProfile(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "repeatPassword") String repeatPassword,
			HttpSession session){
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");
		
		if(loggedUser != null) {
			
			if(password.equals(repeatPassword)) {
				
				Optional<UserBean> findUser = userRepo.findById(loggedUser.getId());
				
				if(findUser.isPresent()) {
					
					findUser.get().setPassword(hashPassword(password));
					
					userRepo.saveAndFlush(findUser.get());
					return new ResponseEntity<>(true,HttpStatus.OK);	
				}
				
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
			}
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
	}
				
	@GetMapping(path = "/loggedUser")
	public ResponseEntity<Integer> loggedUser(HttpSession session){
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user != null) {
			return new ResponseEntity<>(user.getId(), HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
		}		
	}
	

	@PostMapping(path = "/logout")
	public ResponseEntity<Boolean> logout(HttpSession session){
		
		UserBean user = (UserBean) session.getAttribute("user");
		
		if(user != null ) {
			session.invalidate();
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}
	
private String hashPassword(String password) {
		
		StringBuilder result = new StringBuilder();
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(password.getBytes());
			
			byte[] bytes = md.digest();
			
			for(int i = 0; i < bytes.length; i++) {
				result.append((char)bytes[i]);
			}			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
	
		return result.toString();
	}
}
