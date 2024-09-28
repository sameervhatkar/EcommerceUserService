# E-commerce User Microservice

# Overview

This microservice is the heart of the e-commerce platform, responsible for managing user accounts and authentication. It provides endpoints for user registration, login, profile management, and deletion. Additionally, it handles crucial functionalities like JWT-based token authentication, user role management, and secure session management.
All authorization, tokenization, and validation are handled by this service, making it central to the security of the platform. Other services, such as Product, Order, and Payment, validate the token and user details by calling the client service provided by the User Service, ensuring seamless and secure communication across the platform.
This version emphasizes the importance of the User Service and its role in managing authentication and validation for other microservices.

## Technologies Used
* Java
* Spring Boot
* JPA (Java Persistence API)
* Hibernate
* MySQL
* RESTful API
* JWT
* Spring Security

## Architecture
The microservice follows a layered architecture with the following packages:
* Controller: Contains REST controllers for handling HTTP requests.
* DTO (Data Transfer Objects): Defines the objects used to transfer data between layers.
* Entity: Represents the database entities.
* Exceptions: Custom exception handling.
* Mapper: Maps entities to DTOs and vice versa.
* Repository: JPA repositories for database access.
* Service: Contains the business logic.
* Filter: Contains the JWT filter responsible for intercepting HTTP requests and validating JWT tokens for secure access.
* Config: Contains security configuration.

## Entities
### User
```Java
public class User {
    private String name;
    private String email;
    private String password;
    private List<Role> roles;
    private String token;
}
```

### Role
``` Java
public class Role {
  private String role;
}
```

## Endpoints

### UserController
* User Registration: POST /signIn
  - Request Body: SignInRequestDTO
  - Response: UserResponseDTO
* User Login: POST /login
  -	Request Body: LoginRequestDTO
  -	Response: UserResponseDTO
* Authenticate User: GET /authenticate
  -	Response: Boolean (true if authenticated)
* User Logout: POST /logout/{token}
  -	Path Variable: String token
  -	Response: Boolean
* Update User: PUT /updateUser
  - Request Body: UserUpdateRequestDTO
  - Response: UserResponseDTO
* Get User by ID: GET /getUser/{userId}
  - Request Header: Authorization token
  - Path Variable: UUID userId
  - Response: UserResponseDTO
* Get All Users: GET /getUsers
  - Response: List<UserResponseDTO>
* Delete User: DELETE /deleteUser/{userId}
	-	Path Variable: UUID userId
	-	Response: Boolean

## Services

### UserService
* signIn: Registers a new user.
* logIn: Authenticates a user with email and password, returning a JWT token.
* logout: Logs out a user by invalidating their JWT token.
* updateUser: Updates the details of an existing user.
* getUserById: Retrieves a user by their UUID.
* getUser: Retrieves all users.
* deleteUser: Deletes a user by their UUID.
* extractUserId: Extracts the user’s ID from the provided JWT token.

