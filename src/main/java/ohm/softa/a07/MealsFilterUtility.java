package ohm.softa.a07;

import ohm.softa.a07.model.Meal;

import java.util.LinkedList;
import java.util.List;

public abstract class MealsFilterUtility {
	private MealsFilterUtility(){

	}

	public static List<Meal> filterForVegetarian(List<Meal> mealsToFilter){
		List<Meal> result = new LinkedList<>();
		for (Meal m : mealsToFilter){
			if(m.isVegetarian()){
				result.add(m);
			}
		}
		return result;
	}
}
