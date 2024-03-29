# Spring Boot REST API for Music Management

## Overview
This project is a RESTful API developed using Spring Boot. It's designed for managing a music library, allowing users to handle artists and songs. The API provides CRUD (Create, Read, Update, Delete) functionalities for songs and artists. Each song is associated with an artist and includes information like the International Standard Recording Code (ISRC), title, and a link to the artist. Artist data includes details such as name and age.

## Features
- **Manage Songs**: Add, retrieve, update, and delete songs. Each song has an ISRC, title, and associated artist.
- **Manage Artists**: Add, retrieve, update, and delete artists. Artist information includes name and age.
- **Associations**: Each song is linked to an artist, demonstrating a one-to-many relationship.

## Technologies Used
- Java
- Spring Boot
- Maven
- JPA / Hibernate
- PostgreSQL / H2 Database

## Getting Started

### Prerequisites
- JDK 17

### Installation
1. Clone the repository:
   ```
   git clone [repository_url]
   ```
2. Navigate to the project directory:
   ```
   cd [project_name]
   ```
3. (Optional) Update the database configuration in `application.properties` according to your setup.

4. Build the project:
   ```
   mvn clean install
   ```

### Running the Application
Run the application using:
```
mvn spring-boot:run
```

```
The API will be available at `http://localhost:8080/`.

## API Endpoints

### Songs
- **POST /songs**: Add a new song
- **GET /songs**: Get all songs
- **GET /songs/{isrc}**: Get a song by ISRC
- **PUT /songs/{isrc}**: Update a song by ISRC
- **DELETE /songs/{isrc}**: Delete a song by ISRC

### Artists
- **POST /artists**: Add a new artist
- **GET /artists**: Get all artists
- **GET /artists/{id}**: Get an artist by ID
- **PUT /artists/{id}**: Update an artist by ID
- **DELETE /artists/{id}**: Delete an artist by ID
# SongAndArtistAPI
# SongAndArtistAPI
# SongAndArtistAPI
