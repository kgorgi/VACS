//***********************************************************
//* VACS.java Created By Kian Gorgichuk                     *
//* Copyright (c) 2014 Kian Gorgichuk. All rights reserved. *
//***********************************************************

//Import Libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities.*;
import javax.swing.event.*;
import javax.swing.BorderFactory.*; 
import javax.swing.border.Border.*;
import javax.swing.border.TitledBorder.*;
import javax.swing.border.EtchedBorder.*;
import javax.swing.ButtonGroup.*;
import java.lang.Exception.*;
import java.lang.reflect.*;

public class VACS
{
	//JFrame
	private JFrame _vCalculatorFrame = null;
	private int _frameWidth = 605, _frameHeight = 310;
	
	//Menu Bar Components
	private JMenuBar _menuBar = null;
	private JMenu _infoMenu = null;
	private JMenuItem _aboutItem = null;
	private JMenuItem _helpItem = null;
	
	//JFrame Components
	//Right Side
	private JLabel _amountNumText = null;
	private JSlider _amountSlider = null;
	private JButton _selectButton = null;
	
	//Left Side
	private JLabel _inputNumText = null;
	private JTextField _magTextField = null;
	private JTextField _dirTextField = null;
	private JRadioButton _btnRadians = null;
	private JRadioButton _btnDegrees = null;
	private JButton _enterButton = null;
	private FlowLayout _customBtnLayout = null;
	private JPanel _buttonsPanel = null;

	//Calculating Variables
	private VectorAddition _addVectorCalc = null;
	private int _currVectorIndex = 0;
	private int _maxIndex = 0;
	private boolean _exceptionThrown = false;
	
	//OS Variable
	boolean _isMacOS;
	
	public VACS()
	{
		//Get System
		String osName = System.getProperty("os.name").toLowerCase();
		_isMacOS = osName.contains("os x");
		
		//Setup JFrame
		_vCalculatorFrame = new JFrame("Vector Addition");
		_vCalculatorFrame.setLayout(null);
		_vCalculatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_vCalculatorFrame.setResizable(false);
		_vCalculatorFrame.setLocationRelativeTo(null);
		
		//Set Size depending on system
		if(_isMacOS)
			_vCalculatorFrame.setSize(_frameWidth - 5, _frameHeight - 38);
		else 
			_vCalculatorFrame.setSize(_frameWidth, _frameHeight);

		//Setup Image Icon - Windows Only
		ImageIcon img = new ImageIcon(getClass().getResource("src/icon.png"));
		_vCalculatorFrame.setIconImage(img.getImage());
		
		//Set Background Color
		Color backgroundColor = new Color(239,243,249);
		_vCalculatorFrame.getContentPane().setBackground(backgroundColor);
		
		buildUserInterface();
	}
	
	private void buildUserInterface()
	{
		//Create Menu Bar - depending on system
		_menuBar = new JMenuBar();
		if(_isMacOS) //Mac OS X
		{
			_infoMenu = new JMenu("Information");
			_helpItem = new JMenuItem("Help");
			_infoMenu.add(_helpItem);
			_menuBar.add(_infoMenu);
		}
		else //Other Systems
		{
			_infoMenu = new JMenu("Information");
			_aboutItem = new JMenuItem("About");
			_helpItem = new JMenuItem("Help");
			_infoMenu.add(_aboutItem);
			_infoMenu.add(_helpItem);
			_menuBar.add(_infoMenu);
		}
		_vCalculatorFrame.setJMenuBar(_menuBar);
		
		//Set UI Interface Theme
		try{ UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); }
		catch(Exception ex) 
		{ 
			JOptionPane.showMessageDialog(null, ex.toString(), "Error!",  JOptionPane.ERROR_MESSAGE);
			System.err.println(ex.toString()); 
		} //Show Pop-up Alert and Print Out Error to Console
		
		//TOP
		JLabel titleText = new JLabel ("<html> <b>VACS! The Vector Addition Computation System!</b></html>");
		titleText.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setBorder(BorderFactory.createRaisedBevelBorder());
		titlePanel.add(titleText);
		titlePanel.setBounds(10, 10, _frameWidth - 25,40);
		
		//LEFT SIDE
		//Title Text for Selecting Amount
		JLabel amountTitleText = new JLabel ("<html><div style=\"text-align: center;\">" + "Select the Amount of<br>Vectors to Add:" + "</html>");
		amountTitleText.setFont(new Font("Arial", Font.PLAIN, 18));
		
		JPanel amountTextPanel = new JPanel();
		amountTextPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		amountTextPanel.add(amountTitleText);
		amountTextPanel.setBounds(5, 5, 230 , 55);
		
		//Display the Number of Vectors Selected
		_amountNumText = new JLabel ("2");
		_amountNumText.setFont(new Font("Arial", Font.PLAIN,18));
		
		JPanel amountNumPanel = new JPanel();
		amountNumPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		amountNumPanel.add(_amountNumText);
		amountNumPanel.setBounds(5, 55, 230 , 30);
		
		//Slider for Number of Vectors
		_amountSlider = new JSlider(JSlider.HORIZONTAL, 2, 50, 2);
		_amountSlider.setMajorTickSpacing(12);
		_amountSlider.setMinorTickSpacing(6);
		_amountSlider.setPaintTicks(true);
		_amountSlider.setPaintLabels(true);
		_amountSlider.setFont(new Font("Arial", Font.ITALIC, 15));
		
		//Button To Lock Slider
		_selectButton = new JButton("Select");
		_selectButton.setFont(new Font("Arial", Font.PLAIN,16));
		
		JPanel amountSliderPanel = new JPanel();
		FlowLayout customLayout  = new FlowLayout(FlowLayout.CENTER);
		customLayout.setVgap(6);
		amountSliderPanel.setLayout(customLayout);
		amountSliderPanel.add(_amountSlider);
		amountSliderPanel.add(_selectButton);
		amountSliderPanel.setBounds(5, 85, 230 , 90);
		
			
		//Amount Super Panel Handling Nested Panels
		JPanel amountPanel = new JPanel();
		amountPanel.setLayout(null);
		amountPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		amountPanel.add(amountTextPanel);
		amountPanel.add(amountNumPanel);
		amountPanel.add(amountSliderPanel);
		amountPanel.setBounds(10, 60, 240 , 180);
		
		//RIGHT SIDE
		//Title Text for Vector Input
		JLabel inputTitleText = new JLabel ("Enter the Magnitude and Direction");
		inputTitleText.setFont(new Font("Arial", Font.PLAIN, 18));
		
		_inputNumText = new JLabel ("for Vector 1:");
		_inputNumText.setFont(new Font("Arial", Font.PLAIN, 18));
		
		JPanel inputTextPanel = new JPanel();
		inputTextPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		inputTextPanel.add(inputTitleText);
		inputTextPanel.add(_inputNumText);
		inputTextPanel.setBounds(5, 5, 320 , 55);
		
		//Input Magnitude
		JLabel magLabel = new JLabel("Magnitude:");
		magLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		
		_magTextField = new JTextField();
		_magTextField.setFont(new Font("Arial", Font.PLAIN, 18));
		_magTextField.setHorizontalAlignment(JTextField.LEFT);
		_magTextField.setText("");
		_magTextField.setPreferredSize(new Dimension(215, 25));
		
		JPanel magnitudePanel = new JPanel();
		magnitudePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		magnitudePanel.add(magLabel);
		magnitudePanel.add(_magTextField);
		magnitudePanel.setBounds(5, 62, 320 , 35);
		
		//Input Direction
		JLabel dirLabel = new JLabel("Direction:");
		dirLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		
		_dirTextField = new JTextField();
		_dirTextField.setFont(new Font("Arial", Font.PLAIN, 18));
		_dirTextField.setHorizontalAlignment(JTextField.LEFT);
		_dirTextField.setText("");
		_dirTextField.setPreferredSize(new Dimension(228, 25));
		
		JPanel directionPanel = new JPanel();
		directionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		directionPanel.add(dirLabel);
		directionPanel.add(_dirTextField);
		directionPanel.setBounds(5, 100, 320 , 35);
		
		//Mode Label
		JLabel modeLabel = new JLabel("Mode:");
		modeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		modePanel.add(modeLabel);
		modePanel.setBounds(10, 139, 60 , 36);
		
		//Bottom Buttons
		ButtonGroup radioButtons = new ButtonGroup();
		
		_btnRadians = new JRadioButton("Radians");
		
		_btnDegrees = new JRadioButton("Degrees");
		_btnDegrees.setSelected(true);
		
		radioButtons.add(_btnRadians);
		radioButtons.add(_btnDegrees);
		
		_enterButton = new JButton("Enter");
		_enterButton.setFont(new Font("Arial", Font.PLAIN,16));
		
		_buttonsPanel = new JPanel();
		_customBtnLayout = new FlowLayout(FlowLayout.CENTER);
		_customBtnLayout.setHgap(15);
		
		//Mac OS HGap
		if(_isMacOS)
			_customBtnLayout.setHgap(12);
		_buttonsPanel.setLayout(_customBtnLayout);
		_buttonsPanel.add(_btnRadians);
		_buttonsPanel.add(_btnDegrees);
		_buttonsPanel.add(_enterButton);
		_buttonsPanel.setBounds(66, 136, 260 , 39);
			
		//Vector Input Super Panel - Handling Nested Panels
		JPanel vectorPanel = new JPanel();
		vectorPanel.setLayout(null);
		vectorPanel.add(inputTextPanel);
		vectorPanel.add(magnitudePanel);
		vectorPanel.add(directionPanel);
		vectorPanel.add(modePanel);
		vectorPanel.add(_buttonsPanel);
		vectorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		vectorPanel.setBounds(260, 60, 330 , 180);
		
		//Adding Super Panels to JFrame
		_vCalculatorFrame.add(titlePanel);
		_vCalculatorFrame.add(amountPanel);
		_vCalculatorFrame.add(vectorPanel);
		initalizeJComponents();
	}
	
	private void initalizeJComponents()
	{
		
		switchSidePanel(false);
		//Wire Up JSlider
		_amountSlider.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				JSlider source = (JSlider)e.getSource();
				_amountNumText.setText(Integer.toString((int)source.getValue()));
			}
		});
		//Wire Up Select Button
		_selectButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent a)
			{
				//Switch to Right Panel
				switchSidePanel(true);
				
				//Set MaxIndex
				_maxIndex = Integer.parseInt(_amountNumText.getText());
				//Create new VectorAddition Instance (vectorAddition table)
				_addVectorCalc = new VectorAddition(_maxIndex);
			}
		});
		//Wire Up Enter/Calculate Button
		_enterButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent a)
			{
				//Enter Last Vector & Calculate Answer 
				if(_maxIndex - 1 == _currVectorIndex)
				{
					//Enter Last Vector
					enterVector();
					//Exception Handling
					if(_exceptionThrown == true)
					{
						_exceptionThrown = false;
						return;
					}
					//Calculate Answer 
					Vector answer = _addVectorCalc.calculateAnswer();
					String answerDeg = "The Answer is: " + Vector.getStringDeg(answer);
					String answerRad = "The Answer is: " + Vector.getStringRads(answer);
					Object[] btnDiaglogDeg = {"Radians", "OK" };
					Object[] btnDiaglogRad = {"Degrees", "OK" };
					while(true)
					{
						int n = JOptionPane.showOptionDialog(_vCalculatorFrame, answerDeg, "Resultant Vector", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, btnDiaglogDeg, btnDiaglogDeg[1]);
						if(n == JOptionPane.YES_OPTION)
						{
							int j = JOptionPane.showOptionDialog(_vCalculatorFrame, answerRad, "Resultant Vector", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, btnDiaglogRad, btnDiaglogRad[1]);
							if(j == JOptionPane.YES_OPTION)
								continue;
							else
								break;
						}
						else
							break;
					}
					
					//NULL Values 
					_addVectorCalc = null;
					_currVectorIndex = 0;
					_maxIndex = 0;
					
					//Reset GUI
					switchSidePanel(false);
					_amountNumText.setText("2");
					_amountSlider.setValue(2);
					_inputNumText.setText("for Vector 1:");
					//Clear TextFields & Set Focus
					_magTextField.setText("");
					_dirTextField.setText("");
					_magTextField.requestFocusInWindow();
					
					//Change Enter Button To Enter Button
					_enterButton.setText("Enter");
					_customBtnLayout.setHgap(15);
					if(_isMacOS)
						_customBtnLayout.setHgap(12);
					
				}
				//Enter Vector & Change Enter Button to Calculate Button
				else if(_maxIndex - 2 == _currVectorIndex)
				{
					//Enter Vector
					enterVector();
					//Exception Handling
					if(_exceptionThrown == true)
					{
						_exceptionThrown = false;
						return;
					}
					//Increase Current Vector Index
					_currVectorIndex++;
					
					//Change Current Vector for Input
					StringBuilder currVectText  = new StringBuilder();
		
					currVectText.append("for Vector ");
					currVectText.append(Integer.toString(_currVectorIndex + 1));
					currVectText.append(":");
					
					_inputNumText.setText(currVectText.toString());
					
					//Clear TextFields & Set Focus
					_magTextField.setText("");
					_dirTextField.setText("");
					_magTextField.requestFocusInWindow();
					
					//Change Enter Button To Calculate Button
					_enterButton.setText("Calculate");
					_customBtnLayout.setHgap(3);
					if(_isMacOS)
						_customBtnLayout.setHgap(0);
				}
				//Add Vector
				else
				{
					//Enter Vector
					enterVector();
					//Exception Handling
					if(_exceptionThrown == true)
					{
						_exceptionThrown = false;
						return;
					}
					//Increase Current Vector Index
					_currVectorIndex++;
					
					//Change Current Vector for Input
					StringBuilder currVectText  = new StringBuilder();
		
					currVectText.append("for Vector ");
					currVectText.append(Integer.toString(_currVectorIndex + 1));
					currVectText.append(":");
					
					_inputNumText.setText(currVectText.toString());
					
					//Clear TextFields & Set Focus
					_magTextField.setText("");
					_dirTextField.setText("");
					_magTextField.requestFocusInWindow();

				}
			}
		});
		
		//Wire Up About Item Depending on System.
		if(_isMacOS) //Mac Specific
		{
        	try 
        	{
				//Must be done using reflection otherwise will crash on and not compile other systems
				// See http://stackoverflow.com/questions/7256230/in-order-to-macify-a-java-app-to-catch-the-about-event-do-i-have-to-implement for more details
				//Pull Application Class
				Object thisApp = Class.forName("com.apple.eawt.Application").getMethod("getApplication",(Class[]) null).invoke(null, (Object[]) null);

				//Create a Instance of About Listener (Using Proxies)
				Object handleInstance = Proxy.newProxyInstance(Class.forName("com.apple.eawt.AboutHandler").getClassLoader(), new Class[] { Class.forName("com.apple.eawt.AboutHandler") }, new AboutListener());
		
				//Set Instance to Application Class
				thisApp.getClass().getMethod("setAboutHandler", new Class[] { Class.forName("com.apple.eawt.AboutHandler") }).invoke(thisApp, new Object[] { handleInstance });
       		}
       		catch(Exception ex) 
			{ 
				JOptionPane.showMessageDialog(null, ex.toString(), "Error!",  JOptionPane.ERROR_MESSAGE);
				System.err.println(ex.toString()); 
			} //Show Pop-up Alert and Print Out Error to Console
    	}

		else //other systems
		{
			_aboutItem.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent a)
				{	
					//Create Text Information for Dialog
					String info = "<html><div style=\"text-align: center;\">" + 
								  "<font size=\"5\"><b>The Vector Addition Computation System</b></font><br><br>" +
								  "Version: 1.2.5<br><br>" +
								  "<b>Design, Programming, and Testing</b><br>" +
								  "<b>Done By Kian Gorgichuk</b><br><br>" +
								  "<b>Special Thanks to Ms. Bater!</b><br><br>" +
								  "Copyright (c) 2014 Kian Gorgichuk.<br>" + 
								  "All rights reserved.<br>" +
								  "</html>";
					JLabel aboutLabel = new JLabel(info);
					aboutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
					//Create Panel for Dialog
					JPanel aboutPanel = new JPanel();
					aboutPanel.add(aboutLabel);
					//Create Buttons for Message Dialog
					String[] aboutBtn = {"OK"};
					//Display Message
					JOptionPane.showOptionDialog(null, aboutPanel, "About", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, aboutBtn, aboutBtn[0]);
				}
			});
		}
		
		
		//Wire Up Help Item
		_helpItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent a)
			{	
				//Create Text Information for Dialog
				String info = "<html>" + 
							  "<b>Directions:</b><br>" +
							  "1. Use Slider on the Left to Select the Amount of Vectors to Add.<br>" + 
							  "2. Press Select to Confirm Amount of Vectors.<br>" +
							  "3. Enter the Magnitude:<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Must be positive.<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Cannot be Zero.<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Scientific Notation is Supported. (ex: 12e5 or -12e-5)<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Other Characters are Not Supported. (ex: abc#@!) <br>" +
							  "4. Enter the Direction:<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Use the Buttons to Select the Mode in Degrees or Radians.<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Scientific Notation is Supported. (ex: 12e5 or -12e-5)<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Other Characters are Not Supported. (ex: abc#@!) <br>" +
							  "5. Press Enter and Repeat Steps 3 & 4 Until the Calculate Button Appears.<br>" +
							  "6. Press Calculate to Determine Final Answer.<br>" +
							  "7. Use the Buttons in the Popup Window to Select if the<br>" +
							  "&nbsp;&nbsp;&nbsp;&nbsp;Direction is to be Expressed in Radians or Degrees." +
							  "</html>";
				JLabel infoLabel = new JLabel(info);
				infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
				//Create Panel for Dialog
				JPanel infoPanel = new JPanel();
				infoPanel.add(infoLabel);
				//Create Buttons for Message Dialog
				String[] infoBtn = {"OK"};
				//Display Message
				JOptionPane.showOptionDialog(null, infoPanel, "Help", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, infoBtn, infoBtn[0]);
			}
		});
	}
		
	private void switchSidePanel(boolean whichPanel)
	{
		//Right Panel - True
		//Left Panel - False 
		
		if(whichPanel)
		{
			//Disable Left Panel
			_amountSlider.setEnabled(false);
			_selectButton.setEnabled(false);
			
			//Enable Right Panel
			_magTextField.setEnabled(true);
			_dirTextField.setEnabled(true);
			_btnRadians.setEnabled(true);
			_btnDegrees.setEnabled(true);
			_enterButton.setEnabled(true);
			_buttonsPanel.setLayout(_customBtnLayout);
			_magTextField.requestFocusInWindow();
			_vCalculatorFrame.getRootPane().setDefaultButton(_enterButton);
		}
		else 
		{
			//Disable Right Panel
			_magTextField.setEnabled(false);
			_dirTextField.setEnabled(false);
			_btnRadians.setEnabled(false);
			_btnDegrees.setEnabled(false);
			_enterButton.setEnabled(false);
			_buttonsPanel.setLayout(_customBtnLayout);
			
			//Enabled Left Panel
			_amountSlider.setEnabled(true);
			_selectButton.setEnabled(true);
			_vCalculatorFrame.getRootPane().setDefaultButton(_selectButton);
		}
	}
	
	private void enterVector()
	{
		//Enter Vector
		double tempMag;
		double tempDir;
		
		//Checking Values for Exception Handling
		try
		{ 
			tempMag = Double.parseDouble(_magTextField.getText());
			if(tempMag == 0 || tempMag < 0)
				throw new Exception("Magnitude Cannot be Zero or a Negative Value!");
		}
		catch(Exception ex)
		{
			System.err.println(ex.toString()); //Print Out Error to Console
			JOptionPane.showMessageDialog(_vCalculatorFrame, "Invalid Magnitude! See Information/Help #3 for more Details!", "Error!", JOptionPane.WARNING_MESSAGE);
			_exceptionThrown = true;
			return;
		}
		try
		{ 
			tempDir = Double.parseDouble(_dirTextField.getText());
		}
		catch(Exception ex)
		{
			System.err.println(ex.toString()); //Print Out Error to Console
			JOptionPane.showMessageDialog(_vCalculatorFrame, "Invalid Direction! See Information/Help #4 for more Details!", "Error!", JOptionPane.WARNING_MESSAGE);
			_exceptionThrown = true;
			return;
		}
		
		boolean isRads;
		if(_btnRadians.isSelected() == true)
			isRads = true;
		else
			isRads = false;
		_addVectorCalc.addVector(tempMag, tempDir, isRads);
	}
	
	public void showWindow()
	{
		_vCalculatorFrame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		//Get System
		String osName = System.getProperty("os.name").toLowerCase();
		boolean isMacOS = osName.contains("os x");
		if(isMacOS)
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		
		//Create New Window
		VACS calc = new VACS();
		calc.showWindow();
	}

}

//About Listener Object for Mac OS X About Dialog
class AboutListener implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) {
    	
		//Create Text Information for Dialog
		String info = "<html><div style=\"text-align: center;\">" + 
					  "<font size=\"5\"><b>The Vector Addition Computation System</b></font><br><br>" +
					  "Version: 1.2.5<br><br>" +
					  "<b>Design, Programming, and Testing</b><br>" +
					  "<b>Done By Kian Gorgichuk</b><br><br>" +
					  "<b>Special Thanks to Ms. Bater!</b><br><br>" +
					  "Copyright (c) 2014 Kian Gorgichuk.<br>" + 
					  "All rights reserved.<br>" +
					  "</html>";
		JLabel aboutLabel = new JLabel(info);
		aboutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		//Create Panel for Dialog
		JPanel aboutPanel = new JPanel();
		aboutPanel.add(aboutLabel);
		//Create Buttons for Message Dialog
		String[] aboutBtn = {"OK"};
		//Display Message
		JOptionPane.showOptionDialog(null, aboutPanel, "About", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, aboutBtn, aboutBtn[0]);
    	
        return null;
    }
}