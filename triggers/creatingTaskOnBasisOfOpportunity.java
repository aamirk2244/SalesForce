trigger ClosedOpportunityTrigger on Opportunity (after insert, after update) {
      List<Task> taskList = new List<Task>();

    for(Opportunity a: [select Id from Opportunity WHERE Id IN :Trigger.New  AND StageName = 'Closed Won'])
    {
	  // get the newly effected row , and check if it is closed won
        
         taskList.add(new Task(Subject='Folow up Test task',
                               ActivityDate=System.today().addMonths(1),
                                   WhatId=a.Id)); 
        // WhatId is the 'related to' field in the Opportunity Object
      
    }
    
    
    System.debug('opportunity added ');
    System.debug(taskList);
     if (taskList.size() > 0)
     {
        insert taskList;
    }
    
}
