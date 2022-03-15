package com.maxtrain.prs.request;


import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer> {
	Iterable<Request> findByStatusAndUserIdNot(String status, int userId);
}
