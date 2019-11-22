
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.swing.JProgressBar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

public class GPhotoDownloader extends JFrame {
	private JPanel contentPane;
	private JTextField UrltextField;
	private JTextField numtextField;
	private JButton btnStart;
	private JLabel 		label;
	private JProgressBar 	bar;
	private int n;


	public String report="Number of Images Processed :";
	public int err=0;
	private static String DIR =System.getProperty("user.home")+"\\Google";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
			        Path path = Paths.get(DIR);
			        //if directory exists?
			        if (!Files.exists(path)) {
			            try {
			                Files.createDirectories(path);
			            } catch (IOException e) {
			                //fail to create directory
			                e.printStackTrace();
			            }
			        }
					GPhotoDownloader frame = new GPhotoDownloader();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GPhotoDownloader() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		UrltextField = new JTextField();
		UrltextField.setBounds(35, 11, 389, 20);
		contentPane.add(UrltextField);
		UrltextField.setColumns(10);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setBounds(10, 14, 30, 14);
		contentPane.add(lblUrl);
		
		numtextField = new JTextField();
		numtextField.setBounds(227, 42, 86, 20);
		contentPane.add(numtextField);
		numtextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Highest Number of Photos");
		lblNewLabel.setBounds(10, 42, 167, 14);
		contentPane.add(lblNewLabel);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(173, 164, 89, 23);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Progressbar set
				System.setProperty("webdriver.chrome.driver", "c://chromedriver.exe");
		        String baseUrl = UrltextField.getText();	
		        n=Integer.valueOf(numtextField.getText());
		        
		        bar.setDoubleBuffered(true);
		        bar.setIndeterminate(true);
		        SaveImage simage=new SaveImage(baseUrl,n,label,bar);
				simage.addPropertyChangeListener(new ProgressListener(bar,label));
				simage.execute();
				 System.out.println("Finished!!!");
			}
		});
		
		JLabel lblImagesWillBe = new JLabel("Images will be saved in "+DIR);
		lblImagesWillBe.setBounds(10, 87, 402, 20);
		contentPane.add(lblImagesWillBe);
		
		JLabel lblChromedriverexe = new JLabel("Make sure that chromedriver.exe is at at C://chromedriver.exe");
		lblChromedriverexe.setBounds(10, 108, 370, 20);
		contentPane.add(lblChromedriverexe);
		
		bar = new JProgressBar();
		bar.setForeground(Color.green);
		bar.setBounds(10, 217, 402, 14);
		bar.setStringPainted(true);
		contentPane.add(bar);
		
		label = new JLabel("--");
		label.setBounds(10, 236, 345, 14);
		contentPane.add(label);
	}
}
class SaveImage extends SwingWorker<Integer, Integer>{
	
	private String baseUrl;
	private JProgressBar bar;
	private int n;
	private JLabel label;
	public boolean err=false;
//	public static List<Long> urls = new ArrayList<>();
//	public static List<String> base_urls = new ArrayList<>();
	
	public SaveImage(String url1,int n1,JLabel lb,JProgressBar b) {
		// TODO Auto-generated constructor stub
		baseUrl=url1;
		n=n1;
		label=lb;
		bar=b;
	}

	@Override
	protected  Integer doInBackground() {
		// TODO Auto-generated method stub
			
			int progress=0;
			List<Long> urls = new ArrayList<>();
			List<String> base_urls = new ArrayList<>();
			List<String> img_urls = new ArrayList<>();
			base_urls.add(baseUrl);
			int k=0;
			while(!base_urls.isEmpty()) {
				k++;
				WebDriver driver =new ChromeDriver() ;
				String first = base_urls.get(0);
			 	driver.get(first);
			 	base_urls.remove(0);
				WebElement element =null;
				element =driver.findElement(By.xpath("//*[@id=\"rg_s\"]/div[1]"));
				if(!element.isDisplayed()) {
					element =driver.findElement(By.xpath("//*[@id=\"islrg\"]/div[1]/div[1]/a[1]/div[1]"));
				}
				
				
		        try{
		        	element.click();
		        }catch (Exception ex) {
		        	err=true;
		        	 System.out.println("prblm ");
		        	 label.setText("Error!! plz try again ");
		        	 bar.setForeground(Color.RED);
		        	 bar.setIndeterminate(true);
		        	 bar.setStringPainted(false);
		        	 driver.close();
		        }
		        
				int cnt =0;
				int i=2;
				boolean flag=true;
				String past_url = null;
				for(;progress<=n;progress++) {	
					WebElement ele=null;
					try {
						ele =driver.findElement(By.cssSelector("#irc-ss > div:nth-child("+(i+1)+") > div.irc_t.i30052 > div.Q8Bv4e.irc-rac"));
					}catch (Exception e) {
						// TODO: handle exception
					}
					
					int c=0;
					while(flag==true && ele.isDisplayed()==false && c<4) {
							i++;
							c++;
							if(i == 4) {
								i=1;
							}
							ele =driver.findElement(By.cssSelector("#irc-ss > div:nth-child("+(i+1)+") > div.irc_t.i30052 > div.Q8Bv4e.irc-rac"));
							System.out.println(i+" "+ele.isDisplayed());
					}
					
					if(ele.isDisplayed()==false) {
						flag=false;
					}
					
					WebElement element1 =null;
					WebElement element2 =null;
					WebElement element3 = null;
					try {
						if(flag) 
							element2=driver.findElement(By.xpath("//*[@id=\"irc-ss\"]/div["+i+"]/div[1]/div[4]/div[1]/a/div/img"));
						else
							element2=driver.findElement(By.xpath("//*[@id=\"Sva75c\"]/div/div/div[3]/div[2]/div/div[1]/div[1]/div/div[2]/a/img"));
						if(flag)
							element1 = driver.findElement(By.xpath("//*[@id=\"irc-ss\"]/div["+i+"]/div[1]/div[2]"));
						else
							element1 = driver.findElement(By.xpath("//*[@id=\"Sva75c\"]/div/div/div[3]/div[2]/div/div[1]/div[1]/div/div[1]/a[2]/div"));
						if(flag)
							element3= driver.findElement(By.xpath("//*[@id=\"irc-ss\"]/div["+i+"]/div[2]/div[2]/div/div[1]/a"));
						else
							element3= driver.findElement(By.xpath("//*[@id=\"Sva75c\"]/div/div/div[3]/div[2]/div/div[1]/div[3]/div[3]/c-wiz/div/div/div/div[1]/a"));
					}catch (Exception e) {
						// TODO: handle exception
					}
					try {
						String side_url= element3.getAttribute("href");
						base_urls.add(side_url);//store images similar google photos search link
						
					}catch (Exception e) {
						// TODO: handle exception
					}
					String url = element2.getAttribute("src");
					
					if(past_url!=null && past_url.equals(url)) {
						cnt++;
						if(cnt>3) {
							break;
						}
						element1.click();
						continue;
					}
					past_url=url;
					long hc=url.hashCode();   //check if the image is from same location
					if(urls.contains(hc)) {
						element1.click();
						continue;
					}
					urls.add(hc);				// if no add to the list and download the image
					System.out.println(progress);
					System.out.println(url);
			 	
			 		setProgress(progress*100/n);
			 		publish(progress);
			 		
//					SaveImageW simage=new SaveImageW(url,progress);
//					simage.start();
//			        synchronized(simage){
//			            try{
//			                System.out.println("Waiting for the image to save...");
//			                simage.wait();
//			                
//			                
//			            }catch(Exception es){
//			                es.printStackTrace();
//			                System.out.print(es.getMessage());
//			            }
//			          
//			        }
			 		img_urls.add(url);
			 		past_url=url;
			 		element1.click();
				}
				driver.close();
				if(progress>=n) {
					break;
				}
			}
			if(err) {
				label.setText("Error!! plz try again ");
			}else {
				label.setText("Alhamdulillah!Now Initiating Download");
				bar.setBackground(Color.orange);
				bar.setIndeterminate(true);
			}
			System.out.println("******Crawling Ended****!!!");
	        for (int i=0; i<img_urls.size(); i++) { 
	           String url=img_urls.get(i);
				SaveImageW simage=new SaveImageW(url,i+1);
				simage.start();
		        synchronized(simage){
		            try{
		                System.out.println(i+1+" Waiting for the image to save.");
		                simage.wait();
		            }catch(Exception es){
		                es.printStackTrace();
		                System.out.print(es.getMessage());
		            }
		          
		        }
		        progress=i;
		 		setProgress(progress*100/n);
		 		publish(progress);
	        }
		return progress;
	}

}



class SaveImageW extends Thread {
	public String url;
	public int i;

	public SaveImageW(String url2, int i2) {
		// TODO Auto-generated constructor stub
		url=url2;
		i=i2;
	}
    @Override
    public void run(){
    	boolean er=false;
        synchronized(this){

   		 String toFile =System.getProperty("user.home")+"\\Google\\"+String.valueOf(i)+".jpg" ;
   		 URL website = null;
		try {
			website = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er=true;
		} 
   		 ReadableByteChannel rbc = null;
		try {
			rbc = Channels.newChannel(website.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er=true;
		} 
   		 FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(toFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er=true;

		} 
   		 try {
			fos.getChannel().transferFrom(rbc, 0,Long.MAX_VALUE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er=true;

		}
   		 try {
			rbc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er=true;
		}
   		 notify();
        }
    }
}
class ProgressListener implements PropertyChangeListener {
	private JProgressBar bar;
	private JLabel label;
	ProgressListener(JProgressBar b,JLabel lb) {
		this.bar = b;
		this.label=lb;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		//Determine whether the property is progress type
		System.out.println(evt.getPropertyName());
		if ("progress".equals(evt.getPropertyName())) { 
			bar.setIndeterminate(false);
			bar.setValue((int) evt.getNewValue());
			label.setText("images are being collected...");
		}
	}
}

