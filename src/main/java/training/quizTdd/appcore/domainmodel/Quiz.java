package training.quizTdd.appcore.domainmodel;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Quiz {
    private long id;
    private String title;
    private String text;
    private List<String> options;
    private List<Integer> answer;

    public Quiz(final String title,
                final String text,
                final List<String> options,
                final List<Integer> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public Quiz() {
    }

    public Quiz(final long id,
                final String title,
                final String text,
                final List<String> options,
                final List<Integer> answer) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public long getId() {
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
}

