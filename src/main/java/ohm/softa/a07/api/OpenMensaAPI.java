package ohm.softa.a07.api;

import ohm.softa.a07.model.Meal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface OpenMensaAPI {
	// TODO add method to get meals of a day
	// Mensa Kesslerplatz: 268
	// Mensa Insel Schütt: 265
	// Mensa Techfak Erlangen: 256
	// example request: GET /canteens/268/days/2023-11-21/meals
	@GET("canteens/268/days/{date}/meals")
	Call<List<Meal>> getMeals(@Path("date") String date);
}
