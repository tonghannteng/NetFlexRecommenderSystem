import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Statement;

public class UserRatingHistory extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPaneUserRating;
	private static JTable tableUserRating;

	private JPanel userRatingPanel;

	private static DefaultTableModel userRatingTableModel;

	public UserRatingHistory(String userId) {
		initialize();
		initializeFrame();
		loadForm(userId);
	}

	public void loadForm(String userId) {

		// System.out.println(col);

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", DataPosition.root,
					DataPosition.password);

			if (conn != null) {
				
				String query = "SELECT RATING.Id, RATING.UserId, MOVIE.movieTitle, RATING.Rating, RATING.RatingDate "
						+ "FROM RATING "
						+ "INNER JOIN MOVIE ON RATING.movieId = MOVIE.Id "
						+ "WHERE RATING.UserId = '" + userId +"' ORDER BY Rating desc;";
//				System.out.println(query);
				Statement myStatement = (Statement) conn.createStatement();

				ResultSet userRatingData = myStatement.executeQuery(query);

				while (userRatingData.next()) {

					userRatingTableModel.addRow(new Object[] { 
							userRatingData.getString("Id"),
							userRatingData.getString("UserId"), 
							userRatingData.getString("MovieTitle"),
							userRatingData.getString("Rating"), 
							userRatingData.getString("RatingDate") });
				}

				System.out.println("connected");
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("not connected");
		}
	}

	private void initialize() {

		userRatingPanel = new JPanel();
		userRatingPanel.setLayout(new BorderLayout());
		getContentPane().add(userRatingPanel);

		userRatingTableModel = new DefaultTableModel();
		tableUserRating = new JTable(userRatingTableModel);
		userRatingTableModel.addColumn("ID");
		userRatingTableModel.addColumn("USER ID");
		userRatingTableModel.addColumn("MOVIE TITLE");
		userRatingTableModel.addColumn("RATING");
		userRatingTableModel.addColumn("RATING DATE");
		scrollPaneUserRating = new JScrollPane(tableUserRating);

		userRatingPanel.add(scrollPaneUserRating, BorderLayout.CENTER);

		add(userRatingPanel);

	}

	private void initializeFrame() {

		setTitle("User Rating History");
		pack();
		setSize(DataPosition.width, DataPosition.height);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

}
