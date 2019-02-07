package dotterbear.service.eureka.scheduler.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dotterbear.service.eureka.scheduler.entity.EurekaScheduler;

@Repository
public interface EurekaSchedulerRepository extends CrudRepository<EurekaScheduler, String> {

	Optional<EurekaScheduler> findById(String id);

}
