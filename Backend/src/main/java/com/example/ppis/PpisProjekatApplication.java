package com.example.ppis;

import com.example.ppis.model.*;
import com.example.ppis.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
	public CommandLineRunner addData(UserRepository userRepository,
									 RoleRepository roleRepository,
									 SkillTypeRepository skillTypeRepository,
									 SkillRepository skillRepository,
									 EmployeeRepository employeeRepository,
									 EmployeeSkillRepository employeeSkillRepository,
									 CertificateRepository certificateRepository) {
		return(args) -> {
			Role role1 = roleRepository.save(new Role("administrator"));
			Role role2 = roleRepository.save(new Role("hr_manager"));
			Role role3 = roleRepository.save(new Role("suplier_manager"));
			log.info("Sve uloge \n");
			for (Role role : roleRepository.findAll()) {
				log.info(role.getName());
			}
			log.info(" ");

			// korisnici

			User k1 = userRepository.save(new User("admin", "$2y$12$d.WC//FFyNCsaGzHJhalAuH6EMbmKaPDHUWxGhiQvoghruwrUUjCm", "ante.antic@gmail.com", role1));


			log.info("Svi korisnici \n");
			for (User user : userRepository.findAll()) {
				log.info(user.getUsername());
			}
			log.info(" ");

			//tipovi skilova
			List<SkillType> skillTypes = new ArrayList<>();
			SkillType skillType1 = skillTypeRepository.save(new SkillType("Razvoj softvera"));
			SkillType skillType2 = skillTypeRepository.save(new SkillType("Soft vještine"));
			SkillType skillType3 = skillTypeRepository.save(new SkillType("Mreže"));

			log.info("Svi tipovi vještina \n");
			for (SkillType skillType : skillTypeRepository.findAll()) {
				log.info(skillType.getName());
			}
			log.info(" ");

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

			log.info("Sve vještine \n");
			for (Skill skill : skillRepository.findAll()) {
				log.info(skill.getName() + " Tip vještine: " + skill.getSkillType().getName());
			}
			log.info(" ");

			//uposlenici
			Employee employee1 = employeeRepository.save(new Employee("Ivo", "Ivic", new Date(), new Date()));
			Employee employee2 = employeeRepository.save(new Employee("Maja", "Majic", new Date(), new Date()));
			Employee employee3 = employeeRepository.save(new Employee("Stevo", "Stevic", new Date(), new Date()));
			Employee employee4 = employeeRepository.save(new Employee("Ahmo", "Ahmic", new Date(), new Date()));

			log.info("Svi uposlenici \n");
			for (Employee employee : employeeRepository.findAll()) {
				log.info(employee.getFirstName() + " " + employee.getLastName());
			}
			log.info(" ");

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

			log.info("Svi skilovi uposlenika \n");
			for (EmployeeSkill employeeSkill : employeeSkillRepository.findAll()) {
				log.info(employeeSkill.getSkill().getName());
			}
			log.info(" ");

			//Svi certifikati

			Certificate certificate1 = certificateRepository.save(new Certificate(employee1, skill7, "CISCO", new Date(), new Date()));
			Certificate certificate2 = certificateRepository.save(new Certificate(employee1, skill1, "Oracle Certified Associate Java Programmer", new Date(), new Date()));

			Certificate certificate3 = certificateRepository.save(new Certificate(employee2, skill9, "Scrum Master", new Date(), new Date()));
		};
	}


}
