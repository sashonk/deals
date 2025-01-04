package ru.asocial.deals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App
{
    public static void main( String[] args )
    {
        System.out.println("<<<<<---- STARTING ---->>>>>");
        SpringApplication.run(App.class, args);
        System.out.println("<<<<<---- WORKING ---->>>>>");
    }
}
