created a LeadProcessor batch class which update the LeadSource field in Lead object


public class LeadProcessor implements Database.Batchable<sObject>{
      public static Integer recordsProcessed = 0;

    public Database.QueryLocator start(Database.BatchableContext bc)
   {
        return Database.getQueryLocator(
            'SELECT Id,LeadSource FROM Lead'
             );
   }
    public void execute(Database.BatchableContext bc, List<Lead> records)
    {
        // process each batch of records
        // 
    	
        for(Lead rec : records)
        {
            rec.LeadSource = 'Dreamforce';
            recordsProcessed++;
          //  System.debug('debugging this area');

        }
        update records;
    
    
    }
    public void finish(Database.BatchableContext bc)
    {
        // execute any post-processing operations
        // 
    
    	System.debug('Finished Batches');
         System.debug(' records processed. by your batch are= ' + recordsProcessed );
         AsyncApexJob job = [SELECT Id, Status, NumberOfErrors,
            JobItemsProcessed,
            TotalJobItems
            FROM AsyncApexJob
            WHERE Id = :bc.getJobId()];
        System.debug('Printing Jobs ' + job);

    }

}


///////////// test class for above


@isTest
public class LeadProcessorTest 
{
    @isTest
    public static void testingLeadWithBatch()
    {
        List<Lead> lead = new List<Lead>();
        
        // new Lead(Company = 'JohnMiller', LastName = 'Mike'),
        // 
        
        String appending = ' Test ';
        for(Integer i = 0 ; i < 200; i++)
        {
            lead.add(new Lead(Company='JahonMiller' + appending + i , LastName= appending+i));           
        }
       // System.debug('Leads are ' + lead);
        insert lead;
        
        Test.startTest();
        LeadProcessor uca = new LeadProcessor();
        
      //  Id batchId = Database.executeBatch(myBatchObject, 100);
        Id batchId = Database.executeBatch(uca);
        Test.stopTest();
        // after the testing stops, assert records were updated properly
        System.assertEquals(200, [select count() from Lead where LeadSource = 'Dreamforce']);
      //  System.assertEquals(100, [select count() from Lead where LeadSource = 'Web']);
        
        
        
    }
}
