select count() from Contact where Contact.Account.BillingState = 'CA

here Contact.Account      (Account is parent, we are accessing BillingState field which is present in Account)

