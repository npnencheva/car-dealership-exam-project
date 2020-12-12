package uni.project.cardealership.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.cardealership.bean.CarBrandBean;

@Repository
public interface CarBrandRepo  extends JpaRepository<CarBrandBean, Integer>{
	

}
