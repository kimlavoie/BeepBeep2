package ca.uqac.lif.beepbeep2.processor;

import java.util.List;

public class DivisionProcessor extends Processor {

	DivisionProcessor(Pipe input, Pipe output)
	{
		super(input,output);
		
	}
	
	DivisionProcessor(List<Pipe> inputs, List<Pipe> outputs){
	    super(inputs,outputs);
	  }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!outputStreams.isEmpty() && !inputStreams.isEmpty()) {
				
				String[] values = readAll();
				double result = Double.parseDouble(values[0]) / Double.parseDouble(values[1]);
				
				write(String.valueOf(result));
			}
		}

	}


}
