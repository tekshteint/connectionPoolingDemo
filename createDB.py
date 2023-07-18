import os as installer
try:
    import psycopg2
    
except ImportError:
    installer.system("pip install psycopg2 --user")
    print("All packages successfully installed.")    
    import psycopg2

class PostgresConnect:
    def __init__(self):
       
        try:
            self.createDB()
            connection = self.get_connection()
            
        except (Exception, psycopg2.Error) as error:
            print("Error while connecting to PostgreSQL:", error)
            raise SystemExit(1)
        finally:
            if connection:
                self.createTable(connection)
                self.populateTable(connection)
                connection.close()
                print("Database has been created and populated successfully")
    
    def createDB(self):
        conn = psycopg2.connect(
        database="postgres", user='postgres', password='admin', host='127.0.0.1', port= '5432'
            )
        conn.autocommit = True

        cursor = conn.cursor()

        sql = '''CREATE database employees'''
        cursor.execute(sql)
        
        return conn
    
    def get_connection(self):
        DB_HOST = "localhost"
        DB_PORT = "5432"
        DB_NAME = "employees"
        DB_USER = "postgres"
        DB_PWD = "admin"
        connection = None
        try:
            connection = psycopg2.connect(
                host=DB_HOST,
                port=DB_PORT,
                database=DB_NAME,
                user=DB_USER,
                password=DB_PWD
            )
        except (Exception, psycopg2.Error) as error:
            print("Error while connecting to PostgreSQL:", error)
        finally:
            if connection:
                print("Opened database successfully")
        return connection

    
    def createTable(self, connection):
        try:
            with connection.cursor() as cursor:
                sql = """
                    CREATE TABLE EMPLOYEE (
                        ID INT PRIMARY KEY NOT NULL,
                        email CHAR(50),
                        firstName TEXT NOT NULL,
                        lastName TEXT NOT NULL
                    )
                """
                cursor.execute(sql)
                connection.commit()
                
        except (Exception, psycopg2.Error) as error:
            print("Error while creating table:", error)
        finally:
            print("Table created successfully")
    
    def populateTable(self, connection):
        try:
            with connection.cursor() as cursor:
                sql = """
                    INSERT INTO EMPLOYEE (ID, email, firstName, lastName)
                    VALUES (1, 'johndoe@gmail.com', 'John', 'Doe')
                """
                cursor.execute(sql)
                sql = """
                    INSERT INTO EMPLOYEE (ID, email, firstName, lastName)
                    VALUES (2, 'janesmith@gmail.com', 'Jane', 'Smith')
                """
                cursor.execute(sql)
                sql = """
                    INSERT INTO EMPLOYEE (ID, email, firstName, lastName)
                    VALUES (3, 'barakobama@aol.com', 'Barak', 'Obama')
                """
                cursor.execute(sql)
                sql = """
                    INSERT INTO EMPLOYEE (ID, email, firstName, lastName)
                    VALUES (4, 'bobmarley@yahoo.com', 'Bob', 'Marley')
                """
                cursor.execute(sql)
                connection.commit()
        except (Exception, psycopg2.Error) as error:
            print("Error while populating table:", error)
            
            
if __name__ == "__main__":
    PostgresConnect()