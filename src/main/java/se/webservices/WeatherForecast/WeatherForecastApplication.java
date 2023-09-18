package se.webservices.WeatherForecast;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.webservices.WeatherForecast.URLS.Urls;
import se.webservices.WeatherForecast.dto.AverageDTO;
import se.webservices.WeatherForecast.models.DataSource;
import se.webservices.WeatherForecast.models.Forecast;
import se.webservices.WeatherForecast.models.data.Parameter;
import se.webservices.WeatherForecast.models.data.Root;
import se.webservices.WeatherForecast.models.data.TimeSeries;
import se.webservices.WeatherForecast.repositories.ForecastRepository;
import se.webservices.WeatherForecast.services.ForecastService;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static se.webservices.WeatherForecast.models.Colors.*;

@SpringBootApplication
class WeatherForecastApplication implements CommandLineRunner {

	@Autowired
	private  ForecastService forecastService;
	@Autowired
	private ForecastRepository forecastRepository;


	public static void main(String[] args) {
		SpringApplication.run(WeatherForecastApplication.class, args);
	}

	public void smhiAPi() throws IOException {

		var objectMapper = new ObjectMapper();
		Root predictions = objectMapper.readValue(new URL(Urls.smhiAPI()),Root.class);

		// Coordinates SMHI:
		String smhilong = predictions.getGeometry().getCoordinates().get(0).get(0).toString();
		String smhilati = predictions.getGeometry().getCoordinates().get(0).get(1).toString();

//----------------------------------------------- Ta fram ett dygn -----------------------------------------------------

		Date today = new Date(); // skriver dagens datum och tid från att den körs
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.HOUR_OF_DAY,24); // 13:00 till 13:00 nästa dag (24 timmar framåt)
		Date tomorrow = calendar.getTime();  //Morgondagens datum efter klockan passerat 00:00

		System.out.println(today);
		System.out.println(tomorrow);

//----------------------- Presentera tider med data från smhi (ValidTime) - (TimeSeries timeSeries) --------------------

		int nummer = 0;
		for (TimeSeries timeSeries : predictions.getTimeSeries()) {
			Date validTime = timeSeries.getValidTime(); // date validtime = time.getValidTime
			System.out.printf(WHITE_BOLD_BRIGHT +"%d) Valid Time: %s%n", nummer++, validTime);
		}

//-------------------- Få ut ApprovedTime, ReferenceTime, getGeometry( Location: coordinater )--------------------------
		System.out.println("***********************************************");
		System.out.printf(" ApprovedTime: %s %n ReferenceTime: %s %n Geometry: %s, %s %n", predictions.getApprovedTime(), predictions.getReferenceTime(),predictions.getGeometry().getType(),predictions.getGeometry().getCoordinates().get(0).get(1));
		System.out.println("***********************************************");

		String paramName;
		List paramValue = null;
		int paramLvl;
		String paramLvlType;
		String paramUnit;


		for (TimeSeries timeSeries : predictions.getTimeSeries()) {

			Date validTime = timeSeries.getValidTime();

			if (validTime != null) {

				// omvandla till local datum i variabel som ska sparas till databasen
				LocalDate validLocalDate = validTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				// omvandla till local tid i variabel som ska sparas till databasen
				LocalTime validLocalTime = validTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
				int hourOfDay = validLocalTime.getHour();

				calendar.setTime(validTime);

				if (validTime.after(today)&& validTime.before(tomorrow)) {
					// Extract and process other data from timeSeries as needed
					for (Parameter parameter : timeSeries.getParameters()) {
						//long longvar = timeSeries.getValidTime().getTime();
						List<Float> values = parameter.getValues();

						for (Float parameterValue : values)
							if (parameter.getName().equals("t")){
								paramName = parameter.getName();
								paramValue = parameter.getValues();
								paramLvl = parameter.getLevel();
								paramLvlType = parameter.getLevelType();
								paramUnit = parameter.getUnit();

								// Extract more parameter details as needed
								System.out.println("------------------------------------------------\n");
								System.out.println("\t\tTemperature info:");
								System.out.printf("\n Name: %s %n Level type: %s %n Level: %d %n Unit: %s %n Value: %s\n",
										paramName,
										paramLvlType,
										paramLvl,
										paramUnit,
										paramValue);

								// Spara värden till Databasen - SQLight
								Forecast forecast = new Forecast();
								forecast.setHour(hourOfDay);
								forecast.setDate(validLocalDate);
								forecast.setTemperature(parameterValue);
								forecast.setDataSource(DataSource.Smhi);
								forecast.setLongitude(Float.parseFloat(smhilong));
								forecast.setLatitude(Float.parseFloat(smhilati));
								forecast.setCreated(LocalDate.now());
								forecastRepository.save(forecast);

								System.out.println("\n\t\tValidTime: -" + validTime + "-");
								System.out.println("\nCurrent date: " + validLocalDate);
								System.out.println("Hour of day: " + hourOfDay +":00");
								System.out.println("Weather temp: " + parameterValue + " C\n");
								;
							} else if (parameter.getName().equals("pcat")) {
								System.out.println("\t\tPrecipitation(Nederbörd) info:");
								System.out.printf("\n Name: %s %n Level type: %s %n Level: %d %n Unit: %s %n Value: %s\n____________________\n",
										parameter.getName(),
										parameter.getLevelType(),
										parameter.getLevel(),
										parameter.getUnit(),
										parameter.getValues());

								if (parameterValue == 0.0) {
									System.out.println("pcat value: " + parameterValue);
									System.out.println("No precipitation");

								} else if (parameterValue == 1.0) {
									System.out.println("pcat value: " + parameterValue);
									System.out.println("Snow");

								} else if (parameterValue == 2.0) {
									System.out.println("pcat value: " + parameterValue);
									System.out.println("Snow and rain");

								} else if (parameterValue == 3.0) {
									System.out.println("pcat value: " + parameterValue);
									System.out.println("Rain");
								}

								System.out.println("\n------------------------------------------------\n");
							}
					}
				}
			}
		}
	}



	@Override
	public void run(String... args) throws Exception {

		forecastService.getForecasts();
		var forecast = new Forecast();
		forecast.setId(UUID.randomUUID());
		forecast.setTemperature(12);
		forecast.setDate(LocalDate.now());
		forecast.setDataSource(DataSource.Smhi);

		var scan = new Scanner(System.in);
		while(true){
			System.out.println(WHITE_BOLD_BRIGHT+"\n************************");
			System.out.println(WHITE_BOLD_BRIGHT+"1. List all predictions");
			System.out.println(WHITE_BOLD_BRIGHT+"2. Create predictions");
			System.out.println(WHITE_BOLD_BRIGHT+"3. Update predictions");
			System.out.println(WHITE_BOLD_BRIGHT+"4. Delete predictions");
			System.out.println(WHITE_BOLD_BRIGHT+"5. Save SMHI-Data");
			System.out.println(WHITE_BOLD_BRIGHT+"6. Create dummy");
			System.out.println(WHITE_BOLD_BRIGHT+"7. Delete dummy");
			System.out.println(WHITE_BOLD_BRIGHT+"8. Average Data");
			System.out.println(WHITE_BOLD_BRIGHT+ "9. Exit application");
			System.out.println(WHITE_BOLD_BRIGHT+"************************");
			System.out.print(WHITE_BOLD_BRIGHT+"\nAction:");
			int sel = getUserInputInt();
			switch (sel){
				case 1:
					listPredictions();
					break;
				case 2:
					addPrediction(scan);
					break;
				case 3:
					updatePrediction(scan);
					break;
				case 4:
					//deletePrediction(scan);
					break;
				case 5:
					smhiAPi();
					break;
				case 6:
					dummyAdd(scan);
					break;
				case 7:
					deleteAllDummys();
					break;
				case 8:
					calculateAverage();
				case 9:
					break;
			}
		}
	}

	public static int getUserInputInt(){
		Scanner sc = new Scanner(System.in);
		String userInput;
		int userInputInt = 0;
		boolean runInput = true;

		do {
			try{
				userInput = sc.nextLine();
				userInputInt = Integer.parseInt(userInput);
				runInput = false;
			}catch (Exception e){
				System.out.println(RED_BOLD_BRIGHT+"Wrong input format. Try again!"+RESET);
			}
		}while(runInput);
		return userInputInt;
	}



	private void updatePrediction(Scanner scan) throws IOException {

		listPredictions();
		System.out.printf(CYAN_BOLD_BRIGHT+"Ange vilken du vill uppdatera:"+RESET);
		int num = scan.nextInt() ;
		var forecast = forecastService.getByIndex(num-1);
		System.out.printf("%d %d CURRENT: %f %n",
				forecast.getDate(),
				forecast.getHour(),
				forecast.getTemperature()
		);
		System.out.printf("Ange ny temp:");
		var temp = scan.nextInt() ;
		forecast.setTemperature(temp);
		forecastService.update(forecast);
	}

	public static String longitude = "18.02151508449004";
	public static String latitude = "59.30996552541549";

	private void addPrediction(Scanner scan) throws IOException {
		//Input på dag, hour, temp
		//Anropa servicen - Save
		System.out.println(CYAN_BOLD_BRIGHT+"*** CREATE PREDICTION ***"+ RESET);
		System.out.printf(WHITE_BOLD_BRIGHT+"What date is it? ** YYYYMMDD **");
		int dag = scan.nextInt() ;
		System.out.print("Hour:");
		int hour =  scan.nextInt() ;
		System.out.print("Temperature:");
		var temp =  scan.nextInt() ;

		var forecast = new Forecast();
		forecast.setId(UUID.randomUUID());
		//forecast.setDate(dag);
		forecast.setDate(LocalDate.now());
		forecast.setHour(hour);
		forecast.setTemperature(temp);

		forecastService.add(forecast);
	}
	private void dummyAdd(Scanner scan) throws ParseException, IOException {
		System.out.println(CYAN_BOLD_BRIGHT+" CREATE PREDICATION " + RESET);
		//------------- Ange datum --------------
		System.out.println("Add date in the following format (yyyy-MM-dd): ");
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
		String date = scan.next();
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formattedDate = LocalDate.parse(date, sdf);

		var dat = LocalDate.from(formattedDate.atStartOfDay());

		for (int i = 0; i < 24; i++) {

			var forecast = new Forecast();
			forecast.setId(UUID.randomUUID());
			forecast.setDate(dat); //forecast.setDate(dag);
			forecast.setHour(i);
			forecast.setTemperature(i);
			forecast.setLongitude(Float.parseFloat(longitude));
			forecast.setLatitude(Float.parseFloat(latitude));
			forecast.setDataSource(DataSource.Console);
			forecast.setCreated(LocalDate.now());
			forecastService.add(forecast);

		}
	}

	private void deleteAllDummys() {
		for (var forecast : forecastService.getForecasts()) {
			forecastService.deleted(forecast);
		}
	}

	private void calculateAverage(){
		var dag = LocalDate.now();
		List<AverageDTO> dtos = forecastService.calculateAverage(dag);

		for (int hour = 0; hour < dtos.size(); hour++){
			System.out.println(dtos.get(hour).getDate() + "- KL: " + hour + ":00. Average temp: " + dtos.get(hour).getAverage() + "C\n");
		}

	}

	private void listPredictions() {
		int num = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<Forecast> forecasts = forecastService.getForecasts();

		// Sortera listan med predictions baserat på datum och tid
		Collections.sort(forecasts, new Comparator<Forecast>() {
			@Override
			public int compare(Forecast f1, Forecast f2) {
				// Jämför predictions baserat på datum och tid
				int dateComparison = f1.getDate().compareTo(f2.getDate());
				if (dateComparison == 0) {
					// Om datumet är samma, jämför tid
					return Integer.compare(f1.getHour(), f2.getHour());
				}
				return dateComparison;
			}
		});

		System.out.println(CYAN_BOLD_BRIGHT+"\t Listing Predictions " + RESET);

		for (var forecast : forecasts) {

			//String adminTime = sdf.format(forecast.getDate());
//			System.out.printf("\t%d) %n\tId: %s %n\tDag: %s Tid: %d:00 %n\tTemp: %.1f %n\tData Source: %s %n %n",
			System.out.printf(YELLOW_BOLD_BRIGHT + "\t%d) %n\tId: %s %n\tcreated: %s %n\tlatitude: %s %n\tlongitude: %s %n\tDag: %s Tid: %d:00 %n\tTemp: %.1f %n\tData Source: %s %n %n",
					num++,
					forecast.getId(),
					forecast.getCreated(),
					forecast.getLatitude(),
					forecast.getLongitude(),
					forecast.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
					forecast.getHour(),
					forecast.getTemperature(),
					forecast.getDataSource()
			);
		}
	}
}