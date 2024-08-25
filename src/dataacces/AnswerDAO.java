package dataacces;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import enties.Answer;
import enties.Question;

public class AnswerDAO {

	private Connection connection;
	
	public AnswerDAO(Connection connection) {
		this.connection = connection;
	}
	
    public Answer getAnswerTittleFromDatabase(Question question) {
    	Answer answer = new Answer();
       String answerTittleText = "Soru Bulunamadı !";
    	answer.setAnswTittle(answerTittleText);
        String query = "SELECT Answers.AnswerTittle " +
                       "FROM Questions " +
                       "JOIN Answers ON Questions.QueID = Answers.QueID " +
                       "WHERE LOWER(Questions.QueTittle) LIKE LOWER (?)";
        try {
        	PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + question.getQueTittle() + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                	answerTittleText = resultSet.getString("AnswerTittle");
                	answer.setAnswTittle(answerTittleText);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return answer;
    }
	
    public Answer getAnswerCodeFromDatabase(Question question) {
    	Answer answer = new Answer();
    	String answerCodeText= "Soru Bulunamadı !";
        answer.setAnswCode(answerCodeText);
        String query = "SELECT Answers.ExampleCode " +
                       "FROM Questions " +
                       "JOIN Answers ON Questions.QueID = Answers.QueID " +
                       "WHERE LOWER(Questions.QueTittle) LIKE LOWER (?)";
        
        try{
        	
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + question.getQueTittle() + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                	answerCodeText = resultSet.getString("ExampleCode");
                    answer.setAnswCode(answerCodeText);;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return answer;
    }
    
    
}
