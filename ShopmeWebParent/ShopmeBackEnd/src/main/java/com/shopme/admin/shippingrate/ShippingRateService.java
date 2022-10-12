package com.shopme.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.pagging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;

@Service
@Transactional
public class ShippingRateService {

	public static final int RATES_PER_PAGE = 10;
	private static final int DIM_DIVISOR = 139;
private ShippingRateRepository shipRepo;
	
	private CountryRepository countryRepo;
	
	private ProductRepository productRepo;
	
	@Autowired
	public ShippingRateService(ShippingRateRepository shipRepo, CountryRepository countryRepo,ProductRepository productRepo) {
		super();
		
		this.shipRepo = shipRepo;
		this.countryRepo = countryRepo;
		this.productRepo = productRepo;
	}


	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}
	

	public List<Country> listAllCountries() {
		// TODO Auto-generated method stub
		return countryRepo.findAllByOrderByNameAsc();
	}


	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		
		ShippingRate rateInDB = shipRepo.findByCountryAndState(
				rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
						+ rateInForm.getCountry().getName() + ", " + rateInForm.getState()); 					
		}
		shipRepo.save(rateInForm);
	}

	
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		// TODO Auto-generated method stub
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
	}

	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		// TODO Auto-generated method stub
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}

		shipRepo.updateCODSupport(id, codSupported);
	}

	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		// TODO Auto-generated method stub
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);

		}
		shipRepo.deleteById(id);
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId, String state) 
			throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);

		if (shippingRate == null) {
			throw new ShippingRateNotFoundException("No shipping rate found for the given "
					+ "destination. You have to enter shipping cost manually.");
		}

		Product product = productRepo.findById(productId).get();

		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

		return finalWeight * shippingRate.getRate();
	}
	
}
