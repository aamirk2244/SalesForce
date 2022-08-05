List<Account> accounts = [SELECT Id, Name, (SELECT id FROM Contacts), (SELECT id FROM 
opportunities) FROM Account];

// not accounts will have Id, Name of Account , id List from Contacts related to this Account , and an id list of opportunities related to this account

for (Account a : accounts) {
     System.debug('Count of Opportunities is ' + a.Opportunities.size());
     System.debug('Count of Contacts is ' + a.Contacts.size());
}




