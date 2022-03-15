package com.maxtrain.prs.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	private RequestRepository requestRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Request>> GetAll() {
		var requests = requestRepo.findAll();
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Request> GetById(@PathVariable int id){
		var request = requestRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Request> Insert(@RequestBody Request request){
		if(request == null || request.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newRequest = requestRepo.save(request);
		return new ResponseEntity<Request>(newRequest, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Request request) {
		if(request.getId() != id || request == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequest = requestRepo.findById(id);
		if(oldRequest.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		requestRepo.save(request);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity Delete(@PathVariable int id) {
		var request = requestRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		requestRepo.delete(request.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// custom methods
		@SuppressWarnings("rawtypes")
		@PutMapping("/{id}/review")
		public ResponseEntity Review(@PathVariable int id, @RequestBody Request request) {
			String status;
			if(request.getTotal() <= 50) {
				status = "APPROVED";
			}else {
				status = "REVIEW";
			}
			request.setStatus(status);
			return Update(id, request);
		}

		@SuppressWarnings("rawtypes")
		@PutMapping("/{id}/approve")
		public ResponseEntity Approve(@PathVariable int id, @RequestBody Request request) {
			request.setStatus("APPROVED");
			return Update(id, request);
		}

		@SuppressWarnings("rawtypes")
		@PutMapping("/{id}/reject")
		public ResponseEntity Reject(@PathVariable int id, @RequestBody Request request) {
			request.setStatus("REJECTED");
			return Update(id, request);
		}
		
		@GetMapping("/review/{userId}")
		public ResponseEntity<Iterable<Request>> GetReviews(@PathVariable int userId) {
			var requests = requestRepo.findByStatusAndUserIdNot("REVIEW", userId);
			
			return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
		}
}