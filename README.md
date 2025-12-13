#  Student Performance Prediction (K-NN Algorithm)

This project is a **JavaFX application** that predicts the academic performance of students using the **K-Nearest Neighbors (KNN)** machine-learning algorithm.  
The system uses student historical data (grades, attendance, activities, etc.) to estimate how a new student is likely to perform based on **students with similar academic profiles**.

---

##  Features

### ✔ Student Performance Prediction
The application predicts a student's performance level using **KNN**, based on:
- Module grades  
- Attendance  
- Activities  

###  Admin Panel
Administrators can:
- Add/update student grades  
- Insert performance indicators  
- View historical and predicted performance  
- Manage database connection settings  

###  Database Integration
The project uses a **MySQL database** named `school`.  
A ready-to-use dump file (`school_backup.sql`) is included.

---

##  How the KNN Algorithm Works

The **K-Nearest Neighbors (KNN)** algorithm predicts performance by comparing the new student’s data to **other students already stored in the database**.

###  Step-by-step:

**Compute Similarity**  
   For a new student, the algorithm calculates the **distance** between this student and every other student (typically Euclidean distance).

**Find the K Nearest Students**  
   The algorithm selects the **K closest students**, meaning those whose academic profiles are the most similar.

**Predict Performance**  
   The predicted performance (e.g., *Excellent*, *Good*, *Average*, *At Risk*) is determined by the **majority class** among these K neighbors.

In simple terms:  
**“Students who are similar tend to perform similarly — so their outcomes help predict the new student’s performance.”**

---

##  Requirements

Before running the project, ensure you have:

### ** JavaFX SDK (javafx-sdk-21.0.9)**  
Download JavaFX 21 and configure it in your IDE or Maven.

### ** Maven**  
Verify installation:
```bash
mvn -v
```

### ** MySQL Connector JAR**
Add:

```
mysql-connector-j-9.4.0.jar
```

If using another version, update the dependency in `pom.xml`.

### ** MySQL Database**
You must import:
```
school_backup.sql
```

---

##  Database Setup

1. Open MySQL terminal or GUI (Workbench, phpMyAdmin, etc.)
2. Create the database:
   ```sql
   CREATE DATABASE school;
   ```
3. Import the provided SQL file:
   ```bash
   mysql -u your_username -p school < school_backup.sql
   ```
4. Update your database credentials in:

```
schooldb/DatabaseConnection.java
```

Example:
```java
String url = "jdbc:mysql://localhost:3306/school";
String username = "your_username";
String password = "your_password";
```

---

## ▶ Running the Application

Run the JavaFX application using Maven:

```bash
mvn javafx:run
```


## Notes

- Ensure JavaFX SDK is properly linked if you are using IntelliJ/Eclipse.  
- Update the MySQL username/password before running the program.  
- You may adjust the value of **K** in the KNN algorithm inside the prediction logic if performance needs tuning.


