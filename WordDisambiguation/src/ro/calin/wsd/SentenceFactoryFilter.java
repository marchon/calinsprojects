package ro.calin.wsd;

public interface SentenceFactoryFilter {
	boolean approveId(int id);
	boolean approveSentence(Sentence sentence);
}
