package ca.uqac.lif.beepbeep2.processor;

public class TextProcessor extends Processor {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!outputStreams.isEmpty())
				write(options.get("value"));
		}
		
	}

}
