# Simple Wallet Microservice
A simple wallet microservice which do debit/credit, show balance and transaction history.
 
## Assumed Requirement
1.	Current balance per player account id
2.	Debit /Withdrawal per player account id. A debit transaction will only succeed if there are sufficient funds on the account (balance - debit amount >= 0). The caller will supply a transaction id, type, amount and account id. The transaction id must be unique for all transactions. If the transaction id is not unique, the operation must fail.
3.	Credit per player account id. The caller will supply a transaction id , type, amount and account id. The transaction id must be unique for all transactions. If the transaction id is not unique, the operation must fail.
4.	Transaction history per player account id

## Technology Used
1.	Java 11
2.	Maven 4
3.	Spring boot with starts test, web, and jpa dependency 
4.	H2 db

## Unit test

### 1.	TransactionRepositoryTest 
1.1	Happy path check for custom repository method find by account id 
1.2	Negative test for custom repository method find by account id 

### 2.	WalletServiceTest 
2.1	check transaction id test *
2.2	Get transaction history unit Test when the account exist *
2.3	make transaction method unit Test *
2.4	Update account balance unit Test when the account exist *
2.5	check available account balance unit Test when the account exist *
2.6	Save account unit Test when the account exist 
2.7	Get Balance unit Test when the account exist 

## Integration test
1.	WalletControllerTest 
1.1.	Successful save account 
1.2.	Unsuccessful Transaction because of incorrect transaction type 
1.3.	Successful Get Balance Test 
1.4.	Unsuccessful Transaction because of repeated transactionId 
1.5.	Unsuccessful DEBIT Transaction because of not enough balance for credit 
1.6.	Successful Get Transaction History Test *
1.7.	Get Balance Test when the account is not found 
1.8.	Successful CREDIT Transaction 
1.9.	Successful DEBIT Transaction
1.10.	Get Balance Test when the account is not found

## Out of scope
1.	Security, authentication and authorization 

## API Endpoint
Testing Simple wallet microservice.postman_collection.json

* test are work in progress
![image](https://user-images.githubusercontent.com/37999811/136751157-ef92f8d7-be06-46cf-95e1-96301a4164ff.png)
