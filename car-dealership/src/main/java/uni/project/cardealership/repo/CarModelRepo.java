package uni.project.cardealership.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.cardealership.bean.CarBrandBean;
import uni.project.cardealership.bean.CarModelBean;

@Repository
public interface CarModelRepo extends JpaRepository<CarModelBean, Integer>{
	
	List<CarModelBean> findAllByBrandId(int id);

}
