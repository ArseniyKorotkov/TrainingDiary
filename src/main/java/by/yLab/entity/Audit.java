package by.yLab.entity;

import by.yLab.util.Action;
import by.yLab.util.FormatDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Audit {

    private long user_id;
    private Action action;
    private LocalDateTime actionLocalDateTime;

    @Override
    public String toString() {
        return actionLocalDateTime.format(FormatDateTime.reformDateTime()) + action;
    }
}
