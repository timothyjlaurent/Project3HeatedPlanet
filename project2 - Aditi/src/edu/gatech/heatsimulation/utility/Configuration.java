package edu.gatech.heatsimulation.utility;

import static edu.gatech.heatsimulation.utility.ConfigurationConstant.*;
import static edu.gatech.heatsimulation.utility.ConfigurationConstant.ConcurrencyType.*;
import static edu.gatech.heatsimulation.utility.ConfigurationConstant.InitiativeType.*;

public class Configuration {

	public Configuration() {
		initializeSetting();
	}
	
	public void setConcurrencyType(final ConcurrencyType concurrencyType) {
		
		switch (concurrencyType) {
			case CONCURRENCY_TYPE_SIMULATION_THREAD:
				this.concurrencyType = (this.concurrencyType == CONCURRENCY_TYPE_PRESENTATION_THREAD)?
										CONCURRENCY_TYPE_MULTIPLE_THREAD:CONCURRENCY_TYPE_SIMULATION_THREAD;
				break;
			case CONCURRENCY_TYPE_PRESENTATION_THREAD:
				this.concurrencyType = (this.concurrencyType == CONCURRENCY_TYPE_SIMULATION_THREAD)?
										CONCURRENCY_TYPE_MULTIPLE_THREAD:CONCURRENCY_TYPE_PRESENTATION_THREAD;
				break;
			case CONCURRENCY_TYPE_SINGLE_THREAD:
			case CONCURRENCY_TYPE_MULTIPLE_THREAD:
				this.concurrencyType = concurrencyType;
				break;
			default :
				this.concurrencyType = CONCURRENCY_TYPE_SINGLE_THREAD;
				break;
		}
	}

	public ConcurrencyType getConcurrencyType() {
		return concurrencyType;
	}
		
	public void setInitiativeType(final InitiativeType initiativeType) {
		
		switch (initiativeType) {
			case INITIATIVE_TYPE_SIMULATION:
				this.initiativeType = (this.initiativeType == INITIATIVE_TYPE_PRESENTATION)?
						INITIATIVE_TYPE_MASTER:initiativeType;
				break;
			case INITIATIVE_TYPE_PRESENTATION:
				this.initiativeType = (this.initiativeType == INITIATIVE_TYPE_SIMULATION)?
						INITIATIVE_TYPE_MASTER:initiativeType;
				break;
			case INITIATIVE_TYPE_MASTER:
				this.initiativeType = initiativeType;
				break;
			default :
				this.initiativeType = INITIATIVE_TYPE_MASTER;
				break;
		}
	}

	public InitiativeType getInitiativeType() {
		return initiativeType;
	}
	
	public void setBufferLength(final String sBufferLength) {
		
		int bufferLength = DEFAULT_BUFFER_LENGTH;
		if (!sBufferLength.isEmpty()) {
			bufferLength = Integer.parseInt(sBufferLength);
			if (bufferLength <= 0) {
				bufferLength = DEFAULT_BUFFER_LENGTH;
			}
		}
		
		this.bufferLength = bufferLength;
	}
	
	public int getBufferLength() {
		return bufferLength;
	}
	
	private void initializeSetting() {
		concurrencyType = CONCURRENCY_TYPE_SINGLE_THREAD;
		initiativeType = INITIATIVE_TYPE_MASTER;
		bufferLength = DEFAULT_BUFFER_LENGTH;		
	}
	
	private ConcurrencyType concurrencyType;
	private int bufferLength;
	private InitiativeType initiativeType;
}
