package com.ebay.epd.sudoku;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
public class NegativeTests {

    @Autowired
    private TestRestTemplate restTemplate;

    MultiValueMap<String, String> headers = new HttpHeaders() {{
        add("Content-Type", "application/json");
        add("Accept", "application/json");
    }};


    @Test
    public void checkThereIs400errorIfRequestBodyIsNull() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("/board/validate", HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCodeValue(), not(500)); // it fails as there is a bug when processing an empty request body
    }

    @Test
    public void postMethodShouldntBeAllowed() {

        String fieldDescription = "{}";

        HttpEntity<String> entity = new HttpEntity<>(fieldDescription, headers);
        ResponseEntity<String> response = restTemplate.exchange("/board/validate", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCodeValue(), is(405));
    }

    public Integer[][] createTestBoard() {
        Integer[][] fields = new Integer[9][];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Integer[9];
        }
        return fields;
    }
}
