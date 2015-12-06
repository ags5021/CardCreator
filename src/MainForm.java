import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;

public class MainForm extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6969503544294911223L;
	private int sizeX = 300;
	private int sizeY = 300;
	private int locX = 300;
	private int locY = 300;

	private final String WINDOW_SIZE_X = "windowSizeX";
	private final String WINDOW_SIZE_Y = "windowSizeY";
	private final String WINDOW_LOCATION_X = "windowLocationX";
	private final String WINDOW_LOCATION_Y = "windowLocationY";
	private final String CURRENT_CARD_ID = "currentCardID";

	private String executionPath = System.getProperty("user.dir");
	private static MainForm _mainForm;

	private boolean frontview = true;

	private PreviewPanel panelPreview = new PreviewPanel();
	private JLabel lblView;

	public MainForm() {
		_mainForm = this;
		setMinimumSize(new Dimension(50, 50));
		setAlwaysOnTop(true);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				File configFile = new File("config.properties");

				try {
					Properties props = new Properties();

					props.setProperty(WINDOW_SIZE_X, Integer.toString(getWidth()));
					props.setProperty(WINDOW_SIZE_Y, Integer.toString(getHeight()));
					props.setProperty(WINDOW_LOCATION_X, Integer.toString((int) getLocation().getX()));
					props.setProperty(WINDOW_LOCATION_Y, Integer.toString((int) getLocation().getY()));
					props.setProperty(CURRENT_CARD_ID, Integer.toString((int) CardManager.getCurrentCardID()));

					FileWriter writer = new FileWriter(configFile);
					props.store(writer, "application settings");
					writer.close();
				} catch (FileNotFoundException ex) {
					// file does not exist
				} catch (IOException ex) {
					// I/O error
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Card Creator");

		JPanel panelControls = new JPanel();
		getContentPane().add(panelControls, BorderLayout.NORTH);

		JButton btnNewButton = new JButton("New Card");
		panelControls.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("SaveCard");
		panelControls.add(btnNewButton_1);

		JButton btnToggleView = new JButton("Toggle View");
		btnToggleView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToggleView();
			}
		});
		panelControls.add(btnToggleView);

		lblView = new JLabel("Front");
		lblView.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelControls.add(lblView);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardManager.AddCard(); // we add a new card
				ImageSnip imageSnip = new ImageSnip(
						"D:/Projects/Arabic/FrequencyDictParser/workspace/CardCreator/images/screenshot.png",
						_mainForm);
				imageSnip.setVisible(true);
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				BufferedImage image;
				try {
					setExtendedState(JFrame.ICONIFIED);  //hide while taking the screenshot!!
		
					image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					setExtendedState(JFrame.NORMAL);  //now show yo self
					String path = executionPath + "/images/";
					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdir();
					}
					ImageIO.write(image, "png", new File(path + "screenshot2.png"));
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		getContentPane().add(panelPreview, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mnEdit.add(mntmUndo);

		File configFile = new File("config.properties");

		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			sizeX = Integer.parseInt(props.getProperty(WINDOW_SIZE_X));
			sizeY = Integer.parseInt(props.getProperty(WINDOW_SIZE_Y));
			locX = Integer.parseInt(props.getProperty(WINDOW_LOCATION_X));
			locY = Integer.parseInt(props.getProperty(WINDOW_LOCATION_Y));
			reader.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
		// this.setSize(sizeX, sizeY);
		this.setSize(356, 401);
		this.setLocation(locX, locY);
	}

	public void SendImage(BufferedImage img, int height, int width) {
		if (frontview) {
			CardManager.getCurrentCard().AddFront(img, height, width);
			panelPreview.RefreshView(CardManager.getCurrentCard().getFront());
		} else {
			CardManager.getCurrentCard().AddBack(img, height, width);
			panelPreview.RefreshView(CardManager.getCurrentCard().getBack());
		}

	}

	private void RefreshView() {
		if (frontview) {
			panelPreview.RefreshView(CardManager.getCurrentCard().getFront());
		} else {

			panelPreview.RefreshView(CardManager.getCurrentCard().getBack());
		}
	}

	private void ToggleView() {

		this.frontview = !this.frontview; // toggle that flag

		if (this.frontview) {
			this.lblView.setText("Front");
		} else {
			this.lblView.setText("Back");
		}
		RefreshView();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new MainForm().setVisible(true);
	}
}
