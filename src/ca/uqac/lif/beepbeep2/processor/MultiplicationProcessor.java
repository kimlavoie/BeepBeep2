package ca.uqac.lif.beepbeep2.processor;

import java.util.List;

public class MultiplicationProcessor extends Processor {

	MultiplicationProcessor(Pipe input, Pipe output)
	{
		super(input,output);
		
	}
	
	public MultiplicationProcessor(List<Pipe> inputs, List<Pipe> outputs){
	    super(inputs,outputs);
	  }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!outputStreams.isEmpty() && !inputStreams.isEmpty()) {
				double result = 1.0;
				
				String[] values = readAll();
				
				for (String s : values) {
					result *= Double.parseDouble(s);
				}
				write(String.valueOf(result));
			}
		}

	}
}