🌾 AgriTradeHub

AgriTradeHub is a full-stack web application built using Java Spring Boot, designed to bridge the gap between farmers and merchants through a transparent and efficient digital trading platform.
It empowers farmers to list their produce, merchants to browse and order goods, and administrators to manage the system — all within a secure and AI-assisted environment.

🚀 Features
--------------------------------------------------------------------------------
🧑‍🌾 For Farmers

Add and manage product listings with images and real-time stock updates

Track sales and orders directly from the dashboard

Get instant AI-powered insights on market trends and crop prices

🛒 For Merchants

Browse and purchase agricultural products from verified farmers

Access transparent pricing and availability information

Receive order updates via WhatsApp and Email notifications

🧑‍💼 For Admins

Manage users, disputes, and reports

Approve farmer and merchant registrations

Monitor platform activities with a clean dashboard

🤖 Integrated AI Chatbot – AgriBot

Powered by Gemini API

The AI chatbot provides intelligent support for farmers and merchants to understand market trends, price fluctuations, and trading insights.
It ensures responses for every query to enhance the user experience and decision-making.

Tech Integration:

Gemini API (AI Chatbot Integration) – Enables AI-driven chat assistance for analyzing crop prices, market trends, and trading insights.

🧩 Tech Stack
Layer	Technologies Used
Frontend	HTML, CSS, Bootstrap, JavaScript, Thymeleaf
Backend	Java, Spring Boot, Spring MVC, Spring Data JPA
Database	MySQL
AI & APIs	Gemini API (AI Chatbot Integration), WhatsApp & Email APIs
Version Control	Git & GitHub
🛠️ Installation & Setup
Prerequisites

Java 17+

MySQL Server

Maven

IDE (IntelliJ IDEA / Eclipse)





AgritradeHub/
│
├── src/
│   ├── main/
│   │   ├── java/com/myproject/AgritradeHub/
│   │   │   ├── Controller/        → Admin, Farmer, Merchant, Gemini Controllers
│   │   │   ├── Model/             → Entity classes (Users, Products, Orders, etc.)
│   │   │   ├── Repository/        → JPA Repositories
│   │   │   ├── service/           → GeminiService for AI price prediction
│   │   │   ├── config/            → Gemini API configuration
│   │   │   └── AgritradeHubApplication.java
│   │   └── resources/
│   │       ├── templates/         → Thymeleaf HTML templates
│   │       ├── static/            → CSS, JS, images
│   │       └── application.properties
│   └── test/
│       └── AgritradeHubApplicationTests.java
│
├── ProductImage/                  → Uploaded product photos
├── ProfilePicture/                → User profile images
├── pom.xml                        → Maven dependencies
├── mvnw, mvnw.cmd                 → Maven wrapper scripts
├── .gitignore, .gitattributes     → Git configurations
└── README.md


Some important steps:
--------------------

1️⃣ Clone the Repository
git clone https://github.com/vdubey92928/AgritradeHub.git
cd AgritradeHub

2️⃣ Configure the Database

Create a MySQL database named agritradehub

Update credentials in src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/agritradehub
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update

3️⃣ Add Gemini API Key

In application.properties:

gemini.api.key=YOUR_GEMINI_API_KEY

4️⃣ Run the Application
mvn spring-boot:run

5️⃣ Access the App

Visit http://localhost:8080

Default roles:

Admin: manages users and categories

Farmer & Merchant: register/login to access dashboards

📊 AI Module (Gemini API Integration)

Implemented in GeminiService.java and GeminiController.java

Fetches historical crop rate data

Uses Gemini API to predict price trend and future rate

Displays result in gemini.html

d
`
