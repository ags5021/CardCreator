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

	private String executionPath = System.getProperty("user.dir");
	private static MainForm _mainForm;

	private PreviewPanel panelPreview = new PreviewPanel();

	
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
		
				JButton btnNewButton = new JButton("New button");
				panelControls.add(btnNewButton);
				
						JButton btnNewButton_1 = new JButton("Send to new frame");
						panelControls.add(btnNewButton_1);
						btnNewButton_1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
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
							image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

							String path = executionPath + "/images";
							File dir = new File(path);
							if (!dir.exists()) {
								dir.mkdir();
							}
							ImageIO.write(image, "png", new File(path + "screenshot.png"));
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
		this.setSize(340, 401);
		this.setLocation(locX, locY);
	}

	public void SendImage(BufferedImage img, int height, int width) {
		Card card = new Card();
		card.AddFront(img, height, width);
		card.AddFront(img, height, width);
		
		panelPreview.RefreshView(card.getFront());
//		ImageIcon icon = new ImageIcon();
//		icon.setImage(img);
//		JOptionPane.showMessageDialog(null, icon);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new MainForm().setVisible(true);
	}
}
