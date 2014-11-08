package ca.uqac.lif.beepbeep2.processor;

import java.util.List;

public class PlusProcessor extends Processor {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!outputStreams.isEmpty() && !inputStreams.isEmpty()) {
				double result = 0.0;
				
				String[] values = readAll();
				
				for (String s : values) {
					result += Double.parseDouble(s);
				}
				write(String.valueOf(result));
			}
		}
		
		
	}

}
