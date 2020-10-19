package autoclave;
import java.util.LinkedList;
import java.util.Queue;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import com.pi4j.wiringpi.Spi;
import java.util.concurrent.TimeUnit;

public class autoclave {


	
	public static void main(String[] args) {
		

		int channel = Spi.CHANNEL_0;
		int fd = Spi.wiringPiSPISetup(channel, 500000); // 500 kHz
		MAX31855 max31855 = new MAX31855(channel);
		int[] raw = new int[2];
		LinkedListQueue thermo = new LinkedListQueue();
		float sum = 0;
		float average; 
		
		while(thermo.size() < 100) {
			max31855.readRaw(raw);
			sum += max31855.getThermocoupleTemperature(raw[1]);
			TimeUnit.MILLISECONDS.sleep(1);
		}
		
		average = sum/100;
		thermo.enqueue(average);
		
		while (!thermo.isEmpty()) {
			System.out.println(thermo.back() + " Average Temp in celcius"); 
			System.out.println(((thermo.back() * 1.8) + 32) + " Average Temp in fahrenheit");
			thermo.dequeue();
		}
		
		thermo.printQueue();
		
		System.out.println("Endtime is: " + LocalTime.now());
		
	
		
	
	}
}
	
	
	

	
	
	
	

