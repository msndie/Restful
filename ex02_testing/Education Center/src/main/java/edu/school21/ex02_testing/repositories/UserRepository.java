package edu.school21.ex02_testing.repositories;

import edu.school21.ex02_testing.models.User;
import edu.school21.ex02_testing.models.projections.UserProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}