package ro.calin.wsd;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.stream.XMLStreamException;

public class WSD {
	public static void main(String[] args) throws FileNotFoundException,
			XMLStreamException {

		
		XMLSentenceFactory trainFactory = new XMLSentenceFactory(
				new FileReader("corpus/line.xml"), 
				new SentenceFactoryFilter() {
			@Override
			public boolean approveSentence(Sentence sentence) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean approveId(int id) {
				return id % 10 != 9;
			}
		});
		
		XMLSentenceFactory testFactory = new XMLSentenceFactory(
				new FileReader("corpus/line.xml"), 
				new SentenceFactoryFilter() {
			@Override
			public boolean approveSentence(Sentence sentence) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean approveId(int id) {
				return id % 10 == 9;
			}
		});
		
		WordDisambiguation wd = new WordDisambiguation();
		
		wd.learn(trainFactory);
		
		//System.out.println(wd);
		
		System.out.println(wd.disambiguate(testFactory));

	}
}
