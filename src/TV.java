import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TV extends JFrame{
	private static ModifiedSemaphore semMemory;
	private static ModifiedSemaphore semAudio;
	private static ArrayList<String> runningProcessesNames = new ArrayList<>();
	private static ArrayList<Process> runningProcesses = new ArrayList<>();
	//public static Semaphore sem;
	private static  JTextField memoryField;
	//private int freeMemory;
	private static JPanel taskBar;
	private boolean VIEWER_IS_OPEN=false;
	private static JTextArea log;
	
	static JPanel audioQ = new JPanel();
	static JPanel memoQ = new JPanel();
	public JPanel x = new JPanel();

	
	public TV(){
		log = new JTextArea();
		//log.setPreferredSize(new Dimension(600, MAXIMIZED_VERT));
		log.setEditable(false);
		log.setLineWrap(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, MAXIMIZED_VERT));
		scrollPane.setViewportView(log);
		log.setText("Welcome to our Smart TV, this concept project has 2 Semaphores,"+"\n"+" 1 counting semaphore for the Memory &"
				+ " 1 binary Semaphore"+"\n"+" for the critical source the Audio(Speakers) in our case."+"\n"
				+ "only 1 process that requires audio can be played at a time"+"\n"+ "and any other process will be blocked in the Audio"+"\n"
				+ " Queue as long as it fits in memory , if there is no free space in memory ,"+"\n"+" it will be queued up in the"
				+ " Memory Queue"+"\n"+"*********************"+"\n"
						+ "what will be added in the next milestone is"+"\n"+
				"1) ability to force start a process from the queue"+"\n"
						+"2) add the code/library to play read videos & audios");
		JPanel shutdownAndMemo = new JPanel();
		semMemory = new ModifiedSemaphore(500);
		semAudio = new ModifiedSemaphore(1);
		memoryField = new JTextField(semMemory.availablePermits()+"/500mb Free");
		memoryField.setPreferredSize(new Dimension(100, 50));
		taskBar =new JPanel();
		
		
		
		JPanel container =new JPanel();
		
		JPanel Controller =new JPanel();
		JPanel desktop =new JPanel();

		JButton shutDown = new JButton("ShutDown");
		JButton Viewer = new JButton("Viewer",new ImageIcon(getClass().getResource("/icons/Viewer.png")));
		JLabel backGround=new JLabel();
		JLabel audioQueue = new JLabel("Audio Queue");
		audioQueue.setPreferredSize(new Dimension(100, 40));
		audioQueue.setForeground(Color.RED);
		JLabel memoQueue = new JLabel("Memory Queue");
		memoQueue.setPreferredSize(new Dimension(100, 40));
		memoQueue.setForeground(Color.RED);
		
		backGround.setIcon(new ImageIcon(getClass().getResource("/images/BackGround.jpg")));
		//Viewer.setIcon(new ImageIcon(getClass().getResource("/icons/Viewer.png")));
		
		CardLayout Card = new CardLayout();
		container.setLayout(Card);
		taskBar.setLayout(new BorderLayout());
		desktop.setLayout(null);
		setLayout(new BorderLayout());
		
		taskBar.setBackground(Color.DARK_GRAY);
		Controller.setBackground(Color.DARK_GRAY);
		
		Viewer.setBounds(25, 25, 100, 100);
		desktop.setSize(getSize());
		//System.out.println(desktop.getHeight()+","+desktop.getWidth()+","+MAXIMIZED_VERT+","+getHeight());
		//backGround.setBounds(0, 0, 150, 150);
		taskBar.setPreferredSize(new Dimension(MAXIMIZED_HORIZ, 50));
		shutDown.setPreferredSize(new Dimension(100, 50));
		shutdownAndMemo.add(shutDown);
		shutdownAndMemo.add(memoryField);
		shutDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<runningProcesses.size();i++)
				{
					runningProcesses.get(i).kill();
				}
				dispose();
				System.exit(0);
			}
		});
		Viewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(VIEWER_IS_OPEN){
					Card.show(container, "Viewer");
				}else{
					JPanel View =Viewer();
					container.add(View);
					container.getLayout().addLayoutComponent("Viewer", View);
					JButton ViewButton=new JButton("Viewer");
					ViewButton.setPreferredSize(new Dimension(100, 40));
					ViewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							Card.next(container);
						}
					});
					Controller.add(audioQueue);
					Controller.add(audioQ);
					Controller.add(ViewButton);
					Controller.add(memoQueue);
					Controller.add(memoQ);
					Card.show(container, "Viewer");
					VIEWER_IS_OPEN=true;
				}
			}
		});
		
		//desktop.add(backGround,BorderLayout.CENTER);
		desktop.add(Viewer);
		container.add(desktop);
		container.getLayout().addLayoutComponent("desktop", desktop);
		taskBar.add(shutdownAndMemo,BorderLayout.EAST);
		taskBar.add(Controller);
		add(container,BorderLayout.CENTER);
		add(taskBar,BorderLayout.SOUTH);
		add(scrollPane,BorderLayout.EAST);
		
		setAlwaysOnTop(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
	}
	
	public static JPanel Viewer(){
		JPanel Viewer =new JPanel();
		JPanel container =new JPanel();
		JPanel Controller =new JPanel();
		JButton Videos = new JButton("Videos");
		JButton Back = new JButton("Back");
		JButton Audios = new JButton("Audios");
		JLabel backGround=new JLabel();

		//JPanel queue = new JPanel();
		
		FlowLayout containarLayout =new FlowLayout();
		containarLayout.setAlignment(FlowLayout.LEFT);
		FlowLayout controllerLayout =new FlowLayout();
		controllerLayout.setAlignment(FlowLayout.LEFT);
		container.setLayout(containarLayout);
		Viewer.setLayout(new BorderLayout());
		Controller.setLayout(controllerLayout);
		
		Controller.setPreferredSize(new Dimension(MAXIMIZED_HORIZ, 100));
		container.setBorder(BorderFactory.createLineBorder(null, 5, true));
		Videos.setPreferredSize(new Dimension(150,150));
		Audios.setPreferredSize(new Dimension(150,150));
		Back.setEnabled(false);
		Back.setIcon(new ImageIcon(TV.class.getResource("/icons/Back.png")));
		
		
		Back.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				container.removeAll();
				container.add(Videos);
				container.add(Audios);
				Back.setEnabled(false);
				container.repaint();
				container.revalidate();
			}
		});
		Videos.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				JButton stop = new JButton("Stop");
				stop.setPreferredSize(new Dimension(150,150));
				JButton start = new JButton("Start");
				start.setPreferredSize(new Dimension(150,150));
				stop.setEnabled(false);
				taskBar.repaint();
				taskBar.revalidate();
				Back.setEnabled(true);
				container.removeAll();
				File file =new File("src/videos/");
				String[] names = file.list();
				for(String string:names)
				{
					File file1 =new File("src/videos/"+string);
					int fileLength=(int) (file1.length()/(1024*1024));
					JButton button =new JButton(string);
					button.setPreferredSize(new Dimension(150,150));
					button.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							String inQueue="Videos in Audio Queue -> ";
							Queue<Process> queueAudio = semAudio.getQueue();
							Queue<Process> queueMemory = semMemory.getQueue();
							int queueAudioSize = queueAudio.size();
							int queueMemorySize = queueMemory.size();
							boolean isQueued = false;
							boolean queueType = false; // true in audio queue , false in memory queue
							for(int i=0;i<queueAudioSize;i++)
							{
								Process x = queueAudio.remove();
								if(x.getpID().equals(string))
								{
									isQueued=true;
									queueType=true;
								}
								inQueue= inQueue+x.getpID()+" ";
								queueAudio.add(x);
							}
							inQueue=inQueue+"\n"+"Videos in Memory Queue -> ";
							for(int i=0;i<queueMemorySize;i++)
							{
								Process x = queueMemory.remove();
								if(x.getpID().equals(string))
								{
									isQueued=true;
									queueType=false;
								}
								inQueue= inQueue+x.getpID()+" ";
								queueMemory.add(x);
							}
							updateLog(inQueue);
							JLabel label =new JLabel();
							label.setPreferredSize(new Dimension(800,150));
							Back.setEnabled(true);
							container.removeAll();
							if(runningProcessesNames.contains(string))
							{
								String update = string + " is Currently being played";
								updateLog(update);
								label.setText("Now Playing "+string);
								stop.setEnabled(true);
								start.setEnabled(false);
								stop.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										runningProcesses.get(runningProcessesNames.indexOf(string)).kill();
										String update = string + " has been stopped"+"\n";
										audioQ.remove(0);
										Process fromMemoQueue = semMemory.siqnalMemo(runningProcesses.get(runningProcessesNames.indexOf(string)));
										if(fromMemoQueue!=null)
										{
											if(semAudio.waitAudio(fromMemoQueue));
												update = update+fromMemoQueue.getpID()+" has been added to Audio queue since it fits in Memo"+"\n";
												JButton x = new JButton(fromMemoQueue.getpID());
												x.setPreferredSize(new Dimension(100, 40));
												audioQ.add(x);
												memoQ.remove(0);
												x.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														Process interruptedP = runningProcesses.get(0);
														runningProcesses.get(0).Block();
														runningProcesses.remove(0);
														runningProcessesNames.remove(0);
														JButton source  = (JButton)e.getSource();
														Process requiredProcess = new Process(source.getText(), "videos", fileLength);
//														JFXPanel x1=requiredProcess.getPanel();
//														x1.setPreferredSize(new Dimension(1280, 720));
//														container.add(x1);
														Queue<Process> queueAudio = semAudio.getQueue();
														Queue<Process> queueMemory = semMemory.getQueue();
														int queueAudioSize = queueAudio.size();
														int queueMemorySize = queueMemory.size();
														
														JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
														JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
														for(int i=0;i<audioQ.getComponents().length;i++)
															oldAudioQP[i]=(JButton) audioQ.getComponent(i);
														for(int i=0;i<memoQ.getComponents().length;i++)
															oldMemoQP[i]=(JButton) memoQ.getComponent(i);
														
														audioQ.removeAll();
														memoQ.removeAll();
														semAudio.reset();
														semMemory.reset();
														semMemory.waitMemo(requiredProcess);
														semAudio.waitAudio(requiredProcess);
														runningProcessesNames.add(string);
														runningProcesses.add(requiredProcess);
														requiredProcess.run();
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldAudioQP[i+1]);
																audioQ.getComponent(0).setEnabled(false);
																
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldMemoQP[i]);
																audioQ.getComponent(0).setEnabled(false);
															}
															queueMemory.add(x);
														}
														
														if(semMemory.waitMemo(interruptedP))
														{
															semAudio.waitAudio(interruptedP);
															audioQ.add(oldAudioQP[0]);
															audioQ.getComponent(1).setEnabled(true);
														}
														else
														{
															memoQ.add(oldAudioQP[0]);
															memoQ.getComponent(0).setEnabled(true);
														}
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldAudioQP[i+1]);
																}
																else
																{
																	memoQ.add(oldAudioQP[i+1]);
																}
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldMemoQP[i]);
																}
																else
																{
																	memoQ.add(oldMemoQP[i]);
																}
															}
															queueMemory.add(x);
														}
														container.repaint();
														container.revalidate();
													}
												});
										}
										Process fromAudioQueue = semAudio.siqnalAudio();
										if(fromAudioQueue!=null)
										{
											semAudio.waitAudio(fromAudioQueue);
											update=update+ fromAudioQueue.getpID()+" has started playing"+"\n";
											audioQ.getComponent(0).setEnabled(false);
											runningProcessesNames.add(fromAudioQueue.getpID());
											runningProcesses.add(fromAudioQueue);
											fromAudioQueue.run();
										}
										updateLog(update);
										runningProcesses.remove(runningProcessesNames.indexOf(string));
										runningProcessesNames.remove(string);
										memoryField.setText(semMemory.availablePermits()+"/500mb Free");
										taskBar.repaint();
										taskBar.revalidate();
										label.setText("");
										start.setEnabled(true);
										stop.setEnabled(false);
										container.remove(start);
										container.remove(stop);
										container.repaint();
										container.revalidate();
									}
								});
								container.add(stop);
								container.add(start);
								container.add(label);
								container.repaint();
								container.revalidate();
							}
							else if(!runningProcessesNames.contains(string) && !isQueued)
							{
								start.setEnabled(true);
								label.setText("");
								start.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) {
										String newLog="";
										Process requiredProcess = new Process(string, "videos", fileLength);
										if(semMemory.waitMemo(requiredProcess))
										{
											if(semAudio.waitAudio(requiredProcess))
											{
												newLog=newLog+requiredProcess.getpID()+" has started playing"+"\n";
												JButton x = new JButton(requiredProcess.getpID());
												x.setPreferredSize(new Dimension(100, 40));
												x.setEnabled(false);
												audioQ.add(x);
												runningProcessesNames.add(string);
												runningProcesses.add(requiredProcess);
												requiredProcess.run();
												label.setText("Now Playing "+string);
												x.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														Process interruptedP = runningProcesses.get(0);
														runningProcesses.get(0).Block();
														runningProcesses.remove(0);
														runningProcessesNames.remove(0);
														JButton source  = (JButton)e.getSource();
														Process requiredProcess = new Process(source.getText(), "videos", fileLength);
//														JFXPanel x1=requiredProcess.getPanel();
//														x1.setPreferredSize(new Dimension(1280, 720));
//														container.add(x1);
														Queue<Process> queueAudio = semAudio.getQueue();
														Queue<Process> queueMemory = semMemory.getQueue();
														int queueAudioSize = queueAudio.size();
														int queueMemorySize = queueMemory.size();
														
														JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
														JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
														for(int i=0;i<audioQ.getComponents().length;i++)
															oldAudioQP[i]=(JButton) audioQ.getComponent(i);
														for(int i=0;i<memoQ.getComponents().length;i++)
															oldMemoQP[i]=(JButton) memoQ.getComponent(i);
														
														audioQ.removeAll();
														memoQ.removeAll();
														semAudio.reset();
														semMemory.reset();
														semMemory.waitMemo(requiredProcess);
														semAudio.waitAudio(requiredProcess);
														runningProcessesNames.add(string);
														runningProcesses.add(requiredProcess);
														requiredProcess.run();
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldAudioQP[i+1]);
																audioQ.getComponent(0).setEnabled(false);
																
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldMemoQP[i]);
																audioQ.getComponent(0).setEnabled(false);
															}
															queueMemory.add(x);
														}
														
														if(semMemory.waitMemo(interruptedP))
														{
															semAudio.waitAudio(interruptedP);
															audioQ.add(oldAudioQP[0]);
															audioQ.getComponent(1).setEnabled(true);
														}
														else
														{
															memoQ.add(oldAudioQP[0]);
															memoQ.getComponent(0).setEnabled(true);
														}
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldAudioQP[i+1]);
																}
																else
																{
																	memoQ.add(oldAudioQP[i+1]);
																}
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldMemoQP[i]);
																}
																else
																{
																	memoQ.add(oldMemoQP[i]);
																}
															}
															queueMemory.add(x);
														}
														container.repaint();
														container.revalidate();
													}
												});
											}
											else
											{
												requiredProcess.setState(CurrentState.BLOCKED);
												newLog=newLog+" Audio Source currently busy, Process added to Audio Queue"+"\n";
												label.setText("Audio Source currently busy, Process added to Audio Queue");
												JButton x = new JButton(requiredProcess.getpID());
												x.setPreferredSize(new Dimension(100, 40));
												audioQ.add(x);
												x.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														Process interruptedP = runningProcesses.get(0);
														runningProcesses.get(0).Block();
														runningProcesses.remove(0);
														runningProcessesNames.remove(0);
														JButton source  = (JButton)e.getSource();
														Process requiredProcess = new Process(source.getText(), "videos", fileLength);
//														JFXPanel x1=requiredProcess.getPanel();
//														x1.setPreferredSize(new Dimension(1280, 720));
//														container.add(x1);
														Queue<Process> queueAudio = semAudio.getQueue();
														Queue<Process> queueMemory = semMemory.getQueue();
														int queueAudioSize = queueAudio.size();
														int queueMemorySize = queueMemory.size();
														
														JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
														JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
														for(int i=0;i<audioQ.getComponents().length;i++)
															oldAudioQP[i]=(JButton) audioQ.getComponent(i);
														for(int i=0;i<memoQ.getComponents().length;i++)
															oldMemoQP[i]=(JButton) memoQ.getComponent(i);
														
														audioQ.removeAll();
														memoQ.removeAll();
														semAudio.reset();
														semMemory.reset();
														semMemory.waitMemo(requiredProcess);
														semAudio.waitAudio(requiredProcess);
														runningProcessesNames.add(string);
														runningProcesses.add(requiredProcess);
														requiredProcess.run();
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldAudioQP[i+1]);
																audioQ.getComponent(0).setEnabled(false);
																
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldMemoQP[i]);
																audioQ.getComponent(0).setEnabled(false);
															}
															queueMemory.add(x);
														}
														
														if(semMemory.waitMemo(interruptedP))
														{
															semAudio.waitAudio(interruptedP);
															audioQ.add(oldAudioQP[0]);
															audioQ.getComponent(1).setEnabled(true);
														}
														else
														{
															memoQ.add(oldAudioQP[0]);
															memoQ.getComponent(0).setEnabled(true);
														}
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldAudioQP[i+1]);
																}
																else
																{
																	memoQ.add(oldAudioQP[i+1]);
																}
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldMemoQP[i]);
																}
																else
																{
																	memoQ.add(oldMemoQP[i]);
																}
															}
															queueMemory.add(x);
														}
														container.repaint();
														container.revalidate();
													}
												});
											}
										}
										else
										{
											requiredProcess.setState(CurrentState.BLOCKED);
											newLog=newLog+" Not enough space in Memory, Process added to Memory Queue"+"\n";
											label.setText("Not enough space in Memory, Process added to Memory Queue");
											JButton x = new JButton(requiredProcess.getpID());
											x.setPreferredSize(new Dimension(100, 40));
											memoQ.add(x);
											x.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													Process interruptedP = runningProcesses.get(0);
													runningProcesses.get(0).Block();
													runningProcesses.remove(0);
													runningProcessesNames.remove(0);
													JButton source  = (JButton)e.getSource();
													Process requiredProcess = new Process(source.getText(), "videos", fileLength);
//													JFXPanel x1=requiredProcess.getPanel();
//													x1.setPreferredSize(new Dimension(1280, 720));
//													container.add(x1);
													Queue<Process> queueAudio = semAudio.getQueue();
													Queue<Process> queueMemory = semMemory.getQueue();
													int queueAudioSize = queueAudio.size();
													int queueMemorySize = queueMemory.size();
													
													JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
													JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
													for(int i=0;i<audioQ.getComponents().length;i++)
														oldAudioQP[i]=(JButton) audioQ.getComponent(i);
													for(int i=0;i<memoQ.getComponents().length;i++)
														oldMemoQP[i]=(JButton) memoQ.getComponent(i);
													
													audioQ.removeAll();
													memoQ.removeAll();
													semAudio.reset();
													semMemory.reset();
													semMemory.waitMemo(requiredProcess);
													semAudio.waitAudio(requiredProcess);
													runningProcessesNames.add(string);
													runningProcesses.add(requiredProcess);
													requiredProcess.run();
													
													for(int i=0;i<queueAudioSize;i++)
													{
														Process x = queueAudio.remove();
														if(x.getpID().equals(source.getText()))
														{
															audioQ.add(oldAudioQP[i+1]);
															audioQ.getComponent(0).setEnabled(false);
															
														}
														queueAudio.add(x);
													}
													for(int i=0;i<queueMemorySize;i++)
													{
														Process x = queueMemory.remove();
														if(x.getpID().equals(source.getText()))
														{
															audioQ.add(oldMemoQP[i]);
															audioQ.getComponent(0).setEnabled(false);
														}
														queueMemory.add(x);
													}
													
													if(semMemory.waitMemo(interruptedP))
													{
														semAudio.waitAudio(interruptedP);
														audioQ.add(oldAudioQP[0]);
														audioQ.getComponent(1).setEnabled(true);
													}
													else
													{
														memoQ.add(oldAudioQP[0]);
														memoQ.getComponent(0).setEnabled(true);
													}
													
													for(int i=0;i<queueAudioSize;i++)
													{
														Process x = queueAudio.remove();
														if(!x.getpID().equals(source.getText()))
														{
															if(semMemory.waitMemo(x))
															{
																semAudio.waitAudio(x);
																audioQ.add(oldAudioQP[i+1]);
															}
															else
															{
																memoQ.add(oldAudioQP[i+1]);
															}
														}
														queueAudio.add(x);
													}
													for(int i=0;i<queueMemorySize;i++)
													{
														Process x = queueMemory.remove();
														if(!x.getpID().equals(source.getText()))
														{
															if(semMemory.waitMemo(x))
															{
																semAudio.waitAudio(x);
																audioQ.add(oldMemoQP[i]);
															}
															else
															{
																memoQ.add(oldMemoQP[i]);
															}
														}
														queueMemory.add(x);
													}
													container.repaint();
													container.revalidate();
												}
											});
										}
										updateLog(newLog);
										memoryField.setText(semMemory.availablePermits()+"/500mb Free");
										taskBar.repaint();
										taskBar.revalidate();
										start.setEnabled(false);
										stop.setEnabled(true);
										container.remove(start);
										container.remove(stop);
										container.repaint();
										container.revalidate();
									}
								});
								container.add(stop);
								container.add(start);
								container.add(label);
								container.repaint();
								container.revalidate();
							}
							else
							{
								String newLog;
								start.setEnabled(false);
								stop.setEnabled(false);
								if(queueType)
								{
									newLog="Video already in Audio Queue";
									label.setText("Video already in Audio Queue");
								}
								else
								{
									newLog="Video already in Memory Queue";
									label.setText("Video already in Memory Queue");
								}
								updateLog(newLog);
								container.add(stop);
								container.add(start);
								container.add(label);
								container.repaint();
								container.revalidate();
							}
						}
					});
					container.add(button);
				}
				container.repaint();
				container.revalidate();
			}
		});
		
		Audios.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JButton stop = new JButton("Stop");
				stop.setPreferredSize(new Dimension(150,150));
				JButton start = new JButton("Start");
				start.setPreferredSize(new Dimension(150,150));
				stop.setEnabled(false);
				taskBar.repaint();
				taskBar.revalidate();
				Back.setEnabled(true);
				container.removeAll();
				File file =new File("src/audios/");
				String[] names = file.list();
				for(String string:names){
					File file1 =new File("src/audios/"+string);
					int fileLength=(int) (file1.length()/(1024*1024));
					JButton button =new JButton(string);
					button.setPreferredSize(new Dimension(150,150));
					button.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							String inQueue="Audios in Audio Queue -> ";
							Queue<Process> queueAudio = semAudio.getQueue();
							Queue<Process> queueMemory = semMemory.getQueue();
							int queueAudioSize = queueAudio.size();
							int queueMemorySize = queueMemory.size();
							boolean isQueued = false;
							boolean queueType = false; // true in audio queue , false in memory queue
							for(int i=0;i<queueAudioSize;i++)
							{
								Process x = queueAudio.remove();
								if(x.getpID().equals(string))
								{
									isQueued=true;
									queueType=true;
								}
								inQueue= inQueue+x.getpID()+" ";
								queueAudio.add(x);
							}
							for(int i=0;i<queueMemorySize;i++)
							{
								Process x = queueMemory.remove();
								if(x.getpID().equals(string))
								{
									isQueued=true;
									queueType=false;
								}
								inQueue= inQueue+x.getpID()+" ";
								queueMemory.add(x);
							}
							JLabel label =new JLabel();
							label.setPreferredSize(new Dimension(800,150));
							Back.setEnabled(true);
							container.removeAll();
							if(runningProcessesNames.contains(string))
							{
								String newLog = string + " is Currently being played";
								updateLog(newLog);
								label.setText("Now Playing "+string);
								stop.setEnabled(true);
								start.setEnabled(false);
								stop.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										runningProcesses.get(runningProcessesNames.indexOf(string)).kill();
										String newLog = string+ " has been stopped"+"\n";
										Process fromMemoQueue = semMemory.siqnalMemo(runningProcesses.get(runningProcessesNames.indexOf(string)));
										audioQ.remove(0);
										if(fromMemoQueue!=null)
										{
											newLog = newLog+fromMemoQueue.getpID()+" has been added to Audio queue since it fits in Memo"+"\n";
											semAudio.waitAudio(fromMemoQueue);
											JButton x = new JButton(fromMemoQueue.getpID());
											x.setPreferredSize(new Dimension(100, 40));
											audioQ.add(x);
											memoQ.remove(0);
											x.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													Process interruptedP = runningProcesses.get(0);
													runningProcesses.get(0).Block();
													runningProcesses.remove(0);
													runningProcessesNames.remove(0);
													JButton source  = (JButton)e.getSource();
													Process requiredProcess = new Process(source.getText(), "audios", fileLength);
//													JFXPanel x1=requiredProcess.getPanel();
//													x1.setPreferredSize(new Dimension(1280, 720));
//													container.add(x1);
													Queue<Process> queueAudio = semAudio.getQueue();
													Queue<Process> queueMemory = semMemory.getQueue();
													int queueAudioSize = queueAudio.size();
													int queueMemorySize = queueMemory.size();
													
													JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
													JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
													for(int i=0;i<audioQ.getComponents().length;i++)
														oldAudioQP[i]=(JButton) audioQ.getComponent(i);
													for(int i=0;i<memoQ.getComponents().length;i++)
														oldMemoQP[i]=(JButton) memoQ.getComponent(i);
													
													audioQ.removeAll();
													memoQ.removeAll();
													semAudio.reset();
													semMemory.reset();
													semMemory.waitMemo(requiredProcess);
													semAudio.waitAudio(requiredProcess);
													runningProcessesNames.add(string);
													runningProcesses.add(requiredProcess);
													requiredProcess.run();
													
													for(int i=0;i<queueAudioSize;i++)
													{
														Process x = queueAudio.remove();
														if(x.getpID().equals(source.getText()))
														{
															audioQ.add(oldAudioQP[i+1]);
															audioQ.getComponent(0).setEnabled(false);
															
														}
														queueAudio.add(x);
													}
													for(int i=0;i<queueMemorySize;i++)
													{
														Process x = queueMemory.remove();
														if(x.getpID().equals(source.getText()))
														{
															audioQ.add(oldMemoQP[i]);
															audioQ.getComponent(0).setEnabled(false);
														}
														queueMemory.add(x);
													}
													
													if(semMemory.waitMemo(interruptedP))
													{
														semAudio.waitAudio(interruptedP);
														audioQ.add(oldAudioQP[0]);
														audioQ.getComponent(1).setEnabled(true);
													}
													else
													{
														memoQ.add(oldAudioQP[0]);
														memoQ.getComponent(0).setEnabled(true);
													}
													
													for(int i=0;i<queueAudioSize;i++)
													{
														Process x = queueAudio.remove();
														if(!x.getpID().equals(source.getText()))
														{
															if(semMemory.waitMemo(x))
															{
																semAudio.waitAudio(x);
																audioQ.add(oldAudioQP[i+1]);
															}
															else
															{
																memoQ.add(oldAudioQP[i+1]);
															}
														}
														queueAudio.add(x);
													}
													for(int i=0;i<queueMemorySize;i++)
													{
														Process x = queueMemory.remove();
														if(!x.getpID().equals(source.getText()))
														{
															if(semMemory.waitMemo(x))
															{
																semAudio.waitAudio(x);
																audioQ.add(oldMemoQP[i]);
															}
															else
															{
																memoQ.add(oldMemoQP[i]);
															}
														}
														queueMemory.add(x);
													}
													container.repaint();
													container.revalidate();
												}
											});
										}
										Process fromAudioQueue = semAudio.siqnalAudio();
										if(fromAudioQueue!=null)
										{
											newLog=newLog+ fromAudioQueue.getpID()+" has started playing"+"\n";
											semAudio.waitAudio(fromAudioQueue);
											runningProcessesNames.add(fromAudioQueue.getpID());
											runningProcesses.add(fromAudioQueue);
											audioQ.getComponent(0).setEnabled(false);
											fromAudioQueue.run();
										}
										updateLog(newLog);
										runningProcesses.remove(runningProcessesNames.indexOf(string));
										runningProcessesNames.remove(string);
										memoryField.setText(semMemory.availablePermits()+"/500mb Free");
										taskBar.repaint();
										taskBar.revalidate();
										label.setText("");
										start.setEnabled(true);
										stop.setEnabled(false);
										container.remove(start);
										container.remove(stop);
										container.repaint();
										container.revalidate();
									}
								});
								container.add(stop);
								container.add(start);
								container.add(label);
								container.repaint();
								container.revalidate();
							}
							else if(!runningProcessesNames.contains(string) && !isQueued)
							{
								start.setEnabled(true);
								label.setText("");
								start.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) {
										String newLog="";
										Process requiredProcess = new Process(string, "audios",fileLength);
										if(semMemory.waitMemo(requiredProcess))
										{
											if(semAudio.waitAudio(requiredProcess))
											{
												newLog=newLog+requiredProcess.getpID()+" has started playing"+"\n";
												runningProcessesNames.add(string);
												runningProcesses.add(requiredProcess);
												requiredProcess.run();
												label.setText("Now Playing "+string);
												JButton x = new JButton(requiredProcess.getpID());
												x.setPreferredSize(new Dimension(100, 40));
												x.setEnabled(false);
												audioQ.add(x);
												x.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														Process interruptedP = runningProcesses.get(0);
														runningProcesses.get(0).Block();
														runningProcesses.remove(0);
														runningProcessesNames.remove(0);
														JButton source  = (JButton)e.getSource();
														Process requiredProcess = new Process(source.getText(), "audios", fileLength);
														Queue<Process> queueAudio = semAudio.getQueue();
														Queue<Process> queueMemory = semMemory.getQueue();
														int queueAudioSize = queueAudio.size();
														int queueMemorySize = queueMemory.size();
														
														JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
														JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
														for(int i=0;i<audioQ.getComponents().length;i++)
															oldAudioQP[i]=(JButton) audioQ.getComponent(i);
														for(int i=0;i<memoQ.getComponents().length;i++)
															oldMemoQP[i]=(JButton) memoQ.getComponent(i);
														
														audioQ.removeAll();
														memoQ.removeAll();
														semAudio.reset();
														semMemory.reset();
														semMemory.waitMemo(requiredProcess);
														semAudio.waitAudio(requiredProcess);
														runningProcessesNames.add(string);
														runningProcesses.add(requiredProcess);
														requiredProcess.run();
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldAudioQP[i+1]);
																audioQ.getComponent(0).setEnabled(false);
																
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldMemoQP[i]);
																audioQ.getComponent(0).setEnabled(false);
															}
															queueMemory.add(x);
														}
														
														if(semMemory.waitMemo(interruptedP))
														{
															semAudio.waitAudio(interruptedP);
															audioQ.add(oldAudioQP[0]);
															audioQ.getComponent(1).setEnabled(true);
														}
														else
														{
															memoQ.add(oldAudioQP[0]);
															memoQ.getComponent(0).setEnabled(true);
														}
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldAudioQP[i+1]);
																}
																else
																{
																	memoQ.add(oldAudioQP[i+1]);
																}
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldMemoQP[i]);
																}
																else
																{
																	memoQ.add(oldMemoQP[i]);
																}
															}
															queueMemory.add(x);
														}
														container.repaint();
														container.revalidate();
													}
												});
											}
											else
											{
												requiredProcess.setState(CurrentState.BLOCKED);
												newLog=newLog+" Audio Source currently busy, Process added to Audio Queue"+"\n";
												label.setText("Audio Source currently busy, Process added to Audio Queue");
												JButton x = new JButton(requiredProcess.getpID());
												x.setPreferredSize(new Dimension(100, 40));
												audioQ.add(x);
												x.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														Process interruptedP = runningProcesses.get(0);
														runningProcesses.get(0).Block();
														runningProcesses.remove(0);
														runningProcessesNames.remove(0);
														JButton source  = (JButton)e.getSource();
														Process requiredProcess = new Process(source.getText(), "audios",fileLength);
														Queue<Process> queueAudio = semAudio.getQueue();
														Queue<Process> queueMemory = semMemory.getQueue();
														int queueAudioSize = queueAudio.size();
														int queueMemorySize = queueMemory.size();
														
														JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
														JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
														for(int i=0;i<audioQ.getComponents().length;i++)
															oldAudioQP[i]=(JButton) audioQ.getComponent(i);
														for(int i=0;i<memoQ.getComponents().length;i++)
															oldMemoQP[i]=(JButton) memoQ.getComponent(i);
														
														audioQ.removeAll();
														memoQ.removeAll();
														semAudio.reset();
														semMemory.reset();
														semMemory.waitMemo(requiredProcess);
														semAudio.waitAudio(requiredProcess);
														runningProcessesNames.add(string);
														runningProcesses.add(requiredProcess);
														requiredProcess.run();
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldAudioQP[i+1]);
																audioQ.getComponent(0).setEnabled(false);
																
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(x.getpID().equals(source.getText()))
															{
																audioQ.add(oldMemoQP[i]);
																audioQ.getComponent(0).setEnabled(false);
															}
															queueMemory.add(x);
														}
														
														if(semMemory.waitMemo(interruptedP))
														{
															semAudio.waitAudio(interruptedP);
															audioQ.add(oldAudioQP[0]);
															audioQ.getComponent(1).setEnabled(true);
														}
														else
														{
															memoQ.add(oldAudioQP[0]);
															memoQ.getComponent(0).setEnabled(true);
														}
														
														for(int i=0;i<queueAudioSize;i++)
														{
															Process x = queueAudio.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldAudioQP[i+1]);
																}
																else
																{
																	memoQ.add(oldAudioQP[i+1]);
																}
															}
															queueAudio.add(x);
														}
														for(int i=0;i<queueMemorySize;i++)
														{
															Process x = queueMemory.remove();
															if(!x.getpID().equals(source.getText()))
															{
																if(semMemory.waitMemo(x))
																{
																	semAudio.waitAudio(x);
																	audioQ.add(oldMemoQP[i]);
																}
																else
																{
																	memoQ.add(oldMemoQP[i]);
																}
															}
															queueMemory.add(x);
														}
														container.repaint();
														container.revalidate();
													}
												});
											}
										}
										else
										{
											requiredProcess.setState(CurrentState.BLOCKED);
											newLog=newLog+" Not enough space in Memory, Process added to Memory Queue"+"\n";
											label.setText("Not enough space in Memory, Process added to Memory Queue");
											JButton x = new JButton(requiredProcess.getpID());
											x.setPreferredSize(new Dimension(100, 40));
											memoQ.add(x);
											x.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													Process interruptedP = runningProcesses.get(0);
													runningProcesses.get(0).Block();
													runningProcesses.remove(0);
													runningProcessesNames.remove(0);
													JButton source  = (JButton)e.getSource();
													Process requiredProcess = new Process(source.getText(), "audios", fileLength);
//													JFXPanel x1=requiredProcess.getPanel();
//													x1.setPreferredSize(new Dimension(1280, 720));
//													container.add(x1);
													Queue<Process> queueAudio = semAudio.getQueue();
													Queue<Process> queueMemory = semMemory.getQueue();
													int queueAudioSize = queueAudio.size();
													int queueMemorySize = queueMemory.size();
													
													JButton[] oldAudioQP = new JButton[audioQ.getComponents().length];
													JButton[] oldMemoQP = new JButton[memoQ.getComponents().length];
													for(int i=0;i<audioQ.getComponents().length;i++)
														oldAudioQP[i]=(JButton) audioQ.getComponent(i);
													for(int i=0;i<memoQ.getComponents().length;i++)
														oldMemoQP[i]=(JButton) memoQ.getComponent(i);
													
													audioQ.removeAll();
													memoQ.removeAll();
													semAudio.reset();
													semMemory.reset();
													semMemory.waitMemo(requiredProcess);
													semAudio.waitAudio(requiredProcess);
													runningProcessesNames.add(string);
													runningProcesses.add(requiredProcess);
													requiredProcess.run();
													
													for(int i=0;i<queueAudioSize;i++)
													{
														Process x = queueAudio.remove();
														if(x.getpID().equals(source.getText()))
														{
															audioQ.add(oldAudioQP[i+1]);
															audioQ.getComponent(0).setEnabled(false);
															
														}
														queueAudio.add(x);
													}
													for(int i=0;i<queueMemorySize;i++)
													{
														Process x = queueMemory.remove();
														if(x.getpID().equals(source.getText()))
														{
															audioQ.add(oldMemoQP[i]);
															audioQ.getComponent(0).setEnabled(false);
														}
														queueMemory.add(x);
													}
													
													if(semMemory.waitMemo(interruptedP))
													{
														semAudio.waitAudio(interruptedP);
														audioQ.add(oldAudioQP[0]);
														audioQ.getComponent(1).setEnabled(true);
													}
													else
													{
														memoQ.add(oldAudioQP[0]);
														memoQ.getComponent(0).setEnabled(true);
													}
													
													for(int i=0;i<queueAudioSize;i++)
													{
														Process x = queueAudio.remove();
														if(!x.getpID().equals(source.getText()))
														{
															if(semMemory.waitMemo(x))
															{
																semAudio.waitAudio(x);
																audioQ.add(oldAudioQP[i+1]);
															}
															else
															{
																memoQ.add(oldAudioQP[i+1]);
															}
														}
														queueAudio.add(x);
													}
													for(int i=0;i<queueMemorySize;i++)
													{
														Process x = queueMemory.remove();
														if(!x.getpID().equals(source.getText()))
														{
															if(semMemory.waitMemo(x))
															{
																semAudio.waitAudio(x);
																audioQ.add(oldMemoQP[i]);
															}
															else
															{
																memoQ.add(oldMemoQP[i]);
															}
														}
														queueMemory.add(x);
													}
													container.repaint();
													container.revalidate();
												}
											});
										}
										updateLog(newLog);
										memoryField.setText(semMemory.availablePermits()+"/500mb Free");
										taskBar.repaint();
										taskBar.revalidate();
										start.setEnabled(false);
										stop.setEnabled(true);
										container.remove(start);
										container.remove(stop);
										container.repaint();
										container.revalidate();
									}
								});
								container.add(stop);
								container.add(start);
								container.add(label);
								container.repaint();
								container.revalidate();
							}
							else
							{
								String newLog;
								start.setEnabled(false);
								stop.setEnabled(false);
								if(queueType)
								{
									newLog="Audio already in Audio Queue";
									label.setText("Audio already in Audio Queue");
								}
								else
								{
									newLog="Audio already in Memory Queue";
									label.setText("Audio already in Memory Queue");
								}
								updateLog(newLog);
								container.add(stop);
								container.add(start);
								container.add(label);
								container.repaint();
								container.revalidate();
							}
						}
					});
					container.add(button);
				}
				container.repaint();
				container.revalidate();
			}
		});
		container.add(Videos);
		container.add(Audios);
		Controller.add(Back);
		container.add(backGround);
		Viewer.add(container,BorderLayout.CENTER);
		Viewer.add(Controller,BorderLayout.NORTH);
		return Viewer;
	}
	public static void updateLog(String newLog)
	{
		if(log.getText() == null || log.getText().isEmpty())
		{
			log.setText(newLog);
		}
		else
		{
			String x = log.getText();
			x = x +"\n"+ newLog;
			log.setText(x);
		}
	}

	public static void main(String[]arqs){
		TV tv =new TV();
		tv.setVisible(true);
	}
}
