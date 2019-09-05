# Money-transfer service

The service already contains several accounts with id [0..6], 
so you can try update and transfer operations without of creation an account.

## Installation



```bash
mvn clean compile

```

## Usage

```bash
# run server
mvn exec:java

# add account
curl -X POST -i -H "Content-Type: application/json" -d '{"money":125.8}' http://localhost:8080/v1/account

# deposit account
curl -X PUT -i -H "Content-Type: application/json" -d '{"money":70}' http://localhost:8080/v1/account/deposit/3

# withdraw account
curl -X PUT -i -H "Content-Type: application/json" -d '{"money":10}' http://localhost:8080/v1/account/withdraw/3

# transfer money from one account to another
curl -X PUT -i -H "Content-Type: application/json" -d '{"money":10}' http://localhost:8080/v1/account/transfer/4/6
```
