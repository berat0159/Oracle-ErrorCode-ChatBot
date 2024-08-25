package service;

import java.sql.Connection;
import java.sql.SQLException;

import dataacces.AnswerDAO;
import dbutil.DBUtil;
import enties.Answer;
import enties.Question;

public class AnswerService {
    private AnswerDAO answerDAO;

    // Constructor that initializes AnswerDAO with a database connection
    public AnswerService() {
        try {
            Connection connection = DBUtil.getConnection(); // Utility class to get a DB connection
            this.answerDAO = new AnswerDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Answer getAnswerTitle(Question question) {
        // İş mantığı veya ön işleme eklemek gerekirse buraya ekleyin
        return answerDAO.getAnswerTittleFromDatabase(question);
    }

    public Answer getAnswerCode(Question question) {
        // İş mantığı veya ön işleme eklemek gerekirse buraya ekleyin
        return answerDAO.getAnswerCodeFromDatabase(question);
    }
}
