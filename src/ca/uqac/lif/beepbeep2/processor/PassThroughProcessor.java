package ca.uqac.lif.beepbeep2.processor;

public class PassThroughProcessor extends Processor {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!outputStreams.isEmpty() && !inputStreams.isEmpty())
				write(read());
		}
		
		
	}

}
