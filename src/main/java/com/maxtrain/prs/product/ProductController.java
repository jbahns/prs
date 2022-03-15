package com.maxtrain.prs.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Product>> GetAll() {
		var products = productRepo.findAll();
		return new ResponseEntity<Iterable<Product>>(products, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Product> GetById(@PathVariable int id){
		var product = productRepo.findById(id);
		if(product.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Product> Insert(@RequestBody Product product){
		if(product == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newProduct = productRepo.save(product);
		return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Product product) {
		if(product.getId() != id || product.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldProduct = productRepo.findById(product.getId());
		if(oldProduct.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		productRepo.save(product);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity Delete(@PathVariable int id) {
		var product = productRepo.findById(id);
		if(product.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		productRepo.delete(product.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}


