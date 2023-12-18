package com.nighthawk.spring_portfolio.mvc;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.nighthawk.spring_portfolio.mvc.human.Human;
import com.nighthawk.spring_portfolio.mvc.human.HumanApiController;
import com.nighthawk.spring_portfolio.mvc.human.HumanJpaRepository;
import com.nighthawk.spring_portfolio.mvc.jokes.Jokes;
import com.nighthawk.spring_portfolio.mvc.jokes.JokesJpaRepository;
import com.nighthawk.spring_portfolio.mvc.note.Note;
import com.nighthawk.spring_portfolio.mvc.note.NoteJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.person.PersonRole;
import com.nighthawk.spring_portfolio.mvc.person.PersonRoleJpaRepository;

@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired JokesJpaRepository jokesRepo;
    @Autowired NoteJpaRepository noteRepo;
    @Autowired PersonDetailsService personService;
    @Autowired  // Inject RoleJpaRepository
    private PersonRoleJpaRepository personRoleJpaRepository;
    @Autowired HumanJpaRepository humanRepo;
    Set<String> usedClassCodes = HumanApiController.usedClassCodes;
    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {

            // Joke database is populated with starting jokes
            String[] jokesArray = Jokes.init();
            for (String joke : jokesArray) {
                List<Jokes> jokeFound = jokesRepo.findByJokeIgnoreCase(joke);  // JPA lookup
                if (jokeFound.size() == 0)
                    jokesRepo.save(new Jokes(null, joke, Jokes.randomLikes(), Jokes.randomLikes()-1)); //JPA save
            }

            // Person database is populated with test data
            Person[] personArray = Person.init();
            for (Person person : personArray) {
                //findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase
                List<Person> personFound = personService.list(person.getName(), person.getEmail());  // lookup
                if (personFound.size() == 0) {
                    personService.save(person);  // save
                    PersonRole newRole = new PersonRole("ROLE_ADMIN");
                    if (person.getName() == "admin") {
                        personRoleJpaRepository.save(newRole);
                        person.getRoles().add(newRole);
                        System.out.println(person.getRoles());
                    }
                    // Each "test person" starts with a "test note"
                    String text = "Test " + person.getEmail();
                    Note n = new Note(text, person);  // constructor uses new person as Many-to-One association
                    noteRepo.save(n);  // JPA Save  
                    
                }
            }

            Human[] humanArray = Human.init();
            
            for (Human human : humanArray){
                List<Human> humanFound = humanRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(human.getName(), human.getEmail());
                if (humanFound.size() == 0){
                    if(human.getRole() == "Teacher"){
                        String classCode = "";  
                        if (human.getClassCode() == null || classCode.length() == 0 ){
                            int CODE_LENGTH = 6; 
                            SecureRandom random = new SecureRandom();
                            BigInteger randomBigInt;
                            do {
                                randomBigInt = new BigInteger(50, random);
                                classCode = randomBigInt.toString(32).toUpperCase().substring(0, CODE_LENGTH);
                            } while (usedClassCodes.contains(classCode));
                            usedClassCodes.add(classCode);
                        }
                        human.setClassCode(classCode);
                    }
                    humanRepo.save(human);
                    
                }
            }

            // Cars[] carsArray = Cars.init();
            // for (Cars c : carsArray) {
            //     List<Cars> carsFound = .findByJokeIgnoreCase(joke);  // JPA lookup
            //     if (jokeFound.size() == 0)
            //         jokesRepo.save(new Jokes(null, joke, 0, 0)); //JPA save
            // }

        };
    }
}

