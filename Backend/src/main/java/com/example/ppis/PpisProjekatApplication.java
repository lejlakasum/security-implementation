package com.example.ppis;

import com.example.ppis.dto.UserRegisterDTO;
import com.example.ppis.model.*;
import com.example.ppis.repository.*;
import com.example.ppis.service.UserService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class PpisProjekatApplication {
	private static final Logger log =
			LoggerFactory.getLogger(PpisProjekatApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PpisProjekatApplication.class, args);
	}


	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(getHttpConnector());
		return tomcat;
	}

	private Connector getHttpConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(8083);
		connector.setSecure(false);
		connector.setRedirectPort(8443);
		return connector;
	}

	@Bean
	public CommandLineRunner addData(UserService userRepository,
									 RoleRepository roleRepository,
									 SkillTypeRepository skillTypeRepository,
									 SkillRepository skillRepository,
									 EmployeeRepository employeeRepository,
									 EmployeeSkillRepository employeeSkillRepository,
									 CertificateRepository certificateRepository) {
		return(args) -> {
			Role role1 = roleRepository.save(new Role("admin"));
			Role role2 = roleRepository.save(new Role("hr"));


			// korisnici

			UserRegisterDTO k1 = userRepository.postUser(new User("admin", "!aL2sK7$", "admin@mail.com", role1, true));
			UserRegisterDTO k2 = userRepository.postUser(new User("hradmin", "!aL2sK7$!", "hr@mail.com", role2, true));


			//tipovi skilova
			List<SkillType> skillTypes = new ArrayList<>();
			SkillType skillType1 = skillTypeRepository.save(new SkillType("Razvoj softvera"));
			SkillType skillType2 = skillTypeRepository.save(new SkillType("Soft vještine"));
			SkillType skillType3 = skillTypeRepository.save(new SkillType("Mreže"));


			//vještine
			Skill skill1 = skillRepository.save(new Skill("Java programiranje", skillType1));
			Skill skill2 = skillRepository.save(new Skill("React programiranje", skillType1));
			Skill skill3 = skillRepository.save(new Skill("Prezentacija", skillType2));
			Skill skill4 = skillRepository.save(new Skill("C# programiranje", skillType1));
			Skill skill5 = skillRepository.save(new Skill("C++ programiranje", skillType1));
			Skill skill6 = skillRepository.save(new Skill("Relacione baze podataka", skillType1));
			Skill skill7 = skillRepository.save(new Skill("Sigurnost", skillType3));
			Skill skill8 = skillRepository.save(new Skill("Komunikacione vještine", skillType2));
			Skill skill9 = skillRepository.save(new Skill("Liderstvo", skillType2));



			//uposlenici
			Employee employee1 = employeeRepository.save(new Employee("Ivo", "Ivic", Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)), new Date()));
			Employee employee2 = employeeRepository.save(new Employee("Maja", "Majic", Date.from(LocalDateTime.now().minusDays(2).toInstant(ZoneOffset.UTC)), new Date()));
			Employee employee3 = employeeRepository.save(new Employee("Stevo", "Stevic", Date.from(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC)), new Date()));
			Employee employee4 = employeeRepository.save(new Employee("Ahmo", "Ahmic", Date.from(LocalDateTime.now().minusDays(4).toInstant(ZoneOffset.UTC)), new Date()));



			//skillovi kod uposlenika

			EmployeeSkill employeeSkill1 = employeeSkillRepository.save(new EmployeeSkill(employee1, skill1, 5, new Date()));
			EmployeeSkill employeeSkill2 = employeeSkillRepository.save(new EmployeeSkill(employee1, skill2, 5, new Date()));
			EmployeeSkill employeeSkill3 = employeeSkillRepository.save(new EmployeeSkill(employee1, skill4, 3, new Date()));
			EmployeeSkill employeeSkill4 = employeeSkillRepository.save(new EmployeeSkill(employee1, skill5, 4, new Date()));

			EmployeeSkill employeeSkill5 = employeeSkillRepository.save(new EmployeeSkill(employee2, skill2, 5, new Date()));
			EmployeeSkill employeeSkill6 = employeeSkillRepository.save(new EmployeeSkill(employee2, skill3, 5, new Date()));
			EmployeeSkill employeeSkill7 = employeeSkillRepository.save(new EmployeeSkill(employee2, skill9, 5, new Date()));
			EmployeeSkill employeeSkill8 = employeeSkillRepository.save(new EmployeeSkill(employee2, skill8, 4, new Date()));

			EmployeeSkill employeeSkill11 = employeeSkillRepository.save(new EmployeeSkill(employee4, skill5, 5, new Date()));
			EmployeeSkill employeeSkill12 = employeeSkillRepository.save(new EmployeeSkill(employee4, skill6, 5, new Date()));
			EmployeeSkill employeeSkill9 = employeeSkillRepository.save(new EmployeeSkill(employee4, skill7, 3, new Date()));
			EmployeeSkill employeeSkill10 = employeeSkillRepository.save(new EmployeeSkill(employee4, skill8, 4, new Date()));


			//Svi certifikati

			Certificate certificate1 = certificateRepository.save(new Certificate(employee1, skill7, "CISCO", new Date(), new Date()));
			Certificate certificate2 = certificateRepository.save(new Certificate(employee1, skill1, "Oracle Certified Associate Java Programmer", new Date(), new Date()));

			Certificate certificate3 = certificateRepository.save(new Certificate(employee2, skill9, "Scrum Master", new Date(), new Date()));
		};
	}


}
