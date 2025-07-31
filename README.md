# TrendTap

TrendTap is a full-stack web application that helps creators discover high-performing YouTube content and generate fresh content ideas. Users can search by keyword or select specific channels to analyze. The app retrieves and ranks videos by performance, and will evolve to suggest content strategies using AI-powered topic analysis.

Built with a React frontend and Spring Boot backend, TrendTap integrates with the YouTube API via RapidAPI.


## Features

- Search YouTube videos by keyword
- View video results including title, thumbnail, and channel name
- Responsive frontend built with React
- RESTful API backend built with Spring Boot
- API key managed securely via environment variables

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/AlexBogden/trendtap.git
cd trendtap
```

### 2. Backend Setup

```bash
cd backend
cp .env.example .env
```

Replace the placeholder value in `.env` with your actual RapidAPI YouTube API key.  
The `.env` file is ignored by Git. The included `.env.example` file shows the expected format.

Start the backend server:

```bash
./mvnw spring-boot:run
```

The backend runs at `http://localhost:8080`.

### 3. Frontend Setup

```bash
cd ../frontend
npm install
npm start
```

The frontend runs at `http://localhost:3000` and connects to the backend API.

## API Endpoint

```
GET /api/search?q=your-query
```

Returns a list of YouTube videos matching the query.

## Tech Stack

- Java 17
- Spring Boot
- React
- RapidAPI (YouTube API)
- Axios

## Project Structure

```
/backend    # Spring Boot backend
/frontend   # React frontend
```

## Author

Built by [Alex Bogden](https://github.com/AlexBogden)
