package ohm.softa.a07.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ohm.softa.a07.MealsFilterUtility;
import ohm.softa.a07.api.OpenMensaAPI;
import ohm.softa.a07.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController implements Initializable {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private final OpenMensaAPI openMensaAPI;
	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnClose;

	@FXML
	private CheckBox chkVegetarian;

	@FXML
	private ListView<Meal> mealsList;

	private ObservableList<Meal> meals;

	public MainController(){
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.baseUrl("https://openmensa.org/api/v2/")
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);
	}

	@FXML
	private void onRefreshItem(){
		loadMensaData();
	}

	@FXML
	private void onVegetarianChkbox(){
		loadMensaData();
	}

	@FXML
	private void onCloseItem(){
		Platform.exit();
		System.exit(0);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		loadMensaData();
		meals = mealsList.getItems();
	}

	private static Date getUpcomingMondayDate() {
		Calendar cal = Calendar.getInstance();
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return cal.getTime();
	}

	private void loadMensaData() {
		openMensaAPI.getMeals(dateFormat.format(new Date())).enqueue(new Callback<List<Meal>>() {
			@Override
			public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
				if(!response.isSuccessful()) return;

				if (response.body() == null){
					return;
				}

				Platform.runLater(() -> {
					meals.clear();
					meals.addAll(chkVegetarian.isSelected()
						? MealsFilterUtility.filterForVegetarian(response.body())
						: response.body());
				});
			}

			@Override
			public void onFailure(Call<List<Meal>> call, Throwable t) {
				Platform.runLater(() -> {
					meals.clear();
					new Alert(Alert.AlertType.ERROR, "Failed to get mealsProperty", ButtonType.OK).showAndWait();
				});
			}
		});
	}

	/*
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// create a new (observable) list and tie it to the view
				ObservableList<String> list = FXCollections.observableArrayList("Hans", "Dampf");
				mealsList.setItems(list);
			}
		});
	}

	*/
}
