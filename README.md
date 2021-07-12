# carkparkapp 

This application is used to register customer,fetch parking slots information and request for allocate,amend/auto-amend and unallocate the cark park slots

# Note:
1. Customer has to wait for his hourslot time to complete to perform reallocate, but he is allowed to perform cancel any time. 

2. Auto Reallocate logic is created as such: 
Auto Reallocate happens for every 4.5 hours. There is a 4.5 hours period(Maximum slot time of 4 hours according to system+30 minutes grace period)  for a customer to reallocate, post usage of his overall hourslot allocation & usage. If the customer does not perform either reallocate or cancel within maximum of  period, auto reallocate happens with the same hourslot which customer has early provided during previous booking.

# Application Design

This application has the model representation as below,

![image](https://user-images.githubusercontent.com/32460730/124623539-b43a1500-de99-11eb-9fba-82a1cb2b46da.png)

# API Documentation

API documentation on for request/response is configured with Swagger

![image](https://user-images.githubusercontent.com/32460730/124624390-74bff880-de9a-11eb-99d7-940e510c20ac.png)
