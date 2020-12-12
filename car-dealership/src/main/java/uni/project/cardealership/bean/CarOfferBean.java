package uni.project.cardealership.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import uni.project.cardealership.bean.UserBean;

@Entity
@Table(name = "car_offer")
@JsonIgnoreProperties({"user"})
public class CarOfferBean implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserBean user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "brand_id")
	private CarBrandBean carBrand;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "model_id")
	private CarModelBean carModel;
	
	@Column(name = "engine", length = 30)
	private String engine;
	
	@Column(name = "price", length = 16)
	private String price;
	
	@Column(name = "shortDescription", length = 255)
	private String shortDescription;
	
	@Column(name = "carImage", length = 100)
	private String carImage;
	
	public CarOfferBean() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public CarBrandBean getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(CarBrandBean carBrand) {
		this.carBrand = carBrand;
	}

	public CarModelBean getCarModel() {
		return carModel;
	}

	public void setCarModel(CarModelBean carModel) {
		this.carModel = carModel;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getEngine() {
		return engine;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCarImage() {
		return carImage;
	}

	public void setCarImage(String carImage) {
		this.carImage = carImage;
	}
	
}
