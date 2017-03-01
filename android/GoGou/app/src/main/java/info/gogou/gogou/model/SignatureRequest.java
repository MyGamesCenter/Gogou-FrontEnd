package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatureRequest {

    private String content;
	
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
