package se.webservices.WeatherForecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.webservices.WeatherForecast.models.ForeCast;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

/**
 *
 */
@SpringBootApplication
public class WeatherForecastApplication implements CommandLineRunner {

	@Autowired
	private ForecastService forecastService;

	public static void main(String[] args) {
		SpringApplication.run(WeatherForecastApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		forecastService.getAllOnDate(LocalDate.now());

		var scan = new Scanner(System.in);
		while(true){
			System.out.println("1. List all");
			System.out.println("2. Create");
			System.out.println("3. Update");
			System.out.println("9. Exit");
			System.out.print("Action:");
			int sel = scan.nextInt();
			if(sel == 1){
				listPredictions();
			}
			else if(sel == 3){
				updatePrediction(scan);
			}
			else if(sel == 2){


				addPrediction(scan);
			}
			else if(sel == 9){
				break;
			}

		}
	}

	private void updatePrediction(Scanner scan) throws IOException {
//		var a = new Forecast();
//		var b = a;
//		a.setTemperature(12);
//		float f  = b.getTemperature();
//
//		int y = 12;
//		int e = y;
//		e = 19;
//		System.out.println(y);
//
		listPredictions();
		System.out.printf("Ange vilken du vill uppdatera:");
		int num = scan.nextInt() ;
		var forecast = forecastService.getByIndex(num-1);
		System.out.printf("%d %d CURRENT: %f %n",
				forecast.getPredictionDatum(),
				forecast.getPredictionHour(),
				forecast.getPredictionTemperature()
		);
		System.out.printf("Ange ny temp:");
		var temp = scan.nextInt() ;
		forecast.setPredictionTemperature(temp);
		forecastService.update(forecast);
	}

	private void addPrediction(Scanner scan) throws IOException {
		//Input på dag, hour, temp
		//Anropa servicen - Save
		System.out.println("*** CREATE PREDICTION ***");
		System.out.printf("Ange vilken dag:");
		int dag = scan.nextInt() ;
		System.out.print("Hour:");
		int hour =  scan.nextInt() ;
		System.out.print("Temperature:");
		var temp =  scan.nextInt() ;

		var forecast = new ForeCast();
		forecast.setId(UUID.randomUUID());
		//forecast.setDate(dag);
		forecast.setPredictionDatum(LocalDate.now());
		forecast.setPredictionHour(hour);
		forecast.setPredictionTemperature(temp);

		forecastService.add(forecast);
	}
	private void listPredictions() {
		int num = 1;
//		for(var forecast : forecastService.getForecasts()){
//			System.out.printf("%d %d %d %f %n",
//					num,
//					forecast.getDate(),
//					forecast.getHour(),
//					forecast.getTemperature()
//					);
//			num++;
//		}
	}
}
