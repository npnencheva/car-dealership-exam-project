package uni.project.cardealership.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.project.cardealership.bean.CarBrandBean;
import uni.project.cardealership.bean.UserBean;
import uni.project.cardealership.repo.CarBrandRepo;

@RestController
public class CarBrandController {
	
	CarBrandRepo carBrandRepo;
	
	public CarBrandController(CarBrandRepo carBrandRepo) {
		this.carBrandRepo = carBrandRepo;
	}
	
	@GetMapping(path = "/carBrands/all")
	public List<CarBrandBean> getAllCarBrands(HttpSession session){
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user != null) {
			
			return carBrandRepo.findAll();
		}
		
		return Collections.emptyList();
	}
}
