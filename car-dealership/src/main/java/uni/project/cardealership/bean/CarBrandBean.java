package uni.project.cardealership.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "brand")
@JsonIgnoreProperties({"models"})
public class CarBrandBean  implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int  id;
	
	@Column(name = "brand", length = 250, unique = true, nullable = false)
	private String brand;
	
	@OneToMany(mappedBy="brand", fetch = FetchType.EAGER)
    private List<CarModelBean> models;
	
	public CarBrandBean() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public List<CarModelBean> getModels() {
		return models;
	}

	public void setModels(List<CarModelBean> models) {
		this.models = models;
	}
}
