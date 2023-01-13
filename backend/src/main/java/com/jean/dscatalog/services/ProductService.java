package com.jean.dscatalog.services;

import com.jean.dscatalog.dto.CategoryDTO;
import com.jean.dscatalog.dto.ProductDTO;
import com.jean.dscatalog.entities.Category;
import com.jean.dscatalog.entities.Product;
import com.jean.dscatalog.repositories.CategoryRepository;
import com.jean.dscatalog.repositories.ProductRepository;
import com.jean.dscatalog.services.exceptions.DatabaseException;
import com.jean.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = repository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}



	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
		catch (DataIntegrityViolationException e ) {
			throw new DatabaseException("Integrity violation");
		}

	}
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

	entity.setName(dto.getName());
	entity.setDate(dto.getDate());
	entity.setDescription(dto.getDescription());
	entity.setPrice(dto.getPrice());
	entity.setImgUrl(dto.getImgUrl());

	entity.getCategories().clear();
	for (CategoryDTO catDTO : dto.getCategories()) {
		Category category = categoryRepository.getOne(catDTO.getId());
		entity.getCategories().add(category);

	}

	}
}
