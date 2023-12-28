package com.pruebasel.demoselenium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class DemoSeleniumApplication {

    public static void main(String[] args) {
        Hilo hilo = new Hilo("src\\main\\resources\\usuarios.csv");
        hilo.ejecutarPrueba();
        SpringApplication.run(DemoSeleniumApplication.class, args);
    }
}
