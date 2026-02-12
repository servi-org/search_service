package live.servi.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Servicio de busqieda
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SearchServi {

    public static void main(String[] args) {
        SpringApplication.run(SearchServi.class, args);
    }
}
