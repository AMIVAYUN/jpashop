package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}
	@Bean
	Hibernate5Module hibernate5Module(){
		return new Hibernate5Module();
	}
	/**
	 * 아래 방법은 여러 정보가 조회 가능하지만 사실상 의미가 없다 왜냐하면 쿼리가 무지하게 나가서 성능이 저하됨.
	 * @Bean
	 * Hibernate5Module hibernate5Module(){
	 *     Hibernate5Module hibernate5Module = new Hibernate5Module();
	 *     hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
	 *     return hibernate5Module
	 * }
	 */
}
