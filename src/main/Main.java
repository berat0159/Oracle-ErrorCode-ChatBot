package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.management.loading.PrivateClassLoader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import dataacces.AnswerDAO;
import enties.Answer;
import enties.Question;
import main.*;
import service.AnswerService;

public class Main {

	private JFrame frame;
	
	private JTextField questionText;
	private JTextArea answerText;
	private JTextPane answerCode;
	private JLabel lblNewLabel;
	private JButton askButton;
	
	private static String name = "";
	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement preparedStatement;
	private static AnswerService answerService;
	
	private static Question question;
	private static Answer answer;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public Main() {
		answerService = new AnswerService();
		question = new Question();
		answer = new Answer();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
    private void initialize() {
        frame = new JFrame();
        frame.setContentPane(new BackgroundPanel());
        frame.setBackground(new Color(128, 128, 128));
        frame.setBounds(100, 100, 1329, 969);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(100, 10, 10, 10);
        gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        gbc_lblNewLabel.gridwidth = 2;

        Image searchIconImage2 = loadImage("resources/vodafoneicon.png");
        lblNewLabel = new JLabel("DWH Chat Bot");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setIcon(new ImageIcon(searchIconImage2));
        lblNewLabel.setFont(new Font("Impact", Font.PLAIN, 40));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
        frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);


        GridBagConstraints gbc_questionText = new GridBagConstraints();
        gbc_questionText.gridx = 0;
        gbc_questionText.gridy = 1;
        gbc_questionText.gridwidth = 2;
        gbc_questionText.anchor = GridBagConstraints.CENTER;
        gbc_questionText.fill = GridBagConstraints.NONE;

        // Add icon inside the text field
        //JLabel searchIcon = new JLabel(new ImageIcon("images\\icons8-search-64.png")); // Set search icon
        Image searchIconImage = loadImage("resources/icons8-search-64.png");
        JLabel searchIcon = new JLabel(new ImageIcon(searchIconImage));
        searchIcon.setBorder(new EmptyBorder(0, 5, 0, 5));

        questionText = new JTextField();
        questionText.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        questionText.setBorder(new EmptyBorder(0, 10, 0, 10));
        questionText.setPreferredSize(new Dimension(300, 40));
        

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setOpaque(false);
        questionPanel.add(searchIcon, BorderLayout.WEST);
        questionPanel.add(questionText, BorderLayout.CENTER);

        frame.getContentPane().add(questionPanel, gbc_questionText);
        
        GridBagConstraints gbc_askButton = new GridBagConstraints();
        askButton = new JButton("Sorgula");
        askButton.setFont(new Font("Microsoft JhengHei", Font.BOLD | Font.ITALIC, 22));
        askButton.setBackground(new Color(255, 128, 0));
        gbc_askButton.gridx = 0;
        gbc_askButton.gridy = 2;
        gbc_askButton.gridwidth = 2;
        gbc_askButton.insets = new Insets(50, 10, 100, 10);
        frame.getContentPane().add(askButton, gbc_askButton);

        // ActionListener for the button
        ActionListener buttonActionListener = e -> {
            name =questionText.getText();
            question.setQueTittle(name);
            if (question.getQueTittle().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Lütfen bir soru girin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                
                Answer answerCodeData = answerService.getAnswerCode(question);
                Answer answerTitleData = answerService.getAnswerTitle(question);
                
                String answerCodeText = answerCodeData.getAnswCode(); // String türü
                
                // Veriyi GUI'ye ata
                answerCode.setText(answerCodeData.getAnswCode()); // Doğrudan özellik
                answerText.setText(answerTitleData.getAnswTittle()); // Doğrudan özellik
                highlightSyntax(answerCodeText);
                animateTextWriting(answerCodeText);
            }
        };
        
        // Add ActionListener to the button
        askButton.addActionListener(buttonActionListener);

        // Add ActionListener to the text field for Enter key
        questionText.addActionListener(e -> buttonActionListener.actionPerformed(e));

        // Answer text configuration
        GridBagConstraints gbc_answerText = new GridBagConstraints();
        answerText = new JTextArea();
        answerText.setBackground(new Color(192, 192, 192));
        answerText.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
        answerText.setLineWrap(true);
        answerText.setWrapStyleWord(true);
        answerText.setBorder(new EmptyBorder(10, 20, 10, 20));
        answerText.setEditable(false);
        
        gbc_answerText.gridx = 0;
        gbc_answerText.gridy = 3;
        gbc_answerText.gridwidth = 1;
        gbc_answerText.fill = GridBagConstraints.NONE;
        gbc_answerText.insets = new Insets(10, 10, 100, 5); // Right inset for spacing
        JScrollPane scrollPaneAnswerText = new JScrollPane(answerText); // Create JScrollPane for answerText
        scrollPaneAnswerText.setPreferredSize(new Dimension(600, 250)); // Set preferred size for JScrollPane
        frame.getContentPane().add(scrollPaneAnswerText, gbc_answerText); // Add JScrollPane to the frame

        // Answer code configuration
        GridBagConstraints gbc_answerCode = new GridBagConstraints();
        answerCode = new JTextPane();
        answerCode.setBackground(new Color(41, 41, 41));
        answerCode.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
        answerCode.setForeground(new Color(255, 255, 255));
        answerCode.setBorder(new EmptyBorder(10, 20, 10, 20));
        answerCode.setEditable(false);
        
        gbc_answerCode.gridx = 1;
        gbc_answerCode.gridy = 3;
        gbc_answerCode.gridwidth = 1;
        gbc_answerCode.fill = GridBagConstraints.NONE;
        gbc_answerCode.insets = new Insets(10, 5, 100, 10); // Left inset for spacing
        answerCode.setPreferredSize(new Dimension(600, 250));
        frame.getContentPane().add(new JScrollPane(answerCode), gbc_answerCode);

        addStylesToDocument(answerCode.getStyledDocument());
    }
    private Image loadImage(String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            } else {
                throw new RuntimeException("Resim dosyası bulunamadı: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Custom JPanel to paint background image
   /** class BackgroundPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = new ImageIcon("images\\vodafonebackground.jpg").getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
    **/
    
    
    /**private static Properties loadProperties() throws IOException {
    Properties properties = new Properties();
    try (FileInputStream input = new FileInputStream("C:\\Users\\berat\\eclipse-workspace\\DwhBot\\config.properties.txt")) {
        properties.load(input);
    }
    return properties;
}**/
    class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                // Resmi kaynak olarak yükle
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/Virtual Background-6.jpg");
                if (inputStream != null) {
                    // Resmi ImageIcon ile yükle
                    backgroundImage = new ImageIcon(ImageIO.read(inputStream)).getImage();
                } else {
                    throw new RuntimeException("Resim dosyası bulunamadı.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
	private void addStylesToDocument(StyledDocument doc) {
        javax.swing.text.Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        
        MutableAttributeSet keywordStyle = doc.addStyle("keyword", def);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        MutableAttributeSet numberStyle = doc.addStyle("number", def);
        StyleConstants.setForeground(numberStyle, Color.RED);

        MutableAttributeSet stringStyle = doc.addStyle("string", def);
        StyleConstants.setForeground(stringStyle, Color.GREEN);
	}
    private void animateTextWriting(String text) {
        StyledDocument doc = answerCode.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        new SwingWorker<Void, String>() {
            
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= text.length(); i++) {
                    publish(text.substring(0, i));
                    Thread.sleep(15); // Adjust the speed of the animation here
                }
                return null;
            }

            
            protected void process(java.util.List<String> chunks) {
                String latest = chunks.get(chunks.size() - 1);
                try {
                    doc.remove(0, doc.getLength());
                    doc.insertString(0, latest, null);
                    highlightSyntax(latest);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    private void highlightSyntax(String text) {
        StyledDocument doc = answerCode.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        applyPattern(doc, text, "\\b(SELECT|FROM|WHERE|INSERT|INTO|VALUES|UPDATE|SET|DELETE|FROM|DECLARE|BEGIN|END|CREATE|TABLE|SYSTEM|SCOPE|ALTER|FETCH|DECLARE|CONNECT|GRANT|ON|DROP)\\b", "keyword");
        applyPattern(doc, text, "\\b\\d+\\b", "number");
        applyPattern(doc, text, "'[^']*'", "string");
    }
    
    private void applyPattern(StyledDocument doc, String text, String pattern, String style) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        while (m.find()) {
            doc.setCharacterAttributes(m.start(), m.end() - m.start(), doc.getStyle(style), true);
        }
    }

}
