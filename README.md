# DocumentLoadingTool

Ivy used instead of Maven for dependencies.   
Logback used for logging.  
  
DocumentLoadingTool/  
  src/  
    models/Employee.java - POJO for employee data.  
    util/Config.java - helper for loading configuration options from main.conf  
    util/DBHelper.java - establishes connection to the database  
    util/DocumentHelper.java - fascilitations document insertion  
    DocumentLoadtingTool.java - main method, application startup  
    
  main.conf - configuration file with database credentials and document threshold above which bulk insertion is used instead of sequential  
  ivy.xml - dependencies  
  logback.xml - logging configiration  
  .gitignore - files to be ignored by git (class, log and IDE files)  
