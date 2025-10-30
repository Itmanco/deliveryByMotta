package jp.kenschool.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import jp.kenschool.delivery.utils.TemplateUtilities;

@SpringBootApplication
public class DeliveryByMottaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryByMottaApplication.class, args);
	}
	
	@Bean(name = "utils") // Name the bean "utils" for easy access in templates
    public TemplateUtilities templateUtilities() {
        return new TemplateUtilities();
    }

}
