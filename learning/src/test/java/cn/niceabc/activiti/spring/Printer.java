package cn.niceabc.activiti.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("printer")
public class Printer {

    private static Logger log = LoggerFactory.getLogger(Printer.class);

    public void print() {
        log.debug("print.");
    }
    public void end(String scriptVar) {
        log.debug("end." + scriptVar);
    }

    public void timerStart() {
        log.debug("timer-start.");
    }

}
