package autoclave;
import com.pi4j.wiringpi.Spi;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.time.*;

public class autoclaveapi {
	/*In short this program will get sensor data off a thermocouple read 100 values - 1 per ms. 
		 * Aritmetic average these numbers. Process to the relay (on or off) electrical outlet and 
		 * send average value to the Que to later be processed into a graph or general trend. 
		 */
	public static void main(String[] args) {
		
		/*setup vars etc. Also array to be used with MAX31855.java
		this is to be ran on a Raspberry pi */
		int channel = Spi.CHANNEL_0;
		int fd = Spi.wiringPiSPISetup(channel, 500000); // 500 kHz
		MAX31855 max31855 = new MAX31855(channel);
		int[] raw = new int[2];
		Queue<Float> couplerQ = new LinkedList<Float>();
		float sum = 0;
		float average;
		
		/* gets new sensor data 100 times and adds to float sum every time. Added delay of 1ms between reads. 
		in the end I will need to optimize this rate for my solid state relay */
		
		while(couplerQ.size() < 100) { 
			max31855.readRaw(raw);
			sum += max31855.getThermocoupleTemperature(raw[1]);
			TimeUnit.MILLISECONDS.sleep(1);
		}
		// calculates average then sends value to the back of the Queue 
		average = sum/100;
		couplerQ.add(average);
		
		/* prints values in the que. How this is it would just be one value set but this could be looped for a 
		*certain amount of time at a set temp. This would be useful for anything from a smoker
		*to a set-and-forget crock pot. 
		*/
		
		while (couplerQ.size() > 0) {
			System.out.println(couplerQ.peek());
			couplerQ.remove();
		}
		
		System.out.println("Endtime is: " + LocalTime.now());
		}
		
	}
	
	
	
	
	
	


