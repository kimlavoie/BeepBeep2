package ca.uqac.lif.beepbeep2.processor;

public class PrintProcessor extends Processor{

	
	PrintProcessor(Pipe input, Pipe output)
	{
		super(input,output);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!inputStreams.isEmpty())
				System.out.println(read());
		}
		
		
	}

}
