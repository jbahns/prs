package com.maxtrain.prs.requestline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {

	@Autowired
	private RequestlineRepository requestlineRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> GetAll() {
		var requestlines = requestlineRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestlines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Requestline> GetById(@PathVariable int id){
		var requestline = requestlineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Requestline> Insert(@RequestBody Requestline requestline) throws Exception{
		if(requestline == null || requestline.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newRequestline = requestlineRepo.save(requestline);
		var respEntity = this.recalcRequestTotal(newRequestline.getRequest().getId());
		if(respEntity.getStatusCode() !=HttpStatus.OK) {
			throw new Exception("Recalculate request total failed.");
		}
		
		return new ResponseEntity<Requestline>(newRequestline, HttpStatus.CREATED);
	}
	

	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(requestline.getId() != id || requestline == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequestline = requestlineRepo.findById(requestline.getId());
		if(oldRequestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		requestlineRepo.save(oldRequestline.get());
		var respEntity = this.recalcRequestTotal(oldRequestline.get().getRequest().getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed.");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity Delete(@PathVariable int id) throws Exception {
		var requestOpt = requestlineRepo.findById(id);
		if(requestOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var request = requestOpt.get();
		requestlineRepo.delete(request);
		var respEntity = this.recalcRequestTotal(request.getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed.");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// custom method
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity recalcRequestTotal(int requestId) {
		var reqOpt = reqRepo.findById(requestId);
		if(reqOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var request = reqOpt.get();
		var requestTotal=0;
		for(var requestline : request.getRequestlines()) {
			requestTotal += requestline.getProduct().getPrice() * requestline.getQuantity();
		}
		request.setTotal(requestTotal);;
		reqRepo.save(request);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}



