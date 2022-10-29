# Aleksandr Lozhkovoi. Test assessment for Ebay. 

There is test-task solution. It includes checklist to test API for Sudoku Game

# Checklist

## SMOKE TESTS

 - GET /board/ is respond with 200 and contains valid json in response
   (valid json schema) 
  - GET /board/{existUserid} should success (200)
   with valid board (valid json schema)
  -  GET /board/{notExistUserid} is
   respond with 204 (no content) 
   - PUT /board/validate should success
   (200) with valid board (valid json schema)

## SANITY TESTS

- It should success on valid board (user won the game)
- After  PUT `/board/validate` execution GET `/board/id` response is equal `/board/validate` request body
- If user loses, It should return coordinates (x,y) with problem 
- Validate board generation

 **GET /board/ request**
 - There is a new (unique) board for every request execution 

**GET /board/id request**
There is a same id in response for every request execution with specific board id

 **PUT /board/validate request**
 - All fields (id, fields, state, deals link) filled with valid data
 - All fields (id, fields, state, deals link) filled with valid data + IF id doesn't exist, it creates a new board
 - Only required fields (id, fields) are filled 
 - Not all required fields 
   (id, fields) are filled 
  - No field filled
  - There is error "errors": [{"x":"y"} if two same digits in one row
  - There is error "errors": [{"x":"y"} if two same digits in one column
  - There is error "errors": [{"x":"y"} if two same digits in one box

## REGRESSION TESTS

**PUT /board/validate request**
**`id` field validation**

 - There is `id` validation  - must not be null 
 -  There is `id` validation  - must not be empty string
 -  There is `id` validation  - int values should be casted as Strings
 - Request is success if  `id` field is filled with string id value
 
**`field` field validation**

 - There is `field`
   validation  the whole field (`fields":null`) shouldn't not be null
 - There is  `field` validation  -   
   negative index    
 - There is  `field` validation - value is  zero 
 - - There is  `field` validation  - field values could be only int or null  
 - There is  `field` validation  - index above the possible top index  10
 -   There is  `field` validation  - field contains a two-dimensional array
 - array values could be a null
 
**`state` field validation**

 -  Request is success if  `state` field is filled with correct value  from ENUM - COMPLETED
 -  Request is success if  `state` field is filled    with correct value from ENUM - VALID
 - There is  `state` validation  - string value not from ENUM : "sdasdasdas"
 - There is  `state` validation - int value not from ENUM - 1 
 - There is  `state` validation  - PUT /board/validate request - value is null

**`dealsLink` field validation**
Request
 -  Request is success if  `dealsLink` field is filled with any string value
 -  There is `dealsLink` validation  - int values should be casted as Strings
 -  There is no `dealsLink` validation  - could be null
 
 Response
- If game is completed user should see a link
- If game is not completed user should see a link


 - There is HTTP 415 Unsupported Media Type if request content-type format is in an unsupported format (multipart_encoded, graphql)
 - There is 405 (Method Not Allowed) error if POST request is sent 
 - There
   is 405 (Method Not Allowed) error if DELETE request is sent 
  - There is
   validation for wrong PUT request /board/validate/?fields=1,2 
 - There is
   `field` validation `(400 error)`  - PUT /board/validate request - out
   the range `int`  - 999999999999 
  - There is  `field` validation `(400
   error)`  - PUT /board/validate request - empty index

## SECURITY TESTS

 - Check limit requests (Throttling) to avoid DDoS / brute-force
   attacks. 
  - Check using HTTPS on deployed application to avoid Man in
   the Middle Attack There is no any sensitive data (`credentials`, or 
   `API keys`) in URL or Body. 
   - Check user input to avoid common
   vulnerabilities (`SQL-Injection`,  `Remote Code Execution`). 
   - There shouldn't be any fingerprinting headers - `Server`,`X-Powered-By`

## Bugs

 - There is 500 error when user sends `PATCH` request with `null` body

## Possible improvements 

 - It is necessary to add authentication for users to prevent possible
   manipulations with with the influence of one player on another.
   Additionally this will reduce the load on the server due to a
   decrease in the number of requests 
  - Use HTTPS for communication
   between client and server 
   - Add validation for query params (e.g
   /board/id312312312312) now it just ignores it and creates a new board
   every time. User should be able to pass only id value after /board/

## Bonus

Additionally, I created two automated test to check a few negative scenarios. Please use class `NegativeTests`

Source code

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
