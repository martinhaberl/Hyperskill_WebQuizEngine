package training.quizTdd.appcore.domainmodel;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;


@Component
public class Quiz {
    private Integer id;
    private String title;
    private String text;
    private List<String> options;
    private List<Integer> answer;

    public Quiz() {
    }

    public Quiz(String title, String text, List<String> options, List<Integer> answer) {
        this.id = this.setId();
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    private Integer setId() {
        return new SecureRandom().nextInt(0, 1000);
    }
}
