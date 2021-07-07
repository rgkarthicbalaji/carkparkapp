package com.asses.park;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkingSlotApp {
    public static void main(String[] args) {
        SpringApplication.run(ParkingSlotApp.class,args);
    }
}
