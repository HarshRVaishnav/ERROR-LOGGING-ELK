package com.example;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ErrorLoggingApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ErrorLoggingApplication.class, args);
	}
}
























































/*@Autowired
	private IOrderDetailsRepo repository;

	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private int batchSize;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {

		int totalObjects = 10;

		long start = System.currentTimeMillis();
		List<OrderDetails> books = IntStream.range(0, totalObjects)
				.mapToObj(val -> OrderDetails.builder()
						.productCode( val)
						.quantityOrdered(val)
						.build())
				.collect(Collectors.toList());
		System.out.println("Finished creating "+totalObjects+" objects in memory in:" + (System.currentTimeMillis() - start)/1000);
		start = System.currentTimeMillis();
		System.out.println("Inserting ..........");
		for (int i = 0; i < totalObjects; i += batchSize) {
			if( i+ batchSize > totalObjects){
				List<OrderDetails> books1 = books.subList(i, totalObjects - 1);
				repository.saveAll(books1);
				break;
			}
			List<OrderDetails> books1 = books.subList(i, i + batchSize);
			repository.saveAll(books1);
		}
		System.out.println("Finished inserting "+totalObjects+" objects in :" + (System.currentTimeMillis() - start));
	}*/