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
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

	private enum VIEW {FRONT, BACK, ALL};
	private VIEW _view = VIEW.FRONT;
	
	private PreviewPanel panelBack = new PreviewPanel();
	private PreviewPanel panelAll = new PreviewPanel();
	
	private PreviewPanel panelFront = new PreviewPanel();
	private JButton btnNewCard;
	private JButton btnSave;
	private ImageSnip imageSnip;
	private JTextField textFieldNumber;
	
	JButton btnBack = new JButton("Back");
	JButton btnFront = new JButton("Front");
	JButton btnAll = new JButton("All");

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
		getContentPane().setLayout(null);

		JPanel panelControls = new JPanel();
		panelControls.setBounds(0, 0, 334, 33);
		getContentPane().add(panelControls);
		panelControls.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		textFieldNumber = new JTextField();
		textFieldNumber.setEditable(false);
		textFieldNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardManager.setCurrentCardID(Integer.parseInt(textFieldNumber.getText()));
				textFieldNumber.setText(CardManager.getCurrentCardID() + ""); // lazy
			}
		});
		textFieldNumber.setPreferredSize(new Dimension(5, 20));
		textFieldNumber.setText("0");
		textFieldNumber.setSize(new Dimension(5, 20));
		textFieldNumber.setMaximumSize(new Dimension(4, 20));
		panelControls.add(textFieldNumber);
		textFieldNumber.setColumns(4);

		btnNewCard = new JButton("New Card");
		panelControls.add(btnNewCard);

		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_view = VIEW.ALL;
				btnAll.setBackground(Color.GREEN);
				btnFront.setBackground(Color.getColor("#f0f0f0"));
				btnBack.setBackground(Color.getColor("#f0f0f0"));
			}
		});
		btnSave = new JButton("SaveCard");
		btnSave.setEnabled(false);
		panelControls.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = executionPath + "/images/";
				
				// should have used string builder but we shall save that for a refactor
				try(PrintWriter fileWrite = new PrintWriter(new BufferedWriter(new FileWriter("AnkiList.txt", true)))) {

				fileWrite.print(CardManager.getCurrentCardID());
				fileWrite.print("\t");
				// card image format is "CardCreator_CardID_<Front/Back>_ImageOrderNum_RANDOMHASH"  
				// we get the front card and save each image
				int i = 0;
				for (ImageItem img: CardManager.getCard().getFront().getImageItems())
				{
					//we just write image
					String imageName = "CardCreator_"+CardManager.getCurrentCardID()+"_FRONT_" + Integer.toString(i) + "_"+ getSaltString(5) + ".png";
					fileWrite.print("<img src='" + imageName + "'/>\n");
					File file = new File(path + imageName);
					try {
						ImageIO.write(img.get_image(), "png", file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
				// --------------------- BACK ------------------------
				fileWrite.print("\t");
				i = 0;
				for (ImageItem img: CardManager.getCard().getBack().getImageItems())
				{
					String imageName = "CardCreator_"+CardManager.getCurrentCardID()+"_BACK_"+Integer.toString(i) + "_"+ getSaltString(5) + ".png";
					fileWrite.print("<img src='" + imageName + "'/>\n");
					File file = new File(path + imageName);
					try {
						ImageIO.write(img.get_image(), "png", file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
				// --------------------- ALL ------------------------
				fileWrite.print("\t");
				i = 0;
				for (ImageItem img: CardManager.getCard().getAll().getImageItems())
				{
					String imageName = "CardCreator_"+CardManager.getCurrentCardID()+"_ALL_"+Integer.toString(i) + "_"+ getSaltString(5) + ".png";
					fileWrite.print("<img src='" + imageName + "'/>\n");
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
				panelFront.setVisible(false);
				panelBack.setVisible(false);
				panelAll.setVisible(false);
				DisplayNew();
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
					textFieldNumber.setText(CardManager.getCurrentCardID() + ""); // lazy
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
				RefreshView();
				panelFront.setVisible(true);
				panelBack.setVisible(true);
				panelAll.setVisible(true);
				btnFront.doClick(); 
			}
		});

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_view = VIEW.BACK;
				btnBack.setBackground(Color.GREEN);
				btnFront.setBackground(Color.getColor("#f0f0f0"));
				btnAll.setBackground(Color.getColor("#f0f0f0"));
			}
		});
		btnBack.setBounds(107, 227, 89, 23);
		getContentPane().add(btnBack);
		

		panelBack.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelBack.setBounds(0, 252, 343, 172);
		getContentPane().add(panelBack);
		

		btnAll.setBounds(107, 426, 89, 23);
		getContentPane().add(btnAll);

		panelAll.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelAll.setBounds(0, 449, 343, 200);
		getContentPane().add(panelAll);
		

		btnFront.setBounds(117, 34, 67, 23);
		getContentPane().add(btnFront);
		panelFront.setBounds(0, 57, 343, 167);
		getContentPane().add(panelFront);
		panelFront.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_view = VIEW.FRONT;
				btnFront.setBackground(Color.GREEN);
				btnBack.setBackground(Color.getColor("#f0f0f0"));
				btnAll.setBackground(Color.getColor("#f0f0f0"));
			}
		});

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
					textFieldNumber.setText(CardManager.getCurrentCardID() + ""); // lazy
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmSetCardNumber);

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_view == VIEW.FRONT) {
					CardManager.getCard().UndoFront();
					panelFront.RefreshView(CardManager.getCard().getFront());
				} else if (_view == VIEW.BACK) {
					CardManager.getCard().UndoBack();
					panelBack.RefreshView(CardManager.getCard().getBack());
				} else if (_view == VIEW.ALL) {
					CardManager.getCard().UndoAll();
					panelAll.RefreshView(CardManager.getCard().getAll());
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
			textFieldNumber.setText(CardManager.getCurrentCardID() + "");
			
			reader.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
		// this.setSize(sizeX, sizeY);
		this.setSize(369, 709);
		this.setLocation(locX, locY);
		
	}	
	public void ChangeActiveButton(int key)
	{	
		  if ((key == KeyEvent.VK_1))
		  {
			 btnFront.doClick(); 
		  }
		  else if ((key == KeyEvent.VK_2))
		  {
			 btnBack.doClick(); 
		  }
		  else if ((key == KeyEvent.VK_3))
		  {
			 btnAll.doClick(); 
		  }
	}
	
	public void SendImage(BufferedImage img, int height, int width) {
		if (_view == VIEW.FRONT) {
			CardManager.getCard().AddFront(img, height, width);
			panelFront.RefreshView(CardManager.getCard().getFront());
		} else if (_view == VIEW.BACK) {
			CardManager.getCard().AddBack(img, height, width);
			panelBack.RefreshView(CardManager.getCard().getBack());
		} else if (_view == VIEW.ALL) {
			CardManager.getCard().AddAll(img, height, width);
			panelAll.RefreshView(CardManager.getCard().getAll());
		}

	}

	private void RefreshView() {
		panelFront.RefreshView(CardManager.getCard().getFront());
		panelBack.RefreshView(CardManager.getCard().getBack());
		panelAll.RefreshView(CardManager.getCard().getAll());
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
	}

	private void DisplaySaveToggle() {
		btnNewCard.setEnabled(false);
		btnSave.setEnabled(true);
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
