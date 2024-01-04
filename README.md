# Library Book Loaning System 
***

**A simple Library Loaning System to demostrate the RDBMS in Postgres with Java on Spring Boot framework.**


## Entity Relationship Diagram (ERD)

![Schema_Diagram](https://github.com/eggOnion/library_BookLoaningSystem/blob/main/Schema%20Diagram.png?raw=true)


## Cardinalities Relationship between `loan_period` and `learner` Table

### load_period Table

**What is the min & max `learner` that a `loan_period` can have?**
```
Ans: 1

A learner is unique, and so does the loan_period. Only a single learner can belong to 1 specific loan_period - aka id
```
---
### learner Table 

**What is the min `loan_period` that a `learner` can have?** 
```
Ans: 0

A learner can exist and didn't borrowed any book, thus the learner have no loan_period
```

**What is the max `loan_period` that a `learner` can have?**
```
Ans: Many

A learner can borrow different books on multiple times or different days, thus the learner will have many set of different loan_period - aka id
```
***

## Cardinalities Relationship between `loan_period` and `book` Table

### loan_period Table

**What is the min `book` that a `loan_period` can have?**
```
Ans: 1

In order for a loan_period to exist, it must have a minimum of 1 book borrowed.
```

**What is the max `book` that a `loan_period` can have?**
```
Ans: Many

A single loan_period can contain many different set of books borrowed.
```
---

### Book Table
**What is the min `loan_period` that a `book` can have?**
```
Ans: 0

A book is available for borrowed, but nobody wants to borrow it.
```

**What is the max `loan_period` that a `book` can have?**
```
Ans: Many

A book is available for borrowed, and it is so popular that many learner wants to borrow it.
```

***


## Setup Instructions

### 1. Setup Postgres

Create this new database in your Postgres
```
simple-library
```

### 2. Run the application

Open up your terminal from the root folder, then run
```
mvn clean spring-boot:run
```

### 3. Reaching the Endpoints

<br>http://localhost:8080/learners/
<br>http://localhost:8080/books/
<br>http://localhost:8080/loanStatus/

**login:**
> username: user
> <br>password: password


### 4. Using the Endpoints: 


### 4.1 Learners table

**Adding new learners:**

http://localhost:8080/learners/

Using Postman and pass in this JSON under `POST` request
```
{
    "firstName": "Bowery",
    "lastName": "King",
    "email": "boweryking@continental.com",
    "contact_num": "99102139"
}
```

***

### 4.2 Books table

**Adding new books:**

http://localhost:8080/books/

Using Postman and pass in this JSON under `POST` request
```
{
    "title": "Rich Dad Poor Dad",
    "author": "Robert Kiyosaki",
    "genre": "Personal Finance",
    "quantity": 3
}
```

***

### 4.3 LoanPeriod table

**Borrowing a book:**

Change the `{learner_id}` to the available learner in the Learners table of your choice. 
Change the `{book_id}` to the available book in the Books table of your choice. 

Eg; learner_id: 1 is John Wick
Eg; book_id: 1 is The Fellowship of the Ring

```
{
    http://localhost:8080/loanStatus/borrow/{learner_id}/{book_id}
}
```

**Returning a book:**

Using Postman and pass in this JSON under `PUT` request

```
{
    http://localhost:8080/loanStatus/return/{loanStatus_id}
}
```