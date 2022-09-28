package SystClasses;

/**Interface to make certain error management easier to implement. Involved with the projects required lambda implementation.*/
public interface ErrorHandlerInterface
{
    String error01="There was a problem parsing your date. Please check the format and try again.";
    String error02="There was a problem parsing your start or end time. Please check the format and try again.";
    String error03="There was a problem parsing your customer data. Please check the format and try again.";
    String error04="That customer is not in the database. Please add them to the database before setting up an appointment!";
    String error05="There was a problem parsing your appointment information fields. Please check the format and try again.";
    String error06="There are fields that are empty. Please completely fill out the form and try again.";
    String error07="There is an overlapping appointment. Please adjust your times accordingly.";
    String error08="An unknown error occurred. Please check the format and try again.";
    String error09="This user ID was not found in the database.";





    String errorHandler();
}
