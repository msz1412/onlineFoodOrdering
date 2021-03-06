package serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import online.food.ordering.Restaurant;
import service.RestaurantService;
import ch.ivyteam.ivy.environment.Ivy;

public class RestaurantServiceImpl implements RestaurantService {

	private static final String PERSISTENCE_UNIT_NAME = "orderingDS";

	@Override
	public Integer saveRestaurant(Restaurant restaurant) {
		restaurant.setCreatedDate(new Date());
		restaurant.setHasPurchased(false);
		return Ivy.persistence().get(PERSISTENCE_UNIT_NAME).persist(restaurant).getId();
	}

	@Override
	public List<Restaurant> getAllRestaurant(boolean hasPurchased) {

		List<Restaurant> reList = new ArrayList<Restaurant>();
		List<Restaurant> resList =  Ivy.persistence().get(PERSISTENCE_UNIT_NAME).findAll(Restaurant.class);
		for (Restaurant res: resList) {
			if (res.getHasPurchased().equals(hasPurchased)) {
				reList.add(res);
			}
		}
		return reList;
	}

	@Override
	public Restaurant getRestaurantById(Integer restaurantId) {
		EntityManager entityManager = Ivy.persistence().get(PERSISTENCE_UNIT_NAME).createEntityManager();
		Query query = entityManager.createQuery(getRestaurantSql());
		query.setParameter("restaurantId", Integer.valueOf(restaurantId));

		return (Restaurant) query.getSingleResult();
	}

	private String getRestaurantSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("From Restaurant Where id = :restaurantId ");
		return sql.toString();
	}

	@Override
	public void removeRestaurant(Restaurant restaurant) {
		Ivy.persistence().get(PERSISTENCE_UNIT_NAME).remove(restaurant);
	}

	@Override
	public Restaurant cloneRestaurant(Restaurant restaurant) {
		Restaurant res = new Restaurant();
		res.setRestaurantName(restaurant.getRestaurantName());
		res.setOutOfFood(restaurant.getOutOfFood());
		res.setPhone(restaurant.getPhone());
		res.setWebsite(restaurant.getWebsite());
		res.setCreatedDate(new Date());
		res.setNumberOfOrdered(0);
		res.setOrderedFood(null);
		res.setHasPurchased(false);
		res = Ivy.persistence().get(PERSISTENCE_UNIT_NAME).persist(res);

		return res;
	}

	@Override
	public void setPurchasedRestaurant(Restaurant restaurant) {
		restaurant.setHasPurchased(true);
		Ivy.persistence().get(PERSISTENCE_UNIT_NAME).merge(restaurant);
		
	}
}
