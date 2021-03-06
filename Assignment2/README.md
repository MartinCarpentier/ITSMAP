# Weather or not you like it

## Highlevel Requirements

- [x] An **Activity**: the Activity will use the **Service** to get up-to-date data and show it to the user. *It should also allow the user to refresh and check for new data manually*.
- [x] A **Database**: a small database and functionality to store weather information entries (hint: create a DatabaseHelper class). The database will be used by the Service.
- [x] A **Service**: this should be a **Background service** that *manages the database AND periodically retrieves new weather information* from the web service. As such the Service should always have the newest data and the Activity can get it when needed.

## Specific Requirements

- [X] To **communicate between the service/activity and service/database** you must *use a WeatherInfo “model” class that holds AT LEAST the following information*:  
  - ID (corresponding to database entry)  
  - Weather description (text)  
  - Temperature (in celcius)  
  - Timestamp (when the data is from)    
- [x] The activity should **show the current weather** in whatever way you want to present it (above diagram could be a starting point), but it should visualize all the information from the WeatherInfo class.
- [x] The activity *should show the weather data for every 30 minutes from up to 24 hours in the past*. You should use a ListView with an Adaptor for this. You can use an existing adaptor or create a custom adaptor that takes a list of WeatherObjects with custom XML layout.
- [x] The background service **should run all the time** (from the first time the app is started)
- [x] The Activity bind to the service when active and retrieve the most up-to-date weather data through two methods, like these:  
  - WeatherInfo getCurrentWeather()  
  - List<WeatherInfo> getPastWeather()  
- [ ] The Service should send out a **local broadcast** when there is new weather data available. The Activity should register for this, and update the UI if needed.
- [x] The **background service must call the OpenWeatherMap server every 30 minutes** and save the results in the database.
- [X] You must have a **custom icon** for your app
- [x] You **app name** should be “Weather Aarhus group XX” where XX is your group number.
- [X] You *should include LogCat outputs / logging to validate that the service is running*.
- [X] **All resources used should be externalized**.
- [x] Style the app with **your own colors**

## Bonus Requirements

- [X] Make the service run **even if the user reboots** the device.
- [x] Additional weather information (check out the API) and visualization
- [x] Use **different icons to represent the weather situation** (rain, snow, sun, cloudy, etc.)
- [x] Different *layouts, e.g. for large and small devices*
- [ ] Allow the user to select *other cities*.
