package org.example.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@JsonTest
public class Json1Test {
    @Autowired
    private PersonRepo repo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void personSerialize() {
        System.out.println("personSerialize");
        System.out.println("==================");

        Assertions.assertTrue(repo!=null);

        Person person1 = repo.find(24);
        Assertions.assertTrue(person1!=null);
        Assertions.assertTrue(person1.getName()!=null);

        try {
            String json = objectMapper.writeValueAsString(person1);
            System.out.println("json:\n"+json);
        } catch (JsonProcessingException e) {
            throw new Error(e);
        }
    }

    @Test
    public void personDeSerialize() {
        System.out.println("personDeSerialize");
        System.out.println("=================");

        String sourceJson = "{\"id\":24,\"name\":\"Charlie Adams\"}";
        try {
            Person person1 = objectMapper.readValue(sourceJson, Person.class);
            Assertions.assertTrue(person1!=null);
            Assertions.assertTrue(person1.getId()==24);
        } catch (JsonProcessingException e) {
            throw new Error(e);
        }
    }

    @TestConfiguration
    public static class Config {
        @Bean
        public static PersonRepo repo() {
            return new PersonRepoImpl();
        }
    }
}
