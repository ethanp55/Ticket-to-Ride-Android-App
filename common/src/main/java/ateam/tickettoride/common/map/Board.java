package ateam.tickettoride.common.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Board implements Serializable {
    //List of cities and routes on the map/board
    private ArrayList<City> cities;
    private ArrayList<Route> routes;

    public Board() {
        setUpCities();
        setUpRoutes();
    }

    private void setUpCities() {
        cities = new ArrayList<>();

        //Read in the file of cities
        try {
            Scanner scanner = new Scanner(new File("./Cities.txt"));

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                int x = Integer.parseInt(scanner.nextLine());
                int y = Integer.parseInt(scanner.nextLine());

                City city = new City(name, x, y);
                cities.add(city);
            }
        } catch (FileNotFoundException e) {
            System.out.println("CITIES FILE NOT FOUND");
        }
    }

    private void setUpRoutes() {
        routes = new ArrayList<>();

        //Read in the file of routes
        try {
            Scanner scanner = new Scanner(new File("./Routes.txt"));

            while (scanner.hasNextLine()) {
                String city1 = scanner.nextLine();
                String city2 = scanner.nextLine();
                int length = Integer.parseInt(scanner.nextLine());
                String colorName = scanner.nextLine();
                int color = findColor(colorName);
                int xOffset = Integer.parseInt(scanner.nextLine());
                int yOffset = Integer.parseInt(scanner.nextLine());

                Route route = new Route(city1, city2, length, color, xOffset, yOffset);
                routes.add(route);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ROUTES FILE NOT FOUND");
        }
    }

    private int findColor(String colorName) {
        if (colorName.toLowerCase().equals("gray")) {
            return 0xff808080;
        } else if (colorName.toLowerCase().equals("white")) {
            return 0xffffffff;
        } else if (colorName.toLowerCase().equals("blue")) {
            return 0xff0000ff;
        } else if (colorName.toLowerCase().equals("black")) {
            return 0xff000000;
        } else if (colorName.toLowerCase().equals("red")) {
            return 0xffff0000;
        } else if (colorName.toLowerCase().equals("yellow")) {
            return 0xffffff00;
        } else if (colorName.toLowerCase().equals("green")) {
            return 0xff008000;
        } else if (colorName.toLowerCase().equals("purple")) {
            return 0xff800080;
        } else {
            // Orange
            return 0xffffa500;
        }
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
