package cn.niceabc.activiti.chapter11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("printer")
public class Printer {

    private static Logger log = LoggerFactory.getLogger(Printer.class);

    public void printMessage() {
        log.debug("print.");
    }

    public void timerStart() {
        log.debug("timer-start.");
    }

}
