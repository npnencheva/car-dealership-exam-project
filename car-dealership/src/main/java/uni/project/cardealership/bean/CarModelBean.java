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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "model")
@JsonIgnoreProperties({"brand"})
public class CarModelBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "model", length = 250, unique = true, nullable = false)
	private String model;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id", nullable=false)
    private  CarBrandBean brand;
	
	public CarModelBean() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public CarBrandBean getBrand() {
		return brand;
	}

	public void setBrand(CarBrandBean brand) {
		this.brand = brand;
	}
	
}