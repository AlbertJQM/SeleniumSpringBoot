package com.pruebasel.demoselenium;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Hilo {
    private static ChromeOptions options = new ChromeOptions();
    private String rutaCSV;
    private ArrayList<Usuario> listaUsuarios;
    private ArrayList<WebDriver> drivers;
    private ArrayList<Thread> threads;
    public Hilo(String rutaCSV){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers\\chromedriver.exe");
        this.options.addArguments("--disable-dev-shm-usage");
        this.options.addArguments("--headless");
        this.options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        this.options.setBinary("src\\main\\resources\\drivers\\chrome-win64\\chrome.exe");
        this.rutaCSV = rutaCSV;
        this.listaUsuarios = new ArrayList<Usuario>();
        this.drivers = new ArrayList<WebDriver>();
        this.threads = new ArrayList<Thread>();
    }
    public Hilo(String rutaCSV, ArrayList<Usuario> listaUsuarios){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers\\chromedriver.exe");
        this.options.addArguments("--disable-dev-shm-usage");
        this.options.addArguments("--headless");
        this.options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        this.options.setBinary("src\\main\\resources\\drivers\\chrome-win64\\chrome.exe");
        this.rutaCSV = rutaCSV;
        this.listaUsuarios = listaUsuarios;
        this.drivers = new ArrayList<WebDriver>();
        this.threads = new ArrayList<Thread>();
    }
    public void generarUsuarios(){
        try (CSVReader reader = new CSVReader(new FileReader(this.rutaCSV))) {
            // Lee todas las líneas del archivo CSV
            List<String[]> csvData = reader.readAll();
            // Itera sobre las filas y columnas e imprime el contenido
            for (String[] row : csvData) {
                Usuario usuario = new Usuario(row[0], row[1]);
                this.listaUsuarios.add(usuario);
            }
        } catch (IOException | CsvException ex) {
            Logger.getLogger(Test.class.getName()).info("Ocurrió un error: " + ex);
        }
    }
    public void generarDrivers(){
        for(int i = 0; i < this.listaUsuarios.size(); i++){
            this.drivers.add(new ChromeDriver(options));
        }
    }
    public void generarThreads(){
        for(int i = 0; i < this.listaUsuarios.size(); i++){
            this.threads.add(new Thread(new Test(this.listaUsuarios.get(i),"891", this.drivers.get(i))));
        }
    }
    public void iniciarThreads(){
        for(Thread thread : this.threads){
            thread.start();
        }
    }
    public void esperarThreads(){
        for(Thread thread : this.threads) {
            try{
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).info("Ocurrió un error: " + ex);
            }
        }
    }
    public void cerrarDrivers() {
        for(WebDriver driver : this.drivers){
            driver.close();
            driver.quit();
        }
    }

    public void ejecutarPrueba() {
        this.generarUsuarios();
        this.generarDrivers();
        this.generarThreads();
        this.iniciarThreads();
        this.esperarThreads();
        this.cerrarDrivers();
    }
}
