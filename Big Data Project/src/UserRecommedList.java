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

public class UserRecommedList extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPaneUser;
	private static JTable tableUserRecommend;
	private JPanel userRecommendPanel;
	private static DefaultTableModel userRecommendModel;

	public UserRecommedList(String userId) {

		initialize();
		initializeFrame();
		loadForm(userId);

	}

	private void loadForm(String userId) {
		// TODO Auto-generated method stub

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", DataPosition.root,
					DataPosition.password);

			if (conn != null) {

				String query = "SELECT RECOMMEND.Id, RECOMMEND.UserId, MOVIE.movieTitle, RECOMMEND.IndexValue "
						+ "FROM RECOMMEND "
						+ "INNER JOIN MOVIE "
						+ "ON RECOMMEND.MovieId = MOVIE.Id "
						+ "WHERE RECOMMEND.UserId = '" + userId +"' ORDER BY IndexValue desc limit 10;";
				
//				System.out.println(query);
				Statement myStatement = (Statement) conn.createStatement();

				ResultSet userRecommendData = myStatement.executeQuery(query);

				while (userRecommendData.next()) {

					userRecommendModel.addRow(new Object[] { 
							userRecommendData.getString("Id"),
							userRecommendData.getString("UserId"), 
							userRecommendData.getString("movieTitle"),
							userRecommendData.getString("IndexValue") });
				}

				System.out.println("connected");
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("not connected");
		}
	}

	private void initializeFrame() {
		// TODO Auto-generated method stub
		setTitle("User Recommend List");
		pack();
		setSize(DataPosition.width, DataPosition.height);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

	private void initialize() {
		// TODO Auto-generated method stub
		userRecommendPanel = new JPanel();
		userRecommendPanel.setLayout(new BorderLayout());
		getContentPane().add(userRecommendPanel);

		userRecommendModel = new DefaultTableModel();
		tableUserRecommend = new JTable(userRecommendModel);
		userRecommendModel.addColumn("ID");
		userRecommendModel.addColumn("USER ID");
		userRecommendModel.addColumn("MOVIE TITLE");
		userRecommendModel.addColumn("VALUE INDEX");

		scrollPaneUser = new JScrollPane(tableUserRecommend);

		userRecommendPanel.add(scrollPaneUser, BorderLayout.CENTER);

		add(userRecommendPanel);

	}

}
