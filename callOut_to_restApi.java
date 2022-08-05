call outs (like an api call)
**two ways to test callout , one is to create a static resource file , i.e txt file and enter your json text of the api
** second way is to use HTTP mock interface 
https://trailhead.salesforce.com/content/learn/modules/apex_integration_services/apex_integration_rest_callouts

I tried both

--> created AnimalLocator class for callout
--> created AnimalLocatorTest for testing callouts
--> created AnimalLocatorMock for testing with this mock instead of static resource file




public class AnimalLocator {
    
    public static global getAnimalNameById(Integer index)
    {
        //The method must call https://th-apex-http-callout.herokuapp.com/animals/<id>
        //
        
        
        Http http = new Http();
        HttpRequest request = new HttpRequest();
        String endPoint = 'https://th-apex-http-callout.herokuapp.com/animals/' + index;
        request.setEndpoint(endPoint);
        request.setMethod('GET');
        HttpResponse response = http.send(request);
        // If the request is successful, parse the JSON response.
        // 
        String getAnimalName;
        if(response.getStatusCode() == 200) 
        {
            // Deserialize the JSON string into collections of primitive data types.
            Map<String, Object> results = (Map<String, Object>) JSON.deserializeUntyped(response.getBody());
            // Cast the values in the 'animals' key as a list
          //  List<Object> animals = (List<Object>) results.get('animal');
  //        list<Object> l = (list<Object>) results.get('animal');
	//	map<String,Object> m = (map<String,Object>) l[0];
		//Integer i = (Integer) m.get('addressId');
            Map<String, Object>  animalMap = (Map<String, Object>) results.get('animal');
            System.debug('Received the following animals:');
            System.debug('this is the animal name = ' + animalMap.get('name'));
            getAnimalName = String.valueof(animalMap.get('name'));               // casting object to string 
              
            
        }
        return getAnimalName;
    }
    
}




/////////////////// mock class for testing 

@isTest
global class AnimalLocatorMock implements HttpCalloutMock {
    // Implement this interface method
    global HTTPResponse respond(HTTPRequest request) {
        // Create a fake response
        HttpResponse response = new HttpResponse();
        response.setHeader('Content-Type', 'application/json');
        response.setBody('{"animal":{"id":1,"name":"chicken","eats":"chicken food","says":"cluck cluck"}}');
        response.setStatusCode(200);
        return response; 
    }
}


////////////// test class 


@isTest
public class AnimalLocatorTest 
{
		@isTest static void AnimalLocatorMock()
        {
          //    StaticResourceCalloutMock mock = new StaticResourceCalloutMock();
       
                Test.setMock(HttpCalloutMock.class, new AnimalLocatorMock()); 

            
      //      mock.setStaticResource('sampleAnimalResource');
      //  mock.setStatusCode(200);
      //  mock.setHeader('Content-Type', 'application/json;charset=UTF-8');
        // Associate the callout with a mock response
    //    Test.setMock(HttpCalloutMock.class, mock);
        // Call method to test
        
            
            System.assertEquals('chicken', AnimalLocator.getAnimalNameById(1));
           // System.debug('test result from test UsingMock ' + result);
     //   System.assertNotEquals(null,result, 'The callout returned a null response.');
     //   System.assertEquals(200,result.getStatusCode(), 'The status code is not 200.');
        // Verify content type   
 
       
        
        }
    
    
}









