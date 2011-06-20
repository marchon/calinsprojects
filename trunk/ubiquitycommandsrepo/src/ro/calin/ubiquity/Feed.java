package ro.calin.ubiquity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Feed {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String feedName;
	
	@Persistent
	private Text feedContent;
	
	public Feed(){
	}
	
	public Feed(String name, Text content) {
		feedName = name;
		feedContent = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

	public Text getFeedContent() {
		return feedContent;
	}

	public void setFeedContent(Text feedContent) {
		this.feedContent = feedContent;
	}
	
	
}
