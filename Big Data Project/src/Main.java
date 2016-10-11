import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.mysql.jdbc.Statement;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static JTabbedPane tabbedPane = new JTabbedPane();
	private JScrollPane scrollPaneMovie;
	private JScrollPane scrollPaneUser;

	private static JTable tableMovie;
	private static JTable tableUser;

	private JPanel userPanel;
	private JPanel moviePanel;

	private JPanel homePanel = new JPanel();
	private JPanel aboutPanel = new JPanel();

	private JLabel homeLabel = new JLabel("Welcome to Netflex Recommend System");
	private JLabel aboutLabel = new JLabel("About Us");

	private static DefaultTableModel movieTableModel;
	private static DefaultTableModel userTableModel;

	public Main() {

		initialize();
		initializeFrame();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new Main();

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", DataPosition.root,
					DataPosition.password);
			if (conn != null) {

				Statement myStatement = (Statement) conn.createStatement();

				ResultSet userData = myStatement.executeQuery("SELECT UserId, count(*) FROM RATING GROUP BY UserId");

				while (userData.next()) {
					userTableModel.addRow(
							new Object[] { userData.getString("userId"),
									userData.getString("count(*)"),
									"view" });
				}

				ResultSet movieData = myStatement.executeQuery("SELECT * FROM MOVIE");

				while (movieData.next()) {
					movieTableModel.addRow(new Object[] { 
							movieData.getString("Id"), 
							movieData.getString("movieYear"),
							movieData.getString("movieTitle") });
				}

				tableUser.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						int row = tableUser.rowAtPoint(evt.getPoint());
						int col = tableUser.columnAtPoint(evt.getPoint());
						String userId;
						if (col == 1) {
							userId = tableUser.getValueAt(row, col - 1).toString();
							UserRatingHistory rating = new UserRatingHistory(userId);
							rating.setVisible(true);

						} else if (col == 2) {
							userId = tableUser.getValueAt(row, col - 2).toString();
							UserRecommedList userRecommend = new UserRecommedList(userId);
							userRecommend.setVisible(true);
						}

					}
				});

				System.out.println("connected");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("not connected");
		}

	}

	private void initialize() {
		userPanel = new JPanel();
		userPanel.setLayout(new BorderLayout());
		getContentPane().add(userPanel);

		userTableModel = new DefaultTableModel();
		tableUser = new JTable(userTableModel);
		userTableModel.addColumn("ID");
		userTableModel.addColumn("RATING");
		userTableModel.addColumn("RECOMMAND");
		scrollPaneUser = new JScrollPane(tableUser);

		moviePanel = new JPanel();
		moviePanel.setLayout(new BorderLayout());
		getContentPane().add(moviePanel);

		movieTableModel = new DefaultTableModel();
		tableMovie = new JTable(movieTableModel);
		movieTableModel.addColumn("ID");
		movieTableModel.addColumn("PUBLISH YEAR");
		movieTableModel.addColumn("TITLE");
		scrollPaneMovie = new JScrollPane(tableMovie);

		homePanel.add(homeLabel);
		aboutPanel.add(aboutLabel);

		userPanel.add(scrollPaneUser, BorderLayout.CENTER);
		moviePanel.add(scrollPaneMovie, BorderLayout.CENTER);

		tabbedPane.add("HOME", homePanel);
		tabbedPane.add("USER", userPanel);
		tabbedPane.add("MOVIE", moviePanel);
		tabbedPane.add("ABOUT", aboutPanel);

		add(tabbedPane);
	}

	private void initializeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(DataPosition.netflexTile);
		pack();
		setSize(DataPosition.width, DataPosition.height);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

}
