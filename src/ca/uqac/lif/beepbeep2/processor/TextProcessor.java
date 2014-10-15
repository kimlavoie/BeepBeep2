package ca.uqac.lif.beepbeep2.processor;

public class TextProcessor extends Processor {

	
	TextProcessor(Pipe input, Pipe output)
	{
		super(input,output);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!outputStreams.isEmpty())
				write("Text");
		}
		
	}

}
