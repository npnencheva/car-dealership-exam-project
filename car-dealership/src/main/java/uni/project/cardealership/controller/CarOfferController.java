package uni.project.cardealership.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.cardealership.bean.CarBrandBean;
import uni.project.cardealership.bean.CarModelBean;
import uni.project.cardealership.bean.CarOfferBean;
import uni.project.cardealership.bean.UserBean;
import uni.project.cardealership.repo.CarBrandRepo;
import uni.project.cardealership.repo.CarModelRepo;
import uni.project.cardealership.repo.CarOfferRepo;

@RestController
public class CarOfferController {
	
	CarOfferRepo carOfferRepo;
	CarBrandRepo carBrandRepo;
	CarModelRepo carModelRepo;
	
	
	public CarOfferController(CarOfferRepo carOfferRepo, CarBrandRepo carBrandRepo, CarModelRepo carModelRepo) {
		this.carOfferRepo = carOfferRepo;
		this.carBrandRepo = carBrandRepo;
		this.carModelRepo = carModelRepo;
	}
	
	@PostMapping(path = "/carOffer/add")
	public ResponseEntity<Boolean> addCarOffer(
			@RequestParam(value = "image")String carImagePath,
			@RequestParam(value = "brand")int carBrandId,
			@RequestParam(value = "model")int carModelId,
			@RequestParam(value = "engine")String carEngine,
			@RequestParam(value = "price")String carPrice,
			@RequestParam(value = "shortDescription")String carShortDescription,
			HttpSession session) {
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");
		
		if(loggedUser != null) {
				
			Optional<CarBrandBean> optionalCarBrand = carBrandRepo.findById(carBrandId);
			Optional<CarModelBean> optionalCarModel = carModelRepo.findById(carModelId);
			
			if(optionalCarBrand.isPresent()) {
				CarBrandBean carBrandBean = optionalCarBrand.get();
				
				if(optionalCarModel.isPresent()){
				
					CarModelBean carModelBean = optionalCarModel.get();
					
					CarOfferBean carOfferBean = new CarOfferBean();
					
					carOfferBean.setUser(loggedUser);
					carOfferBean.setCarImage(carImagePath);
					carOfferBean.setCarBrand(carBrandBean);
					carOfferBean.setCarModel(carModelBean);
					carOfferBean.setPrice(carPrice);
					carOfferBean.setEngine(carEngine);
					carOfferBean.setShortDescription(carShortDescription);
				
					
					carOfferBean = carOfferRepo.saveAndFlush(carOfferBean);
					
					if(carOfferBean != null) {
						return new ResponseEntity<>(true,HttpStatus.OK);
					}
				}
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping(path = "/carOffers/all")
	public List<CarOfferBean> getAllCarOffers(HttpSession session) {
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");
		
		if(loggedUser != null) {
			return carOfferRepo.findAll();
		}
		return Collections.emptyList();
	}
	
	@GetMapping(path = "/carOffers/byUser")
	public List<CarOfferBean> getCarOffersByUser(HttpSession session) {
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");
		
		if(loggedUser != null) {
			
			return carOfferRepo.findAllByUserId(loggedUser.getId());
		}
		return Collections.emptyList();
	}
	
	@DeleteMapping(path = "/carOffer/delete")
	public ResponseEntity<Boolean> deleteCarOffer(
			@RequestParam(value = "id")int id,
			HttpSession session){
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");
		
		if(loggedUser == null) {
			return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
		}
		
		Optional<CarOfferBean> optionalCarOffer = carOfferRepo.findById(id);
		
		if(optionalCarOffer.isPresent()) {
			CarOfferBean carOffer = optionalCarOffer.get();
			
			if(carOffer.getUser().getId() == loggedUser.getId())	{
				carOfferRepo.delete(carOffer);				
				return new ResponseEntity<>(true, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(false, HttpStatus.I_AM_A_TEAPOT);
			}
			
		}else {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}	
	}
	
	@GetMapping(path="/carOffer/get")
	public ResponseEntity<CarOfferBean> getCarOfferByIs(@RequestParam(value = "id")int id, HttpSession session){
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");

		if(loggedUser != null)
		{
			Optional<CarOfferBean> optionalCarOffer = carOfferRepo.findById(id);
			
			if(optionalCarOffer.isPresent()) {
				CarOfferBean carOffer = optionalCarOffer.get();
								
				return new ResponseEntity<>(carOffer, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);		
	}

	
	@PutMapping(path = "/carOffer/update")
	public ResponseEntity<Boolean> updateCarOffer(
			@RequestParam(value = "id")int id,
			@RequestParam(value = "image")String carImagePath,
			@RequestParam(value = "brand")int carBrandId,
			@RequestParam(value = "model")int carModelId,
			@RequestParam(value = "engine")String carEngine,
			@RequestParam(value = "price")String carPrice,
			@RequestParam(value = "shortDescription")String carShortDescription,
			HttpSession session) {
		
		UserBean loggedUser = (UserBean)session.getAttribute("user");
		
		if(loggedUser != null) {
			
			Optional<CarOfferBean> optionalCarOffer = carOfferRepo.findById(id);
			Optional<CarBrandBean> optionalCarBrand = carBrandRepo.findById(carBrandId);
			Optional<CarModelBean> optionalCarModel = carModelRepo.findById(carModelId);
			
			if((optionalCarOffer.isPresent()) && (optionalCarBrand.isPresent()) && (optionalCarBrand.isPresent())) {
				
				CarOfferBean findCarOfferBean = optionalCarOffer.get();
				CarBrandBean carBrandBean = optionalCarBrand.get();
				CarModelBean carModelBean = optionalCarModel.get();
				
				
				findCarOfferBean.setUser(loggedUser);
				findCarOfferBean.setCarImage(carImagePath);
				findCarOfferBean.setCarBrand(carBrandBean);
				findCarOfferBean.setCarModel(carModelBean);
				findCarOfferBean.setPrice(carPrice);
				findCarOfferBean.setEngine(carEngine);
				findCarOfferBean.setShortDescription(carShortDescription);
				
					
				carOfferRepo.saveAndFlush(findCarOfferBean);
					
				if(findCarOfferBean != null) {
					return new ResponseEntity<>(true,HttpStatus.OK);	
				}
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
}
			
