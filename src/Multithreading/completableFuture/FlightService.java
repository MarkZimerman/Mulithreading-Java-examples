package Multithreading.completableFuture;

import Multithreading.executors.LongTask;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlightService {
    public CompletableFuture<Quote> getQuote(String site){
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting a quote from " + site + "...");
            LongTask.simulate();
//            return new Quote(site, 100 + (int)(Math.random()*10));
            return new Quote(site, 100 + new Random().nextInt(10));
        });
    }

    public Stream<CompletableFuture<Quote>> getQuotes(){
        var sites = List.of("site1", "site2", "site3");
        return sites.stream().map(this::getQuote);
    }
}
