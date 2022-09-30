package io.opentelemetry.examples.feline;

import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class FelineController {
    private final List<String> CATS = List.of("tabby", "jaguar", "leopard");

    private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();

    @Autowired private HttpServletRequest httpServletRequest;

    @GetMapping("/getAnimal")
    public String makeBattle() throws IOException, InterruptedException {
        // Random pause
        Thread.sleep((int) (20 * Math.random()));
        // Return random mammal
        return CATS.get((int)(CATS.size() * Math.random()));
    }
}
