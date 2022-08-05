created AccountProcessor class with future method , which count contacts for each account, with the test class of AccountProcessorTest


public class AccountProcessor {
	@future
    public static void countContacts(List<Id> accId)
    {
        
    //    List<Account> accId = [select Id,Account.Number_Of_Contacts__c from Account];
      //  Map<Id,Account> mapAccount = new Map<Id,Account>([select id from account]);
       // Set<ID> SetACCID = new Set<ID>();
       // SetACCID = mapAccount.keySet();
	//	ID d = null;
       // List<Account> listToUpdate = new List<Account>();
        
     	List<Account> ct = [select Name,id,(select id from Contacts) from Account where Id IN:accId];
        
        for(Account a : ct)
        {
                a.Number_Of_Contacts__c = a.Contacts.size();
                System.debug('counting contacts '+ a.Contacts); 
            
        }
		update ct;        
        
    }
}










//////////////////test class for above


@isTest
public class AccountProcessorTest {
    
    @isTest public static void countContactsTest()
    {
        
        List<Account> conList = new List<Account> {
            new Account(Name='Joe',Phone='6984236'),
                new Account(Name='Kathy',Phone='927483927'),
                new Account(Name='Caroline',Phone='2643243'),
                new Account(Name='Kim',Phone='287492')};
        
                    insert conList;
        String testName = 'Test ';
        String testLname = 'Test LastName ';
        Integer i = 0;
        
        List<Id> AllAccountId = new List<Id>();
        
        for(Account acc: conList)
        {
            Contact ct = new Contact(FirstName=testName + i, LastName= testLname+i, AccountId=acc.Id);
            AllAccountId.add(acc.Id);
            i++;
            insert ct;
        }
        
        
	    Test.startTest();
        
        
        
        
        AccountProcessor.countContacts(AllAccountId);
        Test.stopTest();
       
    }
    
    
}
