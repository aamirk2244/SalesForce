
					//question
					
					


Create a Queueable Apex class that inserts Contacts for Accounts.
Create a Queueable Apex class that inserts the same Contact for each Account for a specific state.
Create an Apex class:
Name: AddPrimaryContact
Interface: Queueable
Create a constructor for the class that accepts as its first argument a Contact sObject and a second argument as a string for the State abbreviation
The execute method must query for a maximum of 200 Accounts with the BillingState specified by the State abbreviation passed into the constructor and insert the Contact sObject record associated to each Account. Look at the sObject clone() method.
Create an Apex test class:
Name: AddPrimaryContactTest
In the test class, insert 50 Account records for BillingState NY and 50 Account records for BillingState CA
Create an instance of the AddPrimaryContact class, enqueue the job, and assert that a Contact record was inserted for each of the 50 Accounts with the BillingState of CA
The unit tests must cover all lines of code included in the AddPrimaryContact class, resulting in 100% code coverage
Before verifying this challenge, run your test class at least once using the Developer Console Run All feature







///////////// starting from here 

public class AddPrimaryContact implements Queueable
{               // create contact for each account , contact will be provided in parameter
    private String billingState;
    private Contact myContact;
    public AddPrimaryContact(Contact obj , String stateAbb)
    {
        this.billingState = stateAbb;
        this.myContact = obj;
    }
    public void execute(QueueableContext context)
    {
        List<Contact> saveContacts = new List<Contact>();
        Contact myClone = myContact.clone();
        List<Account> getAccounts =  [select Id from Account where BillingState= 'CA' limit 200];
        //     System.debug('check this ' + getAccounts);
        
      
        for(Account acc: getAccounts)
        {
            myClone.AccountId = acc.Id;
            saveContacts.add(myClone);
        }
        insert saveContacts;
    }    
    
}













///////////////////test class for above 


@isTest
public class AddPrimaryContactTest {
    @testSetup static void setup()
    {
 		List<Account> addAccounts = new List<Account>();
        // insert 50 for billingAddress NA , and 50 for billing ADdress CA
    	
        for(Integer i = 0; i < 100; i++)
        {
            if (i > 50)
            	addAccounts.add(new Account(Name= 'Test Name ' + i, BillingState='NA'));
            else
                addAccounts.add(new Account(Name= 'Test Name '+ i, BillingState='CA'));
        }
        System.debug('this is inserted ' + addAccounts);
        insert addAccounts;
        
        
    }
    @isTest static void testingClass()
    {
        Contact mydummy = new Contact(FirstName='dummy name', LastName='dummy last name');
        AddPrimaryContact queueableExample = new AddPrimaryContact(mydummy, 'CA');
        Test.startTest();
        System.enqueueJob(queueableExample);
        Test.stopTest();


    }
    
}
