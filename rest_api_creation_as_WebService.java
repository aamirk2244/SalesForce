



created AccountManager class for restApi to get the accounts and their contacts
created AccountManagerTest class for testing

/services/apexrest/Accounts/<Record ID>/contacts                   where /services/apexrest/ will be the default

for accessing the data, write /services/apexrest/Accounts/<Record ID>/contacts     in the workbench 


@RestResource(urlMapping='/Accounts/*/contacts')

global with sharing class AccountManager 
{
    @HttpGet
    global static Account getAccount() {
        RestRequest request = RestContext.request;
        // grab the caseId from the end of the URL
        String accId = request.requestURI.substringBetween('Accounts/','/contacts');
                  Account result =  [SELECT Id,Name, (select Id,Name from Contacts) FROM Account WHERE Id = :accId];
        return result;
    }
}







////////////// test for the above


@isTest
public class AccountManagerTest {
	@isTest static void getAccountWithContacts()
    {
        Account temp = new Account(Name = 'My test Account Name');
                insert(temp);
        Contact c = new Contact(FirstName=' dummy contact', LastName = ' my lastName', AccountId=temp.Id);
        

        insert(c);
         RestRequest request = new RestRequest();
        request.requestUri ='https://yourInstance.my.salesforce.com/services/apexrest/Accounts/' + temp.Id + '/contacts';
        request.httpMethod = 'GET';
        RestContext.request = request; 
        
        Account result = AccountManager.getAccount();
        System.debug('we get this account ' + result);
        System.debug('contacts are ' + result.Contacts);
        //result.Contacts is empty , why?
        // where are the Contacts?
        
        // let's get this info 
    
    	
    }
}




/////////////// sample code from the trailhead tutorial

@RestResource(urlMapping='/Cases/*')
global with sharing class CaseManager {
    @HttpGet
    global static Case getCaseById() {
        RestRequest request = RestContext.request;
        // grab the caseId from the end of the URL
        String caseId = request.requestURI.substring(
          request.requestURI.lastIndexOf('/')+1);
        Case result =  [SELECT CaseNumber,Subject,Status,Origin,Priority
                        FROM Case
                        WHERE Id = :caseId];
        return result;
    }
    @HttpPost
    global static ID createCase(String subject, String status,
        String origin, String priority) {
        Case thisCase = new Case(
            Subject=subject,
            Status=status,
            Origin=origin,
            Priority=priority);
        insert thisCase;
        return thisCase.Id;
    }   
    @HttpDelete
    global static void deleteCase() {
        RestRequest request = RestContext.request;
        String caseId = request.requestURI.substring(
            request.requestURI.lastIndexOf('/')+1);
        Case thisCase = [SELECT Id FROM Case WHERE Id = :caseId];
        delete thisCase;
    }     
    @HttpPut
    global static ID upsertCase(String subject, String status,
        String origin, String priority, String id) {
        Case thisCase = new Case(
                Id=id,
                Subject=subject,
                Status=status,
                Origin=origin,
                Priority=priority);
        // Match case by Id, if present.
        // Otherwise, create new case.
        upsert thisCase;
        // Return the case ID.
        return thisCase.Id;
    }
    @HttpPatch
    global static ID updateCaseFields() {
        RestRequest request = RestContext.request;
        String caseId = request.requestURI.substring(
            request.requestURI.lastIndexOf('/')+1);
        Case thisCase = [SELECT Id FROM Case WHERE Id = :caseId];
        // Deserialize the JSON string into name-value pairs
        Map<String, Object> params = (Map<String, Object>)JSON.deserializeUntyped(request.requestbody.tostring());
        // Iterate through each parameter field and value
        for(String fieldName : params.keySet()) {
            // Set the field and value on the Case sObject
            thisCase.put(fieldName, params.get(fieldName));
        }
        update thisCase;
        return thisCase.Id;
    }    
}
