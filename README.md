# Family Cash Card Application

This repository contains the code for the Family Cash Card application built with Spring Boot. This project serves as a learning tool for building a RESTful API, incorporating security, and connecting to a database.

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Getting Started](#getting-started)
5. [Running the Application](#running-the-application)
6. [API Endpoints](#api-endpoints)
7. [Testing](#testing)
8. [Security](#security)
9. [Project Structure](#project-structure)
10. [Contributing](#contributing)
11. [License](#license)

## Overview
The Family Cash Card application allows parents to manage allowance funds for their children virtually. This project demonstrates building a simple yet functional RESTful API using Spring Boot.

## Features
- Create, edit, delete, and view cash cards.
- Secure endpoints with Spring Security.
- Persist data using Spring Data JPA and a relational database.
- Follow Test-First development approach.

## Technologies Used
- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database (or any other relational database)
- JUnit and Mockito for testing

## Getting Started
### Prerequisites
- Java Development Kit (JDK) 8 or later
- Maven
- An IDE such as Eclipse or IntelliJ IDEA

### Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/bhanuprakash-chowdary/family-cash-card.git
   cd family-cash-card
2. Build the project using Maven:
   * mvn clean install

## Running the Application
1. Start the application:
   * mvn spring-boot:run
2. The application will be accessible at http://localhost:8080.

## API Endpoints
- Create a Cash Card: POST /cashcards
- Edit a Cash Card: PUT /cashcards/{requestedId}
- Delete a Cash Card: DELETE /cashcards/{requestedId}
- View a Cash Card: GET /cashcards/{requestedId}
- List Cash Cards: GET /cashcards

## Testing
- Another unique quality of this course is our Test-First approach to development. In the Labs, you will usually be writing tests before the implementation, then adding implementation in order to make your tests pass.

## Security
- Secure endpoints using Spring Security.
- Configure user roles and permissions to control access.

## Project Structure
- src/main/java/com/example/familycashcard - Main application code.
- src/main/resources - Application configuration files.
- src/test/java/com/example/familycashcard - Test cases.

## Contributing
Contributions are welcome! Please fork the repository and submit pull requests.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.