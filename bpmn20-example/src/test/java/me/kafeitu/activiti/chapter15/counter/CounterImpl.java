package me.kafeitu.activiti.chapter15.counter;

import javax.jws.WebService;

/**
 * An implementation of a Counter WS
 * 
 * @author Esteban Robles Luna
 */
@WebService(endpointInterface = "me.kafeitu.activiti.chapter15.counter.Counter", serviceName = "Counsster")
public class CounterImpl implements Counter {

	protected int count;

	public CounterImpl() {
		this.count = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * {@inheritDoc}
	 */
	public void inc() {
		this.count++;
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		this.setTo(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTo(int value) {
		this.count = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public String prettyPrintCount(String prefix, String suffix) {
		return prefix + this.getCount() + suffix;
	}
}