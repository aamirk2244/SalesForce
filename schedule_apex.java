public class DailyLeadProcessor Implements Schedulable
{
    public void execute(SchedulableContext ctx)
    {
        
        List<Lead> leads = [select Id,LeadSource from Lead where LeadSource= 'temp'];
        System.debug(leads);
        
        for(Lead ld : leads)
        {
            ld.LeadSource = 'Dreamforce';
            
        }
        update leads;
        
       CronTrigger ct = [SELECT TimesTriggered, NextFireTime FROM CronTrigger WHERE Id = :ctx.getTriggerId()];
        System.debug('checking job info ' + ct);

    }
    
}









//////////////// test for the above 


@isTest
public class DailyLeadProcessorTest {
	@isTest static void setup()
    {
        List<Lead> ld = new List<Lead>();
         
        for(Integer i = 0; i < 200; i++)
        {
            Lead tmp = new Lead(FirstName='Test '+i, LastName='lastTestName ' + i, LeadSource = 'temp', Company='temp');
            ld.add(tmp);
        }
        insert ld;
       //i.e                      27            8                  2022
        // Seconds Minutes Hours Day_of_month Month Day_of_week optional_year
		
         String CRON_EXP = '0 0 0 27 8 ? 2022';

        DailyLeadProcessor p = new DailyLeadProcessor();
        Test.startTest();
        String jobId = System.schedule('ScheduledApexTest',CRON_EXP,p);
        
        
        Test.stopTest();
        
    }
}
