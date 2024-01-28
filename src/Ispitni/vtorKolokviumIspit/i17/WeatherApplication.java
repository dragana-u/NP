package Ispitni.vtorKolokviumIspit.i17;

import java.util.*;

interface Updatable{
    void update(float temp, float humidity, float pressure);
}

interface Subject{
    void register(Updatable o);
    void remove (Updatable o);
    void notifyUpdatable();
}

interface Displayable{
    void display();
}

class WeatherDispatcher implements Subject{
    float temperature;
    float humidity;
    float pressure;
    Set<Updatable> updatableSet;

    public WeatherDispatcher() {
        updatableSet = new HashSet<>();
    }


    @Override
    public void register(Updatable o) {
        updatableSet.add(o);
    }
    @Override
    public void remove(Updatable o) {
        updatableSet.remove(o);
    }

    @Override
    public void notifyUpdatable() {
        for(Updatable updatable : updatableSet){
            updatable.update(temperature,humidity,pressure);
        }
    }
    public  void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
        System.out.println();
    }
    public void measurementsChanged(){
        notifyUpdatable();
    }


}

class CurrentConditionsDisplay implements Updatable,Displayable{

    float temperature;
    float humidity;
    Subject weatherStation;

    public CurrentConditionsDisplay(Subject weatherStation) {
        this.weatherStation = weatherStation;
        weatherStation.register(this);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity=humidity;
        display();
    }

    @Override
    public void display() {
        System.out.printf("Temperature: %.1fF\nHumidity: %.1f%%\n",temperature,humidity);
    }
}
class ForecastDisplay implements Updatable,Displayable{
    float currentPressure = 0.0f;
    float previousPressure;
    WeatherDispatcher wd;

    public ForecastDisplay(WeatherDispatcher wd) {
       this.wd = wd;
       wd.register(this);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        previousPressure = currentPressure;
        currentPressure = pressure;
        display();
    }

    @Override
    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure > previousPressure) {
            System.out.println("Improving");
        } else if (currentPressure == previousPressure) {
            System.out.println("Same");
        } else if (currentPressure < previousPressure) {
            System.out.println("Cooler");
        }
    }
}
public class WeatherApplication {
    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}