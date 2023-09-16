# TOPUPer
It is an app that I created in 2015 for a client whose job was to manually send mobile recharge (TOPUP) to the mobile numbers that are listed on a website.
It does the following steps
- Logins to the site containing the mobile numbers using user credentials.
- Fetches the list of transaction to be done (i.e., the mobile numbers and amount to be transferred) from the site.
- Identifies the carrier network (NTC or NCELL) of the mobile number and transfers the balance using the appropriate carrier.
- Continuously looks for SMS receipt of a successful balance transfer, and once confirmed ticks the transaction as successful on the website. If an unsuccessful message is received then it tries again and informs the user.
