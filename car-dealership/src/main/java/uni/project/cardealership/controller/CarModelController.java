package uni.project.cardealership.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.cardealership.bean.CarModelBean;
import uni.project.cardealership.bean.UserBean;
import uni.project.cardealership.repo.CarModelRepo;

@RestController
public class CarModelController {
	
	CarModelRepo carModelRepo;
	
	public CarModelController(CarModelRepo carModelRepo) {
		this.carModelRepo = carModelRepo;
	}
	
	@GetMapping(path = "/carModels/all")
	public List<CarModelBean> getAllCarModels(){
		return carModelRepo.findAll();
	}
	
	@GetMapping(path = "/carModels/brand")
	public List<CarModelBean> getAllCarModelsByBrand(@RequestParam(value = "carBrand")int carBrandId,HttpSession session){
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user != null) {
			
			return carModelRepo.findAllByBrandId(carBrandId);
		}
		
		return Collections.emptyList();
	}
}
