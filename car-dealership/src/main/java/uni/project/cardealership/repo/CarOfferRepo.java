package uni.project.cardealership.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.cardealership.bean.CarModelBean;
import uni.project.cardealership.bean.CarOfferBean;

@Repository
public interface CarOfferRepo  extends JpaRepository<CarOfferBean, Integer>{

	List<CarOfferBean> findAllByUserId(int id);
}
