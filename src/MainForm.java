import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

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
import javax.swing.JOptionPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	private JButton btnNewCard;
	private JButton btnSave;
	private JButton btnToggleView;
	private ImageSnip imageSnip;

	public MainForm() {
		_mainForm = this;
		setMinimumSize(new Dimension(350, 350));
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
		panelControls.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnNewCard = new JButton("New Card");
		panelControls.add(btnNewCard);
		
				btnToggleView = new JButton("Toggle View");
				btnToggleView.setEnabled(false);
				btnToggleView.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ToggleView();
					}
				});
				panelControls.add(btnToggleView);
		
				lblView = new JLabel("Front");
				lblView.setFont(new Font("Tahoma", Font.PLAIN, 14));
				panelControls.add(lblView);

		btnSave = new JButton("SaveCard");
		btnSave.setEnabled(false);
		panelControls.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = executionPath + "/images/";
				
				//String filePath = executionPath + "/AnkiList.txt";
				
				// should have used string builder but we shall save that for a refactor
				try(PrintWriter fileWrite = new PrintWriter(new BufferedWriter(new FileWriter("AnkiList.txt", true)))) {

			
				// card image format is "CardCreator_CardID_<Front/Back>_ImageOrderNum_RANDOMHASH"  
				// we get the front card and save each image
				int i = 0;
				for (ImageItem img: CardManager.getCard().getFront().getImageItems())
				{
					//we just write image
					String imageName = "CardCreator_"+CardManager.getCurrentCardID()+"_FRONT_" + Integer.toString(i) + "_"+ getSaltString(5) + ".png";
					fileWrite.print("<img src='" + imageName + "'/>   ");
					File file = new File(path + imageName);
					try {
						ImageIO.write(img.get_image(), "png", file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
				fileWrite.print("\t");
				i = 0;
				for (ImageItem img: CardManager.getCard().getBack().getImageItems())
				{
					String imageName = "CardCreator_"+CardManager.getCurrentCardID()+"_BACK_"+Integer.toString(i) + "_"+ getSaltString(5) + ".png";
					fileWrite.print("<img src='" + imageName + "'/>  ");
					File file = new File(path + imageName);
					try {
						ImageIO.write(img.get_image(), "png", file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
				fileWrite.print("\n");			
				}catch (IOException e) {
				    //exception handling left as an exercise for the reader
				}
				DisplayNew();
				frontview = true;
				lblView.setText("Front");
				panelPreview.setVisible(false);
				imageSnip.dispatchEvent(new WindowEvent(imageSnip, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnNewCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BufferedImage image;
				try {
					setExtendedState(JFrame.ICONIFIED); // hide while taking the
														// screenshot!!
					image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					setExtendedState(JFrame.NORMAL); // now show yo self
					String path = executionPath + "/images/";
					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdir();
					}
					File file = new File(path + "ACardCreatorscreenshot.png");
					ImageIO.write(image, "png", file);
					CardManager.CreateCard(); // we add a new card
					imageSnip = new ImageSnip(file.getPath(),_mainForm);
					imageSnip.setVisible(true);
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
				DisplaySaveToggle();
				panelPreview.RefreshView(CardManager.getCard().getFront());	
				panelPreview.setVisible(true);
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

		JMenuItem mntmSetCardNumber = new JMenuItem("Set Card Number");
		mntmSetCardNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					CardManager.setCurrentCardID(
							Integer.parseInt(JOptionPane.showInputDialog(_mainForm, "Enter the Current card number")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmSetCardNumber);

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (frontview) {
					CardManager.getCard().UndoFront();
					panelPreview.RefreshView(CardManager.getCard().getFront());
				} else {
					CardManager.getCard().UndoBack();
					panelPreview.RefreshView(CardManager.getCard().getBack());
				}
			}
		});
		mnEdit.add(mntmUndo);

		JMenu mnExport = new JMenu("Export");
		menuBar.add(mnExport);

		JMenuItem mntmExportToAnki = new JMenuItem("Export to Anki");
		mnExport.add(mntmExportToAnki);

		File configFile = new File("config.properties");

		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			sizeX = Integer.parseInt(props.getProperty(WINDOW_SIZE_X));
			sizeY = Integer.parseInt(props.getProperty(WINDOW_SIZE_Y));
			locX = Integer.parseInt(props.getProperty(WINDOW_LOCATION_X));
			locY = Integer.parseInt(props.getProperty(WINDOW_LOCATION_Y));
			CardManager.setCurrentCardID(Integer.parseInt(props.getProperty(CURRENT_CARD_ID)));
			reader.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
		// this.setSize(sizeX, sizeY);
		this.setSize(sizeX, sizeY);
		this.setLocation(locX, locY);
	}

	public void SendImage(BufferedImage img, int height, int width) {
		if (frontview) {
			CardManager.getCard().AddFront(img, height, width);
			panelPreview.RefreshView(CardManager.getCard().getFront());
		} else {
			CardManager.getCard().AddBack(img, height, width);
			panelPreview.RefreshView(CardManager.getCard().getBack());
		}

	}

	private void RefreshView() {
		if (frontview) {
			panelPreview.RefreshView(CardManager.getCard().getFront());
		} else {

			panelPreview.RefreshView(CardManager.getCard().getBack());
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

	private void DisplayNew() {
		btnNewCard.setEnabled(true);
		btnSave.setEnabled(false);
		btnToggleView.setEnabled(false);
	}

	private void DisplaySaveToggle() {
		btnNewCard.setEnabled(false);
		btnSave.setEnabled(true);
		btnToggleView.setEnabled(true);
	}
	
	protected String getSaltString(int num) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < num) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
