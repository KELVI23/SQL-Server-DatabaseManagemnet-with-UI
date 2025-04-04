# SQL Server Database Management with UI

This Java project enables users to parse data from CSV and text files into a SQL Server database, providing a Swing-based graphical user interface (GUI) for database manipulation.

## Features

- **Data Parsing**: Import data from various file formats, including:
  - `books500.csv`: Contains book information.
  - `customers.dat`: Contains customer data.
  - `purchases.csv`: Contains purchase records.

- **Database Management**: Interact with the SQL Server database through a user-friendly Swing GUI, allowing operations such as:
  - Viewing records
  - Inserting new entries
  - Updating existing data
  - Deleting records

## Prerequisites

- **Java Development Kit (JDK)**: Ensure JDK is installed on your system.
- **SQL Server**: A running instance of SQL Server is required.
- **JDBC Driver**: The Microsoft JDBC Driver for SQL Server must be available.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/KELVI23/SQL-Server-DatabaseManagemnet-with-UI.git
   ```

2. **Configure Database Connection**:
   - Locate the `db.properties` file in the project directory.
   - Update the properties with your SQL Server credentials:
     ```properties
     db.url=jdbc:sqlserver://your_server_address:your_port;databaseName=your_database
     db.user=your_username
     db.password=your_password
     ```

3. **Build and Run the Application**:
   - Use your preferred Java IDE to open the project.
   - Build the project to resolve dependencies.
   - Run the `Main` class to launch the application.

## Usage

- **Importing Data**: Utilize the GUI options to select and import data files (`.csv`, `.dat`, etc.) into the database.
- **Managing Records**: Perform CRUD (Create, Read, Update, Delete) operations through the GUI to manage database records effectively.

## Project Structure

- **Source Code**: Located in the `src/a01085867/book` directory.
- **Libraries**: External libraries are stored in the `lib` folder.
- **Resources**: Data files and configuration files are in the root directory, including:
  - `books500.csv`
  - `customers.dat`
  - `purchases.csv`
  - `db.properties`

## Dependencies

- **JDBC Driver**: Ensure the Microsoft JDBC Driver for SQL Server is included in the `lib` directory.

## Notes

- The project includes a pre-built JAR file named `A01085867_Assign2.jar` for direct execution.
- Log configurations are managed via the `log4j2.xml` file.

## License

This project is open-source. Feel free to modify and use it according to your needs.

---

*For any issues or contributions, please refer to the repository on [GitHub](https://github.com/KELVI23/SQL-Server-DatabaseManagemnet-with-UI).* 
